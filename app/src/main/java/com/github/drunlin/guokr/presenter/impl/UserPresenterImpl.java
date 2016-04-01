package com.github.drunlin.guokr.presenter.impl;

import com.github.drunlin.guokr.model.UserModel;
import com.github.drunlin.guokr.presenter.UserPresenter;
import com.github.drunlin.guokr.view.UserView;

import javax.inject.Inject;

import static com.github.drunlin.guokr.util.JavaUtil.call;

/**
 * @author drunlin@outlook.com
 */
public class UserPresenterImpl extends PresenterBase<UserView> implements UserPresenter {
    @Inject UserModel userModel;

    @Override
    public void onViewCreated(boolean firstCreated) {
        super.onViewCreated(firstCreated);

        call(userModel.getUserInfo(), view::setUserInfo);
    }

    @Override
    public void logout() {
        userModel.logout();

        if (!userModel.isLoggedIn()) {
            view.onLoggedOut();
        }
    }
}
