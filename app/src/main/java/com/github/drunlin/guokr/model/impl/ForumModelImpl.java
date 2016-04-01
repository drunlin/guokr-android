package com.github.drunlin.guokr.model.impl;

import com.github.drunlin.guokr.bean.Group;
import com.github.drunlin.guokr.bean.ResultClassMap.GroupsResult;
import com.github.drunlin.guokr.model.ForumModel;
import com.github.drunlin.guokr.model.GroupModel;
import com.github.drunlin.guokr.model.IconLoader;
import com.github.drunlin.guokr.model.Model;
import com.github.drunlin.guokr.model.NetworkModel;
import com.github.drunlin.guokr.model.PostListModel;
import com.github.drunlin.guokr.model.PostModel;
import com.github.drunlin.guokr.model.UserModel;
import com.github.drunlin.guokr.model.request.AccessRequest;
import com.github.drunlin.guokr.module.tool.Injector;
import com.github.drunlin.guokr.module.tool.ReferenceMap;
import com.github.drunlin.signals.impl.Signal2;

import java.util.List;

import javax.inject.Inject;

import static com.github.drunlin.guokr.bean.ResponseCode.ERROR;
import static com.github.drunlin.guokr.bean.ResponseCode.OK;

/**
 * 小组管理器。
 * @see GroupModelImpl
 *
 * @author drunlin@outlook.com
 */
public class ForumModelImpl extends Model implements ForumModel {
    private final Signal2<Integer, List<Group>> joinedGroupsResulted = new Signal2<>();

    private final ReferenceMap<Integer, PostListModel> listModelMap = new ReferenceMap<>();
    private final ReferenceMap<Integer, GroupModel> groupModelMap = new ReferenceMap<>();
    private final ReferenceMap<Integer, PostModel> postModelMap = new ReferenceMap<>();

    @Inject NetworkModel networkModel;
    @Inject UserModel userModel;

    private List<Group> joinedGroups;

    public ForumModelImpl(Injector injector) {
        super(injector);
    }

    @Override
    public PostListModel getPostList(int groupId) {
        return listModelMap.get(groupId, () -> new PostListModelImpl(injector, groupId));
    }

    @Override
    public PostListModel getHotPostList() {
        return getPostList(HOT_POST);
    }

    @Override
    public PostListModel getMyGroupPosts() {
        return getPostList(MY_GROUP_POST);
    }

    @Override
    public void requestJoinedGroups() {
        final String url = "http://www.guokr.com/apis/group/favorite.json";
        new AccessRequest.Builder<>(url, GroupsResult.class, userModel.getToken())
                .addParam("limit", 100)
                .setParseListener(r -> IconLoader.batchLoad(networkModel, r.result, g -> g.icon))
                .setListener(r -> joinedGroupsResulted.dispatch(OK, joinedGroups = r.result))
                .setErrorListener(e -> joinedGroupsResulted.dispatch(ERROR, null))
                .build(networkModel);
    }

    @Override
    public List<Group> getJoinedGroups() {
        return joinedGroups;
    }

    @Override
    public Signal2<Integer, List<Group>> joinedGroupsResulted() {
        return joinedGroupsResulted;
    }

    @Override
    public GroupModel getGroup(int groupId) {
        return groupModelMap.get(groupId, () -> new GroupModelImpl(injector, groupId));
    }

    @Override
    public PostModel getPost(int postId) {
        return postModelMap.get(postId, () -> new PostModelImpl(injector, postId));
    }
}
