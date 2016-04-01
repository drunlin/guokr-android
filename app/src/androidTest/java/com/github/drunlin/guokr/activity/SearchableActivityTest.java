package com.github.drunlin.guokr.activity;

import com.github.drunlin.guokr.bean.SearchEntry;
import com.github.drunlin.guokr.test.ActivityTestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author drunlin@outlook.com
 */
public class SearchableActivityTest extends ActivityTestCase<SearchableActivity> {
    public SearchableActivityTest() {
        super(SearchableActivity.class, false);
    }

    @Override
    protected void init() throws Throwable {
        launchActivity(SearchableActivity.getIntent("guokr"));
    }

    @Override
    protected void onPreview() throws Throwable {
        List<SearchEntry> entries = new ArrayList<>(10);
        for (int i = 0; i < 10; ++i) {
            SearchEntry entry = new SearchEntry();
            entry.content = "<h4>[Article] Title</h4><p>This is a test.<p/>";
            entry.datetime = "2015-01-0" + i;
            entries.add(entry);
        }
        runOnUiThread(() -> activity.setSearchResult(entries));
    }
}
