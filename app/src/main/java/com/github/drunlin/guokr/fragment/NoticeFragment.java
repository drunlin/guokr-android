package com.github.drunlin.guokr.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.activity.MessageListActivity;
import com.github.drunlin.guokr.activity.NoticeListActivity;
import com.github.drunlin.guokr.presenter.NoticePresenter;
import com.github.drunlin.guokr.view.NoticeView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 辅助控制{@link com.github.drunlin.guokr.activity.MainActivity}的通知逻辑。
 *
 * @author drunlin@outlook.com
 */
public class NoticeFragment extends FragmentBase implements NoticeView {
    @Bind(R.id.left_drawer) NavigationView leftDrawer;

    private MenuItem notification;
    private MenuItem notice;
    private MenuItem message;

    private NoticePresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = lifecycle.bind(NoticePresenter.class);

        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ButterKnife.bind(this, getActivity());

        Menu menu = leftDrawer.getMenu();
        notice = menu.findItem(R.id.nav_notice);
        message = menu.findItem(R.id.nav_message);

        leftDrawer.post(() -> lifecycle.onCreate(savedInstanceState));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        notification = menu.findItem(R.id.menu_notice);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_notice:
                presenter.onViewNotices();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setAllNoticesCount(int count) {
        if (count == 0) {
            notification.setVisible(false);
        } else {
            notification.setTitle(String.valueOf(count));
        }
    }

    @Override
    public void setNoticesCount(int count) {
        notice.setTitle(getContext().getString(R.string.nv_action_notices_count, count));
    }

    @Override
    public void setMessagesCount(int count) {
        message.setTitle(getContext().getString(R.string.nv_action_messages_count, count));
    }

    @Override
    public void setLoginStatus(boolean loggedIn) {
        notice.setVisible(loggedIn);
        message.setVisible(loggedIn);
        notification.setVisible(loggedIn);
    }

    @Override
    public void viewNotices() {
        startActivity(new Intent(getContext(), NoticeListActivity.class));
    }

    @Override
    public void viewMessages() {
        startActivity(new Intent(getContext(), MessageListActivity.class));
    }
}
