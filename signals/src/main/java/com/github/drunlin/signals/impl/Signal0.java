package com.github.drunlin.signals.impl;

/**
 * @author drunlin@outlook.com
 */
public class Signal0 extends SafeSignal<Signal0.Listener> {
    @Override
    protected void execute(Listener listener, Object[] values) {
        listener.call();
    }

    public void dispatch() {
        //noinspection deprecation
        super.dispatch();
    }

    public interface Listener {
        void call();
    }
}
