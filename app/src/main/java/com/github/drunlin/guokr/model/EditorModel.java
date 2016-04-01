package com.github.drunlin.guokr.model;

import com.github.drunlin.guokr.bean.Thumbnail;
import com.github.drunlin.signals.impl.Signal0;
import com.github.drunlin.signals.impl.Signal1;

import java.io.InputStream;
import java.util.List;

/**
 * 富文本编辑器图片预览相关。
 *
 * @author drunlin@outlook.com
 */
public interface EditorModel {
    /**
     * 上传图片。
     * @param data
     */
    void uploadImage(byte[] data);

    /**
     * 上传图片。
     * @param stream
     */
    void uploadImage(InputStream stream);

    /**
     * 下载图片。
     * @param url
     */
    void downloadImage(String url);

    /**
     * 内存溢出错误。
     * @return
     */
    Signal0 outOfMemoryError();

    /**
     * 重新上传/下载图片。
     * @param thumbnail
     */
    void reloadImage(Thumbnail thumbnail);

    /**
     * 获取预览图。
     * @return
     */
    List<Thumbnail> getThumbnails();

    /**
     * 添加了预览图。
     * @return
     */
    Signal0 thumbnailAppended();

    /**
     * 预览图被移除。
     * @return [position]
     */
    Signal1<Integer> thumbnailRemoved();

    /**
     * 预览图状态改变。
     * @return [position]
     */
    Signal1<Integer> thumbnailsChanged();

    /**
     * 删除预览图和其对应的图片。
     * @param thumbnail
     */
    void deleteImage(Thumbnail thumbnail);

    /**
     * 获取加载完成的图片。
     * @param url
     * @return
     */
    InputStream getImage(String url);

    /**
     * 清除加载过程中的缓存。
     */
    void purge();
}
