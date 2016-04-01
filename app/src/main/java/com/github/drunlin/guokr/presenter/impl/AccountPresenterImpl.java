package com.github.drunlin.guokr.presenter.impl;

import com.github.drunlin.guokr.bean.UserInfo;
import com.github.drunlin.guokr.model.UserModel;
import com.github.drunlin.guokr.presenter.AccountPresenter;
import com.github.drunlin.guokr.view.AccountView;

import javax.inject.Inject;

/**
 * @author drunlin@outlook.com
 */
public class AccountPresenterImpl extends PresenterBase<AccountView> implements AccountPresenter {
    @Inject UserModel userModel;

    @Override
    public void onViewCreated(boolean firstCreated) {
        bind(userModel.userInfoChanged(), this::onUserInfoChange);

        onUserInfoChange();
    }

    private void onUserInfoChange() {
        UserInfo userInfo = userModel.getUserInfo();
        if (userInfo != null) {
            view.setUserInfo(userInfo);
        } else {
            view.onLoggedOut();
        }
    }

    @Override
    public void onViewUserInfo() {
        if (userModel.isLoggedIn()) {
            view.viewUserInfo();
        } else {
            view.login();
        }
    }
}
