package com.github.drunlin.guokr.model.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.VisibleForTesting;
import android.util.Base64;

import com.github.drunlin.guokr.App;
import com.github.drunlin.guokr.bean.Account;
import com.github.drunlin.guokr.bean.Icon;
import com.github.drunlin.guokr.bean.UserInfo;
import com.github.drunlin.guokr.model.PreferenceModel;

/**
 * 用SharedPreferences实现配置信息的本地存储。
 *
 * @author drunlin@outlook.com
 */
public class PreferenceModelImpl implements PreferenceModel {
    public static String PREFERENCE_NAME = "settings";

    private static final String PREF_USER_KEY = "user_key";
    private static final String PREF_ACCESS_TOKEN = "access_token";
    private static final String PREF_NICKNAME = "nickname";
    private static final String PREF_AVATAR = "avatar";
    private static final String PREF_ENABLE_WATERMARK = "enable_watermark";
    private static final String PREF_MAIN_ACTIVITY_PAGE = "main_activity_page";
    private static final String PREF_SAVE_MAIN_ACTIVITY_PAGE = "save_main_activity_page";
    private static final String PREF_NIGHT_MODE = "night_mode";

    private SharedPreferences preferences;

    public PreferenceModelImpl() {
        this(App.getContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE));
    }

    @VisibleForTesting
    protected PreferenceModelImpl(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    @Override
    public void setAccount(Account account) {
        if (account != null) {
            preferences.edit()
                    .putString(PREF_USER_KEY, account.userKey)
                    .putString(PREF_ACCESS_TOKEN, account.accessToken)
                    .apply();
        } else {
            preferences.edit()
                    .remove(PREF_USER_KEY)
                    .remove(PREF_ACCESS_TOKEN)
                    .apply();
        }
    }

    @Override
    public Account getAccount() {
        Account account = new Account();
        account.userKey = preferences.getString(PREF_USER_KEY, null);
        account.accessToken = preferences.getString(PREF_ACCESS_TOKEN, null);
        return account.userKey != null ? account : null;
    }

    @Override
    public void setUserInfo(UserInfo userInfo) {
        if (userInfo != null) {
            preferences.edit()
                    .putString(PREF_NICKNAME, userInfo.nickname)
                    .putString(PREF_AVATAR, encodeToString(userInfo.avatar.data))
                    .apply();
        } else {
            preferences.edit()
                    .remove(PREF_NICKNAME)
                    .remove(PREF_AVATAR)
                    .apply();
        }
    }

    private String encodeToString(byte[] data) {
        return data != null ? Base64.encodeToString(data, Base64.DEFAULT) : "";
    }

    @Override
    public UserInfo getUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.nickname = preferences.getString(PREF_NICKNAME, null);
        userInfo.avatar = new Icon();
        userInfo.avatar.data = decode(preferences.getString(PREF_AVATAR, ""));
        return userInfo.nickname != null ? userInfo : null;
    }

    private byte[] decode(String str) {
        return Base64.decode(str, Base64.DEFAULT);
    }

    @Override
    public boolean isWatermarkEnable() {
        return preferences.getBoolean(PREF_ENABLE_WATERMARK, false);
    }

    @Override
    public boolean isSavedPagePosition() {
        return preferences.getBoolean(PREF_SAVE_MAIN_ACTIVITY_PAGE, true);
    }

    @Override
    public void setPagePosition(int position) {
        preferences.edit().putInt(PREF_MAIN_ACTIVITY_PAGE, position).apply();
    }

    @Override
    public int getPagePosition() {
        return preferences.getInt(PREF_MAIN_ACTIVITY_PAGE, 0);
    }

    @Override
    public void setNightMode(boolean nightMode) {
        preferences.edit().putBoolean(PREF_NIGHT_MODE, nightMode).apply();
    }

    @Override
    public boolean isNightMode() {
        return preferences.getBoolean(PREF_NIGHT_MODE, false);
    }
}
