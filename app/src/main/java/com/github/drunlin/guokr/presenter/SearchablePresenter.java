package com.github.drunlin.guokr.presenter;

import com.github.drunlin.guokr.bean.SearchEntry;

/**
 * @author drunlin@outlook.com
 */
public interface SearchablePresenter {
    /**
     * 搜索关键词。
     * @param keyword
     */
    void search(String keyword);

    /**
     * 刷新本次搜索的结果。
     */
    void refresh();

    /**
     * 加载更多的搜索结果。
     */
    void loadMore();

    /**
     * 查看单个搜索结果。
     * @param search
     */
    void onViewResult(SearchEntry search);
}
