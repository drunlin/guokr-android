package com.github.drunlin.guokr.bean.adapter;

import com.github.drunlin.guokr.bean.Icon;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 用来解析{@link Icon}
 * 
 * @author drunlin@outlook.com
 */
public class IconTypeAdapter extends SimpleTypeAdapter<Icon> {
    @Override
    public Icon read(JsonReader in) throws IOException {
        List<String> images = new ArrayList<>(3);

        if (in.peek() == JsonToken.BEGIN_OBJECT) {
            String[] imageArr = new String[3];

            in.beginObject();
            while (in.peek() != JsonToken.END_OBJECT) {
                String name = in.nextName();
                if (in.peek() == JsonToken.STRING) {
                    imageArr[getImagePriority(name)] = in.nextString();
                } else {
                    in.skipValue();
                }
            }
            in.endObject();

            for (String image : imageArr) {
                if (image != null) {
                    images.add(image);
                }
            }
        } else {
            images.add(in.nextString());
        }

        Icon icon = new Icon();
        icon.urls = images;
        return icon;
    }

    private int getImagePriority(String name) {
        switch (name) {
            case "small":
                return 0;
            case "normal":
                return 1;
            case "large":
                return 2;
        }
        return 0;
    }
}
