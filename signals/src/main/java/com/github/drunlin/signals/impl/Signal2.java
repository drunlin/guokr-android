package com.github.drunlin.signals.impl;

/**
 * @author drunlin@outlook.com
 */
public class Signal2<A, B> extends SafeSignal<Signal2.Listener<A, B>> {
    @Override
    protected void execute(Listener<A, B> listener, Object[] values) {
        //noinspection unchecked
        listener.call((A) values[0], (B) values[1]);
    }

    public void dispatch(A a, B b) {
        //noinspection deprecation
        super.dispatch(a, b);
    }

    public interface Listener<A, B> {
        void call(A a, B b);
    }
}
