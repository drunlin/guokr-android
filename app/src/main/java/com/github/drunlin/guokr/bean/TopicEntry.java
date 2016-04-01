package com.github.drunlin.guokr.bean;

import com.github.drunlin.guokr.bean.adapter.DateTypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * 文章和帖子列表的单个数据。
 *
 * @author drunlin@outlook.com
 */
public abstract class TopicEntry {
    public int id;
    public String title;
    @SerializedName("resource_url")
    public String resourceUrl;
    @SerializedName("replies_count")
    public int repliesCount;
    @SerializedName("date_created")
    @JsonAdapter(DateTypeAdapter.class)
    public String dateCreated;
}
