package com.github.drunlin.guokr.fragment;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.bean.QuestionEntry;
import com.github.drunlin.guokr.bean.ResultClassMap.QuestionsResult;
import com.github.drunlin.guokr.test.FragmentTestCase;

import java.util.List;

import static com.github.drunlin.guokr.test.util.TestUtils.getResult;

/**
 * @author drunlin@outlook.com
 */
public class QuestionListFragmentTest extends FragmentTestCase<QuestionListFragment> {
    @Override
    protected void init() throws Throwable {
        addToActivity(new QuestionListFragment());
    }

    @Override
    protected void onPreview() throws Throwable {
        List<QuestionEntry> entries = getResult(QuestionsResult.class, R.raw.question_list);
        runOnUiThread(() -> fragment.setData(entries));
    }
}
