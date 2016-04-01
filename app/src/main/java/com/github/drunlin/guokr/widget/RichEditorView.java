package com.github.drunlin.guokr.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import com.github.drunlin.guokr.util.ViewUtils;

import org.jsoup.Jsoup;

import java.util.regex.Pattern;

import jp.wasabeef.richeditor.RichEditor;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import static com.github.drunlin.guokr.util.JavaUtil.call;

/**
 * 基于WebView的富文本编辑器，继承自一个开源组件。
 * @see <a href="https://github.com/wasabeef/richeditor-android">wasabeef:richeditor-android</a>
 *
 * @author drunlin@outlook.com
 */
public class RichEditorView extends RichEditor {
    private static final String BUNDLE_SUPER_STATE = "BUNDLE_SUPER_STATE";
    private static final String BUNDLE_HTML = "BUNDLE_HTML";

    private static final Pattern NOT_EMPTY_PATTERN = Pattern.compile("\\S");

    private OnInterceptRequestListener onInterceptRequestListener;
    private OnTextChangeListener onTextChangeListener;

    private int editorHeight = -1;
    private String editorText = "";
    private boolean editorFocused;

    public RichEditorView(Context context) {
        this(context, null);
    }

    public RichEditorView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.webViewStyle);
    }

    public RichEditorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setImageStyle();

        addEventListener();
    }

    @SuppressLint("AddJavascriptInterface")
    @Override
    protected EditorWebViewClient createWebviewClient() {
        addJavascriptInterface(this, "app");

        return new WebViewClient();
    }

    /**
     * 设置图片的样式。
     */
    private void setImageStyle() {
        exec("javascript:(function() {" +
                "    var css = 'img{display:inline;height:auto;max-width:100%;}';" +
                "    var head = document.head || document.getElementsByTagName('head')[0];" +
                "    var style = document.createElement('style');" +
                "    style.type = 'text/css';" +
                "    if (style.styleSheet){" +
                "        style.styleSheet.cssText = css;" +
                "    } else {" +
                "        style.appendChild(document.createTextNode(css));" +
                "    }" +
                "    head.appendChild(style);" +
                "})();");
    }

    private void addEventListener() {
        exec("javascript:" +
                "window.app.onFocusChanged(document.hasFocus());" +
                "RE.editor.onfocus = function() {" +
                "    window.app.onFocusChanged(true);" +
                "};" +
                "RE.editor.onblur = function() {" +
                "    window.app.onFocusChanged(false);" +
                "};" +
                "" +
                "var editorHeight = 0;" +
                "var editorText = '';" +
                "setInterval(function() {" + //试过oninput方法，但是不理想
                "    if (editorHeight != RE.editor.scrollHeight) {" +
                "        editorHeight = RE.editor.scrollHeight;" +
                "        window.app.onHeightChanged(editorHeight);" +
                "    }" +
                "    if (editorText != RE.editor.innerHTML) {" +
                "        editorText = RE.editor.innerHTML;" +
                "        window.app.onTextChanged(editorText);" +
                "    }" +
                "}, 16);");
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public void onTextChanged(String html) {
        editorText = html;

        call(onTextChangeListener, l -> post(() -> l.onTextChange(html)));
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public void onFocusChanged(boolean focused) {
        editorFocused = focused;

        if (!focused) {
            post(this::focusEditor);
        }
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public void onHeightChanged(int height) {
        editorHeight = ViewUtils.getDimension(getContext(), COMPLEX_UNIT_DIP, height);

        post(this::requestLayout);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (editorHeight > 0 && MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            int height = Math.min(editorHeight, MeasureSpec.getSize(heightMeasureSpec));
            setMeasuredDimension(getMeasuredWidth(), height);
        }
    }

    @Override
    public void setOnTextChangeListener(OnTextChangeListener listener) {
        onTextChangeListener = listener;
    }

    /**
     * 插入HTML文本，应当调用
     * {@link com.github.drunlin.guokr.util.HtmlUtils#escapeHtml(CharSequence)}} 过滤其中的特殊字符。
     * @param html
     */
    public void insertHtml(String html) {
        if (editorFocused) {
            String s = html.replace("\\", "\\\\").replace("'", "\\'");
            exec(String.format("javascript:RE.insertHTML('%s');", s));
        } else {
            focusEditor();

            postDelayed(() -> insertHtml(html), 16);
        }
    }

    @Override
    public void setHtml(String contents) {
        super.setHtml(contents);

        focusEditor();
    }

    /**
     * 清除所有的内容。
     */
    public void clear() {
        setHtml("");
    }

    @Override
    public void removeFormat() {
        exec("javascript:document.execCommand('removeFormat');");
    }

    /**
     * 切换引用格式的开/关。
     */
    @Override
    public void setBlockquote() {
        exec("javascript:" +
                "var selection = document.getSelection();" +
                "var node = selection.focusNode;" +
                "if (node.nodeType == 1 && node.nodeName == 'BLOCKQUOTE') {" +
                "    document.execCommand('formatBlock', true, 'div');" +
                "} else {" +
                "    document.execCommand('formatBlock', true, 'blockquote');" +
                "}");
    }

    @NonNull
    @Override
    public String getHtml() {
        return editorText;
    }

    /**
     * 内容是否为空。
     * @return
     */
    public boolean isEmpty() {
        return !NOT_EMPTY_PATTERN.matcher(Jsoup.parse(getHtml()).text()).find();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        focusEditor();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_SUPER_STATE, super.onSaveInstanceState());
        bundle.putString(BUNDLE_HTML, getHtml());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            setHtml(bundle.getString(BUNDLE_HTML));
            state = bundle.getParcelable(BUNDLE_SUPER_STATE);
        }
        super.onRestoreInstanceState(state);
    }

    /**
     * 设置加载资源的钩子。
     * @param listener
     */
    public void setOnInterceptRequestListener(OnInterceptRequestListener listener) {
        onInterceptRequestListener = listener;
    }

    private class WebViewClient extends EditorWebViewClient {
        @SuppressWarnings("deprecation")
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            if (onInterceptRequestListener != null) {
                return onInterceptRequestListener.onInterceptRequest(url);
            }
            return super.shouldInterceptRequest(view, url);
        }
    }

    public interface OnInterceptRequestListener {
        WebResourceResponse onInterceptRequest(String url);
    }
}
