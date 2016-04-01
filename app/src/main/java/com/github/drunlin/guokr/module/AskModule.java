package com.github.drunlin.guokr.module;

import com.github.drunlin.guokr.model.AskModel;
import com.github.drunlin.guokr.model.QuestionListModel;
import com.github.drunlin.guokr.model.impl.AskModelImpl;
import com.github.drunlin.guokr.model.impl.QuestionListModelImpl;
import com.github.drunlin.guokr.model.impl.QuestionModelImpl;
import com.github.drunlin.guokr.module.tool.Injector;
import com.github.drunlin.guokr.module.tool.SingletonMap;
import com.github.drunlin.guokr.presenter.QuestionListPresenter;
import com.github.drunlin.guokr.presenter.QuestionPresenter;
import com.github.drunlin.guokr.presenter.impl.QuestionListPresenterImpl;
import com.github.drunlin.guokr.presenter.impl.QuestionPresenterImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author drunlin@outlook.com
 */
@Module(injects = {
        QuestionListPresenterImpl.class,
        QuestionListPresenter.class,
        QuestionPresenter.Factory.class,
        QuestionPresenterImpl.class,
        QuestionListModelImpl.class,
        QuestionModelImpl.class,
        AskModelImpl.class
}, complete = false)
@SuppressWarnings("unused")
public class AskModule {
    @Provides
    QuestionListPresenter provideQuestionListPresenter() {
        return new QuestionListPresenterImpl();
    }

    @Singleton
    @Provides
    QuestionPresenter.Factory provideQuestionPresenter() {
        return QuestionPresenterImpl::new;
    }

    @Provides
    QuestionListModel provideQuestionListModel(Injector injector) {
        return SingletonMap.get(QuestionListModel.class, () -> new QuestionListModelImpl(injector));
    }

    @Provides
    AskModel provideAskMode(Injector injector) {
        return SingletonMap.get(AskModel.class, () -> new AskModelImpl(injector));
    }
}
