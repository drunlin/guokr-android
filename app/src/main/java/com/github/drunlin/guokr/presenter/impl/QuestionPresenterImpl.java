package com.github.drunlin.guokr.presenter.impl;

import com.github.drunlin.guokr.bean.Answer;
import com.github.drunlin.guokr.bean.QuestionContent;
import com.github.drunlin.guokr.bean.ResponseCode;
import com.github.drunlin.guokr.model.AskModel;
import com.github.drunlin.guokr.model.QuestionModel;
import com.github.drunlin.guokr.presenter.QuestionPresenter;
import com.github.drunlin.guokr.view.QuestionView;

import javax.inject.Inject;

import static com.github.drunlin.guokr.util.JavaUtil.call;
import static com.github.drunlin.guokr.util.JavaUtil.index;


public class QuestionPresenterImpl
        extends ContentPresenterBase<QuestionContent, Answer, QuestionModel, QuestionView>
        implements QuestionPresenter {

    @Inject AskModel askModel;

    public QuestionPresenterImpl(int id) {
        super(id);
    }

    @Override
    protected QuestionModel getModel() {
        return askModel.getQuestion(contentId);
    }

    @Override
    public void onViewCreated(boolean firstCreated) {
        super.onViewCreated(firstCreated);

        bind(model.supportAnswerResulted(), this::onSupportAnswerResult);
        bind(model.opposeAnswerResulted(), this::onOpposeAnswerResult);
    }

    @Override
    public void onRecommend() {
        if (userModel.checkLoggedIn()) {
            call(model.getContent(), c -> view.recommend(c.title, c.summary, c.url));
        }
    }

    @Override
    public void supportAnswer(Answer answer) {
        model.supportAnswer(answer.id);
    }

    @Override
    public void opposeAnswer(Answer answer) {
        model.opposeAnswer(answer.id);
    }

    private void onSupportAnswerResult(int resultCode, int id) {
        if (resultCode == ResponseCode.OK) {
            index(model.getReplies(), answer -> answer.id == id, view::updateReply);
        } else {
            view.onSupportAnswerFailed();
        }
    }

    private void onOpposeAnswerResult(int resultCode, int id) {
        if (resultCode == ResponseCode.OK) {
            index(model.getReplies(), answer -> answer.id == id, view::updateReply);
        } else {
            view.onOpposeAnswerFailed();
        }
    }
}
