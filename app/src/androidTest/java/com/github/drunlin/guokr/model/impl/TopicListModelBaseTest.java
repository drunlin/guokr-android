package com.github.drunlin.guokr.model.impl;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.bean.PostEntry;
import com.github.drunlin.guokr.bean.ResultClassMap.PostsResult;
import com.github.drunlin.guokr.test.ModelTestCase;

import org.junit.Test;

import static com.github.drunlin.guokr.mock.MockNetworkModel.stubFor;
import static com.github.drunlin.guokr.mock.MockNetworkModel.urlContainers;
import static com.github.drunlin.guokr.test.ModelAsserts.assertRefresh;
import static com.github.drunlin.guokr.test.util.Constants.POST_Id;
import static com.github.drunlin.guokr.test.util.Constants.URL;
import static com.github.drunlin.guokr.test.util.TestUtils.getText;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class TopicListModelBaseTest extends ModelTestCase<TopicListModelBase<PostEntry>> {
    @Override
    protected void init() throws Throwable {
        model = new TopicListModelBase<PostEntry>(injector, PostsResult.class) {};
    }

    @Test
    public void setUrl() throws Exception {
        stubFor(urlContainers(URL + "?retrieve_type=by_id&id=%d", POST_Id))
                .setBody(getText(R.raw.post_list));

        model.resulted().add((a, b, c) -> {
            assertRefresh(a, b, c);
            countDown();
        });

        model.setUrl(URL, "by_id", "id", POST_Id);
        model.requestRefresh();
        await();
    }

    @Test
    public void getPosts() throws Exception {
        assertThat(model.getTopics(), nullValue());

        setUrl();

        assertThat(model.getTopics(), notNullValue());
    }
}
