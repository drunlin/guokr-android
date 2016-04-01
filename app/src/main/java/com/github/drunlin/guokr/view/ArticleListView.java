package com.github.drunlin.guokr.view;

import com.github.drunlin.guokr.bean.ArticleEntry;

/**
 * 单个分类的文章列表。
 *
 * @author drunlin@outlook.com
 */
public interface ArticleListView extends TopicListView<ArticleEntry> {
    /**
     * 浏览指定id的文章。
     * @param articleId
     */
    void viewArticle(int articleId);

    /**
     * 浏览指定id文章的评论。
     * @param articleId
     */
    void viewArticleReplies(int articleId);

    /**
     * 浏览指定分类的文章列表。
     * @param key
     */
    void viewArticles(String key);
}
