package com.github.drunlin.guokr.model;

import android.support.annotation.IntRange;
import android.support.annotation.WorkerThread;

import com.github.drunlin.guokr.bean.CollectionResult;
import com.github.drunlin.guokr.model.request.HttpRequest.RequestError;
import com.github.drunlin.guokr.model.request.JsonRequest;
import com.github.drunlin.guokr.module.tool.Injector;
import com.github.drunlin.signals.impl.Signal3;

import java.util.List;

/**
 * 请求Json集合类型的通用类。
 *
 * @author drunlin@outlook.com
 */
public class JsonListModel<T>
        extends ListModeBase<T, CollectionResult<T>, RequestError> implements CollectionModel<T> {

    protected Class<? extends CollectionResult<T>> clazz;

    protected int limit = 10;
    protected int total;

    public JsonListModel(NetworkModel networkModel, Class<? extends CollectionResult<T>> clazz) {
        super(networkModel);

        this.clazz = clazz;
    }

    public JsonListModel(Injector injector, Class<? extends CollectionResult<T>> clazz) {
        super(injector);

        this.clazz = clazz;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void setLimit(@IntRange(from = 1) int limit) {
        this.limit = limit;
    }

    @Override
    protected void onRequest() {
        new JsonRequest.Builder<>(url, clazz)
                .addParam("limit", limit)
                .addParam("offset", offset)
                .setParseListener(this::onParseResult)
                .setListener(response -> onDeliverResult(response.result))
                .setErrorListener(this::onDeliverError)
                .setTag(this)
                .build(networkModel);
    }

    @Override
    protected void onParseResult(CollectionResult<T> response) {
        total = response.total;

        onParseResult(response.result);
    }

    @WorkerThread
    protected void onParseResult(List<T> result) {}

    @Override
    protected void onDeliverError(RequestError error) {
        super.onDeliverError(error);

        resulted.dispatch(error.getCode(), false, null);
    }

    @Override
    public void request(String url) {
        this.url = url;

        refresh();
    }

    @Override
    public void request(String url, Object... args) {
        request(String.format(url, args));
    }

    @Override
    public void requestRefresh() {
        refresh();
    }

    @Override
    public List<T> getResult() {
        return result;
    }

    @Override
    public Signal3<Integer, Boolean, List<T>> resulted() {
        return resulted;
    }

    @Override
    public int getTotal() {
        return total;
    }
}
