package com.github.drunlin.guokr.presenter.impl;

import com.github.drunlin.guokr.model.UserModel;
import com.github.drunlin.guokr.presenter.LoginPromptPresenter;
import com.github.drunlin.guokr.view.LoginPromptView;

import javax.inject.Inject;

/**
 * @author drunlin@outlook.com
 */
public class LoginPromptPresenterImpl
        extends PresenterBase<LoginPromptView> implements LoginPromptPresenter {

    @Inject UserModel userModel;

    @Override
    public void onViewCreated(boolean firstCreated) {
        if (userModel.isLoggedIn()) {
            view.onLoggedIn();
        } else {
            bind(userModel.loginStateChanged(), this::onLoginStateChange);
        }
    }

    private void onLoginStateChange(boolean login) {
        if (login) {
            view.onLoggedIn();
        }
    }
}
