package com.github.drunlin.guokr.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.drunlin.guokr.App;
import com.github.drunlin.guokr.module.tool.ViewLifecycle;
import com.github.drunlin.guokr.view.LoginNeededView;

/**
 * 本项目中DialogFragment的基类。
 *
 * @author drunlin@outlook.com
 */
public class DialogFragmentBase extends DialogFragment implements LoginNeededView {
    protected final ViewLifecycle lifecycle = App.createLifecycle(this);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        App.post(() -> lifecycle.onCreate(savedInstanceState));

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        lifecycle.onSaveInstanceState();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        lifecycle.onDestroy();

        super.onDismiss(dialog);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        lifecycle.onDestroy();

        super.onCancel(dialog);
    }

    @Override
    public void onLoginStateInvalid() {
        LoginPromptFragment.show(getActivity());
    }
}
