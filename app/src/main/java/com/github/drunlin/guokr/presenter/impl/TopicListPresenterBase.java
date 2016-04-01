package com.github.drunlin.guokr.presenter.impl;

import com.github.drunlin.guokr.bean.ResponseCode;
import com.github.drunlin.guokr.bean.TopicEntry;
import com.github.drunlin.guokr.model.TopicListModel;
import com.github.drunlin.guokr.presenter.TopicListPresenter;
import com.github.drunlin.guokr.view.TopicListView;

import java.util.List;

/**
 * 控制文章，帖子，问答列表的基类，实现对列表的刷新和加载更多操作。
 * @param <T> item的数据
 * @param <M> 处理列表数据的模型
 * @param <V> 显示列表的视图
 *
 * @author drunlin@outlook.com $
 */
public abstract class TopicListPresenterBase<
        T extends TopicEntry,
        M extends TopicListModel<T>,
        V extends TopicListView<T>>
        extends PresenterBase<V> implements TopicListPresenter {

    /**处理列表数据的模型*/
    protected M model;

    /**
     * 由子类实现模型的初始化。
     * @return
     */
    protected abstract M getModel();

    @Override
    public void onCreate(V view) {
        super.onCreate(view);

        model = getModel();
    }

    @Override
    public void onViewCreated(boolean firstCreated) {
        super.onViewCreated(firstCreated);

        bind(model.resulted(), this::onResult);

        List<T> topics = model.getTopics();
        if (firstCreated || topics == null) {
            refresh();
        } else {
            view.setData(topics);
        }
    }

    @Override
    public void refresh() {
        model.requestRefresh();

        view.setLoading(true);
    }

    private void onResult(int code, boolean isRefresh, List<T> posts) {
        if (code == ResponseCode.OK) {
            if (isRefresh) {
                view.setData(posts);
            } else {
                view.onDataAppend();
            }
        } else {
            view.onLoadFailed();
        }
        view.setLoading(false);
    }

    @Override
    public void loadMore() {
        model.requestMore();
    }

    @Override
    public void onViewDestroyed() {
        model.cancel();
    }
}
