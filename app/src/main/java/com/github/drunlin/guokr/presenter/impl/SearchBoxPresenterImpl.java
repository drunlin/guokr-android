package com.github.drunlin.guokr.presenter.impl;

import android.text.TextUtils;

import com.github.drunlin.guokr.presenter.SearchBoxPresenter;
import com.github.drunlin.guokr.view.SearchBoxView;

/**
 * @author drunlin@outlook.com
 */
public class SearchBoxPresenterImpl
        extends PresenterBase<SearchBoxView> implements SearchBoxPresenter {

    @Override
    public void onSearch(String s) {
        if (!TextUtils.isEmpty(s)) {
            view.search(s);
        }
    }
}
