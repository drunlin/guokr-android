package com.github.drunlin.guokr.activity;

import android.graphics.Bitmap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.drunlin.guokr.R;

import butterknife.Bind;

/**
 * 用来显示网页内容的基类。
 *
 * @author drunlin@outlook.com
 */
public abstract class WebActivity extends SecondaryActivity {
    @Bind(R.id.swipe_refresh) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.web_view) WebView webView;

    @Override
    protected void initialize() {
        super.initialize();

        swipeRefreshLayout.setOnRefreshListener(webView::reload);

        //noinspection all
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(onCreateWebViewClient());

        webView.setBackgroundColor(0);
    }

    protected BasicWebViewClient onCreateWebViewClient() {
        return new BasicWebViewClient();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    protected class BasicWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
