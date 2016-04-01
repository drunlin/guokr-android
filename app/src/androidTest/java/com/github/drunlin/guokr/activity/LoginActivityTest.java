package com.github.drunlin.guokr.activity;

import android.util.Log;

import com.github.drunlin.guokr.presenter.LoginPresenter;
import com.github.drunlin.guokr.test.ActivityTestCase;

import org.junit.Test;

import javax.inject.Inject;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;

/**
 * @author drunlin@outlook.com
 */
public class LoginActivityTest extends ActivityTestCase<LoginActivity> {
    private static final String TAG = LoginActivityTest.class.getName();

    @Inject LoginPresenter loginPresenter;

    public LoginActivityTest() {
        super(LoginActivity.class);
    }

    @Test
    public void setCookie() throws Exception {
        doAnswer(invocation -> {
            String cookie = (String) invocation.getArguments()[0];
            assertNotNull(cookie);
            Log.d(TAG, "cookie:" + cookie);
            countDown();
            return invocation;
        }).when(loginPresenter).setCookie(anyString());

        pauseToPreview();
    }
}
