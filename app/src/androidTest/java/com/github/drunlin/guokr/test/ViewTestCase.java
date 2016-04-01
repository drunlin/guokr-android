package com.github.drunlin.guokr.test;

import android.app.Activity;

import com.github.drunlin.guokr.TestContext;

/**
 * @author drunlin@outlook.com
 */
public abstract class ViewTestCase<T extends Activity> extends UiTestCase<T> {
    protected ViewTestCase() {
        super();

        setUpContext();
    }

    public ViewTestCase(Class<? extends T> activityClass) {
        super(activityClass);

        setUpContext();
    }

    protected void setUpContext() {
        TestContext.inject(this);
    }
}
