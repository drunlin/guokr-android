package com.github.drunlin.guokr.module.tool;

import android.os.Bundle;
import android.util.Log;

import com.github.drunlin.guokr.BuildConfig;
import com.github.drunlin.guokr.module.AppModule;
import com.github.drunlin.guokr.presenter.Presenter;
import com.github.drunlin.guokr.presenter.impl.PresenterBase;
import com.github.drunlin.guokr.util.JavaUtil.Converter;

/**
 * 针对Android平台定制的View生命周期管理类。
 *
 * @author drunlin@outlook.com
 */
public class ViewLifecycleImpl implements ViewLifecycle {
    private static final String TAG = ViewLifecycleImpl.class.getName();

    private static final Presenter NULL = new PresenterBase() {};

    private static final String STATE_NONE = "none";
    private static final String STATE_BIND = "bind";
    private static final String STATE_CREATED = "created";

    private final Object view;

    private Presenter presenter = NULL;

    private String state = STATE_NONE;
    private boolean savedInstanceState;

    public ViewLifecycleImpl(Object view) {
        this.view = view;
    }

    /**
     * 检查状态是否正确。
     * @param expectState 应该有的状态
     * @param nextState 将要成为的状态
     * @param methodName 调用的函数名。
     */
    private void checkState(String expectState, String nextState, String methodName) {
        if (!state.equals(expectState)) {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, String.format("Error state : %s at %s()", state, methodName));
            }
        }

        state = nextState;
    }

    private <P> P bind(P presenter) {
        checkState(STATE_NONE, STATE_BIND, "bind");

        this.presenter = (Presenter) presenter;
        AppModule.injector.inject(presenter);
        //noinspection unchecked
        ((Presenter) presenter).onCreate(view);
        return presenter;
    }

    @Override
    public <P> P bind(Class<? extends P> presenterClass) {
        return bind(AppModule.injector.get(presenterClass));
    }

    @Override
    public <P, F> P bind(Class<F> factoryClass, Converter<F, P> delegate) {
        F factory = AppModule.injector.get(factoryClass);
        return bind(delegate.convert(factory));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        checkState(STATE_BIND, STATE_CREATED, "onCreate");

        presenter.onViewCreated(savedInstanceState == null);
    }

    @Override
    public void onSaveInstanceState() {
        checkState(STATE_CREATED, STATE_CREATED, "onSaveInstanceState");

        savedInstanceState = true;
    }

    @Override
    public void onDestroy() {
        checkState(STATE_CREATED, STATE_NONE, "onDestroy");

        if (savedInstanceState) {
            savedInstanceState = false;

            presenter.onViewDestroyed();
        }

        presenter.onDestroy();
    }
}
