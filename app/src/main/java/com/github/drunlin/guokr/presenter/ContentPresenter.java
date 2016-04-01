package com.github.drunlin.guokr.presenter;

import com.github.drunlin.guokr.bean.Reply;

/**
 * 文章，帖子，问答内容的通用基类。
 *
 * @author drunlin@outlook.com
 */
public interface ContentPresenter {
    /**
     * 刷新。
     */
    void refresh();

    /**
     * 加载更多回复。
     */
    void loadMoreReplies();

    /**
     * 分享当前内容。
     */
    void onShare();

    /**
     * 打开内容的链接。
     */
    void onOpenLink();

    /**
     * 收藏内容。
     */
    void onFavor();

    /**
     * 准备回复。
     */
    void onPreReply();

    /**
     * 回复
     * @param content 回复的内容
     */
    void reply(String content);

    /**
     * 取消回复。
     * @param content
     */
    void onCancelReply(String content);

    /**
     * 删除自己发布的回复。
     * @param reply
     */
    void deleteReply(Reply reply);

    /**
     * 复制回复的内容到剪切板。
     * @param reply
     */
    void copyReplyContent(Reply reply);
}
