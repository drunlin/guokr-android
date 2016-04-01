package com.github.drunlin.guokr.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.github.drunlin.guokr.App;
import com.github.drunlin.guokr.Intents;
import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.bean.PostContent;
import com.github.drunlin.guokr.presenter.PostPresenter;
import com.github.drunlin.guokr.presenter.PostPresenter.Factory;
import com.github.drunlin.guokr.util.ToastUtils;
import com.github.drunlin.guokr.view.PostView;

import butterknife.Bind;

/**
 * 单个帖子的内容和评论，标题栏还显示有小组信息。
 *
 * @author drunlin@outlook.com
 */
public class PostActivity extends TopicActivity<PostContent, PostPresenter> implements PostView {
    @Bind(R.id.btn_like) FloatingActionButton likeButton;

    public static Intent getIntent(int postId) {
        Intent intent = new Intent(App.getContext(), PostActivity.class);
        intent.setAction(Intents.ACTION_VIEW_POST);
        intent.putExtra(Intents.EXTRA_POST_ID, postId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        likeButton.setOnClickListener(v -> presenter.like());
    }

    @Override
    protected PostPresenter onCreatePresenter(String action, Intent intent) {
        int groupId = Intent.ACTION_VIEW.equals(action)
                ? Intents.getIdFromUri(intent, "post")
                : intent.getIntExtra(Intents.EXTRA_POST_ID, 0);
        return lifecycle.bind(Factory.class, f -> f.create(groupId));
    }

    @Override
    protected int getFloatingMenuRes() {
        return R.layout.floating_menu_post;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_post, menu);
        return menuVisible;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_group:
                presenter.onViewGroup();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setContent(@NonNull PostContent data) {
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(data.group.name);

        super.setContent(data);
    }

    @Override
    public void viewGroup(int id) {
        startActivity(GroupActivity.getIntent(id));
    }

    @Override
    public void onAlreadyLiked() {
        ToastUtils.showShortTimeToast(R.string.toast_already_liked);
    }

    @Override
    public void onLikeFailed() {
        ToastUtils.showShortTimeToast(R.string.toast_like_failed);
    }

    @Override
    protected ContentViewHolderBase createContentHolder(ViewGroup parent) {
        return new ContentViewHolder(parent);
    }
}
