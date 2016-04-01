package com.github.drunlin.guokr.module.tool;

import android.os.Bundle;

import com.github.drunlin.guokr.util.JavaUtil;

/**
 * @author drunlin@outlook.com
 */
public interface ViewLifecycle {
    /**
     * 创建Presenter并和View绑定。
     * @param presenterClass Presenter的类
     * @param <P>
     * @return
     */
    <P> P bind(Class<? extends P> presenterClass);

    /**
     * 创建Presenter并和View绑定。
     * @param factoryClass Presenter的工厂类
     * @param delegate 利用工厂类创建Presenter的中间层
     * @param <P>
     * @param <F>
     * @return
     */
    <P, F> P bind(Class<F> factoryClass, JavaUtil.Converter<F, P> delegate);

    /**
     * View初始化完成。
     * @param savedInstanceState
     */
    void onCreate(Bundle savedInstanceState);

    /**
     * View保存状态。
     */
    void onSaveInstanceState();

    /**
     * View销毁了。
     */
    void onDestroy();
}
