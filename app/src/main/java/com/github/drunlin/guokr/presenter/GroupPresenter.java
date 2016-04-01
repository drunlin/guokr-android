package com.github.drunlin.guokr.presenter;

/**
 * @author drunlin@outlook.com
 */
public interface GroupPresenter {
    /**
     * 加入小组。
     */
    void joinGroup();

    /**
     * 退出小组。
     */
    void quitGroup();

    /**
     * 打开小组的链接。
     */
    void onOpenLink();

    interface Factory {
        GroupPresenter create(int groupId);
    }
}
