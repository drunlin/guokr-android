package com.github.drunlin.signals;

/**
 * {@link Signal}的抽象类。
 *
 * @author drunlin@outlook.com
 */
public abstract class AbstractSignal<T> implements Signal<T> {
    protected final SlotList<T> slotList = onCreateSlotList();

    protected SlotList<T> onCreateSlotList() {
        return new SlotListImpl<>();
    }

    protected abstract Slot<T> onCreateSlot(Signal<T> signal, T listener, boolean once);

    /**
     * 注册侦听器。
     * @param listener
     * @param once
     * @return
     */
    protected Slot registerListener(T listener, boolean once) {
        Slot slot = slotList.find(listener);
        if (slot == null) {
            slotList.add(slot = onCreateSlot(this, listener, once));
        }
        return slot;
    }

    @Override
    public Slot addOnce(T value) {
        return registerListener(value, true);
    }

    @Override
    public Slot add(T value) {
        return registerListener(value, false);
    }

    @Override
    public void dispatch(Object... values) {
        for (Slot slot : slotList) {
            slot.execute(values);
        }
    }

    @Override
    public Slot remove(T value) {
        Slot slot = slotList.find(value);
        if (slot != null) {
            slotList.remove(slot);
            return slot;
        }
        return null;
    }

    @Override
    public void removeAll() {
        slotList.clear();
    }

    @Override
    public int listenersCount() {
        return slotList.size();
    }
}
