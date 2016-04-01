package com.github.drunlin.guokr.util;

import com.github.drunlin.guokr.util.JavaUtil.Converter;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author drunlin@outlook.com
 */
public class HtmlUtils {
    /**
     * 使用转译符代替其中的HTML符号。
     * @param text
     * @return
     */
    public static String escapeHtml(CharSequence text) {
        StringBuilder out = new StringBuilder();
        withinStyle(out, text, 0, text.length());
        return out.toString();
    }

    private static void withinStyle(StringBuilder out, CharSequence text, int start, int end) {
        for (int i = start; i < end; i++) {
            char c = text.charAt(i);

            if (c == '<') {
                out.append("&lt;");
            } else if (c == '>') {
                out.append("&gt;");
            } else if (c == '&') {
                out.append("&amp;");
            } else if (c >= 0xD800 && c <= 0xDFFF) {
                if (c < 0xDC00 && i + 1 < end) {
                    char d = text.charAt(i + 1);
                    if (d >= 0xDC00 && d <= 0xDFFF) {
                        i++;
                        int codepoint = 0x010000 | (int) c - 0xD800 << 10 | (int) d - 0xDC00;
                        out.append("&#").append(codepoint).append(";");
                    }
                }
            } else if (c > 0x7E || c < ' ') {
                out.append("&#").append((int) c).append(";");
            } else if (c == ' ') {
                while (i + 1 < end && text.charAt(i + 1) == ' ') {
                    out.append("&nbsp;");
                    i++;
                }

                out.append(' ');
            } else {
                out.append(c);
            }
        }
    }

    /**
     * 把HTML格式文本转换为BBCode格式。
     * @param html
     * @return
     */
    public static String htmlToBBCode(String html) {
        //首先格式化不可嵌套的标签
        Map<String, String> regexps = new HashMap<>();

        regexps.put("<br>", "\n");
        regexps.put("<a href=\"(.+?)\">(.*?)</a>", "[url=$1]$2[/url]");
        regexps.put("<img src=\"(.+?)\"( alt=\".*?\")?>", "[img]$1[/img]");

        for (Map.Entry<String, String> entry : regexps.entrySet()) {
            html = html.replaceAll(entry.getKey(), entry.getValue());
        }

        //处理嵌套的标签，有时会有style属性，顺序为从里到外
        Pattern pattern =
                Pattern.compile("(?s)<(\\w+)( style=\"(.+?)\")?>(((?!</?\\1( .*?)?>).)*)</\\1>");

        Map<String, Converter<String, String>> converters = new HashMap<>();
        converters.put("p", s -> "\n" + s + "\n");
        converters.put("div", s -> s);
        converters.put("b", s -> "[b]" + s + "[/b]");
        converters.put("i", s -> "[i]" + s + "[/i]");
        converters.put("blockquote", s -> "[quote]" + s + "[/quote]");
        converters.put("ol", s -> "[list=1]" + s + "[/list]");
        converters.put("ul", s -> "[list]" + s + "[/list]");
        converters.put("li", s -> "[*]" + s);

        Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            StringBuffer buffer = new StringBuffer();

            //处理当前最里层的标签
            do {
                final String tag = matcher.group(1);
                Converter<String, String> converter =
                        converters.containsKey(tag) ? converters.get(tag) : s -> s;

                StringBuilder builder = new StringBuilder(matcher.group(4));

                String style = matcher.group(3);
                if (style != null) {
                    String[] styles = style.split(";");
                    for (String s : styles) {
                        switch (s) {
                            case "font-weight: bold":
                                builder.insert(0, "[b]").append("[/b]");
                                break;
                            case "font-style: italic":
                                builder.insert(0, "[i]").append("[/i]");
                                break;
                        }
                    }
                }

                matcher.appendReplacement(buffer, converter.convert(builder.toString()));
            } while (matcher.find());

            html = matcher.appendTail(buffer).toString();

            //接下来处理上一层的
            matcher.reset(html);
        }

        return html;
    }
}
