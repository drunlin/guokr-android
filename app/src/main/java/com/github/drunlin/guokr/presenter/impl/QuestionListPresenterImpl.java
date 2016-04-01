package com.github.drunlin.guokr.presenter.impl;

import com.github.drunlin.guokr.bean.QuestionEntry;
import com.github.drunlin.guokr.model.QuestionListModel;
import com.github.drunlin.guokr.presenter.QuestionListPresenter;
import com.github.drunlin.guokr.view.QuestionListView;

import javax.inject.Inject;

/**
 * @author drunlin@outlook.com
 */
public class QuestionListPresenterImpl
        extends TopicListPresenterBase<QuestionEntry, QuestionListModel, QuestionListView>
        implements QuestionListPresenter {

    @Inject QuestionListModel questionListModel;

    @Override
    protected QuestionListModel getModel() {
        return questionListModel;
    }

    @Override
    public void onViewQuestion(QuestionEntry entry) {
        view.viewQuestion(entry.id);
    }
}
