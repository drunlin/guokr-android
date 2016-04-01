package com.github.drunlin.guokr.view;

import java.util.List;

/**
 * 搜索框。
 *
 * @author drunlin@outlook.com
 */
public interface SearchBoxView {
    /**
     * 搜索。
     * @param query
     */
    void search(String query);

    /**
     * 搜索的历史记录。
     * @param history
     */
    void setHistory(List<String> history);
}
