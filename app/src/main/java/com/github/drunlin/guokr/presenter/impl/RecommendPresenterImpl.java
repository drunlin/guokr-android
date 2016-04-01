package com.github.drunlin.guokr.presenter.impl;

import com.github.drunlin.guokr.bean.ResponseCode;
import com.github.drunlin.guokr.model.UserModel;
import com.github.drunlin.guokr.presenter.RecommendPresenter;
import com.github.drunlin.guokr.view.RecommendView;

import javax.inject.Inject;

/**
 * @author drunlin@outlook.com
 */
public class RecommendPresenterImpl
        extends LoginNeededPresenterBase<RecommendView> implements RecommendPresenter {

    @Inject UserModel userModel;

    private final String title;
    private final String summary;
    private final String url;

    public RecommendPresenterImpl(String url, String title, String summary) {
        this.url = url;
        this.title = title;
        this.summary = summary;
    }

    @Override
    public void onViewCreated(boolean firstCreated) {
        super.onViewCreated(firstCreated);

        bind(userModel.recommendResulted(), this::onRecommendResult);
    }

    @Override
    public void recommend(String comment) {
        userModel.recommendLink(url, title, summary, comment);
    }

    private void onRecommendResult(int resultCode) {
        if (resultCode == ResponseCode.OK) {
            view.onRecommendSucceed();
        } else {
            view.onRecommendFailed();
        }
    }
}
