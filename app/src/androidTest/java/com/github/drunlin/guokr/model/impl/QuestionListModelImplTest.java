package com.github.drunlin.guokr.model.impl;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.test.ModelTestCase;

import org.junit.Test;

import static com.github.drunlin.guokr.mock.MockNetworkModel.stubFor;
import static com.github.drunlin.guokr.mock.MockNetworkModel.urlContainers;
import static com.github.drunlin.guokr.test.ModelAsserts.assertRefresh;
import static com.github.drunlin.guokr.test.util.TestUtils.getText;

/**
 * @author drunlin@outlook.com
 */
public class QuestionListModelImplTest extends ModelTestCase<QuestionListModelImpl> {
    @Override
    protected void init() throws Throwable {
        model = new QuestionListModelImpl(injector);
    }

    @Test
    public void create() throws Exception {
        stubFor(urlContainers("http://www.guokr.com/apis/ask/question.json" +
                "?retrieve_type=hot_question")).setBody(getText(R.raw.question_list));

        model.resulted().add((a, b, c) -> {
            assertRefresh(a, b, c);
            countDown();
        });

        model.requestRefresh();
        await();
    }
}
