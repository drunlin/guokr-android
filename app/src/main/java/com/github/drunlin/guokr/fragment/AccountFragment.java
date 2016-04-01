package com.github.drunlin.guokr.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.activity.LoginActivity;
import com.github.drunlin.guokr.activity.UserActivity;
import com.github.drunlin.guokr.bean.UserInfo;
import com.github.drunlin.guokr.presenter.AccountPresenter;
import com.github.drunlin.guokr.view.AccountView;
import com.github.drunlin.guokr.widget.CircleImageView;

import butterknife.Bind;

import static com.github.drunlin.guokr.util.JavaUtil.call;

/**
 * 显示在Navigation Drawer顶部的用户信息界面，用于登录或查看用户信息。
 *
 * @author drunlin@outlook.com
 */
public class AccountFragment extends FragmentBase implements AccountView {
    private OnAvatarClickListener onAvatarClickListener;

    @Bind(R.id.btn_avatar) CircleImageView avatarImage;
    @Bind(R.id.text_name) TextView nameText;

    private AccountPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = lifecycle.bind(AccountPresenter.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        avatarImage.setOnClickListener(this::onAvatarClick);
    }

    private void onAvatarClick(View v) {
        call(onAvatarClickListener, OnAvatarClickListener::onAvatarClick);

        presenter.onViewUserInfo();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            onAvatarClickListener = (OnAvatarClickListener) context;
        } catch (ClassCastException e) {
            //do nothing
        }
    }

    @Override
    public void setUserInfo(UserInfo userInfo) {
        nameText.setText(userInfo.nickname);
        avatarImage.setImageData(userInfo.avatar.data);
    }

    @Override
    public void onLoggedOut() {
        nameText.setText(R.string.label_click_to_login);
        avatarImage.setImageResource(R.drawable.ic_account_circle);
    }

    @Override
    public void login() {
        startActivity(new Intent(getContext(), LoginActivity.class));
    }

    @Override
    public void viewUserInfo() {
        startActivity(new Intent(getContext(), UserActivity.class));
    }

    public interface OnAvatarClickListener {
        void onAvatarClick();
    }
}
