package com.github.drunlin.signals;

/**
 * 用来发送事件到注册到它的侦听器。
 * @param <T> 侦听器
 *
 * @author drunlin@outlook.com
 */
public interface Signal<T> {

    /**
     * 注册一次性的侦听器，侦听器会在第一次调用后就自动被移除。
     * @param value
     * @return
     */
    Slot addOnce(T value);

    /**
     * 注册侦听器。
     * @param value
     * @return
     */
    Slot add(T value);

    /**
     * 派发数据到所有的侦听器。
     * @param values
     */
    void dispatch(Object... values);

    /**
     * 移除注册的侦听器。
     * @param value
     * @return
     */
    Slot remove(T value);

    /**
     * 移除所有已经注册的侦听器。
     */
    void removeAll();

    /**
     * 获取侦听器的数量。
     * @return
     */
    int listenersCount();
}
