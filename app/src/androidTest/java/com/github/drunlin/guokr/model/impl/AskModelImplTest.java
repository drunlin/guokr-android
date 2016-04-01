package com.github.drunlin.guokr.model.impl;

import com.github.drunlin.guokr.test.ModelTestCase;

import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

/**
 * @author drunlin@outlook.com
 */
public class AskModelImplTest extends ModelTestCase<AskModelImpl> {
    @Override
    protected void init() throws Throwable {
        model = new AskModelImpl(injector);
    }

    @Test
    public void getQuestion() throws Exception {
        assertNotNull(model.getQuestion(10));
    }
}
