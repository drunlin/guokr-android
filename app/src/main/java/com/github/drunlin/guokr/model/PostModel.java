package com.github.drunlin.guokr.model;

import com.github.drunlin.guokr.bean.PostContent;
import com.github.drunlin.signals.impl.Signal1;

/**
 * 小组帖子数据。
 *
 * @author drunlin@outlook.com
 */
public interface PostModel extends TopicModel<PostContent> {
    /**
     * 顶帖子。
     */
    void like();

    /**
     * 顶帖子结果的侦听器。
     * @return
     */
    Signal1<Integer> likeResulted();
}
