package com.github.drunlin.guokr.model;

import com.github.drunlin.guokr.bean.Basket;
import com.github.drunlin.signals.impl.Signal1;
import com.github.drunlin.signals.impl.Signal2;

import java.util.List;

/**
 * 果篮的获取和创建。
 *
 * @author drunlin@outlook.com
 */
public interface BasketModel {
    /**
     * 请求已有的果篮。
     */
    void requestBaskets();

    /**
     * 果篮列表加载完成。
     * @return [resultCode, baskets]
     */
    Signal2<Integer, List<Basket>> basketsResulted();

    /**
     * 加载成功的果篮数据。
     * @return
     */
    List<Basket> getBaskets();

    /**
     * 请求果篮分类。
     */
    void requestCategories();

    /**
     * 分类数据加载完成。
     * @return [resultCode, basketCategories]
     */
    Signal2<Integer, List<Basket.Category>> categoriesResulted();

    /**
     * 分类数量是否在加载中。
     * @return
     */
    boolean isCategoriesLoading();

    /**
     * 成功加载的分类数据。
     * @return
     */
    List<Basket.Category> getCategories();

    /**
     * 添加到果篮。
     * @param link
     * @param title
     * @param basketId
     */
    void addToBasket(String link, String title, int basketId);

    /**
     * 收藏链接的结果。
     * @return [resultCode, basketId]
     */
    Signal2<Integer, Integer> addToBasketResulted();

    /**
     * 创建新的果篮。
     * @param title
     * @param introduction
     * @param category
     */
    void createBasket(String title, String introduction, int category);

    /**
     * 创建果篮的结果。
     * @return [resultCode]
     */
    Signal1<Integer> createBasketResulted();
}
