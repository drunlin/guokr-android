package com.github.drunlin.guokr.model.request;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.github.drunlin.guokr.bean.NetworkError;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import static com.android.volley.toolbox.HttpHeaderParser.parseCharset;

/**
 * 来自于Google的json序列化工具，把服务器返回的结果自动反序列化。
 * @param <T>
 */
public class JsonRequest<T> extends HttpRequest<T> {
    private final Gson gson = new Gson();
    private final Class<? extends T> clazz;

    public JsonRequest(int method,
                       String url,
                       Class<? extends T> clazz,
                       Listener<T> parseListener,
                       Listener<T> listener,
                       ErrorListener errorListener,
                       Map<String, String> params) {

        super(method, url, parseListener, listener, errorListener, params);

        this.clazz = clazz;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, parseCharset(response.headers));
            return parseNetworkResponseSuccess(response, gson.fromJson(json, clazz));
        } catch (UnsupportedEncodingException | JsonSyntaxException | OutOfMemoryError e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected RequestError parseError(VolleyError volleyError) {
        NetworkResponse response = volleyError.networkResponse;
        try {
            String json = new String(response.data, parseCharset(response.headers));
            NetworkError error = gson.fromJson(json, NetworkError.class);
            return new RequestError(volleyError, error.errorCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.parseError(volleyError);
    }

    protected static class BuilderBase<T, C extends BuilderBase<T, ?>>
            extends HttpRequest.Builder<T, C> {

        protected Class<? extends T> clazz;

        public BuilderBase(String url, Class<? extends T> clazz) {
            super(url);

            this.clazz = clazz;
        }

        @Override
        protected Request createRequest() {
            return new JsonRequest<>(
                    method,
                    url,
                    clazz,
                    parseListener,
                    responseListener,
                    errorListener,
                    params);
        }
    }

    public static class Builder<T> extends BuilderBase<T, Builder<T>> {
        public Builder(String url, Class<? extends T> clazz) {
            super(url, clazz);
        }
    }
}
