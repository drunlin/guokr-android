package com.github.drunlin.guokr.test.util;

/**
 * @author drunlin@outlook.com
 */
public class Value<T> {
    private T value;

    public Value(T v) {
        value = v;
    }

    public Value() {
        super();
    }

    public T getValue() {
        return value;
    }

    public T setValue(T value) {
        return this.value = value;
    }
}
