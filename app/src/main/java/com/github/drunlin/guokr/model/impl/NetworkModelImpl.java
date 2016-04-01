package com.github.drunlin.guokr.model.impl;

import android.support.v4.util.LruCache;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.github.drunlin.guokr.App;
import com.github.drunlin.guokr.bean.ImageData;
import com.github.drunlin.guokr.model.NetworkModel;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.toolbox.HttpHeaderParser.parseCacheHeaders;

/**
 * 用Google Volley实现对网络队列的管理。
 *
 * @author drunlin@outlook.com
 */
public class NetworkModelImpl implements NetworkModel {
    /**唯一的请求队列。*/
    private RequestQueue requestQueue;
    /**图片内存缓存容器。*/
    private LruBitmapCache bitmapCache;

    public NetworkModelImpl() {
        bitmapCache = new LruBitmapCache();

        Cache cache = new DiskBasedCache(App.getContext().getCacheDir(), 10 * 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();
    }

    @Override
    public <T> Request<T> add(Request<T> request) {
        return requestQueue.add(request);
    }

    @Override
    public <T> Request<T> add(Request<T> request, Object target) {
        //noinspection unchecked
        return (Request<T>) add(request).setTag(target);
    }

    @Override
    public void loadImage(String url, Listener<byte[]> listener, ErrorListener errorListener) {
        loadImage(url, listener, errorListener, null);
    }

    @Override
    public void loadImage(String url,
                          Listener<byte[]> listener, ErrorListener errorListener, Object tag) {
        if (url == null) {
            App.post(() -> errorListener.onErrorResponse(new VolleyError("Null url.")));
            return;
        }

        final byte[] data = bitmapCache.get(url);
        if (data == null) {
            add(new Request<byte[]>(GET , url, errorListener) {
                @Override
                protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
                    return Response.success(response.data, parseCacheHeaders(response));
                }

                @Override
                protected void deliverResponse(byte[] response) {
                    bitmapCache.put(url, response);
                    App.post(() -> listener.onResponse(response));
                }
            }, tag);
        } else {
            App.post(() -> listener.onResponse(data));
        }
    }

    @Override
    public ImageData loadImage(String url) {
        return new ImageData(setter -> {
            final Object tag = new Object();
            loadImage(url, setter::set, null, tag);
            return () -> cancel(tag);
        });
    }

    @Override
    public void cancel(Object target) {
        requestQueue.cancelAll(target);
    }

    /**
     * 位图内存缓存容器。
     */
    public static class LruBitmapCache extends LruCache<String, byte[]> {
        public LruBitmapCache() {
            super((int) Runtime.getRuntime().maxMemory() / 64);
        }

        @Override
        protected int sizeOf(String key, byte[] value) {
            return key.length() * 16 + value.length;
        }
    }
}
