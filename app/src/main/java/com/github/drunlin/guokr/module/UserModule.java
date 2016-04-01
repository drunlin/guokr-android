package com.github.drunlin.guokr.module;

import com.github.drunlin.guokr.model.BasketModel;
import com.github.drunlin.guokr.model.MessageModel;
import com.github.drunlin.guokr.model.NoticeModel;
import com.github.drunlin.guokr.model.UserModel;
import com.github.drunlin.guokr.model.impl.BasketModelImpl;
import com.github.drunlin.guokr.model.impl.MessageModelImpl;
import com.github.drunlin.guokr.model.impl.NoticeModelImpl;
import com.github.drunlin.guokr.model.impl.UserModelImpl;
import com.github.drunlin.guokr.module.tool.Injector;
import com.github.drunlin.guokr.module.tool.SingletonMap;
import com.github.drunlin.guokr.presenter.AccountPresenter;
import com.github.drunlin.guokr.presenter.BasketPresenter;
import com.github.drunlin.guokr.presenter.LoginPresenter;
import com.github.drunlin.guokr.presenter.LoginPromptPresenter;
import com.github.drunlin.guokr.presenter.MessageListPresenter;
import com.github.drunlin.guokr.presenter.NoticeListPresenter;
import com.github.drunlin.guokr.presenter.NoticePresenter;
import com.github.drunlin.guokr.presenter.RecommendPresenter;
import com.github.drunlin.guokr.presenter.UserPresenter;
import com.github.drunlin.guokr.presenter.impl.AccountPresenterImpl;
import com.github.drunlin.guokr.presenter.impl.BasketPresenterImpl;
import com.github.drunlin.guokr.presenter.impl.LoginPresenterImpl;
import com.github.drunlin.guokr.presenter.impl.LoginPromptPresenterImpl;
import com.github.drunlin.guokr.presenter.impl.MessageListPresenterImpl;
import com.github.drunlin.guokr.presenter.impl.NoticeListPresenterImpl;
import com.github.drunlin.guokr.presenter.impl.NoticePresenterImpl;
import com.github.drunlin.guokr.presenter.impl.RecommendPresenterImpl;
import com.github.drunlin.guokr.presenter.impl.UserPresenterImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author drunlin@outlook.com
 */
@Module(injects = {
        AccountPresenterImpl.class,
        AccountPresenter.class,
        BasketPresenterImpl.class,
        BasketPresenter.Factory.class,
        RecommendPresenter.Factory.class,
        RecommendPresenterImpl.class,
        LoginPresenterImpl.class,
        LoginPresenter.class,
        LoginPromptPresenterImpl.class,
        LoginPromptPresenter.class,
        NoticeListPresenterImpl.class,
        NoticeListPresenter.class,
        NoticePresenterImpl.class,
        NoticePresenter.class,
        MessageListPresenterImpl.class,
        MessageListPresenter.class,
        UserPresenterImpl.class,
        UserPresenter.class,
        UserModelImpl.class,
        BasketModelImpl.class,
        NoticeModelImpl.class,
        MessageModelImpl.class,
}, complete = false)
@SuppressWarnings("unused")
public class UserModule {
    @Provides
    AccountPresenter provideAccountPresenter() {
        return new AccountPresenterImpl();
    }

    @Singleton
    @Provides
    BasketPresenter.Factory provideBasketPresenter() {
        return BasketPresenterImpl::new;
    }

    @Singleton
    @Provides
    RecommendPresenter.Factory provideRecommendPresenter() {
        return RecommendPresenterImpl::new;
    }

    @Provides
    LoginPresenter provideLoginPresenter() {
        return new LoginPresenterImpl();
    }

    @Provides
    LoginPromptPresenter provideLoginTipPresenter() {
        return new LoginPromptPresenterImpl();
    }

    @Provides
    NoticeListPresenter provideNoticeListPresenter() {
        return new NoticeListPresenterImpl();
    }

    @Provides
    NoticePresenter provideNoticePresenter() {
        return new NoticePresenterImpl();
    }

    @Provides
    MessageListPresenter provideMessageListPresenter() {
        return new MessageListPresenterImpl();
    }

    @Provides
    UserPresenter provideUserPresenter() {
        return new UserPresenterImpl();
    }

    @Provides
    UserModel provideUserModel(Injector injector) {
        return SingletonMap.get(UserModel.class, () -> new UserModelImpl(injector));
    }

    @Provides
    BasketModel provideBasketModel(Injector injector) {
        return SingletonMap.get(BasketModel.class, () -> new BasketModelImpl(injector));
    }

    @Provides
    NoticeModel provideNoticeModel(Injector injector) {
        return SingletonMap.get(NoticeModel.class, () -> new NoticeModelImpl(injector));
    }

    @Provides
    MessageModel provideMessageModel(Injector injector) {
        return SingletonMap.get(MessageModel.class, () -> new MessageModelImpl(injector));
    }
}
