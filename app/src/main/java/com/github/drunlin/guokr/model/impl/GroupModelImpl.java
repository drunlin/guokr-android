package com.github.drunlin.guokr.model.impl;

import com.github.drunlin.guokr.bean.Group;
import com.github.drunlin.guokr.bean.ResultClassMap.GroupsResult;
import com.github.drunlin.guokr.model.GroupModel;
import com.github.drunlin.guokr.model.Model;
import com.github.drunlin.guokr.model.NetworkModel;
import com.github.drunlin.guokr.model.UserModel;
import com.github.drunlin.guokr.model.request.AccessRequest;
import com.github.drunlin.guokr.model.request.JsonRequest;
import com.github.drunlin.guokr.model.request.JsonRequest.Builder;
import com.github.drunlin.guokr.module.tool.Injector;
import com.github.drunlin.signals.impl.Signal1;
import com.github.drunlin.signals.impl.Signal2;

import javax.inject.Inject;

import static com.android.volley.Request.Method.DELETE;
import static com.android.volley.Request.Method.POST;
import static com.github.drunlin.guokr.bean.ResponseCode.OK;

/**
 * 单个小组的数据。
 * @see ForumModelImpl
 *
 * @author drunlin@outlook.com
 */
public class GroupModelImpl extends Model implements GroupModel {
    private final Signal1<Integer> joinGroupResulted = new Signal1<>();
    private final Signal1<Integer> quitGroupResulted = new Signal1<>();
    private final Signal2<Integer, Group> groupResulted = new Signal2<>();

    @Inject NetworkModel networkModel;
    @Inject UserModel userModel;

    private final int groupId;

    private Group group;

    public GroupModelImpl(Injector injector, int groupId) {
        super(injector);

        this.groupId = groupId;
    }

    @Override
    public void requestGroup() {
        final String url = "http://www.guokr.com/apis/group/group.json?retrieve_type=by_id";
        JsonRequest.Builder<GroupsResult> builder = new Builder<>(url, GroupsResult.class)
                .addParam("group_id", groupId)
                .setListener(r -> groupResulted.dispatch(OK, group = r.result.get(0)))
                .setErrorListener(e -> groupResulted.dispatch(e.getCode(), null));
        if (userModel.isLoggedIn()) {
            builder.addParam("access_token", userModel.getToken());
        }
        builder.build(networkModel);
    }

    @Override
    public Signal2<Integer, Group> groupResulted() {
        return groupResulted;
    }

    @Override
    public Group getGroup() {
        return group;
    }

    @Override
    public void joinGroup() {
        final String url = "http://www.guokr.com/apis/group/member.json";
        new AccessRequest.SimpleRequestBuilder(url, userModel.getToken())
                .setMethod(POST)
                .addParam("group_id", groupId)
                .setListener(response -> onJoinOrQuiteGroupSucceed(true, joinGroupResulted))
                .setErrorListener(error -> joinGroupResulted.dispatch(error.getCode()))
                .build(networkModel);
    }

    private void onJoinOrQuiteGroupSucceed(boolean joined, Signal1<Integer> signal) {
        //忽略没有加载完成的情况
        if (group != null) {
            group.isMember = joined;
        }

        signal.dispatch(OK);
    }

    @Override
    public Signal1<Integer> joinGroupResulted() {
        return joinGroupResulted;
    }

    @Override
    public void quitGroup() {
        final String url = "http://www.guokr.com/apis/group/member.json";
        new AccessRequest.SimpleRequestBuilder(url, userModel.getToken())
                .setMethod(DELETE)
                .addParam("group_id", groupId)
                .setListener(response -> onJoinOrQuiteGroupSucceed(false, quitGroupResulted))
                .setErrorListener(error -> quitGroupResulted.dispatch(error.getCode()))
                .build(networkModel);
    }

    @Override
    public Signal1<Integer> quitGroupResulted() {
        return quitGroupResulted;
    }
}
