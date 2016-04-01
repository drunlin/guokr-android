package com.github.drunlin.guokr.view;

import java.util.List;

/**
 * 文章，小组，问答列表共同接口。
 *
 * @author drunlin@outlook.com
 */
public interface TopicListView<T> {
    /**
     * 设置要显示的数据。
     * @param list
     */
    void setData(List<T> list);

    /**
     * 添加了更多数据。
     */
    void onDataAppend();

    /**
     * 设置是否在加载中。
     * @param refreshing
     */
    void setLoading(boolean refreshing);

    /**
     * 提示加载列表内容失败。
     */
    void onLoadFailed();
}
