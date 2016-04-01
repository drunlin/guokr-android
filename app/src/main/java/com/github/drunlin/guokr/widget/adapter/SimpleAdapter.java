package com.github.drunlin.guokr.widget.adapter;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;

import com.github.drunlin.guokr.util.ViewUtils;

import java.util.List;

import butterknife.ButterKnife;

/**
 * 实现了{@link android.support.v7.widget.RecyclerView.Adapter}的常用需求，
 * 主要配合{@link ItemViewHolder}一起使用。
 *
 * @author drunlin@outlook.com
 */
public class SimpleAdapter<T> extends Adapter {
    protected List<T> data;
    protected int currentDataSize;

    private ViewHolderFactory viewHolderFactory;

    private OnLoadMoreListener onLoadMoreListener;

    private int spanCount = 1;

    public SimpleAdapter(ViewHolderFactory factory) {
        viewHolderFactory = factory;
    }

    public void setSpanCount(@IntRange(from = 1) int count) {
        spanCount = count;
    }

    public void setData(List<T> data) {
        this.data = data;

        currentDataSize = getItemCount();

        notifyDataSetChanged();
    }

    public void notifyDataAppended() {
        int size = currentDataSize;
        currentDataSize = getItemCount();
        if (currentDataSize > size) {
            notifyItemInserted(size);
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        onLoadMoreListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewHolderFactory.create(parent, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //noinspection unchecked
        ((ItemViewHolder<T>) holder).setData(data.get(position), position);

        checkLoadMore(position);
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        ((ItemViewHolder) holder).onRecycle();
    }

    /**
     * 根据正在显示的位置来判断是否要加载更多数据。
     * @param position
     */
    protected void checkLoadMore(int position) {
        if (onLoadMoreListener != null) {
            int total = getItemCount() / spanCount;
            int offset = (position + 1) / spanCount;
            if (total <= 4 && offset == total || total - offset == 4) {
                onLoadMoreListener.onLoadMore();
            }
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    /**
     * 实现{@link #onBind(Object, int)}就能自动绑定数据。
     * @param <T>
     */
    public static abstract class ItemViewHolder<T> extends ViewHolder {
        protected T data;

        public ItemViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);
        }

        public ItemViewHolder(Context context, @LayoutRes int layoutRes, ViewGroup parent) {
            this(ViewUtils.inflate(context, layoutRes, parent));
        }

        @CallSuper
        public void setData(T data, int position) {
            this.data = data;

            onBind(data, position);
        }

        protected abstract void onBind(T data, int position);

        protected void onRecycle() {}
    }

    /**
     * 创建ViewHolder的工厂方法。
     */
    public interface ViewHolderFactory {
        ViewHolder create(ViewGroup parent, int viewType);
    }

    /**
     * 侦听加载更多数据。
     */
    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
