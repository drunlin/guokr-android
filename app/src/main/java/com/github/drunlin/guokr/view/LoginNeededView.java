package com.github.drunlin.guokr.view;

/**
 * 某些操作需要登录的界面。
 *
 * @author drunlin@outlook.com
 */
public interface LoginNeededView {
    /**
     * 登录状态验证失败。
     */
    void onLoginStateInvalid();
}
