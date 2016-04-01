package com.github.drunlin.signals.impl;

import com.github.drunlin.signals.AbstractSignal;
import com.github.drunlin.signals.AbstractSlot;
import com.github.drunlin.signals.Signal;
import com.github.drunlin.signals.Slot;

/**
 * 运行时进行类型检查。
 *
 * @author drunlin@outlook.com
 */
public class SimpleSignal extends AbstractSignal<SimpleSignal.Listener> {
    private final Object[] valueClasses;

    /**
     * 参数的类型列表。
     * @param classes
     */
    public SimpleSignal(Object... classes) {
        valueClasses = classes;
    }

    @Override
    protected Slot<Listener> onCreateSlot(Signal<Listener> signal,
                                          final Listener listener, boolean once) {
        return new AbstractSlot<Listener>(signal, listener, once) {
            @Override
            protected void exec(Object[] values) {
                listener.call(values);
            }
        };
    }

    /**
     * 发送事件前会先检查参数的类型与类型列表是否匹配。
     * @param values
     * @throws IllegalArgumentException 参数与类型不匹配
     */
    @Override
    public void dispatch(Object... values) throws IllegalArgumentException {
        if (values.length != valueClasses.length) {
            throw new IllegalArgumentException("Incorrect number of arguments.");
        }
        for (int i = 0; i < values.length; ++i) {
            if (values[i] != null && values[i].getClass() != valueClasses[i]) {
                throw new IllegalArgumentException();
            }
        }
        super.dispatch(values);
    }

    public interface Listener {
        void call(Object[] values);
    }
}
