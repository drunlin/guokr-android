package com.github.drunlin.guokr.model.impl;

import com.github.drunlin.guokr.bean.CollectionResult;
import com.github.drunlin.guokr.bean.TopicEntry;
import com.github.drunlin.guokr.model.JsonListModel;
import com.github.drunlin.guokr.model.TopicListModel;
import com.github.drunlin.guokr.module.tool.Injector;
import com.github.drunlin.guokr.util.UrlUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author drunlin@outlook.com
 */
public abstract class TopicListModelBase<T extends TopicEntry>
        extends JsonListModel<T> implements TopicListModel<T> {

    public TopicListModelBase(Injector injector, Class<? extends CollectionResult<T>> clazz) {
        super(injector, clazz);
    }

    /**
     * 果壳网请求列表数据的通用方法
     * @param url
     * @param retrieveType
     * @param typeKey
     * @param typeValue
     */
    protected void setUrl(String url, String retrieveType, String typeKey, Object typeValue) {
        Map<String, Object> param = new HashMap<>();
        param.put("retrieve_type", retrieveType);
        param.put(typeKey, typeValue);
        setUrl(UrlUtil.addQuery(url, param));
    }

    @Override
    public List<T> getTopics() {
        return result;
    }
}
