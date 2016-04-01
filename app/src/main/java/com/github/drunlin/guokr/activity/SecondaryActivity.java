package com.github.drunlin.guokr.activity;

import android.view.MenuItem;

/**
 * 二级界面的基类。
 *
 * @author drunlin@outlook.com
 */
public abstract class SecondaryActivity extends ToolBarActivity {
    @Override
    protected void onSetupToolbar() {
        super.onSetupToolbar();

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
