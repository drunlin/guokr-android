package com.github.drunlin.guokr.presenter.impl;

import com.github.drunlin.guokr.bean.PostEntry;
import com.github.drunlin.guokr.model.ForumModel;
import com.github.drunlin.guokr.model.PostListModel;
import com.github.drunlin.guokr.presenter.PostListPresenter;
import com.github.drunlin.guokr.view.PostListView;

import javax.inject.Inject;

/**
 * @author drunlin@outlook.com
 */
public class PostListPresenterImpl
        extends TopicListPresenterBase<PostEntry, PostListModel, PostListView>
        implements PostListPresenter {

    @Inject ForumModel groupModel;

    private final int groupId;

    public PostListPresenterImpl(int id) {
        groupId = id;
    }

    @Override
    protected PostListModel getModel() {
        return groupModel.getPostList(groupId);
    }

    @Override
    public void onViewPost(PostEntry entry) {
        view.viewPost(entry.id);
    }
}
