package com.github.drunlin.guokr.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.github.drunlin.guokr.App;
import com.github.drunlin.guokr.util.JavaUtil.Converter;

import java.io.IOException;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

import pl.droidsonroids.gif.GifDrawable;

/**
 * 位图数据相关的工具类。
 * //TODO 支持超长图片
 *
 * @author drunlin@outlook.com
 */
public class BitmapUtils {
    /**设备能显示的最大尺寸。*/
    private static final int MAX_DIMENSION = getMaxTextureSize();
    /**图片默认的最大宽度。*/
    private static final int MAX_WIDTH = (int)
            Math.max(1, Math.sqrt(Runtime.getRuntime().maxMemory() / 1024 / 1024 / 32)) * 300;

    /**
     * @see <a href="http://stackoverflow.com/questions/15313807/
     * android-maximum-allowed-width-height-of-bitmap">
     * Android : Maximum allowed width & height of bitmap</a>
     * @return
     */
    public static int getMaxTextureSize() {
        // Safe minimum default size
        final int IMAGE_MAX_BITMAP_DIMENSION = 2048;

        // Get EGL Display
        EGL10 egl = (EGL10) EGLContext.getEGL();
        EGLDisplay display = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);

        // Initialise
        int[] version = new int[2];
        egl.eglInitialize(display, version);

        // Query total number of configurations
        int[] totalConfigurations = new int[1];
        egl.eglGetConfigs(display, null, 0, totalConfigurations);

        // Query actual list configurations
        EGLConfig[] configurationsList = new EGLConfig[totalConfigurations[0]];
        egl.eglGetConfigs(display, configurationsList, totalConfigurations[0], totalConfigurations);

        int[] textureSize = new int[1];
        int maximumTextureSize = 0;

        // Iterate through all the configurations to located the maximum texture size
        for (int i = 0; i < totalConfigurations[0]; i++) {
            // Only need to check for width since opengl textures are always squared
            egl.eglGetConfigAttrib(
                    display, configurationsList[i], EGL10.EGL_MAX_PBUFFER_WIDTH, textureSize);

            // Keep track of the maximum texture size
            if (maximumTextureSize < textureSize[0])
                maximumTextureSize = textureSize[0];
        }

        // Release
        egl.eglTerminate(display);

        // Return largest texture size found, or default
        return Math.max(maximumTextureSize, IMAGE_MAX_BITMAP_DIMENSION);
    }

    /**
     * 计算缩放比。
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
        final int width = options.outWidth;
        final int height = options.outHeight;

        int inSampleSize = 1;

        int w, h;
        while ((w = width / inSampleSize) > reqWidth || w > MAX_DIMENSION
                || (h = height / inSampleSize) > reqHeight || h > MAX_DIMENSION) {
            inSampleSize *= 2;
        }

        return inSampleSize;
    }

    /**
     * 生成限定高宽的位图。
     * @see BitmapFactory#decodeByteArray(byte[], int, int, Options)
     * @param data
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static BitmapData decodeByteArray(byte[] data, int reqWidth, int reqHeight) {
        Options options = new Options();

        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);

        final int width = options.outWidth;
        final int height = options.outHeight;

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;

        return new BitmapData(
                BitmapFactory.decodeByteArray(data, 0, data.length, options), width, height);
    }

    /**
     * 生成限定宽度的位图。
     * @see BitmapFactory#decodeByteArray(byte[], int, int, Options)
     * @param data
     * @param maxWidth
     * @return
     */
    private static BitmapData decodeByteArray(byte[] data, int maxWidth) {
        return decodeByteArray(data, maxWidth, Integer.MAX_VALUE);
    }

    /**
     * 生成限定高宽的位图。
     * @see BitmapFactory#decodeByteArray(byte[], int, int, Options)
     * @param data
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap createBitmap(byte[] data, int reqWidth, int reqHeight) {
        return decodeByteArray(data, reqWidth, reqHeight).bitmap;
    }

    /**
     * 通过文件头判断是否为GIF图像。
     * @param data
     * @return
     */
    private static boolean isGif(byte[] data) {
        final String gif = "GIF";
        for (int i = 0; i < 3; ++i) {
            if (data[i] != gif.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 用字节数组生成Drawable，非GIF的数据需要creator生成。
     * @param data
     * @param creator
     * @return
     */
    public static Drawable createDrawable(byte[] data,
                                          Converter<BitmapData, BitmapDrawable> creator) {
        if (data == null) {
            return null;
        }
        try {
            return isGif(data)
                    ? new GifDrawable(data) : creator.convert(decodeByteArray(data, MAX_WIDTH));
        } catch (IOException | OutOfMemoryError e) {
            return null;
        }
    }

    /**
     * 用字节数组来创建。
     * @param resources
     * @param data
     * @return
     */
    public static Drawable createDrawable(Resources resources, byte[] data) {
        return createDrawable(data, bitmapData -> new AppBitmapDrawable(resources, bitmapData));
    }

    /**
     * 用字节数组来创建。
     * @param data
     * @return
     */
    public static Drawable createDrawable(byte[] data) {
        return createDrawable(data, AppBitmapDrawable::new);
    }

//    /**
//     * @see #createDrawable(Resources, byte[])
//     * @param resources
//     * @param inputStream
//     * @return
//     */
//    public static Drawable createDrawable(Resources resources, InputStream inputStream) {
//        try {
//            return createDrawable(resources, ByteStreams.toByteArray(inputStream));
//        } catch (IOException | OutOfMemoryError e) {
//            return null;
//        }
//    }

    /**
     * 获取压缩过的图像的原始宽度。
     * @param drawable
     * @return
     */
    public static int getOriginalWidth(Drawable drawable) {
        return drawable instanceof AppBitmapDrawable
                ? ((AppBitmapDrawable) drawable).width : drawable.getIntrinsicWidth();
    }

    /**
     * 获取压缩过的图像的原始高度。
     * @param drawable
     * @return
     */
    public static int getOriginalHeight(Drawable drawable) {
        return drawable instanceof AppBitmapDrawable
                ? ((AppBitmapDrawable) drawable).height : drawable.getIntrinsicHeight();
    }

    private static class BitmapData {
        public final Bitmap bitmap;
        public final int width;
        public final int height;

        public BitmapData(Bitmap b, int w, int h) {
            bitmap = b;
            width = w;
            height = h;
        }
    }

    private static class AppBitmapDrawable extends BitmapDrawable {
        public final int width;
        public final int height;

        public AppBitmapDrawable(Resources res, BitmapData data) {
            super(res, data.bitmap);

            width = data.width;
            height = data.height;
        }

        public AppBitmapDrawable(BitmapData data) {
            this(App.getContext().getResources(), data);
        }
    }
}
