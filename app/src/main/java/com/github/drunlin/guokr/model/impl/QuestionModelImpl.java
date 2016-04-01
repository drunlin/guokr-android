package com.github.drunlin.guokr.model.impl;

import com.github.drunlin.guokr.bean.Answer;
import com.github.drunlin.guokr.bean.QuestionContent;
import com.github.drunlin.guokr.bean.ResponseCode;
import com.github.drunlin.guokr.bean.Result;
import com.github.drunlin.guokr.bean.ResultClassMap.QuestionAnswersResult;
import com.github.drunlin.guokr.bean.ResultClassMap.QuestionContentResult;
import com.github.drunlin.guokr.model.IconLoader;
import com.github.drunlin.guokr.model.QuestionModel;
import com.github.drunlin.guokr.model.UserModel;
import com.github.drunlin.guokr.model.request.AccessRequest;
import com.github.drunlin.guokr.module.tool.Injector;
import com.github.drunlin.signals.impl.Signal2;

import javax.inject.Inject;

import static com.android.volley.Request.Method.DELETE;
import static com.android.volley.Request.Method.POST;
import static com.github.drunlin.guokr.util.JavaUtil.find;

/**
 * @author drunlin@outlook.com
 */
public class QuestionModelImpl
        extends ContentModelBase<QuestionContent, Answer> implements QuestionModel {

    private final Signal2<Integer, Integer> supportAnswerResulted = new Signal2<>();
    private final Signal2<Integer, Integer> opposeAnswerResulted = new Signal2<>();

    @Inject UserModel userModel;

    public QuestionModelImpl(Injector injector, int questionId) {
        super(injector,
                QuestionContentResult.class,
                "http://apis.guokr.com/ask/question/%d.json",
                QuestionAnswersResult.class,
                "http://apis.guokr.com/ask/answer.json?retrieve_type=by_question&question_id=%d",
                questionId);
    }

    @Override
    protected void onParseResponse(Result<QuestionContent> result) {
        IconLoader.load(networkModel, result.result.author.avatar);
    }

    @Override
    public void reply(String content) {
        final String url = "http://apis.guokr.com/ask/answer.json";
        new AccessRequest.SimpleRequestBuilder(url, userModel.getToken())
                .setMethod(POST)
                .addParam("question_id", contentId)
                .addParam("content", content)
                .setListener(response ->  replyResulted.dispatch(ResponseCode.OK))
                .setErrorListener(error -> replyResulted.dispatch(error.getCode()))
                .build(networkModel);
    }

    @Override
    public void supportAnswer(int id) {
        opinionAnswer(id, true, supportAnswerResulted);
    }

    private void opinionAnswer(int id, boolean support, Signal2<Integer, Integer> signal) {
        final String url = "http://www.guokr.com/apis/ask/answer_polling.json";
        new AccessRequest.SimpleRequestBuilder(url, userModel.getToken())
                .setMethod(POST)
                .addParam("answer_id", id)
                .addParam("opinion", support ? "support" : "oppose")
                .setListener(response -> onOpinionAnswerSucceed(id, support, signal))
                .setErrorListener(error -> signal.dispatch(error.getCode(), id))
                .build(networkModel);
    }

    private void onOpinionAnswerSucceed(int id, boolean support, Signal2<Integer, Integer> signal) {
        find(getReplies(), answer -> answer.id == id, answer -> {
            if (support) {
                answer.hasSupported = true;
                answer.supportingsCount += 1;
                if (answer.hasOpposed) {
                    answer.hasOpposed = false;
                    answer.opposingsCount -= 1;
                }
            } else {
                answer.hasOpposed = true;
                answer.opposingsCount += 1;
                if (answer.hasSupported) {
                    answer.hasSupported = false;
                    answer.supportingsCount -= 1;
                }
            }
        });

        signal.dispatch(ResponseCode.OK, id);
    }

    @Override
    public Signal2<Integer, Integer> supportAnswerResulted() {
        return supportAnswerResulted;
    }

    @Override
    public void opposeAnswer(int id) {
        opinionAnswer(id, false, opposeAnswerResulted);
    }

    @Override
    public Signal2<Integer, Integer> opposeAnswerResulted() {
        return opposeAnswerResulted;
    }

    @Override
    public void deleteReply(int id) {
        final String url = "http://www.guokr.com/apis/ask/answer/%d.json";
        new AccessRequest.SimpleRequestBuilder(url, userModel.getToken())
                .setMethod(DELETE)
                .setUrlArgs(id)
                .setListener(response -> onDeleteReplySucceed(id))
                .setErrorListener(error -> deleteReplyResulted.dispatch(error.getCode(), id))
                .build(networkModel);
    }
}
