package com.github.drunlin.signals;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author drunlin@outlook.com
 */
public class AbstractSignalTest {
    private SignalImpl signal;

    @Before
    public void init() {
        signal = new SignalImpl();
    }

    @Test
    public void removeAll() throws Exception {
        for (int i = 0; i < 5; ++i) {
            signal.add(mock(Listener.class));
        }
        signal.removeAll();
        assertThat(signal.listenersCount(), is(0));
    }

    @Test
    public void listenersCount() throws Exception {
        assertThat(signal.listenersCount(), is(0));
        for (int i = 0; i < 10; ++i) {
            signal.add(mock(Listener.class));
        }
        assertThat(signal.listenersCount(), is(10));
    }

    @Test
    public void addOnce() throws Exception {
        Listener listener = mock(Listener.class);
        signal.addOnce(listener);
        signal.dispatch();
        signal.dispatch();
        verify(listener).call();
        assertThat(signal.listenersCount(), is(0));
    }

    @Test
    public void add() throws Exception {
        Listener[] listeners = new Listener[5];
        for (int i = 0; i < listeners.length; ++i) {
            listeners[i] = mock(Listener.class);
            signal.add(listeners[i]);
        }
        signal.dispatch();
        for (Listener listener : listeners) {
            verify(listener).call();
        }
        assertThat(signal.listenersCount(), is(5));
    }

    @Test
    public void dispatch() throws Exception {
        for (int i = 0; i < 3; ++i) {
            Object[] args = new Object[i];
            for (int j = 0; j < i; ++j) {
                args[j] = new Object();
            }
            Listener listener = mock(Listener.class);
            signal.add(listener);
            signal.dispatch(args);
            verify(listener).call(args);
        }
    }

    @Test
    public void remove() throws Exception {
        Listener listener = mock(Listener.class);
        signal.add(listener);
        signal.remove(listener);
        assertThat(signal.listenersCount(), is(0));
    }
    private class SignalImpl extends AbstractSignal<Listener> {
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
    }

    interface Listener {
        void call(Object... values);
    }
}
