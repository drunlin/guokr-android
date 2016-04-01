package com.github.drunlin.guokr.util;

import com.google.common.base.Objects;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author drunlin@outlook.com
 */
public class UrlUtil {
    /**
     * 验证URL的格式是否正确。
     * @param url
     * @return
     */
    public static boolean validateUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 添加GET方法的参数。
     * @param url
     * @param params
     * @param <T>
     * @return
     */
    public static <T> String addQuery(String url, Map<String, T> params) {
        StringBuilder builder = new StringBuilder(url);
        if (url.contains("?")) {
            for (Map.Entry<String, T> param : params.entrySet()) {
                builder.append('&').append(param.getKey()).append('=').append(param.getValue());
            }
        } else {
            builder.append('?');
            for (Map.Entry<String, T> param : params.entrySet()) {
                builder.append(param.getKey()).append('=').append(param.getValue()).append('&');
            }
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    /**
     * 添加GET方法的参数。
     * @param url
     * @param format
     * @param params
     * @return
     */
    public static String addQuery(String url, String format, Object... params) {
        format = String.format(url.contains("?") ? "%s&" : "%s?", url) + format;
        return String.format(format, params);
    }

    /**
     * 判断两个URL指向文件是否相同。
     * @param a
     * @param b
     * @return
     */
    public static boolean sameFile(URL a, URL b) {
        return Objects.equal(a.getProtocol(), b.getProtocol())
                && Objects.equal(a.getHost(), b.getHost())
                && a.getPort() == b.getPort()
                && Objects.equal(a.getRef(), b.getRef());
    }

//    /**
//     * 判断两个URL指向文件是否相同。
//     * @param a
//     * @param b
//     * @return
//     */
//    public static boolean sameFile(String a, String b) {
//        try {
//            return sameFile(new URL(a), new URL(b));
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    private static Map<String, String> getQuery(URL url) {
        String query = url.getQuery();
        if (query == null) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        for (String item : query.split("&")) {
            String[] entry = item.split("=");
            map.put(entry[0], entry[1]);
        }
        return map;
    }

    /**
     * 比较两个URL类型的URL是否相等。
     * @param a
     * @param b
     * @return
     */
    public static boolean equals(URL a, URL b) {
        return sameFile(a, b) && Objects.equal(getQuery(a), getQuery(b));
    }

    /**
     * 比较两个String类型的URL是否相等。
     * @param a
     * @param b
     * @return
     */
    public static boolean equals(String a, String b) {
        try {
            return equals(new URL(a), new URL(b));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断URL a是否包含的URL b的信息。
     * @param a
     * @param b
     * @return
     */
    public static boolean containers(URL a, URL b) {
        Map<String, String> queryA = getQuery(a);
        Map<String, String> queryB = getQuery(b);
        if (queryA != null && queryB != null) {
            for (Map.Entry<String, String> entry : queryB.entrySet()) {
                String v = queryA.get(entry.getKey());
                if (v == null || !v.equals(entry.getValue())) {
                    return false;
                }
            }
        } else if (queryA == null && queryB != null) {
            return false;
        }
        return sameFile(a, b);
    }

    /**
     * 判断URL a是否包含的URL b的信息。
     * @param a
     * @param b
     * @return
     */
    public static boolean containers(String a, String b) {
        try {
            return containers(new URL(a), new URL(b));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
