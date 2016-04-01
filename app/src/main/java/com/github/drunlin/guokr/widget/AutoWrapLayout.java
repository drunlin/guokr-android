package com.github.drunlin.guokr.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 能够自动换行的布局。显示主题站作者，日期和回复数的容器，在作者名过长时日期在第二行显示。
 *
 * @author drunlin@outlook.com
 */
public class AutoWrapLayout extends ViewGroup {
    private boolean singleLine;

    public AutoWrapLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getChildCount() != 2) {
            throw new RuntimeException("AutoWrapLayout should only have two child!");
        }

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int childWidthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST);

        View child1 = getChildAt(0);
        child1.measure(childWidthSpec, heightMeasureSpec);
        View child2 = getChildAt(1);
        child2.measure(childWidthSpec, heightMeasureSpec);

        if (child1.getMeasuredWidth() + child2.getMeasuredWidth() > width) {
            singleLine = false;

            child1.measure(widthMeasureSpec, heightMeasureSpec);
            child2.measure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(
                    widthMeasureSpec,
                    child1.getMeasuredHeight() + child2.getMeasuredHeight());
        } else {
            singleLine = true;

            child2.measure(
                    MeasureSpec.makeMeasureSpec(width - child1.getMeasuredWidth(), MeasureSpec.EXACTLY),
                    heightMeasureSpec);
            setMeasuredDimension(
                    widthMeasureSpec,
                    Math.max(child1.getHeight(), child2.getMeasuredHeight()));
        }
    }

    private void layout(View child, int x, int y, int w, int h) {
        child.layout(x, y, x + w, y + h);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View child1 = getChildAt(0);
        layout(child1, 0, 0, child1.getMeasuredWidth(), child1.getMeasuredHeight());

        View child2 = getChildAt(1);
        if (singleLine) {
            layout(child2, child1.getRight(), child1.getTop(),
                    child2.getMeasuredWidth(), child2.getMeasuredHeight());
        } else {
            layout(child2, 0, child1.getBottom(),
                    child2.getMeasuredWidth(), child2.getMeasuredHeight());
        }
    }
}
