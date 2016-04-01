package com.github.drunlin.guokr.model.impl;

import com.github.drunlin.guokr.bean.ArticleContent;
import com.github.drunlin.guokr.bean.ResponseCode;
import com.github.drunlin.guokr.bean.Result;
import com.github.drunlin.guokr.bean.ResultClassMap.ArticleCommentsResult;
import com.github.drunlin.guokr.bean.ResultClassMap.ArticleContentResult;
import com.github.drunlin.guokr.model.ArticleModel;
import com.github.drunlin.guokr.model.UserModel;
import com.github.drunlin.guokr.model.request.AccessRequest;
import com.github.drunlin.guokr.module.tool.Injector;

import java.util.LinkedList;

import javax.inject.Inject;

import static com.android.volley.Request.Method.DELETE;
import static com.android.volley.Request.Method.POST;

/**
 * @author drunlin@outlook.com
 */
public class ArticleModelImpl
        extends TopicContentModelBase<ArticleContent> implements ArticleModel {

    @Inject UserModel userModel;

    public ArticleModelImpl(Injector injector, int articleId) {
        super(injector,
                ArticleContentResult.class,
                "http://apis.guokr.com/minisite/article/%d.json",
                ArticleCommentsResult.class,
                "http://apis.guokr.com/minisite/article_reply.json?article_id=%d",
                articleId);
    }

    @Override
    protected void onParseResponse(Result<ArticleContent> result) {
        setContentLabels(result.result);
    }

    private void setContentLabels(ArticleContent content) {
        content.labels = new LinkedList<>(content.channels);
        content.labels.add(content.subject);
    }

    @Override
    public void likeReply(int id) {
        final String url = "http://www.guokr.com/apis/minisite/article_reply_liking.json";
        new AccessRequest.SimpleRequestBuilder(url, userModel.getToken())
                .setMethod(POST)
                .addParam("reply_id", id)
                .setListener(response -> onLikeReplySucceed(id))
                .setErrorListener(error -> likeCommentResulted.dispatch(error.getCode(), id))
                .build(networkModel);
    }

    @Override
    public void reply(String content) {
        final String url = "http://apis.guokr.com/minisite/article_reply.json";
        new AccessRequest.SimpleRequestBuilder(url, userModel.getToken())
                .setMethod(POST)
                .addParam("article_id", contentId)
                .addParam("content", content)
                .setListener(response -> replyResulted.dispatch(ResponseCode.OK))
                .setErrorListener(error -> replyResulted.dispatch(error.getCode()))
                .build(networkModel);
    }

    @Override
    public void deleteReply(int id) {
        final String url = "http://www.guokr.com/apis/minisite/article_reply.json";
        new AccessRequest.SimpleRequestBuilder(url, userModel.getToken())
                .setMethod(DELETE)
                .addParam("reply_id", id)
                .setListener(response -> onDeleteReplySucceed(id))
                .setErrorListener(error -> deleteReplyResulted.dispatch(error.getCode(), id))
                .build(networkModel);
    }
}
