package com.github.drunlin.guokr.activity;

import com.github.drunlin.guokr.test.ActivityTestCase;

/**
 * @author drunlin@outlook.com
 */
public class WebPageActivityTest extends ActivityTestCase<WebPageActivity> {
    public WebPageActivityTest() {
        super(WebPageActivity.class, false);
    }

    @Override
    protected void init() throws Throwable {
        launchActivity(WebPageActivity.getIntent("https://m.guokr.com", "Guokr"));
    }

    @Override
    protected void onPreview() throws Throwable {}
}
