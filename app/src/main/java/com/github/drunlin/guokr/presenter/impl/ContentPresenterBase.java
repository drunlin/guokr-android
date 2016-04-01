package com.github.drunlin.guokr.presenter.impl;

import android.text.TextUtils;

import com.github.drunlin.guokr.bean.Content;
import com.github.drunlin.guokr.bean.Reply;
import com.github.drunlin.guokr.bean.ResponseCode;
import com.github.drunlin.guokr.model.ContentModel;
import com.github.drunlin.guokr.presenter.ContentPresenter;
import com.github.drunlin.guokr.util.HtmlUtils;
import com.github.drunlin.guokr.view.ContentView;

import org.jsoup.Jsoup;

import java.util.List;

import static com.github.drunlin.guokr.util.JavaUtil.call;

/**
 * 文章，帖子，问答内容的基类。
 * @param <T> 内容
 * @param <R> 回复
 * @param <M> 处理内容和回复的模型
 * @param <V> 显示内容和回复的视图
 *
 * @author drunlin@outlook.com
 */
public abstract class ContentPresenterBase<
        T extends Content,
        R extends Reply,
        M extends ContentModel<T, R>,
        V extends ContentView<T, R>>
        extends LoginNeededPresenterBase<V> implements ContentPresenter {

    protected M model;

    protected final int contentId;

    public ContentPresenterBase(int id) {
        contentId = id;
    }

    /**
     * 由子类完成模型的初始化。
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

        bind(model.contentResulted(), this::onContentResult);
        bind(model.repliesResulted(), this::onRepliesResult);
        bind(model.replyResulted(), this::onReplyResult);
        bind(model.deleteReplyResulted(), this::onDeleteReplyResult);

        T content = model.getContent();
        if (firstCreated || content == null) {
            refresh();
        } else {
            view.setContent(content);
            view.setReplies(model.getReplies());
        }
    }

    @Override
    public void refresh() {
        model.requestContent();

        view.setLoading(true);
    }

    private void onContentResult(int resultCode, T content) {
        if (resultCode == ResponseCode.OK) {
            model.requestReplies();

            view.setContent(content);
        } else {
            view.onLoadContentFailed();
        }
        view.setLoading(false);
    }

    private void onRepliesResult(int resultCode, boolean isRefresh, List<R> comments) {
        if (resultCode == ResponseCode.OK) {
            if (isRefresh) {
                view.setReplies(comments);
            } else {
                view.onRepliesAppended();
            }
        } else {
            view.onLoadRepliesFailed();
        }
    }

    @Override
    public void loadMoreReplies() {
        model.requestMoreReplies();
    }

    @Override
    public void onShare() {
        call(model.getContent(), c -> view.share(String.format("%s\n%s", c.summary, c.url)));
    }

    @Override
    public void onOpenLink() {
        call(model.getContent(), content -> view.openLink(content.url));
    }

    @Override
    public void onFavor() {
        if (userModel.checkLoggedIn()) {
            call(model.getContent(), content -> view.favor(content.title, content.url));
        }
    }

    @Override
    public void onPreReply() {
        if (userModel.checkLoggedIn()) {
            view.preReply();
        }
    }

    @Override
    public void reply(String content) {
        //编辑器的内容为HTML格式，而果壳需要BBCode格式。
        model.reply(HtmlUtils.htmlToBBCode(content));
    }

    private void onReplyResult(int resultCode) {
        if (resultCode == ResponseCode.OK) {
            model.requestMoreReplies();

            view.onReplyComplete();
        } else {
            view.onReplyFailed();
        }
    }

    @Override
    public void onCancelReply(String content) {
        if (content == null || TextUtils.isEmpty(Jsoup.parse(content).text())) {
            view.onReplyComplete();
        } else {
            view.cancelReply();
        }
    }

    @Override
    public void deleteReply(Reply reply) {
        model.deleteReply(reply.id);
    }

    private void onDeleteReplyResult(int resultCode, int id, int index) {
        if (resultCode == ResponseCode.OK) {
            view.onReplyRemoved(index);
        } else {
            view.onDeleteReplyFailed();
        }
    }

    @Override
    public void copyReplyContent(Reply reply) {
        model.copyReplyContent(reply);
    }

    @Override
    public void onViewDestroyed() {
        model.cancel();
    }
}
