package com.github.drunlin.guokr.presenter;

/**
 *
 *
 * @author drunlin@outlook.com
 */
public interface PostPresenter extends TopicPresenter {
    /**
     * 顶当前帖子
     */
    void like();

    void onViewGroup();

    interface Factory {
        PostPresenter create(int postId);
    }
}
