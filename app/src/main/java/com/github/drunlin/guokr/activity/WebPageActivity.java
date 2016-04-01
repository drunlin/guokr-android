package com.github.drunlin.guokr.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.view.Menu;
import android.view.MenuItem;

import com.github.drunlin.guokr.App;
import com.github.drunlin.guokr.Intents;
import com.github.drunlin.guokr.R;

import butterknife.Bind;

/**
 * 内嵌的网页浏览器。
 *
 * @author drunlin@outlook.com
 */
public class WebPageActivity extends WebActivity {
    @Bind(R.id.app_bar) AppBarLayout appBarLayout;

    private final Handler handler = new Handler();
    private final Runnable hideToolBarRunnable = () -> appBarLayout.setExpanded(false, true);

    public static Intent getIntent(String url, String title) {
        Intent intent = new Intent(App.getContext(), WebPageActivity.class);
        intent.putExtra(Intents.EXTRA_URL, url);
        intent.putExtra(Intents.EXTRA_TITLE, title);
        return intent;
    }

    public static Intent getIntent(String url) {
        return getIntent(url, " ");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web_page);

        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (appBarLayout.getTop() == 0) {
                handler.postDelayed(hideToolBarRunnable, 1500);
            } else {
                handler.removeCallbacks(hideToolBarRunnable);
            }
        });

        //noinspection ConstantConditions
        getSupportActionBar().setTitle(getIntent().getStringExtra(Intents.EXTRA_TITLE));

        webView.loadUrl(getIntent().getStringExtra(Intents.EXTRA_URL));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_web_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_open_link:
                startActivity(Intents.openLinkInBrowser(webView.getUrl()));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
