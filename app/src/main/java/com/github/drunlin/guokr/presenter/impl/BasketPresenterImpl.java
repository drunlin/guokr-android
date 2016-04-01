package com.github.drunlin.guokr.presenter.impl;

import com.github.drunlin.guokr.bean.Basket;
import com.github.drunlin.guokr.bean.ResponseCode;
import com.github.drunlin.guokr.model.BasketModel;
import com.github.drunlin.guokr.presenter.BasketPresenter;
import com.github.drunlin.guokr.view.BasketView;

import java.util.List;

import javax.inject.Inject;

import static com.github.drunlin.guokr.util.JavaUtil.index;

/**
 * 收藏链接到果篮，只负责创建果篮和收藏链接，链接的内容和标题由外部确定。
 *
 * @author drunlin@outlook.com
 */
public class BasketPresenterImpl
        extends LoginNeededPresenterBase<BasketView> implements BasketPresenter {

    @Inject BasketModel basketModel;

    private final String title;
    private final String link;

    public BasketPresenterImpl(String title, String link) {
        this.title = title;
        this.link = link;
    }

    @Override
    public void onViewCreated(boolean firstCreated) {
        super.onViewCreated(firstCreated);

        bind(basketModel.basketsResulted(), this::onBasketsResult);
        bind(basketModel.categoriesResulted(), this::onCategoriesResult);
        bind(basketModel.createBasketResulted(), this::onCreateBasketResult);
        bind(basketModel.addToBasketResulted(), this::onAddToBasketResult);

        List<Basket> baskets = basketModel.getBaskets();
        if (firstCreated || baskets == null) {
            basketModel.requestBaskets();
        } else {
            view.setBaskets(baskets);

            List<Basket.Category> categories = basketModel.getCategories();
            if (categories != null) {
                view.setCategories(categories);
            } else if (!basketModel.isCategoriesLoading()){
                basketModel.requestCategories();
            }
        }
    }

    private void onBasketsResult(int code, List<Basket> baskets) {
        if (code == ResponseCode.OK) {
            basketModel.requestCategories();

            view.setBaskets(baskets);
        } else {
            view.onBasketsLoadFailed();
        }
    }

    private void onCategoriesResult(int code, List<Basket.Category> categories) {
        if (code == ResponseCode.OK) {
            view.setCategories(categories);
        } else {
            view.onCategoriesLoadFailed();
        }
    }

    @Override
    public void addToBasket(int id) {
        basketModel.addToBasket(link, title, id);
    }

    private void onAddToBasketResult(int code, int id) {
        if (code == ResponseCode.OK) {
            index(basketModel.getBaskets(), b -> b.id == id, i -> view.updateBasket(i));
        } else {
            view.onFavorLinkFailed();
        }
    }

    @Override
    public void onPreCreateBasket() {
        if (basketModel.isCategoriesLoading()) {
            view.onCategoriesLoading();
        }

        view.onCreateBasket();
    }

    @Override
    public void createBasket(int categoryId, String name, String description) {
        basketModel.createBasket(name, description, categoryId);
    }

    private void onCreateBasketResult(int code) {
        if (code == ResponseCode.OK) {
            view.setBaskets(basketModel.getBaskets());

            view.onCreateBasketSucceed();
        } else {
            view.onCreateBasketFailed();
        }
    }
}
