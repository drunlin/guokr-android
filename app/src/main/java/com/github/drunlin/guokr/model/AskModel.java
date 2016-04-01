package com.github.drunlin.guokr.model;

/**
 * 问答相关的数据。
 *
 * @author drunlin@outlook.com
 */
public interface AskModel {
    /**
     * 获取问题数据。
     * @param questionId
     * @return
     */
    QuestionModel getQuestion(int questionId);
}
