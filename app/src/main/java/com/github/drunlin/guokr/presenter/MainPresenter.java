package com.github.drunlin.guokr.presenter;

/**
 * @author drunlin@outlook.com
 */
public interface MainPresenter {
    /**
     * 保存子界面位置。
     * @param position
     */
    void savePagePosition(int position);

    /**
     * 浏览果壳网首页。
     */
    void onViewHomePage();

    /**
     * 切换夜间/日间模式。
     */
    void toggleDayNightMode();
}
