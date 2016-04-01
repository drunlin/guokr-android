package com.github.drunlin.signals.impl;

/**
 * @author drunlin@outlook.com
 */
public class Signal3<A, B, C> extends SafeSignal<Signal3.Listener<A, B, C>> {
    @Override
    protected void execute(Listener<A, B, C> listener, Object[] values) {
        //noinspection unchecked
        listener.call((A) values[0], (B) values[1], (C) values[2]);
    }

    public void dispatch(A a, B b, C c) {
        //noinspection deprecation
        super.dispatch(a, b, c);
    }

    public interface Listener<A, B, C> {
        void call(A a, B b, C c);
    }
}
