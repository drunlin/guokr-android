package com.github.drunlin.guokr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.activity.GroupActivity;
import com.github.drunlin.guokr.bean.Group;
import com.github.drunlin.guokr.presenter.GroupListPresenter;
import com.github.drunlin.guokr.util.ToastUtils;
import com.github.drunlin.guokr.view.GroupListView;
import com.github.drunlin.guokr.widget.CircleImageView;
import com.github.drunlin.guokr.widget.adapter.SimpleAdapter;

import java.util.List;

import butterknife.Bind;

/**
 * 小组列表，主要用来显示加入的小组。
 *
 * @author drunlin@outlook.com
 */
public class GroupListFragment extends ListFragment implements GroupListView {
    private SimpleAdapter<Group> adapter;

    private GroupListPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = lifecycle.bind(GroupListPresenter.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_simple, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new SimpleAdapter<>((parent, viewType) -> new ItemHolder(parent));

        recyclerView.setAdapter(adapter);

        swipeLoadLayout.setOnRefreshListener(presenter::refresh);
    }

    @Override
    public void setGroups(List<Group> groups) {
        adapter.setData(groups);
    }

    @Override
    public void onLoadFailed() {
        ToastUtils.showShortTimeToast(R.string.toast_load_failed);
    }

    @Override
    public void viewGroup(int groupId) {
        getActivity().startActivity(GroupActivity.getIntent(groupId));
    }

    class ItemHolder extends SimpleAdapter.ItemViewHolder<Group> {
        @Bind(R.id.img_icon) CircleImageView icon;
        @Bind(R.id.text_name) TextView name;
        @Bind(R.id.text_group_member) TextView groupMember;

        public ItemHolder(ViewGroup parent) {
            super(getContext(), R.layout.item_group, parent);

            itemView.setOnClickListener(v -> presenter.onViewGroup(data));
        }

        @Override
        protected void onBind(Group data, int position) {
            icon.setImageData(data.icon.data);
            name.setText(data.name);
            groupMember.setText(getString(R.string.label_members, data.membersCount));
        }
    }
}
