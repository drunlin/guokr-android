package com.github.drunlin.guokr.module;

import com.github.drunlin.guokr.model.PreferenceModel;
import com.github.drunlin.guokr.model.impl.PreferenceModelImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author drunlin@outlook.com
 */
@Module(injects = PreferenceModel.class, library = true)
@SuppressWarnings("unused")
public class PreferenceModule {
    @Provides
    @Singleton
    PreferenceModel providePreferenceModel() {
        return new PreferenceModelImpl();
    }
}
