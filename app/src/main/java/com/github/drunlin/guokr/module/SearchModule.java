package com.github.drunlin.guokr.module;

import com.github.drunlin.guokr.model.SearchModel;
import com.github.drunlin.guokr.model.impl.SearchModelImpl;
import com.github.drunlin.guokr.module.tool.Injector;
import com.github.drunlin.guokr.module.tool.SingletonMap;
import com.github.drunlin.guokr.presenter.SearchBoxPresenter;
import com.github.drunlin.guokr.presenter.SearchablePresenter;
import com.github.drunlin.guokr.presenter.impl.SearchBoxPresenterImpl;
import com.github.drunlin.guokr.presenter.impl.SearchablePresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * @author drunlin@outlook.com
 */
@Module(injects = {
        SearchablePresenterImpl.class,
        SearchModelImpl.class,
        SearchBoxPresenter.class,
        SearchBoxPresenterImpl.class,
        SearchablePresenter.class,
}, complete = false)
@SuppressWarnings("unused")
public class SearchModule {
    @Provides
    SearchModel provideSearchModel(Injector injector) {
        return SingletonMap.get(SearchModel.class, () -> new SearchModelImpl(injector));
    }

    @Provides
    SearchBoxPresenter provideSearchBoxPresenter() {
        return new SearchBoxPresenterImpl();
    }

    @Provides
    SearchablePresenter provideSearchPresenter() {
        return new SearchablePresenterImpl();
    }
}
