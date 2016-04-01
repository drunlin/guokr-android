package com.github.drunlin.guokr.widget;

import com.github.drunlin.guokr.bean.Author;
import com.github.drunlin.guokr.bean.SimpleUser;
import com.github.drunlin.guokr.test.WidgetTestCase;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author drunlin@outlook.com
 */
public class ArticleAuthorsViewTest extends WidgetTestCase<ArticleAuthorsView> {
    @Override
    protected void initOnUiThread() throws Throwable {
        addToScene(new ArticleAuthorsView(activity));
    }

    @Override
    protected void onPreview() throws Throwable {
        List<SimpleUser> authors = new ArrayList<>();
        Author author = new Author();
        author.nickname = "FGED";
        author.isExists = true;
        author.url = "example.com";
        authors.add(author);
        author = new Author();
        author.nickname = "文章";
        author.isExists = false;
        authors.add(author);
        runOnUiThread(() -> view.setAuthors(authors));
        assertThat(view.getText().toString(), is("FGED 文章"));
    }
}
