package com.github.drunlin.guokr.test.util;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.RawRes;
import android.support.test.InstrumentationRegistry;

import com.github.drunlin.guokr.bean.Result;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 用于测试的工具类。
 *
 * @author drunlin@outlook.com
 */
public class TestUtils {
    /**
     * 获取资源文件的二进制数据。
     * @param resId
     * @return
     * @throws IOException
     */
    public static ByteArrayOutputStream getRawData(@RawRes int resId) throws IOException {
        Context context = InstrumentationRegistry.getTargetContext();
        InputStream is = context.getResources().openRawResource(resId);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        for (int len = is.read(data); len != -1; len = is.read(data)) {
            buffer.write(data, 0, len);
        }
        buffer.flush();
        return buffer;
    }

    /**
     * 获取资源目录下的图片数据。
     * @param resId
     * @return
     * @throws IOException
     */
    public static byte[] getBitmapData(@DrawableRes @RawRes int resId) {
        try {
            return getRawData(resId).toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取资源文件的文本数据。
     * @param resId
     * @return
     */
    public static String getText(@RawRes int resId) {
        try {
            return getRawData(resId).toString();
        } catch (IOException e) {
            throw new RuntimeException("");
        }
    }

    /**
     * 反序列化json数据。
     * @param clazz
     * @param resId
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz, int resId) {
        Gson gson = new Gson();
        String json = getText(resId);
        return gson.fromJson(json, clazz);
    }

    /**
     * 从json数据中生成其中的结果数据。
     * @param clazz
     * @param resId
     * @param <T>
     * @return
     */
    public static <T> T getResult(Class<? extends Result<T>> clazz, int resId) {
        return getBean(clazz, resId).result;
    }
}
