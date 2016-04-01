package com.github.drunlin.guokr.module.tool;

import com.github.drunlin.guokr.util.JavaUtil.Supplier;

/**
 * 管理单例的引用。
 *
 * @author drunlin@outlook.com
 */
public class SingletonMap {
    private static final ReferenceMap referenceMap = new ReferenceMap<>();

    public static <T> T get(Class<T> clazz, Supplier<T> creator) {
        //noinspection unchecked
        return (T) referenceMap.get(clazz, creator);
    }
}
