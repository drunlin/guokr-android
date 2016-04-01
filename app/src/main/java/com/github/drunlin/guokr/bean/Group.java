package com.github.drunlin.guokr.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 单个小组的信息。
 *
 * @author drunlin@outlook.com
 */
public class Group {
    public int id;
    public Icon icon;
    public String name;
    @SerializedName("members_count")
    public int membersCount;
    @SerializedName("is_member")
    public boolean isMember;
}
