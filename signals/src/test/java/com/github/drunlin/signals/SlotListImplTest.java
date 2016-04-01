package com.github.drunlin.signals;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author drunlin@outlook.com
 */
public class SlotListImplTest {
    private SlotListImpl<Runnable> slotList;
    private Runnable[] listeners;

    @Before
    public void init() {
        slotList = new SlotListImpl<>();
        listeners = new Runnable[10];
        for (int i = 0; i < 10; ++i) {
            Slot slot = mock(Slot.class);
            Runnable listener = mock(Runnable.class);
            listeners[i] = listener;
            when(slot.listener()).thenReturn(listener);
            slotList.add(slot);
        }
    }

    @Test
    public void find() throws Exception {
        assertEquals(slotList.find(listeners[3]).listener(), listeners[3]);
        assertThat(slotList.find(null), nullValue());
        assertThat(slotList.find(mock(Runnable.class)), nullValue());
    }

    @Test
    public void iterator() throws Exception {
        int count = 0;
        for (Slot slot : slotList) {
            slotList.remove(slot);
            ++count;
        }
        assertThat(count, is(10));
    }
}
