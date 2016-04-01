package com.github.drunlin.guokr.presenter;

/**
 * @author drunlin@outlook.com
 */
public interface ArticlePresenter extends TopicPresenter {
    /**
     * 响应推荐当前文章。
     */
    void onRecommend();

    interface Factory {
        ArticlePresenter create(int articleId);
    }
}
