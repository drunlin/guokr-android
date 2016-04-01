package com.github.drunlin.guokr.util;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author drunlin@outlook.com
 */
public class UrlUtilTest {
    @Test
    public void addParam() {
        Map<String, Object> params = new HashMap<>();
        params.put("a", "a");
        params.put("b", "b");
        String url = UrlUtil.addQuery("www.xxx.com", params);
        assertTrue(url.matches("^www.xxx.com\\?\\w=\\w&\\w=\\w"));
        assertThat(UrlUtil.addQuery("www.xxx.com", params), allOf(
                containsString("a=a"),
                containsString("b=b")));

        url = UrlUtil.addQuery("www.xxx.com?c=c", params);
        assertTrue(url.matches("^www.xxx.com\\?c=c&\\w=\\w&\\w=\\w"));
        assertThat(UrlUtil.addQuery("www.xxx.com", params), allOf(
                containsString("a=a"),
                containsString("b=b")));
    }

    @Test
    public void addQuery() {
        assertEquals("http://example.com?k=v", UrlUtil.addQuery("http://example.com", "k=%s", "v"));
    }

    @Test
    public void equals() throws Exception {
        String url = "http://example.com";
        assertTrue(UrlUtil.equals(url, "http://example.com"));
        assertTrue(UrlUtil.equals(url, "http://example.com/"));

        url = "http://example.com/data.json?k=v&x=y";
        assertTrue(UrlUtil.equals(url, "http://example.com/data.json?x=y&k=v"));
        assertFalse(UrlUtil.equals(url, "http://exmple.com/data.json?k=v"));
    }
}
