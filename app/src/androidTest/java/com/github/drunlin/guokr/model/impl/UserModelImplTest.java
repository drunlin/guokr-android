package com.github.drunlin.guokr.model.impl;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.bean.Account;
import com.github.drunlin.guokr.model.PreferenceModel;
import com.github.drunlin.guokr.test.ModelAsserts;
import com.github.drunlin.guokr.test.ModelTestCase;
import com.github.drunlin.guokr.test.util.Value;

import org.junit.Assert;
import org.junit.Test;

import javax.inject.Inject;

import static com.github.drunlin.guokr.mock.MockNetworkModel.GET;
import static com.github.drunlin.guokr.mock.MockNetworkModel.post;
import static com.github.drunlin.guokr.mock.MockNetworkModel.stubFor;
import static com.github.drunlin.guokr.mock.MockNetworkModel.urlContainers;
import static com.github.drunlin.guokr.mock.MockNetworkModel.urlEqualTo;
import static com.github.drunlin.guokr.test.util.Constants.ACCESS_TOKEN;
import static com.github.drunlin.guokr.test.util.Constants.COMMENT;
import static com.github.drunlin.guokr.test.util.Constants.SUMMARY;
import static com.github.drunlin.guokr.test.util.Constants.TITLE;
import static com.github.drunlin.guokr.test.util.Constants.URL;
import static com.github.drunlin.guokr.test.util.Constants.USER_KEY;
import static com.github.drunlin.guokr.test.util.TestUtils.getText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

/**
 * @author drunlin@outlook.com
 */
public class UserModelImplTest extends ModelTestCase<UserModelImpl> {
    @Inject PreferenceModel preferenceModel;

    @Override
    protected void init() throws Throwable {
        stubFor(GET, urlContainers("http://apis.guokr.com/community/user/%s.json", USER_KEY))
                .setBody(getText(R.raw.user_info));

        model = new UserModelImpl(injector);
    }

    @Override
    protected void destroy() throws Throwable {
        reset(preferenceModel);
    }

    @Test
    public void isLoggedIn() throws Exception {
        assertFalse(model.isLoggedIn());
        
        login();
        
        assertTrue(model.isLoggedIn());
    }

    @Test
    public void recommendLink() throws Exception {
        login();

        stubFor(post("url=%s&title=%s&summary=%s&comment=%s&target=activity&access_token=%s",
                URL, TITLE, SUMMARY, COMMENT, ACCESS_TOKEN),
                urlEqualTo("http://www.guokr.com/apis/community/user/onRecommendArticle.json"))
                .setBody(getText(R.raw.simpel_result));
        
        model.recommendResulted().add(v -> {
            ModelAsserts.assertOkResult(v);
            countDown();
        });

        model.recommendLink(URL, TITLE, SUMMARY, COMMENT);
        await();
    }

    @Test
    public void loginStateChanged() throws Exception {
        assertNotNull(model.loginStateChanged());
    }

    @Test
    public void recommendResulted() throws Exception {
        assertNotNull(model.recommendResulted());
    }

    @Test
    public void getToken() throws Exception {
        assertNull(model.getToken());

        login();

        assertNotNull(model.getToken());
    }

    @Test
    public void getUserKey() throws Exception {
        assertNull(model.getUserKey());

        login();

        assertNotNull(model.getUserKey());
    }

    private void login() {
        model.setCookie(
                String.format("_32353_ukey=%s ;_32353_access_token=%s", USER_KEY, ACCESS_TOKEN));
    }

    @Test
    public void setCookie() throws Exception {
        doAnswer(invocation -> {
            Account account = (Account) invocation.getArguments()[0];
            assertEquals(ACCESS_TOKEN, account.accessToken);
            assertEquals(USER_KEY, account.userKey);
            return invocation;
        }).when(preferenceModel).setAccount(anyObject());
        model.loginStateChanged().add(Assert::assertTrue);
        
        login();
        
        assertEquals(model.getToken(), ACCESS_TOKEN);
        verify(preferenceModel).setAccount(anyObject());
    }

    @Test
    public void logout() throws Exception {
        login();

        model.loginStateChanged().add(Assert::assertFalse);
        
        model.logout();
        
        assertNull(model.getToken());
        verify(preferenceModel).setAccount(null);
    }

    @Test
    public void checkLoggedIn() throws Exception {
        Value<Boolean> called = new Value<>();

        model.checkTokenFailed().addOnce(() -> called.setValue(true));

        assertFalse(model.checkLoggedIn());
        assertTrue(called.getValue());

        login();

        model.checkTokenFailed().addOnce(Assert::fail);

        assertTrue(model.checkLoggedIn());
    }

    @Test
    public void checkTokenFailed() throws Exception {
        assertNotNull(model.checkTokenFailed());
    }

    @Test
    public void loadUserInfoFailed() throws Exception {
        assertNotNull(model.loadUserInfoFailed());
    }

    @Test
    public void userInfoChanged() throws Exception {
        assertNotNull(model.userInfoChanged());
    }

    @Test
    public void getUserInfo() throws Exception {
        assertNull(model.getUserInfo());

        login();
        await(1, 100);

        assertNotNull(model.getUserInfo());
    }

    @Test
    public void getUserInfo_userKey() throws Exception {
        assertNotNull(model.getUserInfo(USER_KEY));
    }
}
