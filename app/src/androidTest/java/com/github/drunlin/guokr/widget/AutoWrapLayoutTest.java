package com.github.drunlin.guokr.widget;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.test.WidgetTestCase;

/**
 * @author drunlin@outlook.com
 */
public class AutoWrapLayoutTest extends WidgetTestCase<AutoWrapLayout> {
    @Override
    protected void initOnUiThread() throws Throwable {
        activity.setContentView(R.layout.test_auto_wrap_layout);
    }

    @Override
    protected void onPreview() throws Throwable {}
}
