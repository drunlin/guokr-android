package com.github.drunlin.guokr.model.impl;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.bean.Answer;
import com.github.drunlin.guokr.bean.ResultClassMap.QuestionAnswersResult;
import com.github.drunlin.guokr.test.ModelTestCase;

import org.junit.Test;

import static com.github.drunlin.guokr.mock.MockNetworkModel.DELETE;
import static com.github.drunlin.guokr.mock.MockNetworkModel.post;
import static com.github.drunlin.guokr.mock.MockNetworkModel.stubFor;
import static com.github.drunlin.guokr.mock.MockNetworkModel.urlContainers;
import static com.github.drunlin.guokr.mock.MockNetworkModel.urlEqualTo;
import static com.github.drunlin.guokr.test.ModelAsserts.assertOkResult;
import static com.github.drunlin.guokr.test.ModelAsserts.assertRefresh;
import static com.github.drunlin.guokr.test.util.Constants.ACCESS_TOKEN;
import static com.github.drunlin.guokr.test.util.Constants.COMMENT;
import static com.github.drunlin.guokr.test.util.Constants.QUESTION_Id;
import static com.github.drunlin.guokr.test.util.TestUtils.getResult;
import static com.github.drunlin.guokr.test.util.TestUtils.getText;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author drunlin@outlook.com
 */
public class QuestionModelImplTest extends ModelTestCase {
    private final int ANSWER_ID =
            getResult(QuestionAnswersResult.class, R.raw.question_answers).get(0).id;

    private QuestionModelImpl model;

    @Override
    protected void init() throws Throwable {
        model = new QuestionModelImpl(injector, QUESTION_Id);
    }

    @Test
    public void requestQuestion() throws Exception {
        stubFor(urlEqualTo("http://apis.guokr.com/ask/question/%d.json", QUESTION_Id))
                .setBody(getText(R.raw.question_content));

        model.contentResulted().add((a, b) -> {
            assertOkResult(a);
            assertThat(b, notNullValue());
            countDown();
        });

        model.requestContent();
        await();
    }

    @Test
    public void requestAnswers() throws Exception {
        stubFor(urlContainers("http://apis.guokr.com/ask/answer.json" +
                "?retrieve_type=by_question&question_id=%d", QUESTION_Id))
                .setBody(getText(R.raw.question_answers));

        model.repliesResulted().add((a, b, c) -> {
            assertRefresh(a, b, c);
            countDown();
        });

        model.requestReplies();
        await();
    }

    @Test
    public void reply() throws Exception {
        stubFor(post("question_id=%d&content=%s&access_token=%s",
                QUESTION_Id, COMMENT, ACCESS_TOKEN),
                urlEqualTo("http://apis.guokr.com/ask/answer.json"))
                .setBody(getText(R.raw.simpel_result));

        model.replyResulted().add(a -> {
            assertOkResult(a);
            countDown();
        });

        model.reply(COMMENT);
        await();
    }

    @Test
    public void supportAnswer() throws Exception {
        requestAnswers();

        stubFor(post("answer_id=%d&opinion=support&access_token=%s", ANSWER_ID, ACCESS_TOKEN),
                urlEqualTo("http://www.guokr.com/apis/ask/answer_polling.json"))
                .setBody(getText(R.raw.simpel_result));

        Answer answer = model.getReplies().get(0);
        int supportingsCount = answer.supportingsCount;

        model.supportAnswerResulted().add((a, b) -> {
            assertOkResult(a);
            assertEquals((int) b, ANSWER_ID);
            assertEquals(supportingsCount + 1, answer.supportingsCount);
            assertTrue(answer.hasSupported);
            countDown();
        });

        model.supportAnswer(ANSWER_ID);
        await();
    }

    @Test
    public void supportAnswerResulted() throws Exception {
        assertNotNull(model.supportAnswerResulted());
    }

    @Test
    public void opposeAnswer() throws Exception {
        requestAnswers();

        stubFor(post("answer_id=%d&opinion=oppose&access_token=%s", ANSWER_ID, ACCESS_TOKEN),
                urlEqualTo("http://www.guokr.com/apis/ask/answer_polling.json"))
                .setBody(getText(R.raw.simpel_result));

        Answer answer = model.getReplies().get(0);
        int opposingsCount = answer.opposingsCount;

        model.opposeAnswerResulted().add((a, b) -> {
            assertOkResult(a);
            assertEquals((int) b, ANSWER_ID);
            assertEquals(opposingsCount + 1, answer.opposingsCount);
            assertTrue(answer.hasOpposed);
            countDown();
        });

        model.opposeAnswer(ANSWER_ID);
        await();
    }

    @Test
    public void opposeAnswerResulted() throws Exception {
        assertNotNull(model.opposeAnswerResulted());
    }

    @Test
    public void deleteReply() throws Exception {
        stubFor(DELETE, urlEqualTo("http://www.guokr.com/apis/ask/answer/%d.json" +
                "?access_token=%s", ANSWER_ID, ACCESS_TOKEN))
                .setBody(getText(R.raw.simpel_result));

        model.deleteReplyResulted().add((a, b, c) -> {
            assertOkResult(a);
            countDown();
        });

        model.deleteReply(ANSWER_ID);
        await();
    }
}
