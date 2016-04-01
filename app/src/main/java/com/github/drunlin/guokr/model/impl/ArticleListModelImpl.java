package com.github.drunlin.guokr.model.impl;

import com.github.drunlin.guokr.bean.ArticleEntry;
import com.github.drunlin.guokr.bean.ArticleType;
import com.github.drunlin.guokr.bean.Image;
import com.github.drunlin.guokr.bean.ResultClassMap.ArticlesResult;
import com.github.drunlin.guokr.model.ArticleListModel;
import com.github.drunlin.guokr.module.tool.Injector;

import java.util.LinkedList;
import java.util.List;

/**
 * 主题站文章列表的数据，也就是科学人下面的文章。
 *
 * @author drunlin@outlook.com
 */
public class ArticleListModelImpl
        extends TopicListModelBase<ArticleEntry> implements ArticleListModel {

    /**文章列表的URL。*/
    private static final String ARTICLE_URL = "http://apis.guokr.com/minisite/article.json";
    /**一个自定义的key，表示默认的文章类型。*/
    private static final String KEY_ALL = "all";

    /**列表的当前类型*/
    private String articleKey;

    /**
     * 指定类型的文章列表。
     * @param injector
     * @param type 文章类型
     */
    public ArticleListModelImpl(Injector injector, ArticleType type) {
        super(injector, ArticlesResult.class);

        setArticleType(type);
    }

    /**
     * 设置文章类型的类型
     * @param type
     */
    protected void setArticleType(ArticleType type) {
        articleKey = type.key;

        switch (type.type) {
            case ArticleType.CHANNEL:
                setUrlByChannel(articleKey);
                break;
            case ArticleType.SUBJECT:
                setUrlBySubject(articleKey);
                break;
            default:
                setUrlByChannel(articleKey = KEY_ALL);
                break;
        }
    }

    private void setUrlByChannel(String channel) {
        if (channel.equals(KEY_ALL)) {
            setUrl(ARTICLE_URL + "?retrieve_type=by_minisite");
        } else {
            setUrl(ARTICLE_URL, "by_channel", "channel_key", channel);
        }
    }

    private void setUrlBySubject(String subject) {
        setUrl(ARTICLE_URL, "by_subject", "subject_key", subject);
    }

    @Override
    protected void onParseResult(List<ArticleEntry> result) {
        for (ArticleEntry entry : result) {
            initLabels(entry);
            loadImage(entry.image);
        }
    }

    /**
     * 根据channels和subject生成标签。
     * @param entry
     */
    private void initLabels(ArticleEntry entry) {
        //显示的标签为channel+subject
        List<ArticleType> labels = new LinkedList<>(entry.channels);
        labels.add(entry.subject);
        //不显示当前请求的类型
        for (ArticleType label : labels) {
            if (label.key.equals(articleKey)) {
                labels.remove(label);
                break;
            }
        }
        entry.labels = labels;
    }

    /**
     * 根据图片信息加载图片。
     * @param image
     */
    protected void loadImage(Image image) {
        image.data = networkModel.loadImage(image.url);
    }
}
