package com.github.drunlin.guokr.fragment;

import com.github.drunlin.guokr.bean.ArticleType;
import com.github.drunlin.guokr.test.FragmentTestCase;

import java.util.ArrayList;
import java.util.List;

import static com.github.drunlin.guokr.bean.ArticleType.CHANNEL;

/**
 * @author drunlin@outlook.com
 */
public class MinisiteFragmentTest extends FragmentTestCase<MinisiteFragment> {
    @Override
    protected void init() throws Throwable {
        addToActivity(new MinisiteFragment());
    }

    @Override
    protected void onPreview() throws Throwable {
        List<ArticleType> types = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            types.add(new ArticleType(CHANNEL, String.format("%d|item%d", i, i)));
        }
        runOnUiThread(() -> fragment.setTypes(types));
    }
}
