package com.github.drunlin.guokr.model.impl;

import com.github.drunlin.guokr.bean.Basket;
import com.github.drunlin.guokr.bean.ResponseCode;
import com.github.drunlin.guokr.bean.ResultClassMap;
import com.github.drunlin.guokr.bean.ResultClassMap.BasketCategoryResult;
import com.github.drunlin.guokr.bean.ResultClassMap.BasketResult;
import com.github.drunlin.guokr.model.BasketModel;
import com.github.drunlin.guokr.model.Model;
import com.github.drunlin.guokr.model.NetworkModel;
import com.github.drunlin.guokr.model.UserModel;
import com.github.drunlin.guokr.model.request.AccessRequest;
import com.github.drunlin.guokr.module.tool.Injector;
import com.github.drunlin.signals.impl.Signal1;
import com.github.drunlin.signals.impl.Signal2;

import java.util.List;

import javax.inject.Inject;

import static com.android.volley.Request.Method.POST;
import static com.github.drunlin.guokr.util.JavaUtil.find;

/**
 * 果篮相关的数据。
 *
 * @author drunlin@outlook.com
 */
public class BasketModelImpl extends Model implements BasketModel {
    private final Signal2<Integer, List<Basket.Category>> categoriesResulted = new Signal2<>();
    private final Signal1<Integer> createBasketResulted = new Signal1<>();
    private final Signal2<Integer, Integer>favorLinkResulted = new Signal2<>();
    private final Signal2<Integer, List<Basket>> basketsResulted = new Signal2<>();

    @Inject NetworkModel networkModel;
    @Inject UserModel userModel;

    private boolean isLoadingCategory;

    private List<Basket> baskets;
    private List<Basket.Category> categories;

    public BasketModelImpl(Injector injector) {
        super(injector);
    }

    @Override
    public void requestCategories() {
        final String url = "http://www.guokr.com/apis/favorite/category.json";
        new AccessRequest.Builder<>(url, BasketCategoryResult.class, userModel.getToken())
                .setListener(response -> {
                    isLoadingCategory = false;
                    categories = response.result;
                    categoriesResulted.dispatch(ResponseCode.OK, response.result);
                })
                .setErrorListener(error -> {
                    isLoadingCategory = false;
                    categoriesResulted.dispatch(error.getCode(), null);
                })
                .build(networkModel);

        isLoadingCategory = true;
    }

    @Override
    public Signal2<Integer, List<Basket.Category>> categoriesResulted() {
        return categoriesResulted;
    }

    @Override
    public boolean isCategoriesLoading() {
        return isLoadingCategory;
    }

    @Override
    public List<Basket.Category> getCategories() {
        return categories;
    }

    @Override
    public void createBasket(String title, String introduction, int category) {
        final String url = "http://www.guokr.com/apis/favorite/basket.json";
        new AccessRequest.Builder<>(url, BasketResult.class, userModel.getToken())
                .setMethod(POST)
                .addParam("title", title)
                .addParam("introduction", introduction)
                .addParam("category_id", category)
                .setListener(response -> onCreateBasketSucceed(response.result))
                .setErrorListener(error -> createBasketResulted.dispatch(error.getCode()))
                .build(networkModel);
    }

    private void onCreateBasketSucceed(Basket basket) {
        if (baskets != null) {
            baskets.add(basket);
        } else {
            requestBaskets();
        }

        createBasketResulted.dispatch(ResponseCode.OK);
    }

    @Override
    public Signal1<Integer> createBasketResulted() {
        return createBasketResulted;
    }

    @Override
    public void requestBaskets() {
        final String url = "http://www.guokr.com/apis/favorite/basket.json";
        new AccessRequest.Builder<>(url, ResultClassMap.BasketsResult.class, userModel.getToken())
                .addParam("retrieve_type", "by_ukey")
                .addParam("ukey", userModel.getUserKey())
                .addParam("t", System.currentTimeMillis())
                .setListener(r -> basketsResulted.dispatch(ResponseCode.OK, baskets = r.result))
                .setErrorListener(e -> basketsResulted.dispatch(e.getCode(), null))
                .build(networkModel);
    }

    @Override
    public Signal2<Integer, List<Basket>> basketsResulted() {
        return basketsResulted;
    }

    @Override
    public List<Basket> getBaskets() {
        return baskets;
    }

    @Override
    public void addToBasket(String link, String title, int basketId) {
        final String url = "http://www.guokr.com/apis/favorite/link.json";
        new AccessRequest.SimpleRequestBuilder(url, userModel.getToken())
                .setMethod(POST)
                .addParam("title", title)
                .addParam("url", link)
                .addParam("basket_id", basketId)
                .setListener(response -> onFavorLinkSucceed(basketId))
                .setErrorListener(error -> favorLinkResulted.dispatch(error.getCode(), basketId))
                .build(networkModel);
    }

    private void onFavorLinkSucceed(int basketId) {
        find(baskets, v -> v.id == basketId, v -> v.added = true);

        favorLinkResulted.dispatch(ResponseCode.OK, basketId);
    }

    @Override
    public Signal2<Integer, Integer> addToBasketResulted() {
        return favorLinkResulted;
    }
}
