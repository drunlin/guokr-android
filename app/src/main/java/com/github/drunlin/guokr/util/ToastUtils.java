package com.github.drunlin.guokr.util;

import android.support.annotation.StringRes;
import android.widget.Toast;

import com.github.drunlin.guokr.App;

/**
 * @author drunlin@outlook.com
 */
public class ToastUtils {
    /**
     * 显示较短时间的提示。
     * @param text
     */
    public static void showShortTimeToast(CharSequence text) {
        Toast.makeText(App.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示较短时间的提示。
     * @param res
     */
    public static void showShortTimeToast(@StringRes int res) {
        Toast.makeText(App.getContext(), res, Toast.LENGTH_SHORT).show();
    }

//    /**
//     * 显示较长时间的提示。
//     * @param text
//     */
//    public static void showLongTimeToast(CharSequence text) {
//        Toast.makeText(App.getContext(), text, Toast.LENGTH_LONG).show();
//    }
}
