package com.github.drunlin.guokr.model.impl;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.bean.Message;
import com.github.drunlin.guokr.test.ModelTestCase;

import org.junit.Test;

import static com.github.drunlin.guokr.mock.MockNetworkModel.stubFor;
import static com.github.drunlin.guokr.mock.MockNetworkModel.urlContainers;
import static com.github.drunlin.guokr.test.ModelAsserts.assertLoadMore;
import static com.github.drunlin.guokr.test.ModelAsserts.assertRefresh;
import static com.github.drunlin.guokr.test.util.Constants.ACCESS_TOKEN;
import static com.github.drunlin.guokr.test.util.TestUtils.getText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author drunlin@outlook.com
 */
public class MessageModelImplTest extends ModelTestCase<MessageModelImpl> {
    @Override
    protected void init() throws Throwable {
        model = new MessageModelImpl(injector);
    }

    @Test
    public void requestMessages() throws Exception {
        stubFor(urlContainers("http://www.guokr.com/apis/community/user/message.json" +
                "?access_token=%s&offset=0", ACCESS_TOKEN)).setBody(getText(R.raw.message_list));

        model.messagesResulted().add((a, b, c) -> {
            assertRefresh(a, b, c);
            countDown();
        });

        model.requestMessages();
        await();
    }

    @Test
    public void requestMoreMessages() throws Exception {
        requestMessages();
        model.messagesResulted().removeAll();

        stubFor(urlContainers("http://www.guokr.com/apis/community/user/message.json" +
                "?access_token=%s", ACCESS_TOKEN)).setBody(getText(R.raw.message_list));

        model.messagesResulted().add((a, b, c) -> {
            assertLoadMore(a, b, c);
            countDown();
        });

        model.requestMoreMessages();
        await();
    }

    @Test
    public void messagesResulted() throws Exception {
        assertNotNull(model.messagesResulted());
    }

    @Test
    public void getMassages() throws Exception {
        assertNull(model.getMassages());

        requestMessages();

        assertNotNull(model.getMassages());
    }

    @Test
    public void getMessageUrl() throws Exception {
        Message message = new Message();
        message.authorKey = "abc123";
        assertEquals("http://m.guokr.com/user/messages/0623698779/", model.getMessageUrl(message));
    }
}
