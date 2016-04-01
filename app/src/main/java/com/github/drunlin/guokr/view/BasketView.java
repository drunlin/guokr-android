package com.github.drunlin.guokr.view;

import com.github.drunlin.guokr.bean.Basket;

import java.util.List;

/**
 * 收藏链接的界面，也就是果壳网的果篮。
 *
 * @author drunlin@outlook.com
 */
public interface BasketView extends LoginNeededView {
    /**
     * 设置要显示的果篮。
     * @param baskets
     */
    void setBaskets(List<Basket> baskets);

    /**
     * 果蓝加载失败。
     */
    void onBasketsLoadFailed();

    /**
     * 设置新建果篮时需要的分类。
     * @param categories
     */
    void setCategories(List<Basket.Category> categories);

    /**
     * 果篮的分类还在加载中。
     */
    void onCategoriesLoading();

    /**
     * 分类加载失败。
     */
    void onCategoriesLoadFailed();

    /**
     * 创建果篮。
     */
    void onCreateBasket();

    /**
     * 成功创建了果篮。
     */
    void onCreateBasketSucceed();

    /**
     * 创建果篮失败。
     */
    void onCreateBasketFailed();

    /**
     * 收藏失败。也就是添加到果篮失败。
     */
    void onFavorLinkFailed();

    /**
     * 更新某个果篮的状态。
     * @param position
     */
    void updateBasket(int position);
}
