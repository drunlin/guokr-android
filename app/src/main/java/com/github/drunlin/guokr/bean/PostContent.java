package com.github.drunlin.guokr.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 帖子的内容数据。
 *
 * @author drunlin@outlook.com
 */
public class PostContent extends Content {
    public Group group;
    @SerializedName("html")
    private String content;

    public String getContent() {
        return content;
    }
}
