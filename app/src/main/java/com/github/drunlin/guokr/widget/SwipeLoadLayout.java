package com.github.drunlin.guokr.widget;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;

import com.github.drunlin.guokr.util.ViewUtils;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import static com.github.drunlin.guokr.util.JavaUtil.call;

/**
 * 添加一个简单的上拉加载更多。
 *
 * @author drunlin@outlook.com
 */
public class SwipeLoadLayout extends SwipeRefreshLayout {
    private OnLoadMoreListener onLoadMoreListener;

    private int totalScrollY;

    public SwipeLoadLayout(Context context) {
        this(context, null);
    }

    public SwipeLoadLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置加载更多数据的侦听器。
     * @param listener
     */
    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        onLoadMoreListener = listener;
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        if (nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL) {
            totalScrollY = 0;
        }
        return super.onStartNestedScroll(child, target, nestedScrollAxes);
    }

    @Override
    public void onNestedScroll(
            View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);

        totalScrollY += dyUnconsumed;
    }

    @Override
    public void onStopNestedScroll(View target) {
        super.onStopNestedScroll(target);

        if (totalScrollY > scrollLimit()) {
            call(onLoadMoreListener, OnLoadMoreListener::onLoadMore);
        }
    }

    protected float scrollLimit() {
        return ViewUtils.getDimension(getContext(), COMPLEX_UNIT_DIP, 80);
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
