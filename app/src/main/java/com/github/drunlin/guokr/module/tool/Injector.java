package com.github.drunlin.guokr.module.tool;

/**
 * 依赖注入器。
 *
 * @author drunlin@outlook.com
 */
public interface Injector {
    /**
     * 通过类获取其实例。
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T get(Class<T> clazz);

    /**
     * 注入其需要的依赖对象。
     * @param instance
     * @param <T>
     * @return
     */
    <T> T inject(T instance);
}
