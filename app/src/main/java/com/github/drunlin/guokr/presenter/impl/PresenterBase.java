package com.github.drunlin.guokr.presenter.impl;

import android.support.annotation.CallSuper;

import com.github.drunlin.guokr.presenter.Presenter;
import com.github.drunlin.signals.Slot;
import com.github.drunlin.signals.impl.SafeSignal;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.github.drunlin.guokr.util.JavaUtil.foreach;

/**
 * Presenter的基类。
 *
 * @author drunlin@outlook.com
 */
public abstract class PresenterBase<V> implements Presenter<V> {
    /**向Model注册的侦听器。*/
    protected final Set<Slot> slots = new LinkedHashSet<>();

    /**绑定的View。*/
    protected V view;

    @CallSuper
    @Override
    public void onCreate(V view) {
        this.view = view;
    }

    @Override
    public void onViewCreated(boolean firstCreated) {}

    @Override
    public void onViewDestroyed() {}

    @CallSuper
    @Override
    public void onDestroy() {
        removeAllListener();
    }

    /**
     * 绑定Signal和Listener，会在销毁时自移除。
     * @param signal
     * @param listener
     * @param <T>
     */
    protected <T> void bind(SafeSignal<T> signal, T listener) {
        slots.add(signal.add(listener));
    }

//    /**
//     * 同{@link #bind(SafeSignal, Object)}，不过在响应一次后会自动移除。
//     * @param signal
//     * @param listener
//     * @param <T>
//     */
//    protected <T> void bindOnce(SafeSignal<T> signal, T listener) {
//        slots.add(signal.addOnce(listener));
//    }

    /**
     * 移除所有通过{@link #bind(SafeSignal, Object)}注册的事件。
     */
    protected void removeAllListener() {
        foreach(slots, Slot::remove);
        slots.clear();
    }
}
