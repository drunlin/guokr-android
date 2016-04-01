package com.github.drunlin.guokr.model;

import com.github.drunlin.guokr.module.tool.Injector;

/**
 * 提供对Dagger支持。
 *
 * @author drunlin@outlook.com
 */
public class Model {
    /**依赖注入器。*/
    protected Injector injector;

    public Model() {
        super();
    }

    public Model(Injector injector) {
        this.injector = injector;
        injector.inject(this);
    }
}
