package com.github.drunlin.guokr.model.impl;

import com.github.drunlin.guokr.bean.PostContent;
import com.github.drunlin.guokr.bean.ResponseCode;
import com.github.drunlin.guokr.bean.Result;
import com.github.drunlin.guokr.bean.ResultClassMap.PostCommentsResult;
import com.github.drunlin.guokr.bean.ResultClassMap.PostContentResult;
import com.github.drunlin.guokr.model.IconLoader;
import com.github.drunlin.guokr.model.PostModel;
import com.github.drunlin.guokr.model.UserModel;
import com.github.drunlin.guokr.model.request.AccessRequest;
import com.github.drunlin.guokr.model.request.HttpRequest;
import com.github.drunlin.guokr.module.tool.Injector;
import com.github.drunlin.signals.impl.Signal1;

import javax.inject.Inject;

import static com.android.volley.Request.Method.DELETE;
import static com.android.volley.Request.Method.POST;

/**
 * @author drunlin@outlook.com
 */
public class PostModelImpl extends TopicContentModelBase<PostContent> implements PostModel {
    private final Signal1<Integer> likeResulted = new Signal1<>();

    @Inject UserModel userModel;

    public PostModelImpl(Injector injector, int postId) {
        super(injector,
                PostContentResult.class,
                "http://apis.guokr.com/group/post/%d.json",
                PostCommentsResult.class,
                "http://apis.guokr.com/group/post_reply.json?retrieve_type=by_post&post_id=%d",
                postId);
    }

    @Override
    protected void onParseResponse(Result<PostContent> result) {
        IconLoader.load(networkModel, result.result.author.avatar);
    }

    @Override
    public void like() {
        final String url = "http://www.guokr.com/apis/group/post_liking.json";
        new AccessRequest.SimpleRequestBuilder(url, userModel.getToken())
                .setMethod(POST)
                .addParam("post_id", contentId)
                .setListener(response -> likeResulted.dispatch(ResponseCode.OK))
                .setErrorListener(error -> likeResulted.dispatch(error.getCode()))
                .build(networkModel);
    }

    @Override
    public Signal1<Integer> likeResulted() {
        return likeResulted;
    }

    @Override
    public void likeReply(int id) {
        final String url = "http://www.guokr.com/apis/group/post_reply_liking.json";
        new AccessRequest.SimpleRequestBuilder(url, userModel.getToken())
                .setMethod(POST)
                .addParam("reply_id", id)
                .setListener(response -> onLikeReplySucceed(id))
                .setErrorListener(error -> onLikeReplyFailed(error, id))
                .build(networkModel);
    }

    /**
     * 优化{@link com.github.drunlin.guokr.bean.Comment#hasLiked}总是为false。
     * @param error
     * @param id
     */
    private void onLikeReplyFailed(HttpRequest.RequestError error, int id) {
        if (error.getCode() == ResponseCode.ALREADY_LIKED) {
            onLikeReplySucceed(id);
        } else {
            likeCommentResulted.dispatch(error.getCode(), id);
        }
    }

    @Override
    public void reply(String content) {
        final String url = "http://apis.guokr.com/group/post_reply.json";
        new AccessRequest.SimpleRequestBuilder(url, userModel.getToken())
                .setMethod(POST)
                .addParam("post_id", contentId)
                .addParam("content", content)
                .setListener(response -> replyResulted.dispatch(ResponseCode.OK))
                .setErrorListener(error -> replyResulted.dispatch(error.getCode()))
                .build(networkModel);
    }

    @Override
    public void deleteReply(int id) {
        final String url = "http://www.guokr.com/apis/group/post_reply.json";
        new AccessRequest.SimpleRequestBuilder(url, userModel.getToken())
                .setMethod(DELETE)
                .addParam("reply_id", id)
                .addParam("reason", id)
                .setListener(response -> onDeleteReplySucceed(id))
                .setErrorListener(error -> deleteReplyResulted.dispatch(error.getCode(), id))
                .build(networkModel);
    }
}
