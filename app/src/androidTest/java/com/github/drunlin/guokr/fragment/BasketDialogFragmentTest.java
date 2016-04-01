package com.github.drunlin.guokr.fragment;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.bean.Basket;
import com.github.drunlin.guokr.bean.ResultClassMap.BasketCategoryResult;
import com.github.drunlin.guokr.bean.ResultClassMap.BasketsResult;
import com.github.drunlin.guokr.test.FragmentTestCase;

import java.util.List;

import static com.github.drunlin.guokr.test.util.Constants.TITLE;
import static com.github.drunlin.guokr.test.util.Constants.URL;
import static com.github.drunlin.guokr.test.util.TestUtils.getResult;

/**
 * @author drunlin@outlook.com
 */
public class BasketDialogFragmentTest extends FragmentTestCase<BasketDialogFragment> {
    @Override
    protected void initOnUiThread() throws Throwable {
        fragment = BasketDialogFragment.show(activity.getSupportFragmentManager(), TITLE, URL);
    }

    @Override
    public void onPreview() throws Throwable {
        List<Basket> baskets = getResult(BasketsResult.class, R.raw.basket_list);
        baskets.get(0).added = true;
        List<Basket.Category> categories =
                getResult(BasketCategoryResult.class, R.raw.basket_category);
        runOnUiThread(() -> {
            fragment.setBaskets(baskets);
            fragment.setCategories(categories);
            fragment.onCreateBasket();
        });
    }
}
