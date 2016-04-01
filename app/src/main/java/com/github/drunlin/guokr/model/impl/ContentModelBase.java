package com.github.drunlin.guokr.model.impl;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.github.drunlin.guokr.App;
import com.github.drunlin.guokr.bean.CollectionResult;
import com.github.drunlin.guokr.bean.Reply;
import com.github.drunlin.guokr.bean.ResponseCode;
import com.github.drunlin.guokr.bean.Result;
import com.github.drunlin.guokr.model.ContentModel;
import com.github.drunlin.guokr.model.IconLoader;
import com.github.drunlin.guokr.model.JsonListModel;
import com.github.drunlin.guokr.model.Model;
import com.github.drunlin.guokr.model.NetworkModel;
import com.github.drunlin.guokr.model.UserModel;
import com.github.drunlin.guokr.model.request.JsonRequest;
import com.github.drunlin.guokr.module.tool.Injector;
import com.github.drunlin.guokr.util.UrlUtil;
import com.github.drunlin.signals.impl.Signal1;
import com.github.drunlin.signals.impl.Signal2;
import com.github.drunlin.signals.impl.Signal3;

import org.jsoup.Jsoup;

import java.util.List;

import javax.inject.Inject;

import static com.github.drunlin.guokr.util.JavaUtil.foreach;
import static com.github.drunlin.guokr.util.JavaUtil.index;

/**
 * 文章，帖子，问答数据的基类。
 *
 * @author drunlin@outlook.com
 */
public abstract class ContentModelBase<T, R extends Reply>
        extends Model implements ContentModel<T, R> {

    /**每次请求评论的最大数量。*/
    public static final int COMMENTS_LIMIT = 20;

    protected final Signal2<Integer, T> contentResulted = new Signal2<>();
    protected final Signal1<Integer> replyResulted = new Signal1<>();
    protected final Signal3<Integer, Integer, Integer> deleteReplyResulted = new Signal3<>();

    @Inject NetworkModel networkModel;
    @Inject UserModel userModel;

    protected ReplyListModel repliesModel;

    private final Class<? extends Result<T>> contentResultClass;

    /**当前内容的ID。*/
    protected final int contentId;
    /**请求内容的URL。*/
    protected final String contentUrl;
    /**请求评论的URL。*/
    protected final String repliesUrl;

    protected T content;

    /**
     * 用通用的方式请求内容和评论。
     * @param injector
     * @param contentResultClass 内容数据的结构。
     * @param contentUrl 请求内容数据的URL。
     * @param commentResultClass 回复数据的结构。
     * @param repliesUrl 请求回复数据的URL。
     */
    protected ContentModelBase(Injector injector,
                               Class<? extends Result<T>> contentResultClass,
                               String contentUrl,
                               Class<? extends CollectionResult<R>> commentResultClass,
                               String repliesUrl,
                               int id) {
        super(injector);

        this.contentResultClass = contentResultClass;

        this.contentUrl = contentUrl;
        this.repliesUrl = String.format(repliesUrl, id);

        contentId = id;

        repliesModel = new ReplyListModel(networkModel, commentResultClass);
        repliesModel.setLimit(COMMENTS_LIMIT);
    }

    @Override
    public void requestContent() {
        new JsonRequest.Builder<>(contentUrl, contentResultClass)
                .setUrlArgs(contentId)
                .setParseListener(this::onParseResponse)
                .setListener(this::onContentResult)
                .setErrorListener(error -> contentResulted.dispatch(error.getCode(), null))
                .setTag(this)
                .build(networkModel);
    }

    protected void onParseResponse(Result<T> result) {}

    @Override
    public void requestReplies() {
        repliesModel.requestRefresh();
    }

    protected void onContentResult(Result<T> result) {
        content = result.result;

        contentResulted.dispatch(ResponseCode.OK, content);
    }

    public Signal2<Integer, T> contentResulted() {
        return contentResulted;
    }

    public T getContent() {
        return content;
    }

    public void requestMoreReplies() {
        repliesModel.requestMore();
    }

    public Signal3<Integer, Boolean, List<R>> repliesResulted() {
        return repliesModel.resulted();
    }

    public List<R> getReplies() {
        return repliesModel.getResult();
    }

    @Override
    public Signal1<Integer> replyResulted() {
        return replyResulted;
    }

    /**
     * 删除回复成功的事件由此函数处理。
     * @param id
     */
    protected void onDeleteReplySucceed(int id) {
        index(getReplies(), reply -> reply.id == id, index -> {
            getReplies().remove((int) index);

            deleteReplyResulted.dispatch(ResponseCode.OK, id, index);
        });
    }

    @Override
    public Signal3<Integer, Integer, Integer> deleteReplyResulted() {
        return deleteReplyResulted;
    }

    @Override
    public void copyReplyContent(Reply reply) {
        ClipboardManager clipboard =
                (ClipboardManager) App.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText("", Jsoup.parse(reply.content).text()));
    }

    @Override
    public void cancel() {
        networkModel.cancel(this);
        repliesModel.cancel();
    }

    private class ReplyListModel extends JsonListModel<R> {
        public ReplyListModel(NetworkModel networkModel,
                              Class<? extends CollectionResult<R>> clazz) {
            super(networkModel, clazz);
        }

        @Override
        protected void onRequest() {
            url = userModel.isLoggedIn()
                    ? UrlUtil.addQuery(repliesUrl, "access_token=%s", userModel.getToken())
                    : repliesUrl;

            super.onRequest();
        }

        @Override
        protected void onParseResult(CollectionResult<R> response) {
            final List<R> replies = response.result;
            IconLoader.batchLoad(networkModel, replies, item -> item.author.avatar);
            if (userModel.isLoggedIn()) {
                foreach(replies, reply ->
                        reply.isAuthor = userModel.getUserKey().equals(reply.author.userKey));
            }

            super.onParseResult(response);
        }
    }
}
