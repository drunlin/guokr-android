package com.github.drunlin.guokr.model.request;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyLog;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

import java.io.IOException;

import okio.Buffer;

/**
 * @author drunlin@outlook.com
 */
public class MultipartRequest<T> extends JsonRequest<T> {
    private final RequestBody requestBody;

    public MultipartRequest(String url,
                            Class<? extends T> clazz,
                            RequestBody body,
                            Listener<T> parseListener,
                            Listener<T> listener,
                            ErrorListener errorListener) {

        super(Method.POST, url, clazz, parseListener, listener, errorListener, null);

        requestBody = body;
    }

    @Override
    public String getBodyContentType() {
        return requestBody.contentType().toString();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        try {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            return buffer.readByteArray();
        } catch (IOException | OutOfMemoryError e) {
            VolleyLog.e("IOException writing to Buffer");
        }
        return null;
    }

    public static class Builder<T> extends JsonRequest.BuilderBase<T, Builder<T>> {
        protected final MultipartBuilder multipartBuilder;

        public Builder(String url, Class<? extends T> clazz) {
            super(url, clazz);

            multipartBuilder = new MultipartBuilder().type(MultipartBuilder.FORM);
        }

        public Builder<T> addFormDataPart(String name, String value) {
            multipartBuilder.addFormDataPart(name, value);
            return this;
        }

        public Builder<T> addFormDataPart(String name, String filename, RequestBody value) {
            multipartBuilder.addFormDataPart(name, filename, value);
            return this;
        }

        @Override
        protected Request createRequest() {
            return new MultipartRequest<>(
                    url,
                    clazz,
                    multipartBuilder.build(),
                    parseListener,
                    responseListener,
                    errorListener);
        }
    }
}
