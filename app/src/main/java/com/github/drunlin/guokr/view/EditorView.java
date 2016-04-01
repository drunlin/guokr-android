package com.github.drunlin.guokr.view;

import com.github.drunlin.guokr.bean.Thumbnail;

import java.io.InputStream;
import java.util.List;

/**
 * 富文本编辑器。
 *
 * @author drunlin@outlook.com
 */
public interface EditorView extends LoginNeededView {
    /**
     * 图片链接验证成功。
     */
    void onPictureUrlValid();

    /**
     * 图片链接验证失败。
     */
    void onPictureUrlInvalid();

    /**
     * 链接验证失败。
     */
    void onLinkInvalid();

    /**
     * 插入链接。
     * @param name
     * @param url
     */
    void insertLink(String name, String url);

    /**
     * 插入图片。
     * @param url
     * @param data 图片数据
     */
    void insertPicture(String url, InputStream data);

    /**
     * 直接插入图片。
     * @param url
     */
    void insertPicture(String url);

    /**
     * 设置与预览图。
     * @param data
     */
    void setThumbnails(List<Thumbnail> data);

    /**
     * 预览图的有无发生改变。
     * @param empty
     */
    void onThumbnailsChanged(boolean empty);

    /**
     * 预览图被添加。
     */
    void onThumbnailAppended();

    /**
     * 预览图被移除。
     * @param position
     */
    void onThumbnailRemoved(int position);

    /**
     * 更新预览图的状态。
     * @param position
     */
    void updateThumbnail(int position);

    /**
     * 内存不足的错误。
     */
    void onOutOfMemoryError();
}
