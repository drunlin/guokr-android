package com.github.drunlin.guokr.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;

import com.github.drunlin.guokr.util.BitmapUtils;

import pl.droidsonroids.gif.GifImageView;

/**
 * 支持显示PNG，JPEG，GIF的图片控件。
 *
 * @author drunlin@outlook.com
 */
public class BitmapImageView extends GifImageView {
    private boolean adjustViewBounds;

    public BitmapImageView(Context context) {
        this(context, null);
    }

    public BitmapImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BitmapImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 设置byte[]类型的图像数据。
     * @param data
     */
    public void setImageData(byte[] data) {
        setImageDrawable(BitmapUtils.createDrawable(getResources(), data));
    }


    @Override
    public void setAdjustViewBounds(boolean adjustBounds) {
        super.setAdjustViewBounds(adjustBounds);

        adjustViewBounds = adjustBounds;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return;
        }

        final Drawable drawable = getDrawable();
        if (drawable != null && adjustViewBounds) {
            if (MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY) {
                final int width = getMeasuredWidth();
                int height = width * drawable.getIntrinsicHeight() / drawable.getIntrinsicWidth();
                setMeasuredDimension(width, height);
            }
        }
    }
}
