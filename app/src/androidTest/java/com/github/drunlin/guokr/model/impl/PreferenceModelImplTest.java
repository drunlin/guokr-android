package com.github.drunlin.guokr.model.impl;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.bean.Icon;
import com.github.drunlin.guokr.bean.UserInfo;
import com.github.drunlin.guokr.test.ModelTestCase;

import org.junit.Test;

import java.util.Arrays;

import static com.github.drunlin.guokr.test.util.TestUtils.getBitmapData;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author drunlin@outlook.com
 */
public class PreferenceModelImplTest extends ModelTestCase<PreferenceModelImpl> {
    @Override
    protected void init() throws Throwable {
        model = new PreferenceModelImpl();
    }

    @Test
    public void userInfo() throws Exception {
        UserInfo userInfo = new UserInfo();
        userInfo.nickname = "name";
        userInfo.avatar = new Icon();
        userInfo.avatar.data = getBitmapData(R.drawable.ic_avatar);
        model.setUserInfo(userInfo);
        UserInfo savedUserInfo = model.getUserInfo();
        assertEquals(userInfo.nickname, savedUserInfo.nickname);
        assertTrue(Arrays.equals(userInfo.avatar.data, savedUserInfo.avatar.data));
    }
}
