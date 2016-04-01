package com.github.drunlin.guokr.widget;

import android.view.ViewGroup.LayoutParams;

import com.github.drunlin.guokr.bean.ArticleType;
import com.github.drunlin.guokr.test.WidgetTestCase;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.github.drunlin.guokr.test.util.Constants.TYPE_HOT;

/**
 * @author drunlin@outlook.com
 */
public class ArticleLabelsViewTest extends WidgetTestCase<ArticleLabelsView> {
    @Override
    protected void initOnUiThread() throws Throwable {
        addToScene(new ArticleLabelsView(activity), new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
    }

    @Override
    public void onPreview() throws Throwable {
        List<ArticleType> types = new ArrayList<>();
        types.add(TYPE_HOT);
        types.add(new ArticleType(ArticleType.CHANNEL, "review|评论"));
        types.add(new ArticleType(ArticleType.SUBJECT, "math|数学"));
        runOnUiThread(() -> view.setLabels(types));
    }
}
