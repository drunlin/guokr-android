package com.github.drunlin.signals;

/**
 * {@link Slot}的抽象类。
 *
 * @author drunlin@outlook.com
 */
public abstract class AbstractSlot<T> implements Slot<T> {
    private boolean once;
    private boolean enable = true;
    private T listener;
    private Signal<T> signal;

    protected AbstractSlot(Signal<T> signal, T listener, boolean once) {
        this.signal = signal;
        this.listener = listener;
        this.once = once;
    }

    @Override
    public T listener() {
        return listener;
    }

    @Override
    public final void setEnable(boolean value) {
        enable = value;
    }

    @Override
    public final boolean isEnable() {
        return enable;
    }

    @Override
    public final boolean isOnce() {
        return once;
    }

    @Override
    public void execute(Object[] values) {
        if (!enable) {
            return;
        }
        if (once) {
            remove();
        }
        exec(values);
    }

    /**
     * 执行侦听器的回调函数。
     * @param values
     */
    protected abstract void exec(Object[] values);

    @Override
    public void remove() {
        signal.remove(listener);
    }
}
