package com.github.drunlin.guokr.presenter;

import com.github.drunlin.guokr.bean.Notice;

/**
 * @author drunlin@outlook.com
 */
public interface NoticeListPresenter {
    /**
     * 刷新。
     */
    void refresh();

    /**
     * 忽略全部。
     */
    void ignoreAll();

    /**
     * 查看单个提醒。
     * @param notice
     */
    void onViewNotice(Notice notice);

    /**
     * 查看所有的提醒。
     */
    void onViewNotices();
}
