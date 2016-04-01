package com.github.drunlin.guokr.test.util;

import com.github.drunlin.guokr.bean.ArticleType;

import static com.github.drunlin.guokr.bean.ArticleType.CHANNEL;

/**
 * 用于测试的常量，本身没有任何意义。
 *
 * @author drunlin@outlook.com
 */
public class Constants {
    public static final String URL = "http://example.com";
    public static final String USER_KEY = "user_key";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String IMAGE_URL = "http://example.com/img.png";
    public static final String COMMENT = "comment";
    public static final String SUMMARY = "summary";
    public static final ArticleType TYPE_HOT = new ArticleType(CHANNEL, "hot|热点");
    public static final int BASKET_ID = 1111;
    public static final String TITLE = "title";
    public static final String CONTENT_URL = "http://exmple.com/content.json?id=%d";
    public static final String REPLY_URL = "http://exmple.com/comment.json?id=%d";
    public static final int ARTICLE_ID = 441267;
    public static final int GROUP_Id = 10;
    public static final int QUESTION_Id = 624021;
    public static final int POST_Id = 719789;
    public static final int REPLY_ID = 10;
    public static final int LIMIT = 10;
}
