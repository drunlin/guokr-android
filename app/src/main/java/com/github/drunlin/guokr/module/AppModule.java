package com.github.drunlin.guokr.module;

import com.github.drunlin.guokr.model.EditorModel;
import com.github.drunlin.guokr.model.JsonListModel;
import com.github.drunlin.guokr.model.impl.EditorModelImpl;
import com.github.drunlin.guokr.module.tool.Injector;
import com.github.drunlin.guokr.module.tool.InjectorImpl;
import com.github.drunlin.guokr.module.tool.SingletonMap;
import com.github.drunlin.guokr.presenter.EditorPresenter;
import com.github.drunlin.guokr.presenter.MainPresenter;
import com.github.drunlin.guokr.presenter.impl.EditorPresenterImpl;
import com.github.drunlin.guokr.presenter.impl.MainPresenterImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;

/**
 * @author drunlin@outlook.com
 */
@Module(injects = {
        EditorPresenterImpl.class,
        EditorModelImpl.class,
        JsonListModel.class,
        EditorPresenter.class,
        MainPresenter.class,
        MainPresenterImpl.class,
}, includes = {
        NetworkModule.class,
        PreferenceModule.class,
        UserModule.class,
        MinisiteModule.class,
        GroupModule.class,
        AskModule.class,
        SearchModule.class
})
@SuppressWarnings("unused")
public class AppModule {
    public static final Injector injector = new InjectorImpl(ObjectGraph.create(new AppModule()));

    @Provides
    @Singleton
    Injector provideInjector() {
        return injector;
    }

    @Provides
    EditorModel provideEditorModel(Injector injector) {
        return SingletonMap.get(EditorModel.class, () -> new EditorModelImpl(injector));
    }

    @Provides
    EditorPresenter provideEditorPresenter() {
        return new EditorPresenterImpl();
    }

    @Provides
    MainPresenter provideMainPresenter() {
        return new MainPresenterImpl();
    }
}
