package com.github.drunlin.guokr.fragment;

import android.util.Log;

import com.github.drunlin.guokr.test.FragmentTestCase;
import com.github.drunlin.guokr.util.HtmlUtils;

/**
 * @author drunlin@outlook.com
 */
public class EditorFragmentTest extends FragmentTestCase<EditorFragment> {
    private static final String TAG = EditorFragmentTest.class.getName();

    @Override
    protected void initOnUiThread() throws Throwable {
        addToActivity(new EditorFragment());
    }

    @Override
    protected void onPreview() throws Throwable {
        fragment.setOnCompleteListener(content -> Log.d(TAG, HtmlUtils.htmlToBBCode(content)));

        runOnUiThread(() -> fragment.insertHtml("<div>a b c 1 2 3<div>"));
    }
}
