package com.github.drunlin.guokr.model.impl;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.bean.ArticleEntry;
import com.github.drunlin.guokr.bean.ArticleType;
import com.github.drunlin.guokr.test.ModelTestCase;

import org.junit.Test;

import static com.github.drunlin.guokr.bean.ArticleType.CHANNEL;
import static com.github.drunlin.guokr.bean.ArticleType.SUBJECT;
import static com.github.drunlin.guokr.mock.MockNetworkModel.stubFor;
import static com.github.drunlin.guokr.mock.MockNetworkModel.urlEqualTo;
import static com.github.drunlin.guokr.test.ModelAsserts.assertRefresh;
import static com.github.drunlin.guokr.test.util.Constants.IMAGE_URL;
import static com.github.drunlin.guokr.test.util.Constants.LIMIT;
import static com.github.drunlin.guokr.test.util.TestUtils.getText;
import static junit.framework.Assert.assertNotNull;

/**
 * @author drunlin@outlook.com
 */
public class ArticleListModelImplTest extends ModelTestCase<ArticleListModelImpl> {
    @Test
    public void all() throws Exception {
        init("http://apis.guokr.com/minisite/article.json" +
                "?retrieve_type=by_minisite&offset=0&limit=10",
                new ArticleType(CHANNEL, "all|全部"));
    }

    private void init(String url, ArticleType type) throws Exception {
        stubFor(urlEqualTo(url)).setBody(getText(R.raw.article_list));
        stubFor(urlEqualTo(IMAGE_URL)).setBody("data");

        model = new ArticleListModelImpl(injector, type);
        model.resulted().add((a, b, c) -> {
            assertRefresh(a, b, c);
            countDown();
        });

        model.setLimit(LIMIT);
        model.requestRefresh();
        await();

        for (ArticleEntry entry : model.getTopics()) {
            assertNotNull(entry.labels);
            assertNotNull(entry.image.data);
            assertNotNull(entry.dateCreated);
        }
    }

    @Test
    public void channel() throws Exception {
        init("http://apis.guokr.com/minisite/article.json" +
                "?retrieve_type=by_channel&channel_key=hot&offset=0&limit=10",
                new ArticleType(CHANNEL, "hot|热点"));
    }

    @Test
    public void subject() throws Exception {
        init("http://apis.guokr.com/minisite/article.json" +
                "?retrieve_type=by_subject&subject_key=math&offset=0&limit=10",
                new ArticleType(SUBJECT, "math|数学"));
    }
}
