package com.github.drunlin.guokr.model.impl;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.test.ModelTestCase;

import org.junit.Test;

import static com.github.drunlin.guokr.mock.MockNetworkModel.stubFor;
import static com.github.drunlin.guokr.mock.MockNetworkModel.urlContainers;
import static com.github.drunlin.guokr.test.ModelAsserts.assertRefresh;
import static com.github.drunlin.guokr.test.util.TestUtils.getText;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * @author drunlin@outlook.com
 */
public class SearchModelImplTest extends ModelTestCase<SearchModelImpl> {
    @Override
    protected void init() throws Throwable {
        model = new SearchModelImpl(injector);
    }

    @Test
    public void search() throws Exception {
        final String kw = "test";

        stubFor(urlContainers("http://m.guokr.com/search/all/?wd=%s", kw))
                .setBody(getText(R.raw.search));

        model.searchResulted().add((a, b, c) -> {
            assertRefresh(a, b, c);
            countDown();
        });

        model.search(kw);
        await();
    }

    @Test
    public void getSearchList() throws Exception {
        assertNull(model.getSearchList());

        search();

        assertNotNull(model.getSearchList());
    }

    @Test
    public void searchResulted() throws Exception {
        assertNotNull(model.searchResulted());
    }

    @Test
    public void requestRefresh() throws Exception {
        search();

        model.requestRefresh();
        await();
    }
}
