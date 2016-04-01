package com.github.drunlin.guokr.model;

import com.github.drunlin.guokr.bean.Group;
import com.github.drunlin.signals.impl.Signal2;

import java.util.List;

/**
 * 小组相关的网络操作。
 * @see GroupModel
 *
 * @author drunlin@outlook.com
 */
public interface ForumModel {
    /**小组热帖的小组ID。*/
    int HOT_POST = -1;
    /**我的小组的小组ID。*/
    int MY_GROUP_POST = -2;

    /**
     * 根据ID获取帖子。
     * @param groupId
     * @return
     */
    PostListModel getPostList(int groupId);

    /**
     * 小组热帖。
     * @return
     */
    PostListModel getHotPostList();

    /**
     * 请求我的小组。
     */
    PostListModel getMyGroupPosts();

    /**
     * 请求所有我加入的小组。
     */
    void requestJoinedGroups();

    /**
     * 我加入的小组。
     * @return
     */
    List<Group> getJoinedGroups();

    /**
     * 加入的小组的列表加载完成。
     * @return [resultCode, groups]
     */
    Signal2<Integer, List<Group>> joinedGroupsResulted();

    /**
     * 获取小组数据。
     * @param groupId
     * @return
     */
    GroupModel getGroup(int groupId);

    /**
     * 获取帖子数据。
     * @param postId
     * @return
     */
    PostModel getPost(int postId);
}
