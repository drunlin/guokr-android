package com.github.drunlin.guokr.bean.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * @author drunlin@outlook.com
 */
public abstract class SimpleTypeAdapter<T> extends TypeAdapter<T> {
    @Override
    public void write(JsonWriter out, T value) throws IOException {}
}
