package com.github.drunlin.guokr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.activity.SearchableActivity;
import com.github.drunlin.guokr.presenter.SearchBoxPresenter;
import com.github.drunlin.guokr.view.SearchBoxView;
import com.github.drunlin.guokr.widget.SearchView;

import org.cryse.widget.persistentsearch.PersistentSearchView.SearchListener;

import java.util.List;

/**
 * 辅助控制{@link SearchBoxView}的显示和逻辑。
 *
 * @author drunlin@outlook.com
 */
public class SearchBoxFragment extends FragmentBase implements SearchBoxView, SearchListener {
    private SearchView searchView;
    private View searchViewTint;

    private SearchBoxPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = lifecycle.bind(SearchBoxPresenter.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_box, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchViewTint = view;
        searchViewTint.setOnClickListener(v -> searchView.cancelEditing());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        searchView = (SearchView) getActivity().findViewById(R.id.search_view);
        searchView.setSearchListener(this);
    }

    @Override
    public void search(String query) {
        startActivity(SearchableActivity.getIntent(query));

        searchView.closeSearch();
    }

    @Override
    public void setHistory(List<String> history) {
        //TODO 搜索的历史记录
    }

    @Override
    public void onSearch(String query) {
        presenter.onSearch(query);
    }

    @Override
    public void onSearchEditOpened() {
        searchViewTint.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSearchEditClosed() {
        searchViewTint.setVisibility(View.GONE);
    }

    @Override
    public boolean onSearchEditBackPressed() {
        searchView.closeSearch();
        return true;
    }

    @Override
    public void onSearchCleared() {}

    @Override
    public void onSearchTermChanged(String term) {}

    @Override
    public void onSearchExit() {}
}
