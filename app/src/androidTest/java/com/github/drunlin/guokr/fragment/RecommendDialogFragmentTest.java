package com.github.drunlin.guokr.fragment;

import com.github.drunlin.guokr.test.FragmentTestCase;

import static com.github.drunlin.guokr.test.util.Constants.SUMMARY;
import static com.github.drunlin.guokr.test.util.Constants.TITLE;
import static com.github.drunlin.guokr.test.util.Constants.URL;

/**
 * @author drunlin@outlook.com
 */
public class RecommendDialogFragmentTest extends FragmentTestCase<RecommendDialogFragment> {
    @Override
    public void onPreview() throws Throwable {
        RecommendDialogFragment.show(
                activity.getSupportFragmentManager(), TITLE, SUMMARY, URL);
    }
}
