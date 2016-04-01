package com.github.drunlin.signals;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * {@link SlotList}的实现类。
 *
 * @author drunlin@outlook.com
 */
public class SlotListImpl<T>  implements SlotList<T> {
    private LinkedList<Slot> list = new LinkedList<>();

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean add(Slot slot) {
        return list.add(slot);
    }

    @Override
    public void remove(Slot slot) {
        list.remove(slot);
    }

    @Override
    public Slot find(T listener) {
        for (Slot slot : list) {
            if (slot.listener() == listener) {
                return slot;
            }
        }
        return null;
    }

    @Override
    public Iterator<Slot> iterator() {
        //防止迭代器失效
        return new LinkedList<>(list).iterator();
    }

    @Override
    public void clear() {
        list.clear();
    }
}
