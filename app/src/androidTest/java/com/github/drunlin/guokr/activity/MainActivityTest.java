package com.github.drunlin.guokr.activity;

import com.github.drunlin.guokr.test.ActivityTestCase;

/**
 * @author drunlin@outlook.com
 */
public class MainActivityTest extends ActivityTestCase<MainActivity> {
    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void onPreview() throws Throwable {
        runOnUiThread(() -> activity.setPagePosition(0));
    }
}
