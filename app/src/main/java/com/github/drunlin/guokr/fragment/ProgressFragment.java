package com.github.drunlin.guokr.fragment;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.widget.SwipeLoadLayout;

import butterknife.Bind;

/**
 * 显示进度条的Fragment的基类。
 *
 * @author drunlin@outlook.com
 */
public abstract class ProgressFragment extends FragmentBase {
    @Bind(R.id.swipe_load_layout) SwipeLoadLayout swipeLoadLayout;

    /**
     * 是否在加载中。
     * @param refreshing
     */
    public void setLoading(boolean refreshing) {
        swipeLoadLayout.post(() -> swipeLoadLayout.setRefreshing(refreshing));
    }
}
