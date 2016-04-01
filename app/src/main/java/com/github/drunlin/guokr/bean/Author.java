package com.github.drunlin.guokr.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 带有头像的作者信息。
 *
 * @author drunlin@outlook.com
 */
public class Author extends SimpleUser {
    /**果壳网用户ID的一种。*/
    @SerializedName("ukey")
    public String userKey;
    public Icon avatar;
}
