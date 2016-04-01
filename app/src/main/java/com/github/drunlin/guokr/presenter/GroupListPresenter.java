package com.github.drunlin.guokr.presenter;

import com.github.drunlin.guokr.bean.Group;

/**
 * @author drunlin@outlook.com
 */
public interface GroupListPresenter {
    /**
     * 刷新列表。
     */
    void refresh();

    /**
     * 查看小组内容。
     * @param group
     */
    void onViewGroup(Group group);
}
