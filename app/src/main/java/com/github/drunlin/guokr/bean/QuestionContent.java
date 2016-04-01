package com.github.drunlin.guokr.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 问题内容数据。
 *
 * @author drunlin@outlook.com
 */
public class QuestionContent extends Content {
    @SerializedName("is_stared")
    public boolean isStared;
    @SerializedName("recommends_count")
    public int recommendsCount;
}
