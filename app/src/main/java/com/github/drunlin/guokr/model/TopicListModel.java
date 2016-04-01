package com.github.drunlin.guokr.model;

import android.support.annotation.IntRange;

import com.github.drunlin.guokr.bean.TopicEntry;
import com.github.drunlin.signals.impl.Signal3;

import java.util.List;

/**
 * 处理科学人，小组，问答帖子列表的通用接口。
 *
 * @author drunlin@outlook.com
 */
public interface TopicListModel<T extends TopicEntry> {
    /**
     * 设置每次的请求数目。
     * @param limit
     */
    void setLimit(@IntRange(from = 1) int limit);

    /**
     * 请求刷新，会取消上次未完成的请求。
     */
    void requestRefresh();

    /**
     * 请求更多信息，类似于下一页。
     */
    void requestMore();

    /**
     * 侦听服务器返回的结果。
     *
     * @return [resultCode, isRefresh, result]
     */
    Signal3<Integer, Boolean, List<T>> resulted();

    /**
     * 缓存的成功获取的帖子。
     * @return
     */
    List<T> getTopics();

    /**
     * 取消所有的请求。
     */
    void cancel();
}
