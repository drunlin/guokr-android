package com.github.drunlin.signals;

/**
 * @author drunlin@outlook.com
 */
public interface Slot<T> {
    /**
     * 获取与自己关联的侦听器。
     * @return
     */
    T listener();

    /**
     * 设置是否启用，默认为true。
     * @param value
     */
    void setEnable(boolean value);

    /**
     * 是否启用。
     * @return
     */
    boolean isEnable();

    /**
     * 是否为一次性的。
     * @return
     */
    boolean isOnce();

    /**
     * 通过Signal发送事件。
     * @param values
     */
    void execute(Object[] values);

    /**
     * 从关联的signal中移除自己。
     */
    void remove();
}
