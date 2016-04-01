package com.github.drunlin.guokr.presenter.impl;

import com.github.drunlin.guokr.bean.ArticleEntry;
import com.github.drunlin.guokr.bean.ArticleType;
import com.github.drunlin.guokr.model.ArticleListModel;
import com.github.drunlin.guokr.model.MinisiteModel;
import com.github.drunlin.guokr.presenter.ArticleListPresenter;
import com.github.drunlin.guokr.view.ArticleListView;

import javax.inject.Inject;

/**
 * @author drunlin@outlook.com
 */
public class ArticleListPresenterImpl
        extends TopicListPresenterBase<ArticleEntry, ArticleListModel, ArticleListView>
        implements ArticleListPresenter {

    @Inject MinisiteModel minisiteModel;

    /**当前列表的文章类型。*/
    private final String articleKey;

    public ArticleListPresenterImpl(String key) {
        articleKey = key;
    }

    @Override
    protected ArticleListModel getModel() {
        return minisiteModel.getArticles(articleKey);
    }

    @Override
    public void onViewArticle(ArticleEntry entry) {
        view.viewArticle(entry.id);
    }

    @Override
    public void onViewArticleReplies(ArticleEntry entry) {
        view.viewArticleReplies(entry.id);
    }

    @Override
    public void onViewArticles(ArticleType type) {
        view.viewArticles(type.key);
    }
}
