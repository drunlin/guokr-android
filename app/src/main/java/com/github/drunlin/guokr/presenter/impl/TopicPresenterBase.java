package com.github.drunlin.guokr.presenter.impl;

import com.github.drunlin.guokr.bean.Comment;
import com.github.drunlin.guokr.bean.Content;
import com.github.drunlin.guokr.bean.ResponseCode;
import com.github.drunlin.guokr.model.TopicModel;
import com.github.drunlin.guokr.presenter.TopicPresenter;
import com.github.drunlin.guokr.view.TopicView;

import static com.github.drunlin.guokr.util.JavaUtil.index;

/**
 * 文章，帖子内容的基类
 *
 * @author drunlin@outlook.com
 */
public abstract class TopicPresenterBase<
        T extends Content,
        M extends TopicModel<T>,
        V extends TopicView<T>>
        extends ContentPresenterBase<T, Comment, M, V> implements TopicPresenter {

    public TopicPresenterBase(int id) {
        super(id);
    }

    @Override
    public void onViewCreated(boolean firstCreated) {
        super.onViewCreated(firstCreated);

        bind(model.likeCommentResulted(), this::onLikeReplyResult);
    }

    @Override
    public void likeReply(Comment comment) {
        model.likeReply(comment.id);
    }

    private void onLikeReplyResult(int resultCode, int id) {
        if (resultCode == ResponseCode.OK) {
            index(model.getReplies(), v -> v.id == id, index -> view.updateReply(index));
        } else {
            view.onLikeReplyFailed();
        }
    }

    @Override
    public void onReplyComment(Comment comment) {
        onPreReply();

        view.insertQuote(model.getQuote(comment));
    }
}
