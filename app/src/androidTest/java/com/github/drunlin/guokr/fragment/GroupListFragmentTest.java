package com.github.drunlin.guokr.fragment;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.bean.Group;
import com.github.drunlin.guokr.bean.ResultClassMap.GroupsResult;
import com.github.drunlin.guokr.test.FragmentTestCase;

import java.util.List;

import static com.github.drunlin.guokr.test.util.TestUtils.getBitmapData;
import static com.github.drunlin.guokr.test.util.TestUtils.getResult;

/**
 * @author drunlin@outlook.com
 */
public class GroupListFragmentTest extends FragmentTestCase<GroupListFragment> {
    @Override
    protected void init() throws Throwable {
        addToActivity(new GroupListFragment());
    }

    @Override
    protected void onPreview() throws Throwable {
        List<Group> groups = getResult(GroupsResult.class, R.raw.group_list);
        for (Group group : groups) {
            group.icon.data = getBitmapData(R.drawable.ic_avatar);
        }
        runOnUiThread(() -> fragment.setGroups(groups));
    }
}
