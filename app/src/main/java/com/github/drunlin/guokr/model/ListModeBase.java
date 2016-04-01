package com.github.drunlin.guokr.model;

import android.support.annotation.CallSuper;
import android.support.annotation.WorkerThread;

import com.github.drunlin.guokr.bean.ResponseCode;
import com.github.drunlin.guokr.module.tool.Injector;
import com.github.drunlin.signals.impl.Signal3;

import java.util.List;

import javax.inject.Inject;

/**
 * 获取列表数据的通用基类。
 * @param <T> 列表元素类型
 * @param <R> 成功类型
 * @param <E> 错误类型
 *
 * @author drunlin@outlook.com
 */
public abstract class ListModeBase<T, R, E> extends Model {
    protected final Signal3<Integer, Boolean, List<T>> resulted = new Signal3<>();

    @Inject protected NetworkModel networkModel;

    /**将会用于请求的URL。*/
    protected String url;
    /**请求的开始位置。*/
    protected int offset;
    /**当前URL所有的请求成功的结果。*/
    protected List<T> result;

    protected boolean isLoading;
    protected boolean isRefreshing;

    public ListModeBase(NetworkModel networkModel) {
        this.networkModel = networkModel;
    }

    public ListModeBase(Injector injector) {
        super(injector);
    }

    /**
     * 请求URL。
     */
    protected void request() {
        if (isLoading) {
            cancel();
        } else {
            isLoading = true;
        }
        
        onRequest();
    }

    /**
     * 取消正在进行的加载。
     */
    public void cancel() {
        networkModel.cancel(this);
    }

    /**
     * 处理URL请求。
     */
    protected abstract void onRequest();

    /**
     * 解析数据。
     * @param response
     */
    @WorkerThread
    protected abstract void onParseResult(R response);

    /**
     * 分发结果。
     * @param response
     */
    @CallSuper
    protected void onDeliverResult(List<T> response) {
        onResponse(response);

        resulted.dispatch(ResponseCode.OK, isRefreshing, response);

        onLoadComplete();
    }

    protected void onLoadComplete() {
        isLoading = false;
        isRefreshing = false;
    }

    /**
     * 设置解析成功的数据。
     * @param response
     */
    protected void onResponse(List<T> response) {
        if (isRefreshing) {
            result = response;
        } else {
            result.addAll(response);
        }
    }

    /**
     * 分发错误。
     * @param error
     */
    @CallSuper
    protected void onDeliverError(E error) {
        onLoadComplete();
    }

    /**
     * 重新请求。
     */
    protected void refresh() {
        onReset();

        request();
    }

    /**
     * 重置请求信息。
     */
    protected void onReset() {
        offset = 0;
        isRefreshing = true;
    }

    /**
     * 请求更多。
     */
    public void requestMore() {
        if (isLoading) {
            return;
        }

        if (result == null) {
            refresh();
        } else if (canRequestMore()) {
            offset = getNextPageOffset();

            request();
        }
    }

    /**
     * 检查能否进行加载更多操作。
     * @return
     */
    protected boolean canRequestMore() {
        return true;
    }

    /**
     * 请求更多数据时用到的索引。
     * @return
     */
    protected int getNextPageOffset() {
        return result.size();
    }

    /**
     * 是否在加载中。
     * @return
     */
    public boolean isLoading() {
        return isLoading;
    }
}
