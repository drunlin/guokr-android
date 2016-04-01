package com.github.drunlin.guokr.presenter.impl;

import com.github.drunlin.guokr.model.PreferenceModel;
import com.github.drunlin.guokr.presenter.MainPresenter;
import com.github.drunlin.guokr.view.MainView;

import javax.inject.Inject;

/**
 * @author drunlin@outlook.com
 */
public class MainPresenterImpl extends PresenterBase<MainView> implements MainPresenter {
    @Inject PreferenceModel preferenceModel;

    @Override
    public void onViewCreated(boolean firstCreated) {
        if (firstCreated) {
            if (preferenceModel.isSavedPagePosition()) {
                view.setPagePosition(preferenceModel.getPagePosition());
            } else {
                view.setPagePosition(0);
            }
        }

        view.setNightModeEnable(preferenceModel.isNightMode());
    }

    @Override
    public void onViewHomePage() {
        view.viewHomePage("http://guokr.com");
    }

    @Override
    public void savePagePosition(int position) {
        //即使没有启用保存状态也应该保存，这样让保存状态立即生效
        preferenceModel.setPagePosition(position);
    }

    @Override
    public void toggleDayNightMode() {
        boolean nightMode = !preferenceModel.isNightMode();
        preferenceModel.setNightMode(nightMode);
        view.setNightModeEnable(nightMode);
    }
}
