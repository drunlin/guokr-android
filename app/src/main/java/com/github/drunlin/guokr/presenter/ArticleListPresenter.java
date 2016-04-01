package com.github.drunlin.guokr.presenter;

import com.github.drunlin.guokr.bean.ArticleEntry;
import com.github.drunlin.guokr.bean.ArticleType;

/**
 * @author drunlin@outlook.com
 */
public interface ArticleListPresenter extends TopicListPresenter {
    /**
     * 响应浏览文章内容。
     * @param entry
     */
    void onViewArticle(ArticleEntry entry);

    /**
     * 响应浏览文章评论。
     * @param entry
     */
    void onViewArticleReplies(ArticleEntry entry);

    /**
     * 响应浏览文章列表。
     * @param type
     */
    void onViewArticles(ArticleType type);

    interface Factory {
        ArticleListPresenter create(String key);
    }
}
