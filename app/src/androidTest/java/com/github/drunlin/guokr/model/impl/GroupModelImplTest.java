package com.github.drunlin.guokr.model.impl;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.test.ModelTestCase;

import org.junit.Test;

import static com.github.drunlin.guokr.mock.MockNetworkModel.DELETE;
import static com.github.drunlin.guokr.mock.MockNetworkModel.post;
import static com.github.drunlin.guokr.mock.MockNetworkModel.stubFor;
import static com.github.drunlin.guokr.mock.MockNetworkModel.urlContainers;
import static com.github.drunlin.guokr.mock.MockNetworkModel.urlEqualTo;
import static com.github.drunlin.guokr.test.ModelAsserts.assertOkResult;
import static com.github.drunlin.guokr.test.util.Constants.ACCESS_TOKEN;
import static com.github.drunlin.guokr.test.util.Constants.GROUP_Id;
import static com.github.drunlin.guokr.test.util.TestUtils.getText;
import static org.junit.Assert.assertNotNull;

/**
 * @author drunlin@outlook.com
 */
public class GroupModelImplTest extends ModelTestCase<GroupModelImpl> {
    @Override
    protected void init() throws Throwable {
        model = new GroupModelImpl(injector, GROUP_Id);
    }

    @Test
    public void joinGroup() throws Exception {
        stubFor(post("group_id=%d&access_token=%s", GROUP_Id, ACCESS_TOKEN),
                urlEqualTo("http://www.guokr.com/apis/group/member.json"))
                .setBody(getText(R.raw.simpel_result));

        model.joinGroupResulted().add(a -> {
            assertOkResult(a);
            countDown();
        });

        model.joinGroup();
        await();
    }

    @Test
    public void joinGroupResulted() throws Exception {
        assertNotNull(model.joinGroupResulted());
    }

    @Test
    public void quitGroup() throws Exception {
        stubFor(DELETE, urlEqualTo("http://www.guokr.com/apis/group/member.json" +
                "?group_id=%d&access_token=%s", GROUP_Id, ACCESS_TOKEN))
                .setBody(getText(R.raw.simpel_result));

        model.quitGroupResulted().add(a -> {
            assertOkResult(a);
            countDown();
        });

        model.quitGroup();
        await();
    }

    @Test
    public void quitGroupResulted() throws Exception {
        assertNotNull(model.quitGroupResulted());
    }

    @Test
    public void requestGroup() throws Exception {
        stubFor(urlContainers("http://www.guokr.com/apis/group/group.json" +
                "?retrieve_type=by_id&group_id=%d", GROUP_Id, ACCESS_TOKEN))
                .setBody(getText(R.raw.group));

        model.groupResulted().add((a, b) -> {
            assertOkResult(a);
            assertNotNull(b);
            countDown();
        });

        model.requestGroup();
        await();
    }

    @Test
    public void groupResulted() throws Exception {
        assertNotNull(model.groupResulted());
    }
}
