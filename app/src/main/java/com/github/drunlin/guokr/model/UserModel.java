package com.github.drunlin.guokr.model;

import com.github.drunlin.guokr.bean.UserInfo;
import com.github.drunlin.signals.impl.Signal0;
import com.github.drunlin.signals.impl.Signal1;

/**
 * 处理用户相关的数据。
 *
 * @author drunlin@outlook.com
 */
public interface UserModel {
    /**
     * 是否已登录。
     * @return
     */
    boolean isLoggedIn();

    /**
     * 检查是否已登录，没有登录{@link #checkTokenFailed()}会派发事件。
     * @return
     */
    boolean checkLoggedIn();

    /**
     * 检查登录状态失败。
     * @return
     */
    Signal0 checkTokenFailed();

    /**
     * 设置cookie数据。
     * @param cookie
     */
    void setCookie(String cookie);

    /**
     * 登录状态发生改变。
     * @return [loggedIn]
     */
    Signal1<Boolean> loginStateChanged();

    /**
     * 登录用户的用户信息。
     * @return
     */
    UserInfo getUserInfo();

    /**
     * 登录用户的用户信息发生改变。
     * @return
     */
    Signal0 userInfoChanged();

    /**
     * 加载用户信息失败。
     * @return
     */
    Signal0 loadUserInfoFailed();

    /**
     * 获取登录用户的token。
     * @return
     */
    String getToken();

    /**
     * 获取登录用户的标识码。
     * @return
     */
    String getUserKey();

    /**
     * 退出登录。
     */
    void logout();

    /**
     * 推荐链接到关注我的人。
     * @param link
     * @param title
     * @param summary
     * @param comment
     */
    void recommendLink(String link, String title, String summary, String comment);

    /**
     * 推荐链接的结果。
     * @return
     */
    Signal1<Integer> recommendResulted();

    /**
     * 阻塞式加载指定的用户信息。
     * @param userKey
     * @return
     */
    UserInfo getUserInfo(String userKey);
}
