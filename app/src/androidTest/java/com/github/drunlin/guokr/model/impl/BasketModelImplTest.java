package com.github.drunlin.guokr.model.impl;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.test.ModelTestCase;

import org.junit.Test;

import static com.github.drunlin.guokr.mock.MockNetworkModel.post;
import static com.github.drunlin.guokr.mock.MockNetworkModel.stubFor;
import static com.github.drunlin.guokr.mock.MockNetworkModel.urlContainers;
import static com.github.drunlin.guokr.mock.MockNetworkModel.urlEqualTo;
import static com.github.drunlin.guokr.test.ModelAsserts.assertOkResult;
import static com.github.drunlin.guokr.test.util.Constants.ACCESS_TOKEN;
import static com.github.drunlin.guokr.test.util.Constants.BASKET_ID;
import static com.github.drunlin.guokr.test.util.Constants.TITLE;
import static com.github.drunlin.guokr.test.util.Constants.URL;
import static com.github.drunlin.guokr.test.util.Constants.USER_KEY;
import static com.github.drunlin.guokr.test.util.TestUtils.getText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author drunlin@outlook.com
 */
public class BasketModelImplTest extends ModelTestCase<BasketModelImpl> {
    @Override
    protected void init() throws Throwable {
        model = new BasketModelImpl(injector);
    }

    @Test
    public void requestCategories() throws Exception {
        stubFor(urlEqualTo("http://www.guokr.com/apis/favorite/category.json" +
                "?access_token=%s", ACCESS_TOKEN)).setBody(getText(R.raw.basket_category));

        model.categoriesResulted().add((a, b) -> {
            assertOkResult(a);
            assertThat(b, allOf(notNullValue(), hasSize(greaterThan(0))));
            countDown();
        });

        model.requestCategories();
        await();
    }

    @Test
    public void categoriesResulted() throws Exception {
        assertNotNull(model.categoriesResulted());
    }

    @Test
    public void isCategoriesLoading() throws Exception {
        assertFalse(model.isCategoriesLoading());

        model.categoriesResulted().add((v1, v2) -> countDown());
        model.requestCategories();

        assertTrue(model.isCategoriesLoading());

        await();

        assertFalse(model.isCategoriesLoading());
    }

    @Test
    public void getCategories() throws Exception {
        assertNull(model.getCategories());

        requestCategories();

        assertNotNull(model.getCategories());
    }

    @Test
    public void createBasket() throws Exception {
        final String introduction = "introduction";

        stubFor(post("title=%s&introduction=%s&category_id=%d&access_token=%s",
                TITLE, introduction, BASKET_ID, ACCESS_TOKEN),
                urlEqualTo("http://www.guokr.com/apis/favorite/basket.json"))
                .setBody(getText(R.raw.basket_create));

        model.createBasketResulted().add(v -> {
            assertOkResult(v);
            countDown();
        });

        model.createBasket(TITLE, introduction, BASKET_ID);
        await();
    }

    @Test
    public void createBasketResulted() throws Exception {
        assertNotNull(model.createBasketResulted());
    }

    @Test
    public void requestBaskets() throws Exception {
        stubFor(urlContainers("http://www.guokr.com/apis/favorite/basket.json" +
                "?retrieve_type=by_ukey&ukey=%s&access_token=%s", USER_KEY, ACCESS_TOKEN))
                .setBody(getText(R.raw.basket_list));

        model.basketsResulted().add((a, b) -> {
            assertOkResult(a);
            assertThat(b, allOf(notNullValue(), hasSize(greaterThan(0))));
            countDown();
        });

        model.requestBaskets();
        await();
    }

    @Test
    public void getBaskets() throws Exception {
        assertNull(model.getBaskets());

        requestBaskets();

        assertNotNull(model.getBaskets());
    }

    @Test
    public void basketsResulted() throws Exception {
        assertNotNull(model.basketsResulted());
    }

    @Test
    public void addToBasket() throws Exception {
        stubFor(post("basket_id=%d&url=%s&title=%s&access_token=%s",
                BASKET_ID, URL, TITLE, ACCESS_TOKEN),
                urlEqualTo("http://www.guokr.com/apis/favorite/link.json"))
                .setBody(getText(R.raw.favor_link));

        model.addToBasketResulted().add((a, b) -> {
            assertOkResult(a);
            countDown();
        });

        model.addToBasket(URL, TITLE, BASKET_ID);
        await();
    }

    @Test
    public void addToBasketResulted() throws Exception {
        assertNotNull(model.addToBasketResulted());
    }
}
