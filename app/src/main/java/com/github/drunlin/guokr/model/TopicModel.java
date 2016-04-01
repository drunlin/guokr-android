package com.github.drunlin.guokr.model;

import com.github.drunlin.guokr.bean.Comment;
import com.github.drunlin.signals.impl.Signal2;

/**
 * 文章，帖子内容通用的数据处理接口。
 *
 * @author drunlin@outlook.com
 */
public interface TopicModel<T> extends ContentModel<T, Comment> {
    /**
     * 顶一个评论。
     * @param id
     */
    void likeReply(int id);

    /**
     * 侦听顶评论的结果。
     * @return [resultCode, commentId]
     */
    Signal2<Integer, Integer> likeCommentResulted();

    /**
     * 获取评论的引用数据。
     * @param comment
     * @return
     */
    String getQuote(Comment comment);
}
