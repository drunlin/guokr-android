package com.github.drunlin.guokr;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author drunlin@outlook.com
 */
public class Intents {
    public static final String ACTION_VIEW_QUESTION = "com.github.drunlin.guokr.action.VIEW_QUESTION";
    public static final String EXTRA_QUESTION_ID = "com.github.drunlin.guokr.extra.QUESTION_ID";

    public static final String EXTRA_URL = "com.github.drunlin.guokr.extra.URL";
    public static final String EXTRA_TITLE = "com.github.drunlin.guokr.extra.TITLE";

    public static final String ACTION_VIEW_ARTICLES = "com.github.drunlin.guokr.action.VIEW_ARTICLES";
    public static final String ACTION_VIEW_ARTICLE = "com.github.drunlin.guokr.action.VIEW_ARTICLE";
    public static final String ACTION_VIEW_REPLIES = "com.github.drunlin.guokr.action.VIEW_REPLIES";
    public static final String EXTRA_ARTICLE_KEY = "com.github.drunlin.guokr.extra.ARTICLE_KEY";
    public static final String EXTRA_ARTICLE_ID = "com.github.drunlin.guokr.extra.ARTICLE_ID";

    public static final String ACTION_VIEW_POST = "com.github.drunlin.action.VIEW_POST";
    public static final String EXTRA_GROUP_ID = "com.github.drunlin.guokr.extra.GROUP_ID";
    public static final String EXTRA_POST_ID = "com.github.drunlin.extra.POST_ID";

    public static Intent openLinkInBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        return intent;
    }

    public static Intent openLinkInApp(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setPackage(BuildConfig.APPLICATION_ID);
        return intent;
    }

    /**
     * 从Intent.getData()中获取内容的ID。
     * URL的格式为 ^http://((m|(www))\.)?guokr\.com/name/\d+/?$ ，其中的name为下面的参数。
     * @param name URL的路径前缀
     * @return -1为异常情况
     */
    public static int getIdFromUri(@NonNull Intent intent, @NonNull String name) {
        Uri uri = intent.getData();
        if (uri == null) {
            return -1;
        }
        String path = uri.getPath();
        if (path == null) {
            return -1;
        }
        Pattern pattern = Pattern.compile("/" + name + "/(\\d+)/?.*$");
        Matcher matcher = pattern.matcher(path);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return -1;
    }
}
