package com.github.drunlin.guokr.bean;

import com.github.drunlin.guokr.bean.adapter.DateTypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * 文章，帖子，问答内容的基类。
 *
 * @author drunlin@outlook.com
 */
public abstract class Content {
    public String url;
    @SerializedName(value = "title", alternate = "question")
    public String title;
    @SerializedName("replies_count")
    public int repliesCount;
    @SerializedName(value = "is_replyable", alternate = "is_answerable")
    public boolean isReplyable;
    public Author author;
    public String summary;
    @SerializedName(value = "content", alternate = "annotation_html")
    private String content;
    @SerializedName("date_created")
    @JsonAdapter(DateTypeAdapter.class)
    public String dateCreated;

    public String getContent() {
        return content;
    }
}
