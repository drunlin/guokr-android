package com.github.drunlin.guokr.util;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.github.drunlin.guokr.util.JavaUtil.Consumer;

/**
 * @author drunlin@outlook.com
 */
public class ViewUtils {
    /**
     * LayoutInflater.inflate(int, ViewGroup, false)
     * @param context
     * @param layoutRes
     * @param parent
     * @return
     */
    public static View inflate(Context context, @LayoutRes int layoutRes, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(layoutRes, parent, false);
    }

    /**
     * 绑定输入框和按钮，实现内容为空时，按钮不可点击。
     * @param editText
     * @param button
     * @param listener
     */
    public static void setEditTextActionButton(EditText editText, Button button,
                                               Consumer<String> listener) {
        button.setEnabled(false);
        button.setOnClickListener(v -> listener.call(editText.getText().toString()));

        editText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                button.setEnabled(!isEmptyEditText(editText));
            }
        });
    }

    /**
     * 是否为空的输入框。
     * @param editText
     * @return
     */
    public static boolean isEmptyEditText(EditText editText) {
        return TextUtils.isEmpty(editText.getText().toString().trim());
    }

    /**
     * {@link TextWatcher}的简单实现类。
     */
    public static class SimpleTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {}
    }

    /**
     * @see TypedValue#applyDimension(int, float, DisplayMetrics)
     * @param unit
     * @param value
     * @return
     */
    public static int getDimension(Context context, int unit, float value) {
        return (int) TypedValue.applyDimension(
                unit, value, context.getResources().getDisplayMetrics());
    }
}
