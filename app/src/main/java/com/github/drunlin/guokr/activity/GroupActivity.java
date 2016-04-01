package com.github.drunlin.guokr.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;

import com.github.drunlin.guokr.App;
import com.github.drunlin.guokr.Intents;
import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.bean.Group;
import com.github.drunlin.guokr.fragment.PostListFragment;
import com.github.drunlin.guokr.presenter.GroupPresenter;
import com.github.drunlin.guokr.presenter.GroupPresenter.Factory;
import com.github.drunlin.guokr.util.ToastUtils;
import com.github.drunlin.guokr.view.GroupView;
import com.github.drunlin.guokr.widget.LoginPrompt;

/**
 * 单个果壳小组的界面。包含该小组的帖子列表和加入或退出该小组的功能。
 *
 * @author drunlin@outlook.com
 */
public class GroupActivity extends SecondaryActivity implements GroupView {
    private boolean isMember;
    private boolean menuVisible;

    private GroupPresenter presenter;

    public static Intent getIntent(int groupId) {
        Intent intent = new Intent(App.getContext(), GroupActivity.class);
        intent.putExtra(Intents.EXTRA_GROUP_ID, groupId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final int groupId = getGroupId();
        if (groupId == -1) {
            startActivity(Intents.openLinkInBrowser(getIntent().getDataString()));
            finish();
            return;
        }

        presenter = lifecycle.bind(Factory.class, f -> f.create(groupId));

        setContentView(R.layout.activity_group);

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PostListFragment(groupId))
                    .commit();
        }
    }

    /**
     * 从Intent中获取小组ID。
     * @return
     */
    private int getGroupId() {
        if (Intent.ACTION_VIEW.equals(getIntent().getAction())) {
            String url = getIntent().getDataString();
            if (url.matches("^http://(\\w+\\.)?guokr\\.com/group/\\d+/?$")) {
                return Intents.getIdFromUri(getIntent(), "group");
            } else {
                return -1;
            }
        } else {
            return getIntent().getIntExtra(Intents.EXTRA_GROUP_ID, 0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_group, menu);
        menu.findItem(R.id.menu_member)
                .setTitle(isMember ? R.string.menu_join_group : R.string.menu_quite_group);
        return menuVisible;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_member:
                joinOrQuiteGroup();
                return true;
            case R.id.menu_open_link:
                presenter.onOpenLink();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void joinOrQuiteGroup() {
        if (isMember) {
            presenter.quitGroup();
        } else {
            presenter.joinGroup();
        }
    }

    @Override
    public void setGroup(@NonNull Group group) {
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(group.name);

        isMember = group.isMember;
        menuVisible = true;
        invalidateOptionsMenu();
    }

    @Override
    public void onJoinGroupSucceed() {
        isMember = true;

        invalidateOptionsMenu();
    }

    @Override
    public void onJoinGroupFailed() {
        ToastUtils.showShortTimeToast(R.string.toast_join_group_failed);
    }

    @Override
    public void onQuitGroupSucceed() {
        isMember = false;

        invalidateOptionsMenu();
    }

    @Override
    public void onQuitGroupFailed() {
        ToastUtils.showShortTimeToast(R.string.toast_quit_group_failed);
    }

    @Override
    public void onLoginStateInvalid() {
        //noinspection ConstantConditions
        LoginPrompt.show(this, findViewById(R.id.swipe_load_layout));
    }

    @Override
    public void openLink(String url) {
        startActivity(Intents.openLinkInBrowser(url));
    }
}
