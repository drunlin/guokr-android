package com.github.drunlin.signals.impl;

/**
 * @author drunlin@outlook.com
 */
public class Signal1<A> extends SafeSignal<Signal1.Listener<A>> {
    @Override
    protected void execute(Listener<A> listener, Object[] values) {
        //noinspection unchecked
        listener.call((A) values[0]);
    }

    public void dispatch(A a) {
        //noinspection deprecation
        super.dispatch(a);
    }

    public interface Listener<A> {
        void call(A a);
    }
}
