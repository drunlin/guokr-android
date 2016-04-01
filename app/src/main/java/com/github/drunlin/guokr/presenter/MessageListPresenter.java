package com.github.drunlin.guokr.presenter;

import com.github.drunlin.guokr.bean.Message;

/**
 * @author drunlin@outlook.com
 */
public interface MessageListPresenter {
    /**
     * 刷新列表。
     */
    void refresh();

    /**
     * 加载更多。
     */
    void loadMore();

    /**
     * 查看站内信。
     * @param message
     */
    void onViewMessage(Message message);

    /**
     * 查看所以的站内信。
     */
    void onViewMessages();
}
