package com.github.drunlin.guokr.presenter;

import com.github.drunlin.guokr.bean.QuestionEntry;

/**
 * @author drunlin@outlook.com
 */
public interface QuestionListPresenter extends TopicListPresenter {
    void onViewQuestion(QuestionEntry entry);
}
