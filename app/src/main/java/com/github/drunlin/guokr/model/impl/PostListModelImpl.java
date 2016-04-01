package com.github.drunlin.guokr.model.impl;

import com.github.drunlin.guokr.bean.PostEntry;
import com.github.drunlin.guokr.bean.ResultClassMap.PostsResult;
import com.github.drunlin.guokr.model.ForumModel;
import com.github.drunlin.guokr.model.IconLoader;
import com.github.drunlin.guokr.model.PostListModel;
import com.github.drunlin.guokr.model.UserModel;
import com.github.drunlin.guokr.module.tool.Injector;

import java.util.List;

import javax.inject.Inject;

/**
 * @author drunlin@outlook.com
 */
public class PostListModelImpl extends TopicListModelBase<PostEntry> implements PostListModel {
    @Inject UserModel userModel;

    public PostListModelImpl(Injector injector, int groupId) {
        super(injector, PostsResult.class);

        setUrl(groupId);
    }

    /**
     * 根据id设置要请求的url
     * @param groupId
     */
    private void setUrl(int groupId) {
        switch (groupId) {
            case ForumModel.HOT_POST:
                setUrl("http://apis.guokr.com/group/post.json?retrieve_type=hot_post");
                break;
            case ForumModel.MY_GROUP_POST:
                setUrl("http://apis.guokr.com/group/post.json",
                        "recent_replies", "access_token", userModel.getToken());
                break;
            default:
                setUrl("http://apis.guokr.com/group/post.json", "by_group", "group_id", groupId);
        }
    }

    @Override
    protected void onParseResult(List<PostEntry> result) {
        IconLoader.batchLoad(networkModel, result, item -> item.author.avatar);
    }
}
