package com.github.drunlin.guokr.view;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * 文章，帖子，问答内容的通用接口。
 *
 * @author drunlin@outlook.com
 */
public interface ContentView<T, R> extends LoginNeededView {
    /**
     * 设置加载的状态。
     * @param loading
     */
    void setLoading(boolean loading);

    /**
     * 设置显示的内容。
     * @param data
     */
    void setContent(@NonNull T data);

    /**
     * 内容加载失败。
     */
    void onLoadContentFailed();

    /**
     * 设置内容的评论。
     * @param replies
     */
    void setReplies(List<R> replies);

    /**
     * 评论加载失败。
     */
    void onLoadRepliesFailed();

    /**
     * 更多的评论被添加。
     */
    void onRepliesAppended();

    /**
     * 回复的条件检查完毕，用来更新回复相关的界面。
     */
    void preReply();

    /**
     * 完成回复。
     */
    void onReplyComplete();

    /**
     * 回复失败。
     */
    void onReplyFailed();

    /**
     * 更新单个评论。
     * @param position
     */
    void updateReply(int position);

    /**
     * 打开链接。
     * @param url
     */
    void openLink(String url);

    /**
     * 分享内容。
     * @param content
     */
    void share(String content);

    /**
     * 收藏链接。
     * @param name
     * @param url
     */
    void favor(String name, String url);

    /**
     * 取消回复。
     */
    void cancelReply();

    /**
     * 评论被移除。
     * @param position
     */
    void onReplyRemoved(int position);

    /**
     * 删除评论失败。
     */
    void onDeleteReplyFailed();
}
