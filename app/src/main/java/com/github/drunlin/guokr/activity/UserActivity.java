package com.github.drunlin.guokr.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.bean.UserInfo;
import com.github.drunlin.guokr.presenter.UserPresenter;
import com.github.drunlin.guokr.view.UserView;
import com.github.drunlin.guokr.widget.CircleImageView;

import butterknife.Bind;

/**
 * 当前登录用户的信息界面，目前只有一个退出登录功能。
 *
 * @author drunlin@outlook.com
 */
public class UserActivity extends SecondaryActivity implements UserView {
    @Bind(R.id.img_avatar) CircleImageView avatarImage;
    @Bind(R.id.text_name) TextView nameText;
    @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;

    private UserPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = lifecycle.bind(UserPresenter.class);

        setContentView(R.layout.activity_user);

        collapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                presenter.logout();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setUserInfo(UserInfo userInfo) {
        nameText.setText(userInfo.nickname);
        avatarImage.setImageData(userInfo.avatar.data);
    }

    /**
     * 已退出登录，关闭当前界面。
     */
    @Override
    public void onLoggedOut() {
        finish();
    }
}
