package com.github.drunlin.guokr.view;

import com.github.drunlin.guokr.bean.SearchEntry;

import java.util.List;

/**
 * 搜索功能界面
 *
 * @author drunlin@outlook.com
 */
public interface SearchResultView {
    /**
     * 搜索的结果。
     * @param search
     */
    void setSearchResult(List<SearchEntry> search);

    /**
     * 添加更多的搜索结果。
     */
    void onSearchResultAppended();

    /**
     * 是否在加载中。
     * @param loading
     */
    void setLoading(boolean loading);

    /**
     * 加载失败。
     */
    void onLoadFailed();

    /**
     * 查看某个结果。
     * @param url
     */
    void viewResult(String url);
}
