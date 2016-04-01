package com.github.drunlin.guokr.bean;

import android.support.annotation.Nullable;

import com.github.drunlin.guokr.bean.adapter.IconTypeAdapter;
import com.google.gson.annotations.JsonAdapter;

import java.util.List;

/**
 * 有一个或多个不同大小图片的集合，类似于"img:{normal:...,large:...}"的结构。
 *
 * @author drunlin@outlook.com
 */
@JsonAdapter(IconTypeAdapter.class)
public class Icon {
    /**按照图片大小升序排列的图片链接，其中0<=size<=3。*/
    public List<String> urls;
    /**其中一个图片的数据。*/
    public byte[] data;

    /**
     * 中等大小的图片的链接。
     * @return
     */
    public @Nullable String getUrl() {
        return (urls == null || urls.size() == 0)
                ? null : urls.get(Math.min(1, urls.size() - 1));
    }
}
