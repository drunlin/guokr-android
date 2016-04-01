package com.github.drunlin.guokr.presenter.impl;

import com.github.drunlin.guokr.bean.Group;
import com.github.drunlin.guokr.bean.ResponseCode;
import com.github.drunlin.guokr.model.ForumModel;
import com.github.drunlin.guokr.model.GroupModel;
import com.github.drunlin.guokr.presenter.GroupPresenter;
import com.github.drunlin.guokr.view.GroupView;

import javax.inject.Inject;

/**
 * @author drunlin@outlook.com
 */
public class GroupPresenterImpl
        extends LoginNeededPresenterBase<GroupView> implements GroupPresenter {

    @Inject ForumModel forumModel;

    private GroupModel groupModel;

    private final int groupId;

    public GroupPresenterImpl(int id) {
        groupId = id;
    }

    @Override
    public void onCreate(GroupView view) {
        super.onCreate(view);

        groupModel = forumModel.getGroup(groupId);
    }

    @Override
    public void onViewCreated(boolean firstCreated) {
        super.onViewCreated(firstCreated);

        bind(groupModel.groupResulted(), this::onGroupResult);
        bind(groupModel.joinGroupResulted(), this::onJoinGroupResult);
        bind(groupModel.quitGroupResulted(), this::onQuiteGroupResult);

        Group group = groupModel.getGroup();
        if (firstCreated || group == null) {
            groupModel.requestGroup();
        } else {
            view.setGroup(group);
        }
    }

    private void onGroupResult(int resultCode, Group group) {
        if (resultCode == ResponseCode.OK) {
            view.setGroup(group);
        }
    }

    @Override
    public void joinGroup() {
        groupModel.joinGroup();
    }

    private void onJoinGroupResult(int resultCode) {
        if (resultCode == ResponseCode.OK) {
            view.onJoinGroupSucceed();
        } else {
            view.onJoinGroupFailed();
        }
    }

    @Override
    public void quitGroup() {
        groupModel.quitGroup();
    }

    private void onQuiteGroupResult(int resultCode) {
        if (resultCode == ResponseCode.OK) {
            view.onQuitGroupSucceed();
        } else {
            view.onQuitGroupFailed();
        }
    }

    @Override
    public void onOpenLink() {
        view.openLink(String.format("http://m.guokr.com/group/%d/", groupId));
    }
}
