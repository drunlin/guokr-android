package com.github.drunlin.guokr.widget;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;

/**
 * 基于开源库的圆形图片组件。
 *
 * @author drunlin@outlook.com
 */
public class CircleImageView extends de.hdodenhof.circleimageview.CircleImageView {
    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 设置图片数据。
     * @param data
     */
    public void setImageData(byte[] data) {
        if (data != null) {
            setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
        } else {
            setImageBitmap(null);
        }
    }
}
