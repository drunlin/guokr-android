package com.github.drunlin.guokr.test;

import com.github.drunlin.guokr.bean.ResponseCode;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

/**
 * @author drunlin@outlook.com
 */
public class ModelAsserts {
    public static void assertOkResult(int resultCode) {
        assertEquals(ResponseCode.OK, resultCode);
    }

    public static  <T> void assertRefresh(int resultCode, boolean refresh, List<T> list) {
        assertOkResult(resultCode);
        assertTrue(refresh);
        assertThat(list, allOf(notNullValue(), hasSize(greaterThan(0))));
    }

    public static  <T> void assertLoadMore(int resultCode, boolean refresh, List<T> list) {
        assertOkResult(resultCode);
        assertFalse(refresh);
        assertThat(list, allOf(notNullValue(), hasSize(greaterThan(0))));
    }
}
