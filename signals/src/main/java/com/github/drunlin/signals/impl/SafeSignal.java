package com.github.drunlin.signals.impl;

import com.github.drunlin.signals.AbstractSignal;
import com.github.drunlin.signals.AbstractSlot;
import com.github.drunlin.signals.Signal;
import com.github.drunlin.signals.Slot;

/**
 * 用泛型来支持类型安全。
 *
 * @author drunlin@outlook.com
 */
public abstract class SafeSignal<T> extends AbstractSignal<T> {
    @Override
    protected Slot<T> onCreateSlot(Signal<T> signal, final T listener, boolean once) {
        return new AbstractSlot<T>(signal, listener, once) {
            @Override
            protected void exec(Object[] values) {
                SafeSignal.this.execute(listener, values);
            }
        };
    }

    protected abstract void execute(T listener, Object[] values);

    /**
     * 应该调用子类同名的方法。
     * @param values
     */
    @Deprecated
    @Override
    public void dispatch(Object... values) {
        super.dispatch(values);
    }
}
