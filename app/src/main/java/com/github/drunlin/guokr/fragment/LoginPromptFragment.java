package com.github.drunlin.guokr.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.github.drunlin.guokr.App;
import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.activity.LoginActivity;
import com.github.drunlin.guokr.presenter.LoginPromptPresenter;
import com.github.drunlin.guokr.view.LoginPromptView;

import java.util.LinkedList;

/**
 * 提示需要登录，会在登录成功后自动关闭。
 *
 * @author drunlin@outlook.com
 */
public class LoginPromptFragment extends DialogFragmentBase implements LoginPromptView {
    private static final String TAG = "TAG_LOGIN_PROMPT";

    /**用来防止重复弹出对话框的消息队列。*/
    private static final LinkedList<Activity> queue = new LinkedList<>();

    public static void show(Activity activity) {
        if (queue.isEmpty()) {
            popup(activity);
        }

        queue.add(activity);
    }

    private static void popup(Activity activity) {
        if (activity.isTaskRoot()) {
            FragmentManager fragmentManager =
                    ((AppCompatActivity) activity).getSupportFragmentManager();
            if (fragmentManager.findFragmentByTag(TAG) == null) {
                new LoginPromptFragment().show(fragmentManager, TAG);
            }
        }

        App.post(() -> {
            queue.poll();

            if (!queue.isEmpty()) {
                popup(queue.getFirst());
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lifecycle.bind(LoginPromptPresenter.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.title_login_needed)
                .setPositiveButton(R.string.btn_login, (dialog, which) ->
                        startActivity(new Intent(getContext(), LoginActivity.class)))
                .setNegativeButton(R.string.btn_cancel, null)
                .show();
    }

    @Override
    public void onLoggedIn() {
        dismiss();
    }
}
