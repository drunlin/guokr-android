package com.github.drunlin.guokr.view;

import com.github.drunlin.guokr.bean.ArticleContent;

/**
 * 文章内容和评论。
 *
 * @author drunlin@outlook.com
 */
public interface ArticleView extends TopicView<ArticleContent> {
    /**
     * 推荐当前文章。
     * @param title
     * @param summary
     * @param url
     */
    void recommend(String title, String summary, String url);
}
