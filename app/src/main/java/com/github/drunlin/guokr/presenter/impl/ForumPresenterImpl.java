package com.github.drunlin.guokr.presenter.impl;

import com.github.drunlin.guokr.model.UserModel;
import com.github.drunlin.guokr.presenter.ForumPresenter;
import com.github.drunlin.guokr.view.ForumView;

import javax.inject.Inject;

/**
 * @author drunlin@outlook.com
 */
public class ForumPresenterImpl extends PresenterBase<ForumView> implements ForumPresenter {
    @Inject UserModel userModel;

    @Override
    public void onViewCreated(boolean firstCreated) {
        super.onViewCreated(firstCreated);

        bind(userModel.loginStateChanged(), view::setLoginStatus);

        view.setLoginStatus(userModel.isLoggedIn());
    }
}
