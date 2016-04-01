package com.github.drunlin.guokr.view;

import com.github.drunlin.guokr.bean.UserInfo;

/**
 * 帐号信息界面，可以登录或查看已登录的用户信息。
 *
 * @author drunlin@outlook.com
 */
public interface AccountView {
    /**
     * 设置已登录的用户信息。
     * @param userInfo
     */
    void setUserInfo(UserInfo userInfo);

    /**
     * 退出登录时调用。
     */
    void onLoggedOut();

    /**
     * 显示登录界面。
     */
    void login();

    /**
     * 查看当前登录用户详细信息。
     */
    void viewUserInfo();
}
