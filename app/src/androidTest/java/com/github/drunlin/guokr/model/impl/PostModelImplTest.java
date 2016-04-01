package com.github.drunlin.guokr.model.impl;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.test.ModelTestCase;

import org.junit.Test;

import static com.github.drunlin.guokr.mock.MockNetworkModel.DELETE;
import static com.github.drunlin.guokr.mock.MockNetworkModel.post;
import static com.github.drunlin.guokr.mock.MockNetworkModel.stubFor;
import static com.github.drunlin.guokr.mock.MockNetworkModel.urlEqualTo;
import static com.github.drunlin.guokr.test.ModelAsserts.assertOkResult;
import static com.github.drunlin.guokr.test.util.Constants.ACCESS_TOKEN;
import static com.github.drunlin.guokr.test.util.Constants.COMMENT;
import static com.github.drunlin.guokr.test.util.Constants.POST_Id;
import static com.github.drunlin.guokr.test.util.Constants.REPLY_ID;
import static com.github.drunlin.guokr.test.util.TestUtils.getText;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * @author drunlin@outlook.com
 */
public class PostModelImplTest extends ModelTestCase<PostModelImpl> {
    @Override
    protected void init() throws Throwable {
        model = new PostModelImpl(injector, POST_Id);
    }

    @Test
    public void setPostId() throws Exception {
        stubFor(urlEqualTo("http://apis.guokr.com/group/post/%d.json", POST_Id))
                .setBody(getText(R.raw.post_content));

        model.contentResulted().add((v1, v2) -> {
            assertOkResult(v1);
            assertThat(v2, notNullValue());
            countDown();
        });

        model.requestContent();
        await();
    }

    @Test
    public void like() throws Exception {
        stubFor(post("post_id=%d&access_token=%s", POST_Id, ACCESS_TOKEN),
                urlEqualTo("http://www.guokr.com/apis/group/post_liking.json"))
                .setBody(getText(R.raw.simpel_result));

        model.likeResulted().add((v) -> {
            assertOkResult(v);
            countDown();
        });

        model.like();
        await();
    }

    @Test
    public void likeResulted() throws Exception {
        assertNotNull(model.likeResulted());
    }

    @Test
    public void likeReply() throws Exception {
        stubFor(post("reply_id=%d&access_token=%s", REPLY_ID, ACCESS_TOKEN),
                urlEqualTo("http://www.guokr.com/apis/group/post_reply_liking.json"))
                .setBody(getText(R.raw.simpel_result));

        model.likeCommentResulted().add((v1, v2) -> {
            assertOkResult(v1);
            assertEquals(REPLY_ID, (int) v2);
            countDown();
        });

        model.likeReply(REPLY_ID);
        await();
    }

    @Test
    public void reply() throws Exception {
        stubFor(post("post_id=%d&content=%s&access_token=%s", POST_Id, COMMENT, ACCESS_TOKEN),
                urlEqualTo("http://apis.guokr.com/group/post_reply.json"))
                .setBody(getText(R.raw.simpel_result));

        model.replyResulted().add(v -> {
            assertOkResult(v);
            countDown();
        });

        model.reply(COMMENT);
        await();
    }

    @Test
    public void deleteReply() throws Exception {
        stubFor(DELETE, urlEqualTo("http://www.guokr.com/apis/group/post_reply.json" +
                "?reply_id=%d&reason=%d&access_token=%s", REPLY_ID, REPLY_ID, ACCESS_TOKEN))
                .setBody(getText(R.raw.simpel_result));

        model.deleteReplyResulted().add((a, b, c) -> {
            assertOkResult(a);
            countDown();
        });

        model.deleteReply(REPLY_ID);
        await();
    }
}
