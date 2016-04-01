package com.github.drunlin.guokr.model.request;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.github.drunlin.guokr.BuildConfig;
import com.github.drunlin.guokr.model.NetworkModel;
import com.github.drunlin.guokr.util.UrlUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.android.volley.toolbox.HttpHeaderParser.parseCacheHeaders;

/**
 * 添加在工作线程中运行回调。
 * @param <T>
 */
public abstract class HttpRequest<T> extends Request<T> {
    protected static final Set<Integer> BODY_METHODS = new HashSet<>();
    static {
        BODY_METHODS.add(Method.POST);
        BODY_METHODS.add(Method.PUT);
        BODY_METHODS.add(Method.PATCH);
    }

    protected final Listener<T> parseListener;
    protected final Listener<T> listener;
    protected final ErrorListener errorListener;
    protected Map<String, String> params;

    public HttpRequest(int method,
                       String url,
                       Listener<T> parseListener,
                       Listener<T> listener,
                       ErrorListener errorListener,
                       Map<String, String> params) {

        super(method, url, null);

        this.parseListener = parseListener;
        this.listener = listener;
        this.errorListener = errorListener;
        this.params = params;

        if (BuildConfig.DEBUG) {
            setRetryPolicy(new DefaultRetryPolicy(100_000, 0, 1));
        }
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

    protected final Response<T> parseNetworkResponseSuccess(NetworkResponse response, T result) {
        if (parseListener != null) {
            parseListener.onResponse(result);
        }
        return Response.success(result, parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(T response) {
        if (listener != null) {
            listener.onResponse(response);
        }
    }

    protected RequestError parseError(VolleyError volleyError) {
        return new RequestError(volleyError);
    }

    @Override
    public void deliverError(VolleyError volleyError) {
        if (errorListener != null) {
            errorListener.onErrorResponse(parseError(volleyError));
        }
    }

    public static class RequestError {
        private int code;
        private VolleyError error;

        public RequestError(VolleyError error, int code) {
            this.error = error;
            this.code = code;
        }

        public RequestError(VolleyError error) {
            this.error = error;
        }

        public VolleyError getError() {
            return error;
        }

        public int getCode() {
            return code;
        }
    }

    public interface ErrorListener {
        void onErrorResponse(RequestError error);
    }

    /**
     * Request的工具类。
     * @param <T> 结果的类型
     * @param <C> 方便此类可以被继承
     */
    @SuppressWarnings("unchecked")
    protected abstract static class Builder<T, C extends Builder<T, ?>> {
        protected String url;
        protected Object[] urlArgs;
        protected int method = Method.GET;
        protected Map<String, String> params;
        protected Listener<T> parseListener;
        protected Listener<T> responseListener;
        protected ErrorListener errorListener;
        protected Object tag;

        public Builder(String url) {
            this.url = url;
        }

        public C setUrlArgs(Object... args) {
            urlArgs = args;
            return (C) this;
        }

        public C setMethod(int method) {
            this.method = method;
            return (C) this;
        }

        public C addParam(String key, Object value) {
            if (params == null) {
                params = new HashMap<>();
            }
            params.put(key, String.valueOf(value));
            return (C) this;
        }

        public C setParseListener(Listener<T> listener) {
            parseListener = listener;
            return (C) this;
        }

        public C setListener(Listener<T> listener) {
            responseListener = listener;
            return (C) this;
        }

        public C setErrorListener(ErrorListener listener) {
            errorListener = listener;
            return (C) this;
        }

        public C setTag(Object tag) {
            this.tag = tag;
            return (C) this;
        }

        protected abstract Request createRequest();

        public void build(NetworkModel networkModel) {
            if (urlArgs != null) {
                url = String.format(url, urlArgs);
            }
            if (params != null & !BODY_METHODS.contains(method)) {
                url = UrlUtil.addQuery(url, params);
            }
            networkModel.add(createRequest(), tag);
        }
    }
}
