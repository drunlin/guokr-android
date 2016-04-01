package com.github.drunlin.guokr.view;

import com.github.drunlin.guokr.bean.Notice;

import java.util.List;

/**
 * 提醒列表界面。
 *
 * @author drunlin@outlook.com
 */
public interface NoticeListView extends LoginNeededView {
    /**
     * 所有的提醒列表。
     * @param notices
     */
    void setNotices(List<Notice> notices);

    /**
     * 加载列表失败。
     */
    void onLoadFailed();

    /**
     * 忽略所有未读提醒。
     */
    void onIgnoreAllFailed();

    /**
     * 是否在加载中。
     * @param refreshing
     */
    void setLoading(boolean refreshing);

    /**
     * 查看单个通知。
     * @param url
     */
    void viewNotice(String url);

    /**
     * 查看所有的通知。
     * @param url
     */
    void openInBrowser(String url);
}
