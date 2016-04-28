package com.github.drunlin.guokr.model.impl;

import com.github.drunlin.guokr.bean.CollectionResult;
import com.github.drunlin.guokr.bean.Comment;
import com.github.drunlin.guokr.bean.ResponseCode;
import com.github.drunlin.guokr.bean.Result;
import com.github.drunlin.guokr.model.TopicModel;
import com.github.drunlin.guokr.module.tool.Injector;
import com.github.drunlin.guokr.util.HtmlUtils;
import com.github.drunlin.guokr.util.JavaUtil;
import com.github.drunlin.signals.impl.Signal2;

import org.jsoup.Jsoup;

/**
 * 文章，帖子数据的基类。
 *
 * @author drunlin@outlook.com
 */
public abstract class TopicContentModelBase<T>
        extends ContentModelBase<T, Comment> implements TopicModel<T> {

    protected final Signal2<Integer, Integer> likeCommentResulted = new Signal2<>();

    public TopicContentModelBase(Injector injector,
                                 Class<? extends Result<T>> contentResultClass,
                                 String contentUrl,
                                 Class<? extends CollectionResult<Comment>> commentResultClass,
                                 String repliesUrl,
                                 int id) {
        super(injector, contentResultClass, contentUrl, commentResultClass, repliesUrl, id);
    }

    /**
     * 顶操作成功。
     * @param id
     */
    protected void onLikeReplySucceed(int id) {
        JavaUtil.foreach(getReplies(), comment -> comment.id == id, comment -> {
            comment.hasLiked = true;
            comment.likingsCount += 1;
        });
        likeCommentResulted.dispatch(ResponseCode.OK, id);
    }

    @Override
    public Signal2<Integer, Integer> likeCommentResulted() {
        return likeCommentResulted;
    }

    @Override
    public String getQuote(Comment comment) {
        String name = HtmlUtils.escapeHtml(comment.author.nickname);
        String quote = HtmlUtils.escapeHtml(
                Jsoup.parse(HtmlUtils.removeQuotes(comment.content)).text());
        return String.format(
                "<div><blockquote>引用 @%s 的话：%s</blockquote><br></div>", name, quote);
    }
}
