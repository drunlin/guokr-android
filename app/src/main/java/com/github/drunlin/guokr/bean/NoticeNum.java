package com.github.drunlin.guokr.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 通知数目信息。
 *
 * @author drunlin@outlook.com
 */
public class NoticeNum {
    @SerializedName("n")
    public int notice;
    @SerializedName("r")
    public int message;
}
