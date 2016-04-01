package com.github.drunlin.guokr.view;

/**
 * 显示未读提醒数的界面，包括提醒，站内信的数目。
 *
 * @author drunlin@outlook.com
 */
public interface NoticeView {
    /**
     * 所有的未读提醒。
     * @param count
     */
    void setAllNoticesCount(int count);

    /**
     * 提醒数目。
     * @param count
     */
    void setNoticesCount(int count);

    /**
     * 站内信数目。
     * @param count
     */
    void setMessagesCount(int count);

    /**
     * 设置登录的状态。
     * @param loggedIn
     */
    void setLoginStatus(boolean loggedIn);

    /**
     * 查看通知。
     */
    void viewNotices();

    /**
     * 查看站内信。
     */
    void viewMessages();
}
