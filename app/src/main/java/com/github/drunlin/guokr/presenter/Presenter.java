package com.github.drunlin.guokr.presenter;

/**
 * 本项目中所有Presenter都需要实现的基本接口。除了响应自身的生命周期，还需响应Android中View的特殊生命周期。
 *
 * @author drunlin@outlook.com
 */
public interface Presenter<V> {
    /**
     * 与View绑定后进行初始化。
     */
    void onCreate(V view);

    void onViewCreated(boolean firstCreated);

    /**
     * 响应销毁View，由于Android的特殊性在recreate时不会被调用。
     */
    void onViewDestroyed();

    /**
     * 与View解除绑定后进行销毁。
     */
    void onDestroy();
}
