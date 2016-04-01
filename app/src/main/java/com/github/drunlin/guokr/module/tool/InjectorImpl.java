package com.github.drunlin.guokr.module.tool;

import dagger.ObjectGraph;

/**
 * 基于Dagger的依赖注入工具。
 *
 * @author drunlin@outlook.com
 */
public class InjectorImpl implements Injector {
    private final ObjectGraph objectGraph;

    public InjectorImpl(ObjectGraph graph) {
        objectGraph = graph;
    }

    @Override
    public <T> T get(Class<T> clazz) {
        return objectGraph.get(clazz);
    }

    @Override
    public <T> T inject(T instance) {
        return objectGraph.inject(instance);
    }
}
