package com.github.drunlin.guokr.model.impl;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.bean.Comment;
import com.github.drunlin.guokr.bean.ResultClassMap.ArticleCommentsResult;
import com.github.drunlin.guokr.test.ModelTestCase;

import org.junit.Test;

import static com.github.drunlin.guokr.mock.MockNetworkModel.DELETE;
import static com.github.drunlin.guokr.mock.MockNetworkModel.post;
import static com.github.drunlin.guokr.mock.MockNetworkModel.stubFor;
import static com.github.drunlin.guokr.mock.MockNetworkModel.urlContainers;
import static com.github.drunlin.guokr.mock.MockNetworkModel.urlEqualTo;
import static com.github.drunlin.guokr.test.ModelAsserts.assertOkResult;
import static com.github.drunlin.guokr.test.ModelAsserts.assertRefresh;
import static com.github.drunlin.guokr.test.util.Constants.ACCESS_TOKEN;
import static com.github.drunlin.guokr.test.util.Constants.ARTICLE_ID;
import static com.github.drunlin.guokr.test.util.Constants.COMMENT;
import static com.github.drunlin.guokr.test.util.TestUtils.getResult;
import static com.github.drunlin.guokr.test.util.TestUtils.getText;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author drunlin@outlook.com
 */
public class ArticleModelImplTest extends ModelTestCase<ArticleModelImpl> {
    private final int REPLY_ID =
            getResult(ArticleCommentsResult.class, R.raw.article_replies).get(0).id;
    
    @Override
    protected void init() throws Throwable {
        model = new ArticleModelImpl(injector, ARTICLE_ID);
    }

    @Test
    public void requestContent() throws Exception {
        stubFor(urlEqualTo("http://apis.guokr.com/minisite/article/%d.json", ARTICLE_ID))
                .setBody(getText(R.raw.article_content));

        model.contentResulted().add((a, b) -> {
            assertOkResult(a);
            assertThat(b, notNullValue());
            countDown();
        });

        model.requestContent();
        await();
    }

    @Test
    public void requestReplies() throws Exception {
        stubFor(urlContainers("http://apis.guokr.com/minisite/article_reply.json?article_id=%d",
                ARTICLE_ID)).setBody(getText(R.raw.article_replies));

        model.repliesResulted().add((a, b, c) -> {
            assertRefresh(a, b, c);
            countDown();
        });

        model.requestReplies();
        await();
    }

    @Test
    public void likeReply() throws Exception {
        requestReplies();

        stubFor(post("reply_id=%d&access_token=%s", REPLY_ID, ACCESS_TOKEN),
                urlEqualTo("http://www.guokr.com/apis/minisite/article_reply_liking.json"))
                .setBody(getText(R.raw.like_comment));

        Comment comment = model.getReplies().get(0);
        int likingsCount = comment.likingsCount;

        model.likeCommentResulted().add((a, b) -> {
            assertOkResult(a);
            assertEquals((int) b, REPLY_ID);
            assertEquals(likingsCount + 1, comment.likingsCount);
            assertTrue(comment.hasLiked);
            countDown();
        });

        model.likeReply(REPLY_ID);
        await();
    }

    @Test
    public void reply() throws Exception {
        stubFor(post("article_id=%d&content=%s&access_token=%s", ARTICLE_ID, COMMENT, ACCESS_TOKEN),
                urlEqualTo("http://apis.guokr.com/minisite/article_reply.json"))
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
        stubFor(DELETE, urlEqualTo("http://www.guokr.com/apis/minisite/article_reply.json" +
                "?reply_id=%d&access_token=%s", REPLY_ID, ACCESS_TOKEN))
                .setBody(getText(R.raw.simpel_result));

        model.deleteReplyResulted().add((a, b, c) -> {
            assertOkResult(a);
            countDown();
        });

        model.deleteReply(REPLY_ID);
        await();
    }
}
