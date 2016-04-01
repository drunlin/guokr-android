package com.github.drunlin.guokr.bean;

/**
 * 有大小信息的图片。
 *
 * @author drunlin@outlook.com
 */
public class Image {
    public String url;
    public int width;
    public int height;
    /**图片的数据，需要自己设置。*/
    public transient ImageData data;
}
