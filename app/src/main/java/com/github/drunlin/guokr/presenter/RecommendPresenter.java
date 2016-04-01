package com.github.drunlin.guokr.presenter;

/**
 * @author drunlin@outlook.com
 */
public interface RecommendPresenter {
    /**
     * 推荐链接并附加评论。
     * @param comment
     */
    void recommend(String comment);

    interface Factory {
        RecommendPresenter create(String url, String title, String summary);
    }
}
