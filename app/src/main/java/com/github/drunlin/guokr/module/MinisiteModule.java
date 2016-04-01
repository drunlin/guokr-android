package com.github.drunlin.guokr.module;

import com.github.drunlin.guokr.model.MinisiteModel;
import com.github.drunlin.guokr.model.impl.ArticleListModelImpl;
import com.github.drunlin.guokr.model.impl.ArticleModelImpl;
import com.github.drunlin.guokr.model.impl.MinisiteModelImpl;
import com.github.drunlin.guokr.module.tool.Injector;
import com.github.drunlin.guokr.module.tool.SingletonMap;
import com.github.drunlin.guokr.presenter.ArticleListPresenter;
import com.github.drunlin.guokr.presenter.ArticlePresenter;
import com.github.drunlin.guokr.presenter.MinisitePresenter;
import com.github.drunlin.guokr.presenter.impl.ArticleListPresenterImpl;
import com.github.drunlin.guokr.presenter.impl.ArticlePresenterImpl;
import com.github.drunlin.guokr.presenter.impl.MinisitePresenterImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author drunlin@outlook.com
 */
@Module(injects = {
        ArticleListPresenterImpl.class,
        ArticleListPresenter.Factory.class,
        ArticlePresenterImpl.class,
        ArticlePresenter.Factory.class,
        MinisitePresenterImpl.class,
        MinisitePresenter.class,
        MinisiteModelImpl.class,
        ArticleListModelImpl.class,
        ArticleModelImpl.class,
}, complete = false)
@SuppressWarnings("unused")
public class MinisiteModule {
    @Provides
    @Singleton
    ArticleListPresenter.Factory provideArticleListPresenter() {
        return ArticleListPresenterImpl::new;
    }

    @Singleton
    @Provides
    ArticlePresenter.Factory provideArticlePresenter() {
        return ArticlePresenterImpl::new;
    }

    @Provides
    MinisitePresenter provideMinisitePresenter(Injector injector) {
        return new MinisitePresenterImpl();
    }

    @Provides
    MinisiteModel provideMinisiteModel(Injector injector) {
        return SingletonMap.get(MinisiteModel.class, () -> new MinisiteModelImpl(injector));
    }
}
