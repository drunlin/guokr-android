package com.github.drunlin.signals;

/**
 * 管理Slot的列表。
 * @param <T> 侦听器
 *
 * @author drunlin@outlook.com
 */
public interface SlotList<T> extends Iterable<Slot> {
    /**
     * Slot的数量。
     * @return
     */
    int size();

    /**
     * 查找Listener对应的Slot。
     * @param listener
     * @return
     */
    Slot find(T listener);

    /**
     * 添加Slot。
     * @param slot
     * @return
     */
    boolean add(Slot slot);

    /**
     * 移除已经添加的Slot。
     * @param slot
     */
    void remove(Slot slot);

    /**
     * 清除所有。
     */
    void clear();
}
