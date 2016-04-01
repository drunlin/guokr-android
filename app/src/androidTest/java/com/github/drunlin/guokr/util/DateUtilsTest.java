package com.github.drunlin.guokr.util;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static junit.framework.Assert.assertEquals;

/**
 * @author drunlin@outlook.com
 */
public class DateUtilsTest {
    @Test
    public void format() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2016, 0, 1, 1, 1);
        Date date = calendar.getTime();

        assertEquals("今天 01:01", DateUtils.format(date, calendar));

        calendar.set(2016, 0, 2);
        assertEquals("昨天 01:01", DateUtils.format(date, calendar));

        calendar.set(2016, 0, 3);
        assertEquals("2016-01-01 01:01", DateUtils.format(date, calendar));
    }
}
