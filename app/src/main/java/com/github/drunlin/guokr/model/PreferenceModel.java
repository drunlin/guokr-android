package com.github.drunlin.guokr.model;

import com.github.drunlin.guokr.bean.Account;
import com.github.drunlin.guokr.bean.UserInfo;

/**
 * 存储配置信息。
 *
 * @author drunlin@outlook.com
 */
public interface PreferenceModel {
    /**
     * 保存帐号信息。
     * @param account
     */
    void setAccount(Account account);

    /**
     * 获取保存的帐号信息。
     * @return
     */
    Account getAccount();

    /**
     * 是否启用图片水印。
     * @return
     */
    boolean isWatermarkEnable();

    /**
     * 保存用户信息。
     * @param userInfo
     */
    void setUserInfo(UserInfo userInfo);

    /**
     * 获取保存的用户信息。
     * @return
     */
    UserInfo getUserInfo();

    /**
     * 是否保存主界面状态。
     * @return
     */
    boolean isSavedPagePosition();

    /**
     * 保存主界面状态。
     * @param position
     */
    void setPagePosition(int position);

    /**
     * 获取保存的主界面状态。
     * @return
     */
    int getPagePosition();

    /**
     * 保存夜间模式状态。
     * @param nightMode
     */
    void setNightMode(boolean nightMode);

    /**
     * 是否为夜间模式。
     * @return
     */
    boolean isNightMode();
}
