package com.github.drunlin.guokr.widget;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.bean.ArticleContent;
import com.github.drunlin.guokr.bean.ResultClassMap.ArticleContentResult;
import com.github.drunlin.guokr.test.WidgetTestCase;

import org.junit.Test;

import static com.github.drunlin.guokr.test.util.TestUtils.getResult;

/**
 * @author drunlin@outlook.com
 */
public class RichTextViewTest extends WidgetTestCase<RichTextView> {
    @Override
    protected void initOnUiThread() throws Throwable {
        addToScene(new RichTextView(activity));
    }

    @Test
    public void setHtml() throws Throwable {
        ArticleContent content = getResult(ArticleContentResult.class, R.raw.article_content);
        runOnUiThread(() ->  view.setHtml(content.getContent()));
        pauseToPreview();
    }
}
