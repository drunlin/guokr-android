package com.github.drunlin.guokr.model;

import android.support.annotation.WorkerThread;

import com.github.drunlin.guokr.bean.Icon;
import com.github.drunlin.guokr.util.JavaUtil.Consumer;
import com.github.drunlin.guokr.util.JavaUtil.Converter;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import static com.github.drunlin.guokr.util.JavaUtil.foreach;

/**
 * 加载图标资源的工具类。
 *
 * @author drunlin@outlook.com
 */
public class IconLoader {
    /**
     * 加载单个图标数据。
     * @param networkModel
     * @param icon
     * @param latch
     */
    private static void load(NetworkModel networkModel, Icon icon, CountDownLatch latch) {
        networkModel.loadImage(
                icon.getUrl(), data -> onLoaded(icon, data, latch), e -> latch.countDown());
    }

    /**
     * 图标数据加载完成。
     * @param icon
     * @param data
     * @param latch
     */
    private static void onLoaded(Icon icon, byte[] data, CountDownLatch latch) {
        icon.data = data;
        latch.countDown();
    }

    /**
     * 加载指定数量的数据。
     * @param count
     * @param loader
     */
    private static void load(int count, Consumer<CountDownLatch> loader) {
        CountDownLatch latch = new CountDownLatch(count);

        loader.call(latch);

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 阻塞式地加载单个图标。
     * @param networkModel
     * @param icon
     */
    @WorkerThread
    public static void load(NetworkModel networkModel, Icon icon) {
        load(1, latch -> load(networkModel, icon, latch));
    }

    /**
     * 阻塞式地加载集合中的图标。
     * @param networkModel
     * @param list
     * @param converter
     * @param <T>
     */
    @WorkerThread
    public static <T> void batchLoad(NetworkModel networkModel,
                                     List<T> list, Converter<T, Icon> converter) {
        load(list.size(), latch ->
                foreach(list, item -> load(networkModel, converter.convert(item), latch)));
    }
}
