package com.github.drunlin.guokr.activity;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.bean.Group;
import com.github.drunlin.guokr.bean.ResultClassMap.GroupsResult;
import com.github.drunlin.guokr.test.ActivityTestCase;

import static com.github.drunlin.guokr.test.util.Constants.GROUP_Id;
import static com.github.drunlin.guokr.test.util.TestUtils.getResult;

/**
 * @author drunlin@outlook.com
 */
public class GroupActivityTest extends ActivityTestCase<GroupActivity> {
    public GroupActivityTest() {
        super(GroupActivity.class, false);
    }

    @Override
    protected void init() throws Throwable {
        launchActivity(GroupActivity.getIntent(GROUP_Id));
    }

    @Override
    protected void onPreview() throws Throwable {
        Group group = getResult(GroupsResult.class, R.raw.group).get(0);
        runOnUiThread(() -> activity.setGroup(group));
    }
}
