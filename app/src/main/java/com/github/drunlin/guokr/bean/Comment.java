package com.github.drunlin.guokr.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 文章和帖子的评论。
 *
 * @author drunlin@outlook.com
 */
public class Comment extends Reply {
    @SerializedName("likings_count")
    public int likingsCount;
    @SerializedName("current_user_has_liked")
    public boolean hasLiked;
}
