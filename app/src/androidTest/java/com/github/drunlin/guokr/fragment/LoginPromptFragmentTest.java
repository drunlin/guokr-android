package com.github.drunlin.guokr.fragment;

import com.github.drunlin.guokr.test.FragmentTestCase;

/**
 * @author drunlin@outlook.com
 */
public class LoginPromptFragmentTest extends FragmentTestCase<LoginPromptFragment> {
    @Override
    protected void onPreview() throws Throwable {
        LoginPromptFragment.show(activity);
        LoginPromptFragment.show(activity);
    }
}
