package com.github.drunlin.guokr.model.impl;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.bean.PostEntry;
import com.github.drunlin.guokr.model.ForumModel;
import com.github.drunlin.guokr.test.ModelTestCase;
import com.github.drunlin.guokr.test.util.Constants;

import org.junit.Test;

import static com.github.drunlin.guokr.mock.MockNetworkModel.stubFor;
import static com.github.drunlin.guokr.mock.MockNetworkModel.urlContainers;
import static com.github.drunlin.guokr.mock.MockNetworkModel.urlEqualTo;
import static com.github.drunlin.guokr.test.ModelAsserts.assertOkResult;
import static com.github.drunlin.guokr.test.util.Constants.GROUP_Id;
import static com.github.drunlin.guokr.test.util.TestUtils.getText;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

/**
 * @author drunlin@outlook.com
 */
public class PostListModelImplTest extends ModelTestCase<PostListModelImpl> {
    @Override
    protected void init() throws Throwable {
        stubFor(urlEqualTo(Constants.IMAGE_URL)).setBody("data");
    }

    @Test
    public void create_myGroupPost() throws Exception {
        stubFor(urlContainers("http://apis.guokr.com/group/post.json" +
                "?retrieve_type=recent_replies&access_token=%s", userModel.getToken()))
                .setBody(getText(R.raw.post_list));

        model = new PostListModelImpl(injector, ForumModel.MY_GROUP_POST);

        validate();
    }

    private void validate() {
        model.resulted().add((a, b, c) -> {
            assertOkResult(a);
            assertThat(c, allOf(notNullValue(), hasSize(greaterThan(0))));
            for (PostEntry entry : c) {
                assertNotNull(entry.author.avatar.data);
                assertNotNull(entry.dateLastReplied);
            }
            countDown();
        });

        model.requestRefresh();
        await();
    }

    @Test
    public void create_hotPost() throws Exception {
        stubFor(urlContainers("http://apis.guokr.com/group/post.json" +
                "?retrieve_type=hot_post")).setBody(getText(R.raw.post_list));

        model = new PostListModelImpl(injector, ForumModel.HOT_POST);

        validate();
    }

    @Test
    public void create_withId() throws Exception {
        stubFor(urlContainers("http://apis.guokr.com/group/post.json" +
                "?retrieve_type=by_group&group_id=%d", GROUP_Id))
                .setBody(getText(R.raw.post_list));

        model = new PostListModelImpl(injector, GROUP_Id);

        validate();
    }
}
