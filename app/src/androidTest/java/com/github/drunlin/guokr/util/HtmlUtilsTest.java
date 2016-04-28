package com.github.drunlin.guokr.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author drunlin@outlook.com
 */
public class HtmlUtilsTest {
    @Test
    public void htmlToBBCode() throws Exception {
        final String html =
                        "<p>" +
                        "<a href=\"http://guokr.com\">Guokr</a>\n" +
                        "<img src=\"http://guokr.com/logo.png\">\n" +
                        "</p>" +
                        "<b>b<i>bi</i></b>" +
                        "<b style=\"font-style: italic;\">bi</b>" +
                        "<i style=\"font-weight: bold;\">bi</i>" +
                        "<ol><li><b>1</b></li><li><i>2</i></li><li><b><i>3</i></b></li></ol>" +
                        "<ul><li><b>1</b></li><li><i>2</i></li><li><b><i>3</i></b></li></ul>" +
                        "<blockquote>abc\n<blockquote>def</blockquote></blockquote>";
        final String bbCode =
                        "\n" +
                        "[url=http://guokr.com]Guokr[/url]\n" +
                        "[img]http://guokr.com/logo.png[/img]\n" +
                        "\n" +
                        "[b]b[i]bi[/i][/b]" +
                        "[b][i]bi[/i][/b]" +
                        "[i][b]bi[/b][/i]" +
                        "[list=1][*][b]1[/b][*][i]2[/i][*][b][i]3[/i][/b][/list]" +
                        "[list][*][b]1[/b][*][i]2[/i][*][b][i]3[/i][/b][/list]" +
                        "[quote]abc\n[quote]def[/quote][/quote]";
        assertEquals(bbCode, HtmlUtils.htmlToBBCode(html));
    }

    @Test
    public void removeQuotes() throws Exception {
        assertEquals("a", HtmlUtils.removeQuotes(
                "<blockquote>1<blockquote>2</blockquote></blockquote>a<blockquote>3</blockquote>"));
        assertEquals("abc", HtmlUtils.removeQuotes(
                "a<blockquote>1<blockquote>2</blockquote></blockquote>b<blockquote>3</blockquote>c"));
    }
}
