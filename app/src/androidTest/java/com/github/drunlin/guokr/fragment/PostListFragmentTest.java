package com.github.drunlin.guokr.fragment;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.bean.PostEntry;
import com.github.drunlin.guokr.bean.ResultClassMap.PostsResult;
import com.github.drunlin.guokr.test.FragmentTestCase;

import java.util.List;

import static com.github.drunlin.guokr.test.util.TestUtils.getBitmapData;
import static com.github.drunlin.guokr.test.util.TestUtils.getResult;

/**
 * @author drunlin@outlook.com
 */
public class PostListFragmentTest extends FragmentTestCase<PostListFragment> {
    @Override
    protected void init() throws Throwable {
        addToActivity(new PostListFragment(60));
    }

    @Override
    protected void onPreview() throws Throwable {
        List<PostEntry> entries = getResult(PostsResult.class, R.raw.post_list);
        for (PostEntry entry : entries) {
            entry.dateCreated = "2015-01-01";
            entry.author.avatar.data = getBitmapData(R.drawable.ic_avatar);
        }
        runOnUiThread(() -> fragment.setData(entries));
    }
}
