package com.github.drunlin.guokr.presenter;

import com.github.drunlin.guokr.bean.PostEntry;

/**
 * @author drunlin@outlook.com
 */
public interface PostListPresenter extends TopicListPresenter {
    void onViewPost(PostEntry entry);

    interface Factory {
        PostListPresenter create(int groupId);
    }
}
