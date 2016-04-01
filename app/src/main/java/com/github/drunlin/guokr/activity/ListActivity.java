package com.github.drunlin.guokr.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.view.LoginNeededView;
import com.github.drunlin.guokr.widget.LoginPrompt;
import com.github.drunlin.guokr.widget.SwipeLoadLayout;

import butterknife.Bind;

/**
 * 包含一个RecyclerView和SwipeLoadLayout的Activity。
 *
 * @author drunlin@outlook.com
 */
public abstract class ListActivity extends SecondaryActivity implements LoginNeededView {
    @Bind(R.id.recycler_view) RecyclerView recyclerView;
    @Bind(R.id.swipe_load_layout) SwipeLoadLayout swipeLoadLayout;

    @Override
    protected void initialize() {
        super.initialize();

        recyclerView.setLayoutManager(onCreateLayoutManager());
    }

    /**
     * 创建RecyclerView必须的LayoutManager。
     * @return
     */
    protected RecyclerView.LayoutManager onCreateLayoutManager() {
        return new LinearLayoutManager(this);
    }

    /**
     * 设置是否正在加载中。
     * @param loading
     */
    public void setLoading(boolean loading) {
        swipeLoadLayout.post(() -> swipeLoadLayout.setRefreshing(loading));
    }

    @Override
    public void onLoginStateInvalid() {
        LoginPrompt.show(this, swipeLoadLayout);
    }
}
