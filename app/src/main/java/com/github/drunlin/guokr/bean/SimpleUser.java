package com.github.drunlin.guokr.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 最简单的用户信息。
 *
 * @author drunlin@outlook.com
 */
public class SimpleUser {
    @SerializedName("is_exists")
    public boolean isExists;
    public String nickname;
    public String url;
}
