package com.github.drunlin.guokr.model.impl;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.test.ModelTestCase;

import org.junit.Test;

import static com.github.drunlin.guokr.mock.MockNetworkModel.stubFor;
import static com.github.drunlin.guokr.mock.MockNetworkModel.urlContainers;
import static com.github.drunlin.guokr.test.ModelAsserts.assertOkResult;
import static com.github.drunlin.guokr.test.util.Constants.ACCESS_TOKEN;
import static com.github.drunlin.guokr.test.util.Constants.GROUP_Id;
import static com.github.drunlin.guokr.test.util.Constants.POST_Id;
import static com.github.drunlin.guokr.test.util.TestUtils.getText;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * @author drunlin@outlook.com
 */
public class ForumModelImplTest extends ModelTestCase<ForumModelImpl> {
    @Override
    protected void init() throws Throwable {
        model = new ForumModelImpl(injector);
    }

    @Test
    public void getPostList() {
        assertNotNull(model.getPostList(GROUP_Id));
    }

    @Test
    public void getHotPostList() throws Exception {
        assertNotNull(model.getHotPostList());
    }

    @Test
    public void getMyGroupPosts() throws Exception {
        assertNotNull(model.getMyGroupPosts());
    }

    @Test
    public void requestJoinedGroups() throws Exception {
        stubFor(urlContainers("http://www.guokr.com/apis/group/favorite.json" +
                "?access_token=%s", ACCESS_TOKEN)).setBody(getText(R.raw.group_list));

        model.joinedGroupsResulted().addOnce((a, b) -> {
            assertOkResult(a);
            assertNotNull(b);
            countDown();
        });

        model.requestJoinedGroups();
        await();
    }

    @Test
    public void joinedGroupsResulted() throws Exception {
        assertNotNull(model.joinedGroupsResulted());
    }

    @Test
    public void getJoinedGroups() throws Exception {
        assertNull(model.getJoinedGroups());

        requestJoinedGroups();

        assertNotNull(model.getJoinedGroups());
    }

    @Test
    public void getGroup() throws Exception {
        assertNotNull(model.getGroup(GROUP_Id));
    }

    @Test
    public void getPost() throws Exception {
        assertNotNull(model.getPost(POST_Id));
    }
}
