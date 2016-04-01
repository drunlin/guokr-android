package com.github.drunlin.guokr.bean;

import com.github.drunlin.guokr.bean.adapter.DateTypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * 通知信息。
 *
 * @author drunlin@outlook.com
 */
public class Notice {
    public int id;
    @SerializedName("is_read")
    public boolean isRead;
    @SerializedName("date_last_updated")
    @JsonAdapter(DateTypeAdapter.class)
    public String dateLastUpdated;
    public String content;
}
