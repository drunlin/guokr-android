package com.github.drunlin.guokr.bean;

import com.github.drunlin.guokr.bean.adapter.DateTypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * 站内信信息。
 *
 * @author drunlin@outlook.com
 */
public class Message {
    public int id;
    @SerializedName("is_read")
    public boolean isRead;
    @SerializedName("ukey_another")
    public String authorKey;
    @SerializedName("date_created")
    @JsonAdapter(DateTypeAdapter.class)
    public String dateCreated;
    public String content;
    public transient UserInfo another;
}
