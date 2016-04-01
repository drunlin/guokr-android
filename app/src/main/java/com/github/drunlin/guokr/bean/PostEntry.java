package com.github.drunlin.guokr.bean;

import com.github.drunlin.guokr.bean.adapter.DateTypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * 帖子列表的单个数据。
 *
 * @author drunlin@outlook.com
 */
public class PostEntry extends TopicEntry {
    @SerializedName("date_last_replied")
    @JsonAdapter(DateTypeAdapter.class)
    public String dateLastReplied;
    public Author author;
    public Group group;
}
