package com.github.drunlin.guokr.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.drunlin.guokr.Intents;
import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.bean.Message;
import com.github.drunlin.guokr.presenter.MessageListPresenter;
import com.github.drunlin.guokr.util.ToastUtils;
import com.github.drunlin.guokr.view.MessageListView;
import com.github.drunlin.guokr.widget.CircleImageView;
import com.github.drunlin.guokr.widget.RichTextView;
import com.github.drunlin.guokr.widget.adapter.SimpleAdapter;
import com.github.drunlin.guokr.widget.adapter.SimpleAdapter.ItemViewHolder;

import java.util.List;

import butterknife.Bind;

import static android.graphics.Typeface.BOLD;
import static android.graphics.Typeface.NORMAL;

/**
 * 站内信界面，显示站内信列表。目前只能读，对于回复之类的操作在网页中完成。
 *
 * @author drunlin@outlook.com
 */
public class MessageListActivity extends ListActivity implements MessageListView {
    private SimpleAdapter<Message> adapter;

    private MessageListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = lifecycle.bind(MessageListPresenter.class);

        setContentView(R.layout.activity_messages);

        adapter = new SimpleAdapter<>((parent, viewType) -> new ItemHolder(parent));
        adapter.setOnLoadMoreListener(presenter::loadMore);

        recyclerView.setAdapter(adapter);

        swipeLoadLayout.setOnRefreshListener(presenter::refresh);
        swipeLoadLayout.setOnLoadMoreListener(presenter::loadMore);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_messages, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_open_link:
                presenter.onViewMessages();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setMassages(List<Message> massages) {
        adapter.setData(massages);
    }

    @Override
    public void onMassagesAppended() {
        adapter.notifyDataAppended();
    }

    @Override
    public void onLoadMessagesFailed() {
        ToastUtils.showShortTimeToast(R.string.toast_load_failed);
    }

    @Override
    public void viewMessage(String url) {
        startActivity(WebPageActivity.getIntent(url, getString(R.string.title_messages)));
    }

    @Override
    public void viewMessages(String url) {
        startActivity(Intents.openLinkInBrowser(url));
    }

    class ItemHolder extends ItemViewHolder<Message> {
        @Bind(R.id.text_name) TextView name;
        @Bind(R.id.img_avatar) CircleImageView avatar;
        @Bind(R.id.text_content) RichTextView content;
        @Bind(R.id.text_date) TextView date;

        public ItemHolder(ViewGroup parent) {
            super(MessageListActivity.this, R.layout.item_massage, parent);

            itemView.setOnClickListener(v -> presenter.onViewMessage(data));
        }

        @Override
        protected void onBind(Message message, int position) {
            avatar.setImageData(message.another.avatar.data);
            name.setText(message.another.nickname);
            name.setTypeface(null, message.isRead ? NORMAL : BOLD);
            date.setText(message.dateCreated);
            content.setHtml(message.content);
        }
    }
}
