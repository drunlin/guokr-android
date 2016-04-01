package com.github.drunlin.guokr.bean;

import android.support.annotation.NonNull;

/**
 * 异步加载图片数据的工具类。
 *
 * @author drunlin@outlook.com
 */
public class ImageData {
    private final LoaderFactory mLoaderFactory;

    public ImageData(@NonNull LoaderFactory factory) {
        mLoaderFactory = factory;
    }

    public Loader get(@NonNull Setter setter) {
        return mLoaderFactory.create(setter);
    }

    /**
     * 用来设置图片数据。
     */
    public interface Setter {
        void set(byte[] data);
    }

    /**
     * 一次性的图片数据加载器。
     */
    public interface Loader {
        void cancel();
    }

    /**
     * 创建加载器的工厂类。
     */
    public interface LoaderFactory {
        Loader create(Setter setter);
    }
}
