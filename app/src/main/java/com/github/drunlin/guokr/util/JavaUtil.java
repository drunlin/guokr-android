package com.github.drunlin.guokr.util;

import android.support.annotation.NonNull;

/**
 * 自造的语法糖。
 *
 * @author drunlin@outlook.com
 */
public class JavaUtil {
    /**
     * 无返回值有一个参数的函数。
     * @param <T>
     */
    public interface Consumer<T> {
        void call(T v);
    }

    /**
     * 有返回值无参数的函数。
     * @param <T>
     */
    public interface Supplier<T> {
        T call();
    }

    /**
     * 有返回值和一个参数的转换器。
     * @param <F>
     * @param <T>
     */
    public interface Converter<F, T> {
        T convert(F from);
    }

    /**
     * 返回布尔值带一个参数的函数。
     * @param <T>
     */
    public interface Predicate<T> {
        boolean call(T v);
    }

    /**
     * 防止空对象的函数调用。
     * @param target
     * @param consumer
     * @param <T>
     */
    public static <T> void call(T target, @NonNull Consumer<T> consumer) {
        if (target != null) {
            consumer.call(target);
        }
    }

    /**
     * 防止空对象的函数调用。
     * @param target
     * @param runnable
     * @param <T>
     */
    public static <T> void call(T target, @NonNull Runnable runnable) {
        if (target != null) {
            runnable.run();
        }
    }

    /**
     * 找出符合条件的第一个索引。
     * @param collection
     * @param filter
     * @param <E>
     * @return 没有找到返回-1。
     */
    public static <E> int indexOf(Iterable<E> collection, @NonNull Predicate<E> filter) {
        if (collection == null) {
            return -1;
        }

        int index = 0;
        for (E e : collection) {
            if (filter.call(e)) {
                return index;
            }
            index += 1;
        }
        return -1;
    }

    /**
     * 找出符合条件的第一个索引。如果找到则调用<code>consumer.call</code>。
     * @param collection
     * @param filter
     * @param consumer
     * @param <E>
     */
    public static <E> void index(Iterable<E> collection,
                                 @NonNull Predicate<E> filter,
                                 @NonNull Consumer<Integer> consumer) {
        if (collection == null) {
            return;
        }

        int index = 0;
        for (E item : collection) {
            if (filter.call(item)) {
                consumer.call(index);
                break;
            }
            index += 1;
        }
    }

    /**
     * 寻找第一个符合条件的元素。
     * @param collection
     * @param filter
     * @param <E>
     * @return
     */
    public static <E> E find(Iterable<E> collection, @NonNull Predicate<E> filter) {
        if (collection == null) {
            return null;
        }

        for (E e : collection) {
            if (filter.call(e)) {
                return e;
            }
        }
        return null;
    }

    /**
     * 寻找第一个符合条件的元素。
     * @param collection
     * @param filter
     * @param consumer
     * @param <E>
     */
    public static <E> void find(Iterable<E> collection,
                                   @NonNull Predicate<E> filter, @NonNull Consumer<E> consumer) {
        if (collection == null) {
            return;
        }

        //noinspection all
        for (E item : collection) {
            if (filter.call(item)) {
                consumer.call(item);
                break;
            }
        }
    }

    /**
     * 遍历所有符合条件的元素。
     * @param collection
     * @param filter
     * @param consumer
     * @param <E>
     */
    public static <E> void foreach(Iterable<E> collection,
                                   @NonNull Predicate<E> filter, @NonNull Consumer<E> consumer) {
        if (collection == null) {
            return;
        }

        //noinspection all
        for (E item : collection) {
            if (filter.call(item)) {
                consumer.call(item);
            }
        }
    }

    /**
     * 遍历整个集合。
     * @param collection
     * @param consumer
     * @param <E>
     */
    public static <E> void foreach(Iterable<E> collection, @NonNull Consumer<E> consumer) {
        if (collection == null) {
            return;
        }

        //noinspection all
        for (E item : collection) {
            consumer.call(item);
        }
    }
}
