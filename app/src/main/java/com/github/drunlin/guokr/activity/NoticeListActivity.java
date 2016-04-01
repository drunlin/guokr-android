package com.github.drunlin.guokr.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.drunlin.guokr.Intents;
import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.bean.Notice;
import com.github.drunlin.guokr.presenter.NoticeListPresenter;
import com.github.drunlin.guokr.util.ToastUtils;
import com.github.drunlin.guokr.view.NoticeListView;
import com.github.drunlin.guokr.widget.adapter.SimpleAdapter;
import com.github.drunlin.guokr.widget.adapter.SimpleAdapter.ItemViewHolder;

import java.util.List;

import butterknife.Bind;

import static android.graphics.Typeface.BOLD;
import static android.graphics.Typeface.NORMAL;

/**
 * 通知列表界面，由于API的限制，只能显示未读的通知，在网页中打开通知。
 *
 * @author drunlin@outlook.com
 */
public class NoticeListActivity extends ListActivity implements NoticeListView {
    private SimpleAdapter<Notice> adapter;

    private NoticeListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = lifecycle.bind(NoticeListPresenter.class);

        setContentView(R.layout.activity_notices);

        adapter = new SimpleAdapter<>((parent, viewType) -> new ItemHolder(parent));

        recyclerView.setAdapter(adapter);

        swipeLoadLayout.setOnRefreshListener(presenter::refresh);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_notices, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_open_link:
                presenter.onViewNotices();
                return true;
            case R.id.menu_ignore_all:
                presenter.ignoreAll();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setNotices(List<Notice> notices) {
        adapter.setData(notices);
    }

    @Override
    public void onLoadFailed() {
        ToastUtils.showShortTimeToast(R.string.toast_load_failed);
    }

    @Override
    public void onIgnoreAllFailed() {
        ToastUtils.showShortTimeToast(R.string.toast_ignore_all_failed);
    }

    @Override
    public void viewNotice(String url) {
        startActivity(WebPageActivity.getIntent(url, getString(R.string.title_notices)));
    }

    @Override
    public void openInBrowser(String url) {
        startActivity(Intents.openLinkInBrowser(url));
    }

    class ItemHolder extends ItemViewHolder<Notice> {
        @Bind(R.id.text_content) TextView content;
        @Bind(R.id.text_date) TextView date;

        public ItemHolder(ViewGroup parent) {
            super(NoticeListActivity.this, R.layout.item_notice, parent);

            itemView.setOnClickListener(v -> presenter.onViewNotice(data));
        }

        @Override
        protected void onBind(Notice notice, int position) {
            content.setText(notice.content);
            content.setTypeface(null, notice.isRead ? NORMAL : BOLD);
            date.setText(notice.dateLastUpdated);
        }
    }
}
