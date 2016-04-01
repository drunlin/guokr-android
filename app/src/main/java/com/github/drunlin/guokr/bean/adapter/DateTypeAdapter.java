package com.github.drunlin.guokr.bean.adapter;

import com.github.drunlin.guokr.util.DateUtils;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.util.Date;

/**
 * 格式化果壳的时间。
 *
 * @author drunlin@outlook.com
 */
public class DateTypeAdapter extends SimpleTypeAdapter<String> {
    @Override
    public String read(JsonReader in) throws IOException {
        switch (in.peek()) {
            case STRING:
                //From 2015-11-30T22:53:34.767380+08:00 to 2015-11-30 22:53:34
                String date = in.nextString().replaceAll("^(.+)T(.{8}).*$", "$1 $2");
                return DateUtils.format(date);
            case NUMBER:
                return DateUtils.format(new Date(in.nextLong() * 1000));
            case NULL:
                in.nextNull();
            default:
                return null;
        }
    }
}
