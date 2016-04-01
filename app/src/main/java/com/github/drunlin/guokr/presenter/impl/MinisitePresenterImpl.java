package com.github.drunlin.guokr.presenter.impl;

import com.github.drunlin.guokr.model.MinisiteModel;
import com.github.drunlin.guokr.presenter.MinisitePresenter;
import com.github.drunlin.guokr.view.MinisiteView;

import javax.inject.Inject;

import static com.github.drunlin.guokr.util.JavaUtil.indexOf;

/**
 * 管理文章列表
 *
 * @author drunlin@outlook.com
 */
public class MinisitePresenterImpl
        extends PresenterBase<MinisiteView> implements MinisitePresenter {

    @Inject MinisiteModel model;

    @Override
    public void onViewCreated(boolean firstCreated) {
        view.setTypes(model.getTypes());
    }

    @Override
    public void onDisplayArticleList(String key) {
        int position = indexOf(model.getTypes(), v -> v.key.equals(key));
        if (position != -1) {
            view.displayAt(position);
        } else {
            view.onNoSuchTypeError();
        }
    }
}
