package com.github.drunlin.guokr.activity;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.bean.Icon;
import com.github.drunlin.guokr.bean.UserInfo;
import com.github.drunlin.guokr.test.ActivityTestCase;

import static com.github.drunlin.guokr.test.util.TestUtils.getBitmapData;

/**
 * @author drunlin@outlook.com
 */
public class UserActivityTest extends ActivityTestCase<UserActivity> {
    public UserActivityTest() {
        super(UserActivity.class);
    }

    @Override
    protected void onPreview() throws Throwable {
        UserInfo userInfo = new UserInfo();
        userInfo.nickname = "Name";
        userInfo.avatar = new Icon();
        userInfo.avatar.data = getBitmapData(R.drawable.ic_avatar);
        runOnUiThread(() -> activity.setUserInfo(userInfo));
    }
}
