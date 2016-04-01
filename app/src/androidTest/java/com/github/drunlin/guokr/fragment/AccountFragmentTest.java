package com.github.drunlin.guokr.fragment;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.bean.Icon;
import com.github.drunlin.guokr.bean.UserInfo;
import com.github.drunlin.guokr.test.FragmentTestCase;

import static com.github.drunlin.guokr.test.util.TestUtils.getBitmapData;

/**
 * @author drunlin@outlook.com
 */
public class AccountFragmentTest extends FragmentTestCase<AccountFragment> {
    @Override
    protected void init() throws Throwable {
        addToActivity(new AccountFragment());
    }

    @Override
    protected void onPreview() throws Throwable {
        UserInfo userInfo = new UserInfo();
        userInfo.nickname = "Name";
        userInfo.avatar = new Icon();
        userInfo.avatar.data = getBitmapData(R.drawable.ic_avatar);
        runOnUiThread(() -> fragment.setUserInfo(userInfo));
    }
}
