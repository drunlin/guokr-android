package com.github.drunlin.guokr.activity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.drunlin.guokr.App;
import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.bean.Comment;
import com.github.drunlin.guokr.bean.Content;
import com.github.drunlin.guokr.presenter.TopicPresenter;
import com.github.drunlin.guokr.util.ToastUtils;
import com.github.drunlin.guokr.view.TopicView;

import butterknife.Bind;

import static com.github.drunlin.guokr.util.JavaUtil.call;

/**
 * 文章，帖子界面的基类，它们有相同的回复列表界面。
 *
 * @author drunlin@outlook.com
 */
public abstract class TopicActivity<T extends Content, P extends TopicPresenter>
        extends ContentActivity<T, Comment, P> implements TopicView<T> {

    @Override
    public void onLoadRepliesFailed() {
        ToastUtils.showShortTimeToast(R.string.toast_load_comments_failed);
    }

    @Override
    public void onLikeReplyFailed() {
        ToastUtils.showShortTimeToast(R.string.toast_like_comment_failed);
    }

    @Override
    public void onReplyFailed() {
        ToastUtils.showShortTimeToast(R.string.toast_comment_failed);
    }

    @Override
    public void onDeleteReplyFailed() {
        ToastUtils.showShortTimeToast(R.string.toast_delete_comment_failed);
    }

    @Override
    public void insertQuote(String quote) {
        App.post(() -> call(getEditor(), editor -> editor.insertHtml(quote)));
    }

    @Override
    protected ReplyViewHolderBase createReplyHolder(ViewGroup parent) {
        return new ReplyViewHolder(parent);
    }

    class ReplyViewHolder extends ReplyViewHolderBase {
        @Bind(R.id.text_like) TextView likesCount;
        @Bind(R.id.btn_like) ImageButton like;

        public ReplyViewHolder(ViewGroup parent) {
            super(R.layout.item_topic_reply, parent, R.menu.item_reply);

            like.setOnClickListener(v -> presenter.likeReply(data));
        }

        @Override
        protected void onMenuCreated(Menu menu) {
            super.onMenuCreated(menu);

            menu.findItem(R.id.menu_like).setVisible(!data.hasLiked);
        }

        protected boolean onMenuClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.menu_comment:
                    presenter.onReplyComment(data);
                    return true;
                case R.id.menu_like:
                    presenter.likeReply(data);
                    return true;
            }
            return super.onMenuClick(menuItem);
        }

        @Override
        protected void onBind(Comment comment, int position) {
            super.onBind(comment, position);

            likesCount.setText(String.valueOf(comment.likingsCount));
            like.setEnabled(!comment.hasLiked);
        }
    }
}
