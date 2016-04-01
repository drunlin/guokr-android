package com.github.drunlin.guokr.fragment;

import com.github.drunlin.guokr.test.FragmentTestCase;

/**
 * @author drunlin@outlook.com
 */
public class ForumFragmentTest extends FragmentTestCase<ForumFragment> {
    @Override
    protected void init() throws Throwable {
        addToActivity(new ForumFragment());
    }

    @Override
    protected void onPreview() throws Throwable {
        runOnUiThread(() -> fragment.setLoginStatus(true));
    }
}
