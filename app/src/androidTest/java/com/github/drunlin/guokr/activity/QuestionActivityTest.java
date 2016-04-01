package com.github.drunlin.guokr.activity;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.bean.Answer;
import com.github.drunlin.guokr.bean.QuestionContent;
import com.github.drunlin.guokr.bean.ResultClassMap.QuestionAnswersResult;
import com.github.drunlin.guokr.bean.ResultClassMap.QuestionContentResult;
import com.github.drunlin.guokr.test.ActivityTestCase;

import java.util.List;

import static com.github.drunlin.guokr.test.util.Constants.QUESTION_Id;
import static com.github.drunlin.guokr.test.util.TestUtils.getBitmapData;
import static com.github.drunlin.guokr.test.util.TestUtils.getResult;

/**
 * @author drunlin@outlook.com
 */
public class QuestionActivityTest extends ActivityTestCase<QuestionActivity> {
    public QuestionActivityTest() {
        super(QuestionActivity.class, false);
    }

    @Override
    protected void init() throws Throwable {
        launchActivity(QuestionActivity.getIntent(QUESTION_Id));
    }

    @Override
    protected void onPreview() throws Throwable {
        QuestionContent content = getResult(QuestionContentResult.class, R.raw.question_content);
        content.dateCreated = "2015-01-01";
        content.author.avatar.data = getBitmapData(R.drawable.ic_avatar);

        List<Answer> answers = getResult(QuestionAnswersResult.class, R.raw.question_answers);
        for (Answer answer : answers) {
            answer.dateCreated = "2015-01-01";
            answer.author.avatar.data = getBitmapData(R.drawable.ic_avatar);
        }

        runOnUiThread(() -> {
            activity.setContent(content);
            activity.setReplies(answers);
        });
    }
}
