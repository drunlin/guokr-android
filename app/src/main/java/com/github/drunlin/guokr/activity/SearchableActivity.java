package com.github.drunlin.guokr.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.drunlin.guokr.App;
import com.github.drunlin.guokr.Intents;
import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.bean.SearchEntry;
import com.github.drunlin.guokr.presenter.SearchablePresenter;
import com.github.drunlin.guokr.util.ToastUtils;
import com.github.drunlin.guokr.view.SearchResultView;
import com.github.drunlin.guokr.widget.RichTextView;
import com.github.drunlin.guokr.widget.SearchView;
import com.github.drunlin.guokr.widget.SwipeLoadLayout;
import com.github.drunlin.guokr.widget.adapter.SimpleAdapter;
import com.github.drunlin.guokr.widget.adapter.SimpleAdapter.ItemViewHolder;

import java.util.List;

import butterknife.Bind;

/**
 * 搜索文章，帖子，问答结果的界面。
 *
 * @author drunlin@outlook.com
 */
public class SearchableActivity extends ActivityBase implements SearchResultView {
    @Bind(R.id.search_view) SearchView searchView;
    @Bind(R.id.recycler_view) RecyclerView recyclerView;
    @Bind(R.id.swipe_load_layout) SwipeLoadLayout swipeLoadLayout;

    private SimpleAdapter<SearchEntry> adapter;

    private SearchablePresenter presenter;

    public static Intent getIntent(String query) {
        Intent intent = new Intent(App.getContext(), SearchableActivity.class);
        intent.setAction(Intent.ACTION_SEARCH);
        intent.putExtra(SearchManager.QUERY, query);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = lifecycle.bind(SearchablePresenter.class);

        setContentView(R.layout.activity_searchable);

        searchView.setHomeButtonListener(this::finish);

        adapter = new SimpleAdapter<>((parent, viewType) -> new ItemHolder(parent));
        adapter.setOnLoadMoreListener(presenter::loadMore);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        swipeLoadLayout.setOnRefreshListener(presenter::refresh);
        swipeLoadLayout.setOnLoadMoreListener(presenter::loadMore);

        if (savedInstanceState == null) {
            handleIntent(getIntent());
        }
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchView.setSearchString(query, true);
            presenter.search(query);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        handleIntent(intent);
    }

    @Override
    public void setLoading(boolean loading) {
        swipeLoadLayout.post(() -> swipeLoadLayout.setRefreshing(loading));
    }

    @Override
    public void setSearchResult(List<SearchEntry> search) {
        adapter.setData(search);

        recyclerView.scrollToPosition(0);
    }

    @Override
    public void onSearchResultAppended() {
        adapter.notifyDataAppended();
    }

    @Override
    public void onLoadFailed() {
        ToastUtils.showShortTimeToast(R.string.toast_load_failed);
    }

    @Override
    public void viewResult(String url) {
        startActivity(Intents.openLinkInApp(url));
    }

    class ItemHolder extends ItemViewHolder<SearchEntry> {
        @Bind(R.id.text_content) RichTextView content;
        @Bind(R.id.text_date) TextView date;

        public ItemHolder(ViewGroup parent) {
            super(SearchableActivity.this, R.layout.item_search, parent);

            itemView.setOnClickListener(v -> presenter.onViewResult(data));
        }

        @Override
        protected void onBind(SearchEntry data, int position) {
            content.setHtml(data.content);
            date.setText(data.datetime);
        }
    }
}
