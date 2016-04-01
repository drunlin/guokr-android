package com.github.drunlin.guokr.widget;

import android.view.ViewGroup.LayoutParams;

import com.github.drunlin.guokr.test.WidgetTestCase;

import org.junit.Test;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static junit.framework.Assert.assertEquals;

/**
 * @author drunlin@outlook.com
 */
public class RichEditorViewTest extends WidgetTestCase<RichEditorView> {
    @Override
    protected void initOnUiThread() throws Throwable {
        addToScene(new RichEditorView(activity), new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
    }

    @Override
    protected void onPreview() throws Throwable {}

    @Test
    public void insertHtml() throws Exception {
        final String html = "<div>Test</div>";

        view.insertHtml(html);
        await(1, 2_000);
        assertEquals(html, view.getHtml());
    }
}
