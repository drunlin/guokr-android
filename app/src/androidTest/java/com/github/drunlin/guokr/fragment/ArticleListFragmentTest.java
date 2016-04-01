package com.github.drunlin.guokr.fragment;

import android.util.Log;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.bean.ArticleEntry;
import com.github.drunlin.guokr.bean.ArticleType;
import com.github.drunlin.guokr.bean.ImageData;
import com.github.drunlin.guokr.bean.ResultClassMap.ArticlesResult;
import com.github.drunlin.guokr.test.FragmentTestCase;

import java.util.List;

import static com.github.drunlin.guokr.test.util.TestUtils.getBitmapData;
import static com.github.drunlin.guokr.test.util.TestUtils.getResult;
import static java.util.Collections.singletonList;

public class ArticleListFragmentTest extends FragmentTestCase<ArticleListFragment> {
    private static final String TAG = ArticleListFragmentTest.class.getSimpleName();

    @Override
    protected void init() throws Throwable {
        addToActivity(new ArticleListFragment("hot"));
    }

    @Override
    protected void onPreview() throws Throwable {
        List<ArticleEntry> entries = getResult(ArticlesResult.class, R.raw.article_list);
        for (ArticleEntry entry : entries) {
            entry.image.data = new ImageData(setter -> {
                setter.set(getBitmapData(R.drawable.img_png));
                return () -> Log.d(TAG, "ImageData$Loader.cancel() - " + entry.title);
            });
            entry.dateCreated = "2015-01-01";
            entry.labels = singletonList(new ArticleType(ArticleType.CHANNEL, "hot|热点"));
        }
        runOnUiThread(() -> fragment.setData(entries));
    }
}
