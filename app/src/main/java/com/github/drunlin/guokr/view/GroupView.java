package com.github.drunlin.guokr.view;

import android.support.annotation.NonNull;

import com.github.drunlin.guokr.bean.Group;

/**
 * 单个小组内容。
 *
 * @author drunlin@outlook.com
 */
public interface GroupView extends LoginNeededView {
    /**
     * 小组的信息。
     * @param group
     */
    void setGroup(@NonNull Group group);

    /**
     * 加入小组成功。
     */
    void onJoinGroupSucceed();

    /**
     * 加入小组失败。
     */
    void onJoinGroupFailed();

    /**
     * 退出小组成功。
     */
    void onQuitGroupSucceed();

    /**
     * 退出小组失败。
     */
    void onQuitGroupFailed();

    /**
     * 在浏览器中打开当前小组的链接。
     * @param url
     */
    void openLink(String url);
}
