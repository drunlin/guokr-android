package com.github.drunlin.guokr.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.activity.PostActivity;
import com.github.drunlin.guokr.bean.PostEntry;
import com.github.drunlin.guokr.presenter.PostListPresenter;
import com.github.drunlin.guokr.presenter.PostListPresenter.Factory;
import com.github.drunlin.guokr.view.PostListView;
import com.github.drunlin.guokr.widget.CircleImageView;
import com.github.drunlin.guokr.widget.adapter.SimpleAdapter;
import com.github.drunlin.guokr.widget.adapter.SimpleAdapter.ItemViewHolder;

import butterknife.Bind;

/**
 * 单个小组的帖子列表。
 *
 * @author drunlin@outlook.com
 */
public class PostListFragment
        extends TopicListFragment<PostEntry, PostListPresenter> implements PostListView {

    private static final String ARGUMENT_GROUP_ID = "ARGUMENT_GROUP_ID";

    @Deprecated
    public PostListFragment() {
        super();
    }

    @SuppressLint("ValidFragment")
    public PostListFragment(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARGUMENT_GROUP_ID, id);
        setArguments(bundle);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int groupId = getArguments().getInt(ARGUMENT_GROUP_ID);
        presenter = lifecycle.bind(Factory.class, f -> f.create(groupId));
    }

    @Override
    protected ItemViewHolder<PostEntry> onCreateViewHolder(ViewGroup parent) {
        return new ItemHolder(parent);
    }

    @Override
    public void viewPost(int id) {
        startActivity(PostActivity.getIntent(id));
    }

    class ItemHolder extends SimpleAdapter.ItemViewHolder<PostEntry> {
        @Bind(R.id.img_avatar) CircleImageView avatar;
        @Bind(R.id.text_title) TextView title;
        @Bind(R.id.text_group) TextView group;
        @Bind(R.id.text_author) TextView author;
        @Bind(R.id.text_date) TextView date;
        @Bind(R.id.text_replies_count) TextView repliesCount;

        public ItemHolder(ViewGroup parent) {
            super(getContext(), R.layout.item_post, parent);

            itemView.setOnClickListener(v -> presenter.onViewPost(data));
        }

        @Override
        protected void onBind(PostEntry data, int position) {
            avatar.setImageData(data.author.avatar.data);
            title.setText(data.title);
            group.setText(data.group.name);
            author.setText(data.author.nickname);
            date.setText(data.dateLastReplied != null ? data.dateLastReplied : data.dateCreated);
            repliesCount.setText(String.valueOf(data.repliesCount));
        }
    }
}
