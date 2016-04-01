package com.github.drunlin.guokr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.presenter.TopicListPresenter;
import com.github.drunlin.guokr.util.ToastUtils;
import com.github.drunlin.guokr.view.TopicListView;
import com.github.drunlin.guokr.widget.adapter.SimpleAdapter;
import com.github.drunlin.guokr.widget.adapter.SimpleAdapter.ItemViewHolder;

import java.util.List;

/**
 * 文章，小组，问答列表的基类。
 *
 * @author drunlin@outlook.com
 */
public abstract class TopicListFragment<T, P extends TopicListPresenter>
        extends ListFragment implements TopicListView<T> {

    protected SimpleAdapter<T> adapter;

    protected P presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_simple, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new SimpleAdapter<>((parent, viewType) -> onCreateViewHolder(parent));
        adapter.setOnLoadMoreListener(presenter::loadMore);

        recyclerView.setAdapter(adapter);

        swipeLoadLayout.setOnRefreshListener(presenter::refresh);
        swipeLoadLayout.setOnLoadMoreListener(presenter::loadMore);
    }

    protected abstract ItemViewHolder<T> onCreateViewHolder(ViewGroup parent);

    @Override
    public void setData(List<T> dataList) {
        adapter.setData(dataList);
    }

    @Override
    public void onDataAppend() {
        adapter.notifyDataAppended();
    }

    @Override
    public void onLoadFailed() {
        ToastUtils.showShortTimeToast(R.string.toast_load_failed);
    }
}
