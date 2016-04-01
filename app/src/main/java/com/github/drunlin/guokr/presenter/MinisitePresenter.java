package com.github.drunlin.guokr.presenter;

import com.github.drunlin.guokr.view.MinisiteView;

/**
 * @author drunlin@outlook.com
 */
public interface MinisitePresenter extends Presenter<MinisiteView> {
    /**
     * 显示某个分类。
     * @param key
     */
    void onDisplayArticleList(String key);
}
