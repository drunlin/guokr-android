package com.github.drunlin.guokr.test;

import android.app.Activity;
import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

/**
 * @author drunlin@outlook.com
 */
public abstract class ActivityTestCase<T extends Activity> extends ViewTestCase<T> {
    public ActivityTestCase(Class<T> activityClass) {
        super(activityClass);
    }

    public ActivityTestCase(Class<T> activityClass, boolean autoStart) {
        activityTestRule = new ActivityTestRule<>(activityClass, true, autoStart);
    }

    protected void launchActivity(final Intent intent) {
        activity = activityTestRule.launchActivity(intent);
    }
}
