package com.github.drunlin.guokr.model;

import com.github.drunlin.guokr.bean.Answer;
import com.github.drunlin.guokr.bean.QuestionContent;
import com.github.drunlin.signals.impl.Signal2;

/**
 * 问答内容的数据。
 *
 * @author drunlin@outlook.com
 */
public interface QuestionModel extends ContentModel<QuestionContent, Answer> {
    /**
     * 支持回答。
     * @param id
     */
    void supportAnswer(int id);

    /**
     * 支持回答的结果。
     * @return
     */
    Signal2<Integer, Integer> supportAnswerResulted();

    /**
     * 反对回答。
     * @param id
     */
    void opposeAnswer(int id);

    /**
     * 反对回答的结果。
     * @return
     */
    Signal2<Integer, Integer> opposeAnswerResulted();
}
