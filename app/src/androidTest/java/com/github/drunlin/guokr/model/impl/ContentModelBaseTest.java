package com.github.drunlin.guokr.model.impl;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.bean.ArticleContent;
import com.github.drunlin.guokr.bean.Comment;
import com.github.drunlin.guokr.bean.ResultClassMap.ArticleCommentsResult;
import com.github.drunlin.guokr.bean.ResultClassMap.ArticleContentResult;
import com.github.drunlin.guokr.test.ModelTestCase;

import org.junit.Test;

import static com.github.drunlin.guokr.mock.MockNetworkModel.stubFor;
import static com.github.drunlin.guokr.mock.MockNetworkModel.urlContainers;
import static com.github.drunlin.guokr.mock.MockNetworkModel.urlEqualTo;
import static com.github.drunlin.guokr.test.ModelAsserts.assertLoadMore;
import static com.github.drunlin.guokr.test.ModelAsserts.assertOkResult;
import static com.github.drunlin.guokr.test.ModelAsserts.assertRefresh;
import static com.github.drunlin.guokr.test.util.Constants.ARTICLE_ID;
import static com.github.drunlin.guokr.test.util.Constants.CONTENT_URL;
import static com.github.drunlin.guokr.test.util.Constants.REPLY_URL;
import static com.github.drunlin.guokr.test.util.TestUtils.getText;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

public class ContentModelBaseTest extends ModelTestCase<ContentModelBase<ArticleContent, Comment>> {
    @Override
    protected void init() throws Throwable {
        model = new ContentModelBase<ArticleContent, Comment>(
                injector,
                ArticleContentResult.class,
                CONTENT_URL,
                ArticleCommentsResult.class,
                REPLY_URL,
                ARTICLE_ID) {
            @Override
            public void reply(String content) {}

            @Override
            public void deleteReply(int id) {}
        };
    }

    @Test
    public void requestContent() throws Exception {
        stubFor(urlEqualTo(CONTENT_URL, ARTICLE_ID)).setBody(getText(R.raw.article_content));

        model.contentResulted().add((a, b) -> {
            assertOkResult(a);
            assertNotNull(b);
            countDown();
        });

        model.requestContent();
        await();
    }

    @Test
    public void contentResulted() throws Exception {
        assertNotNull(model.contentResulted());
    }

    @Test
    public void getContent() throws Exception {
        assertNull(model.getContent());

        requestContent();

        assertNotNull(model.getContent());
    }

    @Test
    public void requestReplies() throws Exception {
        stubFor(urlContainers(REPLY_URL, ARTICLE_ID)).setBody(getText(R.raw.article_replies));

        model.repliesResulted().addOnce((a, b, c) -> {
            assertRefresh(a, b, c);
            countDown();
        });

        model.requestReplies();
        await();
    }

    @Test
    public void requestMoreReplies() throws Exception {
        requestReplies();

        model.repliesResulted().add((a, b, c) -> {
            assertLoadMore(a, b, c);
            countDown();
        });

        model.requestMoreReplies();
        await();
    }

    @Test
    public void repliesResulted() throws Exception {
        assertNotNull(model.repliesResulted());
    }

    @Test
    public void getReplies() throws Exception {
        assertNull(model.getReplies());

        requestReplies();

        assertNotNull(model.getReplies());
    }

    @Test
    public void replyResulted() throws Exception {
        assertNotNull(model.repliesResulted());
    }

    @Test
    public void deleteReplyResulted() throws Exception {
        assertNotNull(model.deleteReplyResulted());
    }
}
