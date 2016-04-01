package com.github.drunlin.guokr.model.impl;

import com.github.drunlin.guokr.bean.CollectionResult;
import com.github.drunlin.guokr.bean.QuestionEntry;
import com.github.drunlin.guokr.bean.ResultClassMap.QuestionsResult;
import com.github.drunlin.guokr.model.QuestionListModel;
import com.github.drunlin.guokr.module.tool.Injector;

import java.util.ArrayList;
import java.util.List;

import static com.github.drunlin.guokr.util.JavaUtil.foreach;

/**
 * @author drunlin@outlook.com
 */
public class QuestionListModelImpl
        extends TopicListModelBase<QuestionEntry> implements QuestionListModel {

    public QuestionListModelImpl(Injector injector) {
        super(injector, QuestionsResult.class);

        setUrl("http://www.guokr.com/apis/ask/question.json?retrieve_type=hot_question");
    }

//    /**
//     * 请求某一标签下面的所有问题
//     * @param tag
//     */
//    private void setUrlByTag(String tag) {
//        setUrl("http://apis.guokr.com/ask/question.json", "by_tag", "tag_name", tag);
//    }
//
//    /**
//     * 请求热门问答
//     */
//    private void setUrlByHot() {
//        setUrl("http://www.guokr.com/apis/ask/question.json?retrieve_type=hot_question");
//    }


    @Override
    protected void onParseResult(CollectionResult<QuestionEntry> response) {
        removeNullItems(response);

        super.onParseResult(response);
    }

    /**
     * 移除列表中的空对象，好像只有问答列表才有这种情况。
     * @param response
     */
    private void removeNullItems(CollectionResult<QuestionEntry> response) {
        List<QuestionEntry> result = response.result;
        List<QuestionEntry> entries = new ArrayList<>(result.size());
        foreach(result, entry -> entry != null, entries::add);
        response.result = entries;
    }
}
