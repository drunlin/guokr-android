package com.github.drunlin.guokr.presenter.impl;

import android.text.TextUtils;

import com.github.drunlin.guokr.bean.ResponseCode;
import com.github.drunlin.guokr.bean.SearchEntry;
import com.github.drunlin.guokr.model.SearchModel;
import com.github.drunlin.guokr.presenter.SearchablePresenter;
import com.github.drunlin.guokr.view.SearchResultView;

import java.util.List;

import javax.inject.Inject;

/**
 * @author drunlin@outlook.com
 */
public class SearchablePresenterImpl
        extends PresenterBase<SearchResultView> implements SearchablePresenter {

    @Inject SearchModel searchModel;

    @Override
    public void onCreate(SearchResultView view) {
        super.onCreate(view);
    }

    @Override
    public void onViewCreated(boolean firstCreated) {
        super.onViewCreated(firstCreated);

        bind(searchModel.searchResulted(), this::onSearchResult);

        if (!firstCreated) {
            view.setSearchResult(searchModel.getSearchList());
        }
    }

    @Override
    public void search(String keyword) {
        keyword = keyword.trim();
        if (!TextUtils.isEmpty(keyword)) {
            searchModel.search(keyword);

            view.setLoading(true);
        }
    }

    private void onSearchResult(int isOk, boolean isRefresh, List<SearchEntry> searches) {
        if (isOk == ResponseCode.OK) {
            if (isRefresh) {
                view.setSearchResult(searches);
            } else {
                view.onSearchResultAppended();
            }
        } else {
            view.onLoadFailed();
        }
        view.setLoading(false);
    }


    @Override
    public void refresh() {
        searchModel.requestRefresh();
    }

    @Override
    public void loadMore() {
        searchModel.requestMore();
    }

    @Override
    public void onViewResult(SearchEntry search) {
        view.viewResult(search.url);
    }
}
