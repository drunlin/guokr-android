package com.github.drunlin.guokr.test;

import android.support.annotation.CallSuper;

import org.junit.After;
import org.junit.Before;

/**
 * 测试用例的基类。
 */
public abstract class TestCase {
    @Before
    @CallSuper
    public void setUp() throws Throwable {
        init();
    }

    protected void init() throws Throwable {}

    @After
    @CallSuper
    public void tearDown() throws Throwable {
        destroy();
    }

    protected void destroy() throws Throwable {}
}
