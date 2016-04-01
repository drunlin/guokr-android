package com.github.drunlin.guokr.test;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * View测试的基类，主要用于测试自定义View。
 *
 * @author drunlin@outlook.com
 */
public abstract class WidgetTestCase<T extends View> extends UiTestCase<AppCompatActivity> {
    protected T view;

    public WidgetTestCase() {
        super(AppCompatActivity.class);
    }

    protected void addToScene(T v) {
        addToScene(v, new LayoutParams(MATCH_PARENT, MATCH_PARENT));
    }

    protected void addToScene(T v, LayoutParams layoutParams) {
        activity.setContentView(view = v, layoutParams);
    }

    protected void addToScene(int resId) {
        ViewGroup viewGroup = (ViewGroup) activity.findViewById(android.R.id.content);
        //noinspection unchecked
        view = (T) activity.getLayoutInflater().inflate(resId, viewGroup, false);
        activity.setContentView(view);
    }
}
