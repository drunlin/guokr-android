package com.github.drunlin.guokr.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 问答的回答。
 *
 * @author drunlin@outlook.com
 */
public class Answer extends Reply {
    @SerializedName("supportings_count")
    public int supportingsCount;
    @SerializedName("current_user_has_supported")
    public boolean hasSupported;
    @SerializedName("opposings_count")
    public int opposingsCount;
    @SerializedName("current_user_has_opposed")
    public boolean hasOpposed;
}
