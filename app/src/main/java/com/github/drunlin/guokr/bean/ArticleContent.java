package com.github.drunlin.guokr.bean;

import java.util.List;

/**
 * 文章内容。
 *
 * @author drunlin@outlook.com
 */
public class ArticleContent extends Content {
    public List<ArticleType> channels;
    public ArticleType subject;
    public List<SimpleUser> authors;
    public transient List<ArticleType> labels;
}
