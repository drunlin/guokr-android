package com.github.drunlin.guokr.bean;

import android.graphics.Bitmap;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * 编辑器的预览图。
 *
 * @author drunlin@outlook.com
 */
public class Thumbnail {
    public static final int LOADING = 0;
    public static final int FAILED = 1;
    public static final int SUCCESS = 2;

    @Retention(SOURCE)
    @IntDef({LOADING, FAILED, SUCCESS})
    public @interface State {}

    public String url;
    public @State int state = LOADING;
    public Bitmap bitmap;
}
