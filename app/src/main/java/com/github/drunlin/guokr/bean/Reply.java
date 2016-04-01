package com.github.drunlin.guokr.bean;

import com.github.drunlin.guokr.bean.adapter.DateTypeAdapter;
import com.github.drunlin.guokr.bean.adapter.HtmlTypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * 回复的内容。
 *
 * @author drunlin@outlook.com
 */
public class Reply {
    public int id;
    public Author author;
    public transient boolean isAuthor;
    @SerializedName("date_created")
    @JsonAdapter(DateTypeAdapter.class)
    public String dateCreated;
    @SerializedName("html")
    @JsonAdapter(HtmlTypeAdapter.class)
    public String content;
}
