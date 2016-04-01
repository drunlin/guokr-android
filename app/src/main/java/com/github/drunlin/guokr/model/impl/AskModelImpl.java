package com.github.drunlin.guokr.model.impl;

import com.github.drunlin.guokr.model.AskModel;
import com.github.drunlin.guokr.model.Model;
import com.github.drunlin.guokr.model.QuestionModel;
import com.github.drunlin.guokr.module.tool.Injector;
import com.github.drunlin.guokr.module.tool.ReferenceMap;

/**
 * @author drunlin@outlook.com
 */
public class AskModelImpl extends Model implements AskModel {
    private ReferenceMap<Integer, QuestionModel> questionModelMap = new ReferenceMap<>();

    public AskModelImpl(Injector injector) {
        super(injector);
    }

    @Override
    public QuestionModel getQuestion(int questionId) {
        return questionModelMap.get(questionId, () -> new QuestionModelImpl(injector, questionId));
    }
}
