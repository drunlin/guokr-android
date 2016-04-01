package com.github.drunlin.guokr.view;

import com.github.drunlin.guokr.bean.PostEntry;

/**
 * 小组帖子列表界面。
 *
 * @author drunlin@outlook.com
 */
public interface PostListView extends TopicListView<PostEntry> {
    /**
     * 查看帖子内容。
     * @param postId
     */
    void viewPost(int postId);
}
