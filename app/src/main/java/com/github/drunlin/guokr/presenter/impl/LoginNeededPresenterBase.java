package com.github.drunlin.guokr.presenter.impl;

import android.support.annotation.CallSuper;

import com.github.drunlin.guokr.model.UserModel;
import com.github.drunlin.guokr.view.LoginNeededView;

import javax.inject.Inject;

/**
 * @author drunlin@outlook.com
 */
public abstract class LoginNeededPresenterBase<V extends LoginNeededView> extends PresenterBase<V> {
    @Inject UserModel userModel;

    @CallSuper
    @Override
    public void onViewCreated(boolean firstCreated) {
        bind(userModel.checkTokenFailed(), view::onLoginStateInvalid);
    }
}
