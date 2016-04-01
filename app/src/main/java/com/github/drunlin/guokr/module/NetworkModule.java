package com.github.drunlin.guokr.module;

import com.github.drunlin.guokr.model.NetworkModel;
import com.github.drunlin.guokr.model.impl.NetworkModelImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author drunlin@outlook.com
 */
@Module(library = true)
@SuppressWarnings("unused")
public class NetworkModule {
    @Provides
    @Singleton
    NetworkModel provideNetwork() {
        return new NetworkModelImpl();
    }
}
