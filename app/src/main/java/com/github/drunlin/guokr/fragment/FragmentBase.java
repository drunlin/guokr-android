package com.github.drunlin.guokr.fragment;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.github.drunlin.guokr.App;
import com.github.drunlin.guokr.module.tool.ViewLifecycle;

import butterknife.ButterKnife;

/**
 * 本项目中Fragment的基类。
 *
 * @author drunlin@outlook.com
 */
public class FragmentBase extends Fragment {
    protected final ViewLifecycle lifecycle = App.createLifecycle(this);

    @CallSuper
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        App.post(() -> lifecycle.onCreate(savedInstanceState));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        lifecycle.onSaveInstanceState();
    }

    @Override
    public void onDestroyView() {
        lifecycle.onDestroy();

        super.onDestroyView();
    }
}
