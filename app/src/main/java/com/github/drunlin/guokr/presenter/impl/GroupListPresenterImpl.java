package com.github.drunlin.guokr.presenter.impl;

import com.github.drunlin.guokr.bean.Group;
import com.github.drunlin.guokr.bean.ResponseCode;
import com.github.drunlin.guokr.model.ForumModel;
import com.github.drunlin.guokr.presenter.GroupListPresenter;
import com.github.drunlin.guokr.view.GroupListView;

import java.util.List;

import javax.inject.Inject;

/**
 * @author drunlin@outlook.com
 */
public class GroupListPresenterImpl
        extends PresenterBase<GroupListView> implements GroupListPresenter {

    @Inject ForumModel groupModel;

    @Override
    public void onCreate(GroupListView view) {
        super.onCreate(view);
    }

    @Override
    public void onViewCreated(boolean firstCreated) {
        super.onViewCreated(firstCreated);

        bind(groupModel.joinedGroupsResulted(), this::onGroupsResult);

        List<Group> groups = groupModel.getJoinedGroups();
        if (firstCreated || groups == null) {
            refresh();
        } else {
            view.setGroups(groups);
        }
    }

    @Override
    public void refresh() {
        groupModel.requestJoinedGroups();

        view.setLoading(true);
    }

    private void onGroupsResult(int resultCode, List<Group> groups) {
        if (resultCode == ResponseCode.OK) {
            view.setGroups(groups);
        } else {
            view.onLoadFailed();
        }
        view.setLoading(false);
    }

    @Override
    public void onViewGroup(Group group) {
        view.viewGroup(group.id);
    }
}
