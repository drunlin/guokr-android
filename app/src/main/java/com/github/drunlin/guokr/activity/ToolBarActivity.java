package com.github.drunlin.guokr.activity;

import android.support.annotation.CallSuper;
import android.support.v7.widget.Toolbar;

import com.github.drunlin.guokr.R;

import butterknife.Bind;

/**
 * 使用ToolBar的Activity基类。
 *
 * @author drunlin@outlook.com
 */
public abstract class ToolBarActivity extends ActivityBase {
    @Bind(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void initialize() {
        super.initialize();

        onSetupToolbar();
    }

    /**
     * Toolbar已完成初始化，进行相关设置。
     */
    @CallSuper
    protected void onSetupToolbar() {
        setSupportActionBar(toolbar);
    }
}
