package com.github.drunlin.guokr.model;

import com.github.drunlin.guokr.bean.Reply;
import com.github.drunlin.signals.impl.Signal1;
import com.github.drunlin.signals.impl.Signal2;
import com.github.drunlin.signals.impl.Signal3;

import java.util.List;

/**
 * 文章，帖子，问答的通用接口。
 * @param <T> 内容
 * @param <R> 回复
 *
 * @author drunlin@outlook.com
 */
public interface ContentModel<T, R> {
    /**
     * 请求内容数据。
     */
    void requestContent();

    /**
     * 帖子内容加载完成。
     * @return [resultCode, content]
     */
    Signal2<Integer, T> contentResulted();

    /**
     * 获取内容。
     * @return
     */
    T getContent();

    /**
     * 请求评论。
     */
    void requestReplies();

    /**
     * 请求更多的评论。
     */
    void requestMoreReplies();

    /**
     * 评论加载完成。
     * @return [resultCode, isRefresh, replies]
     */
    Signal3<Integer, Boolean, List<R>> repliesResulted();

    /**
     * 已经获取到的评论。
     * @return
     */
    List<R> getReplies();

    /**
     * 评论当前帖子，包括回复帖子和更帖。
     * @param content
     */
    void reply(String content);

    /**
     * 评论文章的结果。
     * @return [resultCode]
     */
    Signal1<Integer> replyResulted();

    /**
     * 删除我的一条评论。
     * @param id
     */
    void deleteReply(int id);

    /**
     * 删除评论的结果。
     * @return [resultCode, replyId, position]
     */
    Signal3<Integer, Integer, Integer> deleteReplyResulted();

    /**
     * 复制评论内容到剪切板。
     * @param reply
     */
    void copyReplyContent(Reply reply);

    /**
     * 取消加载内容和评论。
     */
    void cancel();
}
