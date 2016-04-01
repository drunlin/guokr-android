package com.github.drunlin.guokr.view;

import com.github.drunlin.guokr.bean.PostContent;

/**
 * 小组帖子内容界面。
 *
 * @author drunlin@outlook.com
 */
public interface PostView extends TopicView<PostContent> {
    /**
     * 顶帖子失败。
     */
    void onLikeFailed();

    /**
     * 已经顶过了。
     */
    void onAlreadyLiked();

    /**
     * 查看该帖子所在的小组。
     * @param id
     */
    void viewGroup(int id);
}
