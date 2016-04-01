package com.github.drunlin.guokr.view;

import com.github.drunlin.guokr.bean.UserInfo;

/**
 * 登录的用户信息界面。
 *
 * @author drunlin@outlook.com
 */
public interface UserView {
    /**
     * 用户信息。
     * @param userInfo
     */
    void setUserInfo(UserInfo userInfo);

    /**
     * 退出登录。
     */
    void onLoggedOut();
}
