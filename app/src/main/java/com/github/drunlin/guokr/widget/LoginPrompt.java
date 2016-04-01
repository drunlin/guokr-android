package com.github.drunlin.guokr.widget;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.activity.LoginActivity;

/**
 * 简单的登录提示。
 *
 * @author drunlin@outlook.com
 */
public class LoginPrompt {
    /**
     * 显示登录提示。。
     * @param activity
     * @param view
     */
    public static void show(@NonNull Activity activity, @NonNull View view) {
        Snackbar.make(view, R.string.title_login_needed, Snackbar.LENGTH_LONG)
                .setAction(R.string.btn_login, v ->
                        activity.startActivity(new Intent(activity, LoginActivity.class)))
                .show();
    }
}
