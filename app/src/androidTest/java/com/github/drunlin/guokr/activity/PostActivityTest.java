package com.github.drunlin.guokr.activity;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.bean.Comment;
import com.github.drunlin.guokr.bean.PostContent;
import com.github.drunlin.guokr.bean.ResultClassMap.PostCommentsResult;
import com.github.drunlin.guokr.bean.ResultClassMap.PostContentResult;
import com.github.drunlin.guokr.test.ActivityTestCase;

import java.util.List;

import static com.github.drunlin.guokr.test.util.Constants.POST_Id;
import static com.github.drunlin.guokr.test.util.TestUtils.getBitmapData;
import static com.github.drunlin.guokr.test.util.TestUtils.getResult;

/**
 * @author drunlin@outlook.com
 */
public class PostActivityTest extends ActivityTestCase<PostActivity> {
    public PostActivityTest() {
        super(PostActivity.class, false);
    }

    @Override
    protected void init() throws Throwable {
        launchActivity(PostActivity.getIntent(POST_Id));
    }

    @Override
    protected void onPreview() throws Throwable {
        PostContent content = getResult(PostContentResult.class, R.raw.post_content);
        content.dateCreated = "2015-01-01";
        content.author.avatar.data = getBitmapData(R.drawable.ic_avatar);

        List<Comment> comments = getResult(PostCommentsResult.class, R.raw.post_replies);
        for (Comment comment : comments) {
            comment.dateCreated = "2015-01-01";
            comment.author.avatar.data = getBitmapData(R.drawable.ic_avatar);
        }
        comments.get(0).hasLiked = true;

        runOnUiThread(() -> {
            activity.setContent(content);
            activity.setReplies(comments);
        });
    }
}
