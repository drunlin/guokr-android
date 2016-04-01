package com.github.drunlin.guokr.model.impl;

import android.webkit.CookieManager;

import com.github.drunlin.guokr.bean.Account;
import com.github.drunlin.guokr.bean.ResponseCode;
import com.github.drunlin.guokr.bean.ResultClassMap.UserInfoResult;
import com.github.drunlin.guokr.bean.UserInfo;
import com.github.drunlin.guokr.model.IconLoader;
import com.github.drunlin.guokr.model.Model;
import com.github.drunlin.guokr.model.NetworkModel;
import com.github.drunlin.guokr.model.PreferenceModel;
import com.github.drunlin.guokr.model.UserModel;
import com.github.drunlin.guokr.model.request.AccessRequest;
import com.github.drunlin.guokr.model.request.JsonRequest;
import com.github.drunlin.guokr.module.tool.Injector;
import com.github.drunlin.signals.impl.Signal0;
import com.github.drunlin.signals.impl.Signal1;
import com.google.common.base.Objects;

import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;

import static com.android.volley.Request.Method.POST;

/**
 * 与用户相关的数据操作。
 *
 * @author drunlin@outlook.com
 */
public class UserModelImpl extends Model implements UserModel {
    private final Signal1<Integer> recommendResulted = new Signal1<>();
    private final Signal1<Boolean> loggedStateChanged = new Signal1<>();
    private final Signal0 checkTokenFailed = new Signal0();
    private final Signal0 userInfoChanged = new Signal0();
    private final Signal0 loadUserInfoFailed = new Signal0();

    @Inject NetworkModel networkModel;
    @Inject PreferenceModel preferenceModel;

    private Account account;
    private UserInfo userInfo;

    public UserModelImpl(Injector injector) {
        super(injector);

        account = preferenceModel.getAccount();
        userInfo = preferenceModel.getUserInfo();

        if (isLoggedIn()) {
            loadUserInfo();
        }
    }

    private void loadUserInfo() {
        final String url = "http://apis.guokr.com/community/user/%s.json";
        new JsonRequest.Builder<>(url, UserInfoResult.class)
                .setUrlArgs(getUserKey())
                .setParseListener(this::loadAvatar)
                .setListener(response -> setUserInfo(response.result))
                .setErrorListener(error -> loadUserInfoFailed.dispatch())
                .build(networkModel);
    }

    private void loadAvatar(UserInfoResult response) {
        IconLoader.load(networkModel, response.result.avatar);
    }

    private void setUserInfo(UserInfo info) {
        if (Objects.equal(userInfo, info)) {
            return;
        }

        userInfo = info;

        preferenceModel.setUserInfo(info);

        userInfoChanged.dispatch();
    }

    @Override
    public Signal0 loadUserInfoFailed() {
        return loadUserInfoFailed;
    }

    @Override
    public Signal0 userInfoChanged() {
        return userInfoChanged;
    }

    @Override
    public void setCookie(String cookie) {
        Account account = new Account();
        for (String s : cookie.split(";")) {
            String[] kv = s.split("=");
            String k = kv[0].trim();
            if (k.equals("_32353_ukey")) {
                account.userKey = kv[1].trim();
                if (account.accessToken != null) {
                    break;
                }
            } else if (k.equals("_32353_access_token")) {
                account.accessToken = kv[1].trim();
                if (account.userKey != null) {
                    break;
                }
            }
        }
        setAccount(account);
    }

    private void setAccount(Account info) {
        if (Objects.equal(account, info)) {
            return;
        }

        account = info;

        preferenceModel.setAccount(account);

        if (isLoggedIn()) {
            loadUserInfo();
        } else {
            setUserInfo(null);
        }

        loggedStateChanged.dispatch(isLoggedIn());
    }

    @Override
    public Signal1<Boolean> loginStateChanged() {
        return loggedStateChanged;
    }

    @Override
    public UserInfo getUserInfo(String userKey) {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        UserInfo[] userInfo = new UserInfo[1];
        String url = "http://apis.guokr.com/community/user/%s.json";
        new JsonRequest.Builder<>(url, UserInfoResult.class)
                .setUrlArgs(userKey)
                .setParseListener(this::loadAvatar)
                .setListener(response -> {
                    userInfo[0] = response.result;
                    countDownLatch.countDown();
                })
                .setErrorListener(e -> countDownLatch.countDown())
                .build(networkModel);

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return userInfo[0];
    }

    @Override
    public UserInfo getUserInfo() {
        return userInfo;
    }

    @Override
    public String getUserKey() {
        return checkLoggedIn() ? account.userKey : null;
    }

    @Override
    public String getToken() {
        return checkLoggedIn() ? account.accessToken : null;
    }

    @Override
    public boolean checkLoggedIn() {
        if (isLoggedIn()) {
            return true;
        } else {
            checkTokenFailed.dispatch();
            return false;
        }
    }

    @Override
    public boolean isLoggedIn() {
        return account != null;
    }

    @Override
    public Signal0 checkTokenFailed() {
        return checkTokenFailed;
    }

    @Override
    public void logout() {
        setAccount(null);

        //noinspection deprecation
        CookieManager.getInstance().removeAllCookie();
    }

    @Override
    public void recommendLink(String link, String title, String summary, String comment) {
        final String url = "http://www.guokr.com/apis/community/user/recommend.json";
        new AccessRequest.SimpleRequestBuilder(url, getToken())
                .setMethod(POST)
                .addParam("title", title)
                .addParam("url", link)
                .addParam("summary", summary)
                .addParam("comment", comment)
                .addParam("target", "activity")
                .setListener(response -> recommendResulted.dispatch(ResponseCode.OK))
                .setErrorListener(error -> recommendResulted.dispatch(error.getCode()))
                .build(networkModel);
    }

    @Override
    public Signal1<Integer> recommendResulted() {
        return recommendResulted;
    }
}
