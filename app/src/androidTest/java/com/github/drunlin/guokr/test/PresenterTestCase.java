package com.github.drunlin.guokr.test;

import com.github.drunlin.guokr.TestContext;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;

/**
 * @author drunlin@outlook.com
 */
public abstract class PresenterTestCase<V, P> extends TestCase {
    private final Class<? extends V> viewClass;

    protected V view;
    protected P presenter;

    public PresenterTestCase(Class<? extends V> clazz) {
        viewClass = clazz;
    }

    @Override
    public void setUp() throws Throwable {
        view = mock(viewClass);

        TestContext.inject(this);

        super.setUp();
    }

    protected void resetMocks() {
        //noinspection unchecked
        reset(view);
        TestContext.resetMocks(this);
    }

    @Override
    public void tearDown() throws Throwable {
        resetMocks();

        super.tearDown();
    }
}
