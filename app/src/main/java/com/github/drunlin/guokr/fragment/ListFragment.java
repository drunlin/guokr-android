package com.github.drunlin.guokr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.drunlin.guokr.R;

import butterknife.Bind;

/**
 * 主要用列表显示的Fragment的基类。
 *
 * @author drunlin@outlook.com
 */
public abstract class ListFragment extends ProgressFragment {
    @Bind(R.id.recycler_view) RecyclerView recyclerView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setLayoutManager(onCreateLayoutManager());
    }

    protected RecyclerView.LayoutManager onCreateLayoutManager() {
        return new LinearLayoutManager(getContext());
    }
}
