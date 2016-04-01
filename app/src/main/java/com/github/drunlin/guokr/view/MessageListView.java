package com.github.drunlin.guokr.view;

import com.github.drunlin.guokr.bean.Message;

import java.util.List;

/**
 * 站内信列表界面。
 *
 * @author drunlin@outlook.com
 */
public interface MessageListView extends LoginNeededView {
    /**
     * 设置要显示的数据。
     * @param massages
     */
    void setMassages(List<Message> massages);

    /**
     * 是否在加载中。
     * @param refreshing
     */
    void setLoading(boolean refreshing);

    /**
     * 更多数据被添加。
     */
    void onMassagesAppended();

    /**
     * 加载数据失败。
     */
    void onLoadMessagesFailed();

    /**
     * 查看单个站内信。
     * @param url
     */
    void viewMessage(String url);

    /**
     * 查看所以的站内信。
     * @param url
     */
    void viewMessages(String url);
}
