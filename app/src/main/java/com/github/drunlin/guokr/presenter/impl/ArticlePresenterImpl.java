package com.github.drunlin.guokr.presenter.impl;

import com.github.drunlin.guokr.bean.ArticleContent;
import com.github.drunlin.guokr.model.ArticleModel;
import com.github.drunlin.guokr.model.MinisiteModel;
import com.github.drunlin.guokr.presenter.ArticlePresenter;
import com.github.drunlin.guokr.view.ArticleView;

import javax.inject.Inject;

import static com.github.drunlin.guokr.util.JavaUtil.call;

/**
 * @author drunlin@outlook.com
 */
public class ArticlePresenterImpl
        extends TopicPresenterBase<ArticleContent, ArticleModel, ArticleView>
        implements ArticlePresenter {

    @Inject MinisiteModel minisiteModel;

    public ArticlePresenterImpl(int id) {
        super(id);
    }

    @Override
    protected ArticleModel getModel() {
        return minisiteModel.getArticle(contentId);
    }

    @Override
    public void onRecommend() {
        if (userModel.checkLoggedIn()) {
            call(model.getContent(), data -> view.recommend(data.title, data.summary, data.url));
        }
    }
}
