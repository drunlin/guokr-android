package com.github.drunlin.guokr.model.request;

import com.android.volley.Request;
import com.android.volley.Response.Listener;
import com.github.drunlin.guokr.bean.SimpleResult;
import com.github.drunlin.guokr.model.NetworkModel;
import com.github.drunlin.guokr.util.UrlUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 自动添加access_token的请求。
 * @param <T>
 */
public class AccessRequest<T> extends JsonRequest<T> {
    private final String url;

    private String redirectUrl;

    public AccessRequest(int method,
                         String url,
                         Class<? extends T> clazz,
                         String accessToken,
                         Listener<T> parseListener,
                         Listener<T> listener,
                         ErrorListener errorListener,
                         Map<String, String> params) {

        super(method, url, clazz, parseListener, listener, errorListener, params);

        if (BODY_METHODS.contains(method)) {
            this.url = url;

            if (params == null) {
                params = this.params = new HashMap<>();
            }
            params.put("access_token", accessToken);
        } else {
            this.url = UrlUtil.addQuery(url, "access_token=%s", accessToken);
        }
    }

    @Override
    public void setRedirectUrl(String url) {
        redirectUrl = url;

        super.setRedirectUrl(redirectUrl);
    }

    @Override
    public String getUrl() {
        return redirectUrl != null ? redirectUrl : url;
    }

    @Override
    public String getOriginUrl() {
        return url;
    }

    public static class Builder<T> extends JsonRequest.Builder<T> {
        protected String accessToken;

        public Builder(String url, Class<? extends T> clazz, String accessToken) {
            super(url, clazz);

            this.accessToken = accessToken;
        }

        @Override
        protected Request createRequest() {
            return new AccessRequest<>(
                    method,
                    url,
                    clazz,
                    accessToken,
                    parseListener,
                    responseListener,
                    errorListener,
                    params);
        }

        @Override
        public void build(NetworkModel networkModel) {
            if (accessToken != null) {
                super.build(networkModel);
            }
        }
    }

    public static class SimpleRequestBuilder extends Builder<SimpleResult> {
        public SimpleRequestBuilder(String url, String accessToken) {
            super(url, SimpleResult.class, accessToken);
        }
    }
}
