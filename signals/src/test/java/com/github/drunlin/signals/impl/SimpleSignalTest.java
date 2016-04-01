package com.github.drunlin.signals.impl;

import org.junit.Test;

/**
 * @author drunlin@outlook.com
 */
public class SimpleSignalTest {
    @Test(expected = IllegalArgumentException.class)
    public void dispatch_error() throws Exception {
        SimpleSignal signal = new SimpleSignal(int.class, char.class);
        signal.dispatch(1, 2);
    }
}
