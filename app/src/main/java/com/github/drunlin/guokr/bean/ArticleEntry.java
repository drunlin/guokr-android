package com.github.drunlin.guokr.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 主题站文章列表每项的数据结构。
 *
 * @author drunlin@outlook.com
 */
public class ArticleEntry extends TopicEntry {
    @SerializedName("image_info")
    public Image image;
    public List<ArticleType> channels;
    public ArticleType subject;
    public transient List<ArticleType> labels;
    public List<SimpleUser> authors;
    public String summary;
}
