package com.github.drunlin.guokr.activity;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.bean.ArticleContent;
import com.github.drunlin.guokr.bean.ArticleType;
import com.github.drunlin.guokr.bean.Comment;
import com.github.drunlin.guokr.bean.ResultClassMap.ArticleCommentsResult;
import com.github.drunlin.guokr.bean.ResultClassMap.ArticleContentResult;
import com.github.drunlin.guokr.test.ActivityTestCase;

import java.util.LinkedList;
import java.util.List;

import static com.github.drunlin.guokr.bean.ArticleType.SUBJECT;
import static com.github.drunlin.guokr.test.util.Constants.ARTICLE_ID;
import static com.github.drunlin.guokr.test.util.Constants.TYPE_HOT;
import static com.github.drunlin.guokr.test.util.TestUtils.getBitmapData;
import static com.github.drunlin.guokr.test.util.TestUtils.getResult;
import static org.mockito.Mockito.spy;

public class ArticleActivityTest extends ActivityTestCase<ArticleActivity> {
    public ArticleActivityTest() {
        super(ArticleActivity.class, false);
    }

    @Override
    protected void init() throws Throwable {
        launchActivity(ArticleActivity.getViewRepliesIntent(ARTICLE_ID));
    }

    @Override
    public void onPreview() throws Throwable {
        ArticleContent content = spy(getResult(ArticleContentResult.class, R.raw.article_content));
        List<ArticleType> labels = content.labels = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            ArticleType label = i == 0 ? TYPE_HOT : new ArticleType(SUBJECT, "math|数学");
            labels.add(label);
        }
        content.dateCreated = "2015-01-01";

        List<Comment> comments = getResult(ArticleCommentsResult.class, R.raw.article_replies);
        comments.get(0).isAuthor = true;
        for (Comment comment : comments) {
            comment.dateCreated = "2015-01-01";
            comment.author.avatar.data = getBitmapData(R.drawable.ic_avatar);
        }
        runOnUiThread(() -> {
            activity.setContent(content);
            activity.setReplies(comments);
            activity.onLoginStateInvalid();
        });
        await(1, 4_000);
        runOnUiThread(() -> {
            activity.preReply();
            activity.onLoginStateInvalid();
        });
    }
}
