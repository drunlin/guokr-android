package com.github.drunlin.guokr.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;

import com.github.drunlin.guokr.R;

/**
 * 兼容旧版本的有着色功能的按钮。
 *
 * @author drunlin@outlook.com
 */
public class TintImageButton extends AppCompatImageButton {
    private ColorStateList tintList;

    public TintImageButton(Context context) {
        this(context, null);
    }

    public TintImageButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TintImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TintImageButton);
        tintList = array.getColorStateList(R.styleable.TintImageButton_tint);
        DrawableCompat.setTintList(getDrawable(), tintList);
        array.recycle();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        drawable = DrawableCompat.wrap(drawable.mutate());
        DrawableCompat.setTintList(drawable, tintList);
        super.setImageDrawable(drawable);
    }
}
