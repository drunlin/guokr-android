package com.github.drunlin.guokr.widget;

import android.content.Context;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import com.github.drunlin.guokr.bean.SimpleUser;

import java.util.List;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

/**
 * 显示主题站文章的作者名。
 *
 * @author drunlin@outlook.com
 */
public class ArticleAuthorsView extends TextView {
    public ArticleAuthorsView(Context context) {
        this(context, null);
    }

    public ArticleAuthorsView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setLinksClickable(true);
    }

    /**
     * 设置作者列表。
     * @param authors
     */
    public void setAuthors(List<SimpleUser> authors) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        for (SimpleUser author : authors) {
            if (builder.length() > 0) {
                builder.append(" ");
            }
            final int length = author.nickname.length();
            SpannableString string = new SpannableString(author.nickname);
            if (author.isExists) {
                string.setSpan(
                        new ForegroundColorSpan(0xFF46B6B6), 0, length, SPAN_EXCLUSIVE_EXCLUSIVE);
                string.setSpan(
                        new NoUnderlineURLSpan(author.url), 0, length, SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                string.setSpan(
                        new ForegroundColorSpan(0xFFB8B8B8), 0, length, SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            builder.append(string);
        }
        setText(builder);
    }

    /**
     * 去掉连接的下划线。
     */
    private class NoUnderlineURLSpan extends URLSpan {
        public NoUnderlineURLSpan(String url) {
            super(url);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);

            ds.setUnderlineText(false);
        }
    }
}
