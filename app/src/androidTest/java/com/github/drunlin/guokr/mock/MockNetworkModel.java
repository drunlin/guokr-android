package com.github.drunlin.guokr.mock;

import android.util.Log;

import com.android.volley.Request;
import com.github.drunlin.guokr.model.impl.NetworkModelImpl;
import com.github.drunlin.guokr.util.UrlUtil;
import com.google.common.base.Objects;
import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 用来测试的虚拟服务器。
 *
 * @author drunlin@outlook.com
 */
public class MockNetworkModel extends NetworkModelImpl {
    private static final String TAG = MockNetworkModel.class.getSimpleName();

    private static MockWebServer server;
    private static Map<String, String> urlMap = new HashMap<>();
    private static Map<Matcher, MockResponse> responseMap = new HashMap<>();

    private static MockNetworkModel instance;

    public static MockNetworkModel getInstance() {
        if (instance == null) {
            instance = new MockNetworkModel();
        }
        return instance;
    }

    private MockNetworkModel() {
        server = new MockWebServer();
        server.setDispatcher(new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest recordedRequest)
                    throws InterruptedException {
                try {
                    return MockNetworkModel.this.dispatch(recordedRequest);
                } catch (Exception e) {
                    return new MockResponse().setResponseCode(404);
                }
            }
        });
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理请求的结果。
     * @param recordedRequest
     * @return
     */
    private MockResponse dispatch(RecordedRequest recordedRequest) {
        String path = recordedRequest.getPath();
        String url = urlMap.get(path);
        for (Entry<Matcher, MockResponse> entry : responseMap.entrySet()) {
            Matcher m = entry.getKey();
            if (m.methodMatcher.matching(recordedRequest) && m.urlMatcher.matching(url)) {
                return entry.getValue();
            }
        }
        return new MockResponse().setResponseCode(404);
    }

    /**
     * 把请求的URL转化为虚拟服务器可以的URL。
     * @param request
     * @return
     */
    @Override
    public <T> Request<T> add(Request<T> request) {
        String url = request.getUrl();
        Log.d(TAG, url);
        String path = "/" + request.getUrl().hashCode();
        urlMap.put(path, url);
        request.setRedirectUrl(server.url(path).toString());
        return super.add(request);
    }

    /**
     * 注册处理请求的匹配器。
     * @param methodMatcher
     * @param urlMatcher
     * @return
     */
    public static MockResponse stubFor(MethodMatcher methodMatcher, UrlMatcher urlMatcher) {
        Matcher matcher = new Matcher();
        matcher.methodMatcher = methodMatcher;
        matcher.urlMatcher = urlMatcher;
        MockResponse response = new MockResponse();
        response.setResponseCode(200);
        responseMap.put(matcher, response);
        return response;
    }

    /**
     * 注册处理GET请求的匹配器。
     * @param matcher
     * @return
     */
    public static MockResponse stubFor(UrlMatcher matcher) {
        return stubFor(GET, matcher);
    }

    /**
     * 清理所有注册的匹配器。
     */
    public static void clear() {
        responseMap.clear();
    }


    //======================================下面所有都为匹配器=======================================

    /**
     * 匹配URL完全相等。
     * @param format
     * @param args
     * @return
     */
    public static UrlMatcher urlEqualTo(String format, Object... args) {
        return urlEqualTo(String.format(format, args));
    }

    /**
     * 匹配URL完全相等。
     * @param url
     * @return
     */
    public static UrlMatcher urlEqualTo(String url) {
        return s -> UrlUtil.equals(s, url);
    }

//    public static UrlMatcher urlMatching(String url) {
//        return s -> s.matches(url);
//    }
//
//    /**
//     * 用正则表达式进行匹配。
//     * @param format
//     * @param args
//     * @return
//     */
//    public static UrlMatcher urlMatching(String format, Object... args) {
//        return urlMatching(String.format(format, args));
//    }

    /**
     * 匹配是否包含。
     * @param url
     * @return
     */
    public static UrlMatcher urlContainers(String url) {
        return s -> UrlUtil.containers(s, url);
    }

    /**
     * 匹配是否包含。
     * @param format
     * @param args
     * @return
     */
    public static UrlMatcher urlContainers(String format, Object... args) {
        return urlContainers(String.format(format, args));
    }

    /**匹配GET方法。*/
    public static final MethodMatcher GET = request -> request.getMethod().equals("GET");

    /**匹配DELETE方法。*/
    public static final MethodMatcher DELETE = request -> request.getMethod().equals("DELETE");

    /**
     * 匹配POST方法。
     * @param format 基本格式为 k=v&k=v ，其中k, v可以用String.format的格式替换
     * @param args String.format的参数
     * @return
     */
    public static MethodMatcher post(String format, Object... args) {
        return request -> request.getMethod().equals("POST") && matchingBody(request, format, args);
    }

    /**
     * 匹配PUT方法。
     * @param format
     * @param args
     * @return
     */
    public static MethodMatcher put(String format, Object... args) {
        return request -> request.getMethod().equals("PUT") && matchingBody(request, format, args);
    }

    private static boolean matchingBody(RecordedRequest request, String format, Object... args) {
        Map<String, String> params = new HashMap<>();
        for (String s : new String(request.getBody().readByteArray()).split("&")) {
            String[] param = s.split("=");
            params.put(param[0], param[1]);
        }
        for (String s : String.format(format, args).split("&")) {
            String[] param = s.split("=");
            //noinspection deprecation
            if (!Objects.equal(params.get(param[0]), URLEncoder.encode(param[1]))) {
                return false;
            }
        }
        return true;
    }

    public interface UrlMatcher {
        boolean matching(String url);
    }

    public interface MethodMatcher {
        boolean matching(RecordedRequest request);
    }

    private static class Matcher {
        public MethodMatcher methodMatcher;
        public UrlMatcher urlMatcher;
    }
}
