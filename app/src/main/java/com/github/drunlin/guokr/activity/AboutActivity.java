package com.github.drunlin.guokr.activity;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.github.drunlin.guokr.R;

/**
 * 本应用的关于界面。
 *
 * @author drunlin@outlook.com
 */
public class AboutActivity extends SecondaryActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);
    }

    public static class AboutFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.activity_about);
        }
    }
}
