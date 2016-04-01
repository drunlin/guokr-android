package com.github.drunlin.guokr.view;

import com.github.drunlin.guokr.bean.Answer;
import com.github.drunlin.guokr.bean.QuestionContent;

/**
 * 问答内容界面。
 *
 * @author drunlin@outlook.com
 */
public interface QuestionView extends ContentView<QuestionContent, Answer> {
    /**
     * 支持答案失败。
     */
    void onSupportAnswerFailed();

    /**
     * 反对答案失败。
     */
    void onOpposeAnswerFailed();

    /**
     * 推荐问题。
     * @param title
     * @param summary
     * @param url
     */
    void recommend(String title, String summary, String url);
}
