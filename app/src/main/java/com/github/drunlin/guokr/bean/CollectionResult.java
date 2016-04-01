package com.github.drunlin.guokr.bean;

import java.util.List;

/**
 * 通用的集合类型结果。
 *
 * @author drunlin@outlook.com
 */
public abstract class CollectionResult<T> extends Result<List<T>> {
    public int limit;
    public int offset;
    public int total;
}
