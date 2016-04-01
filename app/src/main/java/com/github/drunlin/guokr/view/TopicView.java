package com.github.drunlin.guokr.view;

import com.github.drunlin.guokr.bean.Comment;

/**
 * 文章和帖子的通用接口。
 *
 * @author drunlin@outlook.com
 */
public interface TopicView<T> extends ContentView<T, Comment> {
    /**
     * 顶回复失败。
     */
    void onLikeReplyFailed();

    /**
     * 在回复中插入引用。
     * @param quote
     */
    void insertQuote(String quote);
}
