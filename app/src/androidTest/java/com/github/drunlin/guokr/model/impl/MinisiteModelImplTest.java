package com.github.drunlin.guokr.model.impl;

import com.github.drunlin.guokr.test.ModelTestCase;

import org.junit.Test;

import static com.github.drunlin.guokr.test.util.Constants.ARTICLE_ID;
import static com.github.drunlin.guokr.test.util.Constants.TYPE_HOT;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * @author drunlin@outlook.com
 */
public class MinisiteModelImplTest extends ModelTestCase<MinisiteModelImpl> {
    @Override
    protected void init() throws Throwable {
        model = new MinisiteModelImpl(injector);
    }

    @Test
    public void getArticles_byType() throws Exception {
        assertNotNull(model.getArticles(TYPE_HOT));
    }

    @Test
    public void getArticles_byKey() throws Exception {
        assertNotNull(model.getArticles("hot"));
    }

    @Test
    public void getTypes() throws Exception {
        assertThat(model.getTypes(), allOf(notNullValue(), hasSize(greaterThan(0))));
    }

    @Test
    public void getArticles() throws Exception {
        assertNotNull(model.getArticles("hot"));
    }

    @Test
    public void getArticle() throws Exception {
        assertNotNull(model.getArticle(ARTICLE_ID));
    }
}
