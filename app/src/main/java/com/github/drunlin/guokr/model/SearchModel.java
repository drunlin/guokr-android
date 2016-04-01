package com.github.drunlin.guokr.model;

import com.github.drunlin.guokr.bean.SearchEntry;
import com.github.drunlin.signals.impl.Signal3;

import java.util.List;

/**
 * 搜索文章，帖子，问答。
 *
 * @author drunlin@outlook.com
 */
public interface SearchModel {
    /**
     * 搜索关键词。
     * @param query
     */
    void search(String query);

    /**
     * 刷新搜索结果。
     */
    void requestRefresh();

    /**
     * 请求更多的搜索结果。
     */
    void requestMore();

    /**
     * 加载完成的搜索结果。
     * @return
     */
    List<SearchEntry> getSearchList();

    /**
     * 数据加载完成。
     * @return [resultCode, isRefresh, searchResult]
     */
    Signal3<Integer, Boolean, List<SearchEntry>> searchResulted();
}
