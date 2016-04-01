package com.github.drunlin.guokr.view;

/**
 * 推荐链接到关注自己的人。
 *
 * @author drunlin@outlook.com
 */
public interface RecommendView extends LoginNeededView {
    /**
     * 推荐成功。
     */
    void onRecommendSucceed();

    /**
     * 推荐失败。
     */
    void onRecommendFailed();
}
