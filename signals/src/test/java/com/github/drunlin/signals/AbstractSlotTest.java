package com.github.drunlin.signals;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * @author drunlin@outlook.com
 */
public class AbstractSlotTest {
    private AbstractSlot<Listener> slot;
    private Listener listener;
    private Signal<Listener> signal;

    @Before
    public void init() {
        //noinspection unchecked
        signal = mock(Signal.class);
        listener = mock(Listener.class);
        slot = new SlotImpl(signal, listener, true);
    }

    @Test
    public void listener() throws Exception {
        assertThat(slot.listener(), is(listener));
    }

    @Test
    public void enable() throws Exception {
        slot.setEnable(false);
        assertFalse(slot.isEnable());
        slot.execute(new Object[0]);
        verify(listener, never()).call(any());

        slot.setEnable(true);
        assertTrue(slot.isEnable());
        slot.execute(new Object[0]);
        verify(listener).call();
    }

    @Test
    public void isOnce() throws Exception {
        assertTrue(slot.isOnce());
        slot.execute(new Object[0]);
        verify(signal).remove(listener);
    }

    @Test
    public void execute() throws Exception {
        final Object[] args = new Object[]{1, 2};
        slot.execute(args);
        verify(listener).call(args);
    }

    @Test
    public void remove() throws Exception {
        slot.remove();
        verify(signal).remove(listener);
    }

    class SlotImpl extends AbstractSlot<Listener> {
        protected SlotImpl(Signal<Listener> signal, Listener listener, boolean once) {
            super(signal, listener, once);
        }

        @Override
        protected void exec(Object[] values) {
            listener.call(values);
        }
    }

    interface Listener {
        void call(Object... values);
    }
}
