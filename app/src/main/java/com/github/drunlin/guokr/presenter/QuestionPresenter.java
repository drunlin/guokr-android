package com.github.drunlin.guokr.presenter;

import com.github.drunlin.guokr.bean.Answer;

/**
 * @author drunlin@outlook.com
 */
public interface QuestionPresenter extends ContentPresenter {
    /**
     * 推荐问题。
     */
    void onRecommend();

    /**
     * 支持回答。
     * @param answer
     */
    void supportAnswer(Answer answer);

    /**
     * 反对回答。
     * @param answer
     */
    void opposeAnswer(Answer answer);

    interface Factory {
        QuestionPresenter create(int questionId);
    }
}
