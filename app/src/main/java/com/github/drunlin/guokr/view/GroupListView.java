package com.github.drunlin.guokr.view;

import com.github.drunlin.guokr.bean.Group;

import java.util.List;

/**
 * 主要用来显示我加入的小组。
 *
 * @author drunlin@outlook.com
 */
public interface GroupListView {
    /**
     * 要显示的小组列表。
     * @param groups
     */
    void setGroups(List<Group> groups);

    /**
     * 是否正在刷新。
     * @param refreshing
     */
    void setLoading(boolean refreshing);

    /**
     * 加载失败。
     */
    void onLoadFailed();

    /**
     * 浏览指定的小组。
     * @param groupId
     */
    void viewGroup(int groupId);
}
