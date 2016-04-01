package com.github.drunlin.guokr.model;

import android.support.annotation.IntRange;

import com.github.drunlin.signals.impl.Signal3;

import java.util.List;

/**
 * 获取集合类数据的通用接口。
 *
 * @author drunlin@outlook.com
 */
public interface CollectionModel<T> {
    /**
     * 设置将会请求的URL。
     * @param url
     */
    void setUrl(String url);

    /**
     * 设置每次请求的最大数目。
     * @param limit
     */
    void setLimit(@IntRange(from = 1) int limit);

    /**
     * 请求指定的URL。
     * @param url
     */
    void request(String url);

    void request(String url, Object... args);

    /**
     * 刷新数据。
     */
    void requestRefresh();

    /**
     * 请求更多数据。
     */
    void requestMore();

    /**
     * 是否在正在加载。
     * @return
     */
    boolean isLoading();

    /**
     * 已经成功获取的数据。
     * @return
     */
    List<T> getResult();

    /**
     * 数据加载完成。
     *
     * @return [resultCode, isRefresh, result]
     */
    Signal3<Integer, Boolean, List<T>> resulted();

    /**
     * 服务器端数据的总大小。
     * @return
     */
    int getTotal();

    /**
     * 取消当前请求。
     */
    void cancel();
}
