package com.github.drunlin.guokr.presenter;

/**
 * @author drunlin@outlook.com
 */
public interface BasketPresenter {
    /**
     * 收藏到果蓝。
     * @param id
     */
    void addToBasket(int id);

    /**
     * 准备创建果篮。
     */
    void onPreCreateBasket();

    /**
     * 创建新的果篮
     * @param categoryId 从果壳上获取的分类
     * @param name 果篮的名字
     * @param description 果篮的描述
     */
    void createBasket(int categoryId, String name, String description);

    interface Factory {
        BasketPresenter create(String title, String url);
    }
}
