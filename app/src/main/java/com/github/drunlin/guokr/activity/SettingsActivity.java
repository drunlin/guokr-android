package com.github.drunlin.guokr.activity;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.model.impl.PreferenceModelImpl;

/**
 * 设置界面。
 *
 * @author drunlin@outlook.com
 */
public class SettingsActivity extends SecondaryActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            getPreferenceManager().setSharedPreferencesName(PreferenceModelImpl.PREFERENCE_NAME);
            addPreferencesFromResource(R.xml.activity_settings);
        }
    }
}
