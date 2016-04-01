package com.github.drunlin.guokr.model.impl;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.test.ModelTestCase;
import com.github.drunlin.guokr.test.util.Value;

import org.junit.Test;

import static com.github.drunlin.guokr.mock.MockNetworkModel.put;
import static com.github.drunlin.guokr.mock.MockNetworkModel.stubFor;
import static com.github.drunlin.guokr.mock.MockNetworkModel.urlContainers;
import static com.github.drunlin.guokr.mock.MockNetworkModel.urlEqualTo;
import static com.github.drunlin.guokr.test.ModelAsserts.assertOkResult;
import static com.github.drunlin.guokr.test.util.Constants.ACCESS_TOKEN;
import static com.github.drunlin.guokr.test.util.TestUtils.getText;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * @author drunlin@outlook.com
 */
public class NoticeModelImplTest extends ModelTestCase<NoticeModelImpl> {
    @Override
    protected void init() throws Throwable {
        model = new NoticeModelImpl(injector);
    }

    @Test
    public void requestNoticeCount() throws Exception {
        stubFor(urlContainers("http://www.guokr.com/apis/community/rn_num.json" +
                "?access_token=%s", ACCESS_TOKEN)).setBody(getText(R.raw.notice_num));

        model.noticeCountResulted().add((a, b) -> {
            assertOkResult(a);
            assertNotNull(b);
            countDown();
        });

        model.requestNoticesCount();
        await();
    }

    @Test
    public void noticeCountResulted() throws Exception {
        assertNotNull(model.noticeCountResulted());
    }

    @Test
    public void getNoticeNum() throws Exception {
        assertNull(model.getNoticeNum());

        requestNoticeCount();

        assertNotNull(model.getNoticeNum());
    }

    @Test
    public void requestNotices() throws Exception {
        stubFor(urlContainers("http://www.guokr.com/apis/community/notice.json" +
                "?access_token=%s", ACCESS_TOKEN)).setBody(getText(R.raw.notice_list));

        model.noticesResulted().add((a, b) -> {
            assertOkResult(a);
            assertNotNull(b);
            countDown();
        });

        model.requestNotices();
        await();
    }

    @Test
    public void noticesResulted() throws Exception {
        assertNotNull(model.noticesResulted());
    }

    @Test
    public void getNotices() throws Exception {
        assertNull(model.getNotices());

        requestNotices();

        assertNotNull(model.getNotices());
    }

    @Test
    public void startServer() throws Exception {
        stubFor(urlContainers("http://www.guokr.com/apis/community/rn_num.json" +
                "?access_token=%s", ACCESS_TOKEN)).setBody(getText(R.raw.notice_num));

        model.noticeCountResulted().add((a, b) -> {
            assertOkResult(a);
            assertNotNull(b);
            countDown();
        });

        model.startServer(100);
        await(2);
        model.noticeCountResulted().removeAll();
        model.stopServer();
    }

    @Test
    public void stopServer() throws Exception {
        stubFor(urlContainers("http://www.guokr.com/apis/community/rn_num.json" +
                "?access_token=%s", ACCESS_TOKEN)).setBody(getText(R.raw.notice_num));

        Value<Integer> count = new Value<>(0);
        model.noticeCountResulted().add((a, b) -> count.setValue(count.getValue() + 1));

        model.startServer(100);
        model.stopServer();
        await(1, 500);

        assertThat(count.getValue(), lessThanOrEqualTo(1));
    }

    @Test
    public void ignoreAllNotices() throws Exception {
        stubFor(put("access_token=%s", ACCESS_TOKEN),
                urlEqualTo("http://www.guokr.com/apis/community/notice_ignore.json"))
                .setBody(getText(R.raw.simpel_result));

        model.ignoreAllNoticesResulted().add(v -> {
            assertOkResult(v);
            countDown();
        });

        model.ignoreAllNotices();
        await();
    }

    @Test
    public void ignoreAllNoticesResulted() throws Exception {
        assertNotNull(model.ignoreAllNoticesResulted());
    }
}
