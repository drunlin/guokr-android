package com.github.drunlin.guokr.test;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * @author drunlin@outlook.com
 */
public abstract class FragmentTestCase<T extends Fragment> extends ViewTestCase<AppCompatActivity> {
    protected T fragment;

    public FragmentTestCase() {
        super(AppCompatActivity.class);
    }

    protected void addToActivity(T fragment) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        if (this.fragment != null) {
            transaction.remove(this.fragment);
        }
        this.fragment = fragment;
        transaction.add(android.R.id.content, fragment, null).commit();
    }
}
