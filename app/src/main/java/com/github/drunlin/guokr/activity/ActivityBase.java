package com.github.drunlin.guokr.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.drunlin.guokr.App;
import com.github.drunlin.guokr.module.tool.ViewLifecycle;

import butterknife.ButterKnife;

/**
 * 本项目中Activity的基类。
 *
 * @author drunlin@outlook.com
 */
public abstract class ActivityBase extends AppCompatActivity {
    protected final ViewLifecycle lifecycle = App.createLifecycle(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App.post(() -> lifecycle.onCreate(savedInstanceState));
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        initialize();
    }

    protected void initialize() {
        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);

        initialize();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        lifecycle.onSaveInstanceState();
    }

    @Override
    protected void onDestroy() {
        lifecycle.onDestroy();

        super.onDestroy();
    }
}
