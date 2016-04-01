package com.github.drunlin.guokr.model;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.bean.CollectionResult;
import com.github.drunlin.guokr.test.ModelTestCase;

import org.junit.Test;

import static com.github.drunlin.guokr.mock.MockNetworkModel.stubFor;
import static com.github.drunlin.guokr.mock.MockNetworkModel.urlContainers;
import static com.github.drunlin.guokr.mock.MockNetworkModel.urlEqualTo;
import static com.github.drunlin.guokr.test.ModelAsserts.assertLoadMore;
import static com.github.drunlin.guokr.test.ModelAsserts.assertOkResult;
import static com.github.drunlin.guokr.test.util.Constants.URL;
import static com.github.drunlin.guokr.test.util.TestUtils.getText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author drunlin@outlook.com
 */
public class JsonListModelTest extends ModelTestCase<JsonListModel<Integer>> {
    class IntArrayResult extends CollectionResult<Integer> {}

    private static final int LIMIT = 5;

    @Override
    protected void init() throws Throwable {
        model = new JsonListModel<>(injector, IntArrayResult.class);
    }

    @Test
    public void resulted() throws Exception {
        assertNotNull(model.resulted());
    }

    private void validateRefreshResult() {
        model.resulted().add((a, b, c) -> {
            assertOkResult(a);
            assertTrue(b);
            assertThat(c, allOf(notNullValue(), hasSize(LIMIT)));
            countDown();
        });
    }

    @Test
    public void setUrl() throws Exception {
        stubFor(urlContainers(URL)).setBody(getText(R.raw.simple_collection));

        validateRefreshResult();

        model.setUrl(URL);
        model.requestRefresh();
        await();
    }

    @Test
    public void setLimit() throws Exception {
        stubFor(urlEqualTo(URL + "?limit=%d&offset=0", LIMIT))
                .setBody(getText(R.raw.simple_collection));

        validateRefreshResult();

        model.setLimit(LIMIT);
        model.request(URL);
        await();

        assertThat(model.getResult().size(), is(LIMIT));
    }

    @Test
    public void request() throws Exception {
        setLimit();
    }

    @Test
    public void requestRefresh() throws Exception {
        setUrl();
    }

    @Test
    public void requestMore() throws Exception {
        request();

        stubFor(urlEqualTo(URL + "?limit=%d&offset=%d", LIMIT, LIMIT))
                .setBody(getText(R.raw.simple_collection_offset5));

        model.resulted().removeAll();
        model.resulted().add((a, b, c) -> {
            assertLoadMore(a, b, c);
            countDown();
        });

        model.requestMore();
        await();

        assertThat(model.getResult().size(), is(LIMIT * 2));
    }

    @Test
    public void getResult() throws Exception {
        //noinspection unchecked
        assertThat(model.getResult(), anyOf(nullValue(), hasSize(0)));

        requestRefresh();

        assertThat(model.getResult(), hasSize(greaterThan(0)));
    }

    @Test
    public void getTotal() throws Exception {
        assertThat(model.getTotal(), is(0));

        setUrl();

        assertThat(model.getTotal(), is(10));
    }

    @Test
    public void cancel() throws Exception {
        stubFor(urlContainers(URL)).setBody(getText(R.raw.simple_collection));

        model.resulted().add((a, b, c) -> fail());

        model.request(URL);
        model.cancel();
        await(1, 100);
    }
}
