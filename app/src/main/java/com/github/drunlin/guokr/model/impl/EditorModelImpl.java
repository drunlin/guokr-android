package com.github.drunlin.guokr.model.impl;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;

import com.github.drunlin.guokr.App;
import com.github.drunlin.guokr.bean.ResultClassMap.ImageResult;
import com.github.drunlin.guokr.bean.Thumbnail;
import com.github.drunlin.guokr.model.EditorModel;
import com.github.drunlin.guokr.model.Model;
import com.github.drunlin.guokr.model.NetworkModel;
import com.github.drunlin.guokr.model.PreferenceModel;
import com.github.drunlin.guokr.model.UserModel;
import com.github.drunlin.guokr.model.request.MultipartRequest;
import com.github.drunlin.guokr.module.tool.Injector;
import com.github.drunlin.guokr.util.BitmapUtils;
import com.github.drunlin.signals.impl.Signal0;
import com.github.drunlin.signals.impl.Signal1;
import com.google.common.io.ByteStreams;
import com.jakewharton.disklrucache.DiskLruCache;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.inject.Inject;

import static android.graphics.Bitmap.CompressFormat.JPEG;
import static com.github.drunlin.guokr.bean.Thumbnail.FAILED;
import static com.github.drunlin.guokr.util.JavaUtil.call;
import static com.github.drunlin.guokr.util.JavaUtil.foreach;
import static com.github.drunlin.guokr.util.JavaUtil.index;

/**
 * 处理编辑器的数据，主要是处理图片数据。
 *
 * @author drunlin@outlook.com
 */
public class EditorModelImpl extends Model implements EditorModel {
    private final Signal0 outOfMemoryError = new Signal0();
    private final Signal0 imageAppended = new Signal0();
    private final Signal1<Integer> imageChanged = new Signal1<>();
    private final Signal1<Integer> thumbnailRemoved = new Signal1<>();

    private final Object CACHE_LOCK = new Object();

    @Inject UserModel userModel;
    @Inject NetworkModel networkModel;
    @Inject PreferenceModel preferenceModel;

    /**正在上传的图片，用来实现重试操作。*/
    private final Map<Thumbnail, byte[]> uploadingImages = new WeakHashMap<>();

    private final List<Thumbnail> thumbnails = new ArrayList<>();

    private DiskLruCache diskLruCache;

    public EditorModelImpl(Injector injector) {
        super(injector);
    }

    /**
     * 防止内存溢出的调用。
     * @param runnable
     */
    private void memoryCheckCall(Runnable runnable, long size) {
        if (App.getContext().freeMemory() < size * 1.5) {
            outOfMemoryError.dispatch();
            return;
        }

        try {
            runnable.run();
        } catch (OutOfMemoryError e) {
            System.gc();

            outOfMemoryError.dispatch();
        }
    }

    @Override
    public Signal0 outOfMemoryError() {
        return outOfMemoryError;
    }

    @Override
    public void uploadImage(InputStream stream) {
        int size = 0;
        try {
            size = stream.available();
        } catch (IOException e) {
            e.printStackTrace();
        }
        memoryCheckCall(() -> {
            try {
                call(ByteStreams.toByteArray(stream), this::uploadPicture);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, size);
    }

    @Override
    public void uploadImage(byte[] data) {
        memoryCheckCall(() -> uploadPicture(data), data.length);
    }

    /**
     * 所有上传图片方式的最终调用的函数。
     * @param data
     */
    private void uploadPicture(byte[] data) {
        if (!userModel.checkLoggedIn()) {
            return;
        }

        Thumbnail thumbnail = new Thumbnail();
        thumbnail.bitmap = createThumbnail(data);
        addThumbnail(thumbnail);

        uploadingImages.put(thumbnail, data);

        final String url = "http://www.guokr.com/apis/image.json?enable_watermark=%b";
        new MultipartRequest.Builder<>(url, ImageResult.class)
                .setUrlArgs(preferenceModel.isWatermarkEnable())
                .addFormDataPart("access_token", userModel.getToken())
                .addFormDataPart("upload_file", String.valueOf(Arrays.hashCode(data)),
                        RequestBody.create(MediaType.parse("image/*"), data))
                .setParseListener(response -> cacheImage(response.result.url, data))
                .setListener(response -> onUploadImageSucceed(thumbnail, response.result.url))
                .setErrorListener(error -> onLoadImageFailed(thumbnail))
                .setTag(thumbnail)
                .build(networkModel);
    }

    /**
     * 根据当前图片数据生成小的预览图。
     * @param data
     * @return
     */
    private Bitmap createThumbnail(byte[] data) {
        return BitmapUtils.createBitmap(data, 42, 42);
    }

    /**
     * 把预览图添加到列表。
     * @param thumbnail
     */
    private void addThumbnail(Thumbnail thumbnail) {
        thumbnails.add(thumbnail);

        imageAppended.dispatch();
    }

    @Override
    public Signal1<Integer> thumbnailRemoved() {
        return thumbnailRemoved;
    }

    /**
     * 上传图片成功。
     * @param thumbnail
     * @param url
     */
    private void onUploadImageSucceed(Thumbnail thumbnail, String url) {
        thumbnail.url = url;

        uploadingImages.remove(thumbnail);

        onLoadImageSuccess(thumbnail);
    }

    /**
     * 上传或下载图片成功。
     * @param thumbnail
     */
    private void onLoadImageSuccess(Thumbnail thumbnail) {
        thumbnail.state = Thumbnail.SUCCESS;

        onImageResult(thumbnail);
    }

    /**
     * 响应服务器返回的结果。
     * @param thumbnail
     */
    private void onImageResult(Thumbnail thumbnail) {
        index(thumbnails, item -> item == thumbnail, index -> imageChanged.dispatch((int) index));
    }

    /**
     * 上传或下载图片失败。
     * @param thumbnail
     */
    private void onLoadImageFailed(Thumbnail thumbnail) {
        thumbnail.state = FAILED;

        onImageResult(thumbnail);
    }

    /**
     * 缓存图片到磁盘，防止图片太多而内存溢出。这是一个耗时地操作，必须要在工作线程中运行。
     * @param url
     * @param data
     * @throws IOException
     */
    @WorkerThread
    private void cacheImage(String url, byte[] data) {
        try {
            initDiskCache();

            String key = getKey(url);
            DiskLruCache.Editor editor = diskLruCache.edit(key);
            OutputStream outputStream = editor.newOutputStream(0);
            Bitmap scaledBitmap = BitmapUtils.createBitmap(data, 256, 256);
            scaledBitmap.compress(JPEG, 90, outputStream);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化磁盘缓存。
     * @throws IOException
     */
    private void initDiskCache() throws IOException {
        synchronized (CACHE_LOCK) {
            if (diskLruCache == null) {
                String cachePath = App.getContext().getCacheDir().getPath();
                File path = new File(cachePath + File.separator + "editor");
                diskLruCache = DiskLruCache.open(path, 0, 1, 20 * 1024 * 1024);
            }
        }
    }

    /**
     * 把URL的MD5作为key。
     * @param url
     * @return
     */
    private String getKey(String url) {
        byte[] bytes = new byte[0];
        try {
            bytes = MessageDigest.getInstance("MD5").digest(url.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return byteArrayToHex(bytes);
    }

    private String byteArrayToHex(byte[] byteArray) {
        char[] hexDigits =
                {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] resultCharArray = new char[byteArray.length * 2];
        int index = 0;

        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }

        return new String(resultCharArray);
    }

    @Override
    public void downloadImage(String url) {
        Thumbnail thumbnail = new Thumbnail();
        thumbnail.url = url;

        addThumbnail(thumbnail);

        networkModel.loadImage(url,
                r -> onDownloadImageSucceed(thumbnail, r),
                e -> onLoadImageFailed(thumbnail), thumbnail);
    }

    private void onDownloadImageSucceed(Thumbnail thumbnail, byte[] data) {
        thumbnail.bitmap = createThumbnail(data);
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    cacheImage(thumbnail.url, data);
                } catch (Exception e) {
                    cancel(false);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                onLoadImageSuccess(thumbnail);
            }

            @Override
            protected void onCancelled() {
                onLoadImageFailed(thumbnail);
            }
        }.execute();
    }

    @Override
    public void reloadImage(Thumbnail thumbnail) {
        if (thumbnail.state != FAILED) {
            return;
        }

        deleteImage(thumbnail);

        if (!TextUtils.isEmpty(thumbnail.url)) {
            downloadImage(thumbnail.url);
        } else if (uploadingImages.containsKey(thumbnail)){
            uploadImage(uploadingImages.get(thumbnail));
        }
    }

    @Override
    public Signal0 thumbnailAppended() {
        return imageAppended;
    }

    @Override
    public Signal1<Integer> thumbnailsChanged() {
        return imageChanged;
    }

    public List<Thumbnail> getThumbnails() {
        return thumbnails;
    }

    @Override
    public void deleteImage(Thumbnail thumbnail) {
        networkModel.cancel(thumbnail);
        uploadingImages.remove(thumbnail);

        index(thumbnails, item -> item == thumbnail, index -> {
            thumbnails.remove((int) index);

            thumbnailRemoved.dispatch(index);
        });
    }

    @Override
    public InputStream getImage(String url) {
        synchronized (CACHE_LOCK) {
            if (diskLruCache == null) {
                return null;
            }
        }

        String key = getKey(url);
        try {
            DiskLruCache.Snapshot snapshot = diskLruCache.get(key);
            return snapshot != null ? snapshot.getInputStream(0) : null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void purge() {
        foreach(thumbnails, item -> networkModel.cancel(item));

        thumbnails.clear();
        uploadingImages.clear();

        synchronized (CACHE_LOCK) {
            if (diskLruCache != null) {
                try {
                    diskLruCache.delete();
                    diskLruCache.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
