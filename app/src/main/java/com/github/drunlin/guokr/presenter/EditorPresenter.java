package com.github.drunlin.guokr.presenter;

import com.github.drunlin.guokr.bean.Thumbnail;

import java.io.InputStream;

/**
 * @author drunlin@outlook.com
 */
public interface EditorPresenter {
    /**
     * 检查直接插入图片时的链接。
     * @param url
     */
    void onInsertPicture(String url);

    /**
     * 插入外链图片。
     * @param url
     */
    void insertPicture(String url);

    /**
     * 插入图片。
     * @param stream
     */
    void insertPicture(InputStream stream);

    /**
     * 移除图片。
     * @param thumbnail
     */
    void removePicture(Thumbnail thumbnail);

    /**
     * 重新加载图片。
     * @param thumbnail
     */
    void reloadPicture(Thumbnail thumbnail);

    /**
     * 插入链接。
     * @param name
     * @param url
     */
    void onInsertLink(String name, String url);

    /**
     * 插入图片。
     * @param thumbnail
     */
    void onInsertPicture(Thumbnail thumbnail);
}
