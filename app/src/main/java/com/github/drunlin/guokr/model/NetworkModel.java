package com.github.drunlin.guokr.model;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.github.drunlin.guokr.bean.ImageData;

/**
 * 网络管理器。
 *
 * @author drunlin@outlook.com
 */
public interface NetworkModel {
    /**
     * 把Request添加到队列。
     * @param request
     * @return
     */
    <T> Request<T> add(Request<T> request);

    /**
     * 把Request添加到队列，并设置tag。
     * @param request
     * @param target
     * @return
     */
    <T> Request<T> add(Request<T> request, Object target);

    /**
     * 加载图片并缓存，如果缓存中存在图片，则直接调用listener.onResponse。
     * @param url
     * @param listener
     * @param errorListener
     */
    void loadImage(String url, Listener<byte[]> listener, ErrorListener errorListener);

    /**
     * 加载图片并缓存，如果缓存中存在图片，则直接调用listener.onResponse。
     * @param url
     * @param listener
     * @param errorListener
     * @param tag
     */
    void loadImage(String url, Listener<byte[]> listener, ErrorListener errorListener, Object tag);

    /**
     * 获取一个图片加载器。
     * @param url
     * @return
     */
    ImageData loadImage(String url);

    /**
     * 取消请求。
     * @param target
     */
    void cancel(Object target);
}
