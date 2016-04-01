package com.github.drunlin.guokr.model;

import android.support.annotation.Nullable;

import com.github.drunlin.guokr.bean.ArticleType;

import java.util.List;

/**
 * 管理所有文章列表。
 *
 * @author drunlin@outlook.com
 */
public interface MinisiteModel {
    /**
     * 请求类型对应文章列表。
     * @param type
     */
    @Nullable
    ArticleListModel getArticles(ArticleType type);

    /**
     * 请求类型对应文章列表。
     * @param key
     * @return
     */
    @Nullable
    ArticleListModel getArticles(String key);

    /**
     * 文章的所有类型。
     * @return
     */
    List<ArticleType> getTypes();

    /**
     * 获取文章数据。
     * @param articleId
     * @return
     */
    ArticleModel getArticle(int articleId);
}
