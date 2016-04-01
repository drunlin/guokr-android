package com.github.drunlin.guokr.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebView;

import com.github.drunlin.guokr.R;
import com.github.drunlin.guokr.presenter.LoginPresenter;
import com.github.drunlin.guokr.view.LoginView;

/**
 * 登录界面。通过网页登录成功后，获取cookie中验证相关的信息。
 *
 * @author drunlin@outlook.com
 */
public class LoginActivity extends WebActivity implements LoginView {
    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = lifecycle.bind(LoginPresenter.class);

        setContentView(R.layout.activity_login);

        webView.loadUrl("https://account.guokr.com/sign_in/?display=mobile");
    }

    @Override
    protected BasicWebViewClient onCreateWebViewClient() {
        return new BasicWebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //登录成功后会跳转到果壳首页
                if (url.matches("^http://(m|(www))\\.guokr\\.com/$")) {
                    CookieManager cookieManager = CookieManager.getInstance();
                    String cookie = cookieManager.getCookie(url);
                    presenter.setCookie(cookie);
                    view.stopLoading();
                } else {
                    super.onPageStarted(view, url, favicon);
                }
            }
        };
    }

    /**
     * 成功登录后关闭当前界面。
     */
    @Override
    public void onLoggedIn() {
        finish();
    }
}
