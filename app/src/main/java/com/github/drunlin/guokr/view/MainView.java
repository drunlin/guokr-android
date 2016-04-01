package com.github.drunlin.guokr.view;

/**
 * 该App的主界面。
 *
 * @author drunlin@outlook.com
 */
public interface MainView {
    /**
     * 设置要显示的界面。
     * @param position
     */
    void setPagePosition(int position);

    /**
     * 是否启用夜间模式。
     * @param enable
     */
    void setNightModeEnable(boolean enable);

    /**
     * 在浏览器中打开果壳网的首页。
     * @param url 果壳网址
     */
    void viewHomePage(String url);
}
