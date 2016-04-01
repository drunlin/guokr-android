package com.github.drunlin.guokr.view;

import com.github.drunlin.guokr.bean.QuestionEntry;

/**
 * @author drunlin@outlook.com
 */
public interface QuestionListView extends TopicListView<QuestionEntry> {
    /**
     * 查看问题的内容。
     * @param questionId
     */
    void viewQuestion(int questionId);
}
