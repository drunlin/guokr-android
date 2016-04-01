package com.github.drunlin.guokr.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 站内信列表的特殊结构。
 *
 * @author drunlin@outlook.com
 */
public class MessagesResult extends Result<MessagesResult.R> {
    public static class R {
        @SerializedName("recent_list")
        public List<Message> recentList;
    }
}
