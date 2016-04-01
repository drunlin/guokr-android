package com.github.drunlin.guokr.presenter.impl;

import com.github.drunlin.guokr.bean.PostContent;
import com.github.drunlin.guokr.bean.ResponseCode;
import com.github.drunlin.guokr.model.ForumModel;
import com.github.drunlin.guokr.model.PostModel;
import com.github.drunlin.guokr.presenter.PostPresenter;
import com.github.drunlin.guokr.view.PostView;

import javax.inject.Inject;

import static com.github.drunlin.guokr.util.JavaUtil.call;

public class PostPresenterImpl
        extends TopicPresenterBase<PostContent, PostModel, PostView> implements PostPresenter {

    @Inject ForumModel forumModel;

    public PostPresenterImpl(int id) {
        super(id);
    }

    @Override
    protected PostModel getModel() {
        return forumModel.getPost(contentId);
    }

    @Override
    public void onViewCreated(boolean firstCreated) {
        super.onViewCreated(firstCreated);

        bind(model.likeResulted(), this::onLikeResult);
    }

    @Override
    public void like() {
        model.like();
    }

    private void onLikeResult(int resultCode) {
        if (resultCode == ResponseCode.ALREADY_LIKED) {
            view.onAlreadyLiked();
        } else if (resultCode != ResponseCode.OK) {
            view.onLikeFailed();
        }
    }

    @Override
    public void onViewGroup() {
        call(model.getContent(), content -> view.viewGroup(content.group.id));
    }
}
