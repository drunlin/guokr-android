package com.github.drunlin.guokr.presenter.impl;

import com.github.drunlin.guokr.model.UserModel;
import com.github.drunlin.guokr.presenter.LoginPresenter;
import com.github.drunlin.guokr.view.LoginView;

import javax.inject.Inject;

/**
 * @author drunlin@outlook.com
 */
public class LoginPresenterImpl extends PresenterBase<LoginView> implements LoginPresenter {
    @Inject UserModel userModel;

    @Override
    public void setCookie(String cookie) {
        userModel.setCookie(cookie);

        if (userModel.isLoggedIn()) {
            view.onLoggedIn();
        }
    }
}
