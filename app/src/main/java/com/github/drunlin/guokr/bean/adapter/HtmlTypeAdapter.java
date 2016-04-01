package com.github.drunlin.guokr.bean.adapter;

import com.google.gson.stream.JsonReader;

import java.io.IOException;

/**
 * 去掉HTML文本最后的 <p></p> 标签
 *
 * @author drunlin@outlook.com
 */
public class HtmlTypeAdapter extends SimpleTypeAdapter<String> {
    @Override
    public String read(JsonReader in) throws IOException {
        String html = in.nextString();
        if (html.endsWith("</p>")) {
            int index = html.lastIndexOf("<p>");
            int len = html.length();
            return html.substring(0, index) + html.substring(index + 3, len - 4);
        }
        return html;
    }
}
