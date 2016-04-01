package com.github.drunlin.guokr.model.impl;

import com.github.drunlin.guokr.model.ListModeBase;
import com.github.drunlin.guokr.module.tool.Injector;
import com.github.drunlin.guokr.test.ModelTestCase;

import org.junit.Test;
import org.mockito.Mockito;

public class ListModeBaseTest extends ModelTestCase<ListModeBaseTest.ListModeBaseImpl> {
    @Override
    protected void init() throws Throwable {
        model = Mockito.spy(new ListModeBaseImpl(injector));
    }

    @Test
    public void request() throws Exception {

    }

    @Test
    public void setResponse() throws Exception {

    }

    @Test
    public void deliverResult() throws Exception {

    }

    @Test
    public void deliverError() throws Exception {

    }

    @Test
    public void reset() throws Exception {

    }

    @Test
    public void refresh() throws Exception {

    }

    @Test
    public void checkCanRequestMore() throws Exception {

    }

    @Test
    public void requestMore() throws Exception {

    }

    @Test
    public void isLoading() throws Exception {

    }

    public static class ListModeBaseImpl extends ListModeBase {
        public ListModeBaseImpl(Injector injector) {
            super(injector);
        }

        @Override
        protected void onRequest() {

        }

        @Override
        protected void onParseResult(Object response) {

        }

        @Override
        public void cancel() {

        }
    }
}