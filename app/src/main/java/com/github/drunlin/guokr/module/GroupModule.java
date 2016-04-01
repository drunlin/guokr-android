package com.github.drunlin.guokr.module;

import com.github.drunlin.guokr.model.ForumModel;
import com.github.drunlin.guokr.model.impl.ForumModelImpl;
import com.github.drunlin.guokr.model.impl.GroupModelImpl;
import com.github.drunlin.guokr.model.impl.PostListModelImpl;
import com.github.drunlin.guokr.model.impl.PostModelImpl;
import com.github.drunlin.guokr.module.tool.Injector;
import com.github.drunlin.guokr.module.tool.SingletonMap;
import com.github.drunlin.guokr.presenter.ForumPresenter;
import com.github.drunlin.guokr.presenter.GroupListPresenter;
import com.github.drunlin.guokr.presenter.GroupPresenter;
import com.github.drunlin.guokr.presenter.PostListPresenter;
import com.github.drunlin.guokr.presenter.PostPresenter;
import com.github.drunlin.guokr.presenter.impl.ForumPresenterImpl;
import com.github.drunlin.guokr.presenter.impl.GroupListPresenterImpl;
import com.github.drunlin.guokr.presenter.impl.GroupPresenterImpl;
import com.github.drunlin.guokr.presenter.impl.PostListPresenterImpl;
import com.github.drunlin.guokr.presenter.impl.PostPresenterImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author drunlin@outlook.com
 */
@Module(injects = {
        ForumPresenterImpl.class,
        ForumPresenter.class,
        GroupListPresenter.class,
        GroupListPresenterImpl.class,
        GroupPresenter.Factory.class,
        GroupPresenterImpl.class,
        PostListPresenterImpl.class,
        PostListPresenter.Factory.class,
        PostPresenter.Factory.class,
        PostPresenterImpl.class,
        ForumModelImpl.class,
        PostListModelImpl.class,
        PostModelImpl.class,
        GroupModelImpl.class
}, complete = false)
@SuppressWarnings("unused")
public class GroupModule {
    @Provides
    ForumPresenter provideGroupPresenter() {
        return new ForumPresenterImpl();
    }

    @Provides
    GroupListPresenter provideGroupListPresenter() {
        return new GroupListPresenterImpl();
    }

    @Singleton
    @Provides
    GroupPresenter.Factory provideGroupPresenter1() {
        return GroupPresenterImpl::new;
    }

    @Provides
    @Singleton
    PostListPresenter.Factory providePostListPresent() {
        return PostListPresenterImpl::new;
    }

    @Singleton
    @Provides
    PostPresenter.Factory providePostPresenter() {
        return PostPresenterImpl::new;
    }

    @Provides
    ForumModel provideGroupModel(Injector injector) {
        return SingletonMap.get(ForumModel.class, () -> new ForumModelImpl(injector));
    }
}
