package com.github.drunlin.guokr.presenter;

import com.github.drunlin.guokr.bean.Comment;

/**
 * 文章，帖子的通用接口。
 *
 * @author drunlin@outlook.com
 */
public interface TopicPresenter extends ContentPresenter {
    /**
     * 顶回复。
     * @param comment
     */
    void likeReply(Comment comment);

    /**
     * 回复评论。
     * @param comment
     */
    void onReplyComment(Comment comment);
}
