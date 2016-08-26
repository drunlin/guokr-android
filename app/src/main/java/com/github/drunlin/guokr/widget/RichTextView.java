package com.github.drunlin.guokr.widget;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.util.LruCache;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.github.drunlin.guokr.App;
import com.github.drunlin.guokr.Intents;
import com.github.drunlin.guokr.util.BitmapUtils;
import com.github.drunlin.guokr.util.ViewUtils;
import com.google.common.base.Objects;
import com.google.common.io.ByteStreams;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import static com.github.drunlin.guokr.util.JavaUtil.call;

/**
 * 显示HTML格式的富文本控件。
 *
 * @author drunlin@outlook.com
 */
public class RichTextView extends View {
    private static final ExecutorService layoutThreadPool = Executors.newCachedThreadPool();

    private final Object LOCK = new Object();

    private final ExecutorService threadPool = Executors.newFixedThreadPool(3);

    private final TextPaint mTextPaint;

    private Size mSize;
    private Layout mLayout;
    private boolean mVisible;

    private String mHtml;

    public RichTextView(Context context) {
        this(context, null);
    }

    public RichTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressWarnings("ResourceType")
    public RichTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.density = getContext().getResources().getDisplayMetrics().density;

        final Resources.Theme theme = context.getTheme();

        TypedArray array;

        array = theme.obtainStyledAttributes(new int[] {android.R.attr.textAppearance});
        int id = array.getResourceId(0, -1);
        array.recycle();

        int[] arr = new int[] {
                android.R.attr.textSize,
                android.R.attr.textColor,
                android.R.attr.textColorLink
        };
        array = theme.obtainStyledAttributes(id, arr);
        mTextPaint.setColor(array.getColor(1, 0));
        mTextPaint.linkColor = array.getColor(2, 0);
        array.recycle();

        mTextPaint.setTextSize(ViewUtils.getDimension(getContext(), TypedValue.COMPLEX_UNIT_SP, 14));

        array = context.obtainStyledAttributes(attrs, new int[] {android.R.attr.text});
        if (array.hasValue(0)) {
            setHtml(array.getText(0).toString());
        }
        array.recycle();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return mLayout != null && super.dispatchTouchEvent(event);
    }

    /**
     * 处理点击链接的行为。
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();

        if (action != MotionEvent.ACTION_UP && action != MotionEvent.ACTION_DOWN) {
            return false;
        }

        final Spannable buffer = (Spannable) mLayout.getText();

        final int x = (int) event.getX() - getPaddingLeft() + getScrollX();
        final int y = (int) event.getY() - getPaddingTop() + getScrollY();

        final int line = mLayout.getLineForVertical(y);
        final int off = mLayout.getOffsetForHorizontal(line, x);

        ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);

        if (link.length != 0) {
            if (action == MotionEvent.ACTION_UP) {
                try {
                    URLSpan urlSpan = (URLSpan) link[0];
                    if (urlSpan != null) {
                        getContext().startActivity(Intents.openLinkInApp(urlSpan.getURL()));
                    } else {
                        link[0].onClick(this);
                    }
                } catch (ActivityNotFoundException e) {
                    link[0].onClick(this);
                }
            } else {
                Selection.setSelection(
                        buffer, buffer.getSpanStart(link[0]), buffer.getSpanEnd(link[0]));
            }
            return true;
        } else {
            Selection.removeSelection(buffer);
            return false;
        }
    }

    /**
     * 异步处理任务。
     * @param runnable
     */
    private void execute(Runnable runnable) {
        layoutThreadPool.execute(runnable);
    }

    /**
     * 检查是否为当前的HTML数据。
     * @param html
     * @return
     */
    private boolean isCurrentHtml(String html) {
        return Objects.equal(mHtml, html);
    }

    /**
     * 创建用于文本显示的布局。
     * @param text
     * @return
     */
    private Layout createLayout(CharSequence text) {
        return new StaticLayout(
                text, mTextPaint, getMeasuredWidth(), Layout.Alignment.ALIGN_NORMAL, 1F, 0, true);
    }

    /**
     * 创建用于文本显示的布局。
     * @param html
     * @return
     */
    private Layout createLayout(String html) {
        CharSequence text =
                Html.fromHtml(html, url -> new FutureDrawable(this, threadPool, url), null);
        return createLayout(text);
    }

    /**
     * 设置要显示的HTML文本。
     * @param text
     */
    public void setHtml(String text) {
        if (isInEditMode()) {
            mHtml = text;
            return;
        }

        if (isCurrentHtml(text)) {
            return;
        }

        synchronized (LOCK) {
            mHtml = text;
            mSize = null;
            mLayout = null;
        }

        if (mHtml != null && getMeasuredWidth() != 0) {
            execute(() -> calculateSize(mHtml));
        }

        requestLayout();
    }

    /**
     * 计算文本的显示大小。
     * @param html
     */
    private void calculateSize(final String html) {
        final Layout layout = createLayout(html);

        synchronized (LOCK) {
            if (isCurrentHtml(html)) {
                if (mVisible) {
                    mLayout = layout;
                }

                mSize = new Size(layout.getWidth(), layout.getHeight());

                LOCK.notify();
            }
        }
    }

    /**
     * 请求刷新布局，主要用于更新图片位置。
     */
    public void requestRefresh() {
        execute(this::relayout);
    }

    /**
     * 重新布局。
     */
    private void relayout() {
        String html;
        Layout layout;

        synchronized (LOCK) {
            html = mHtml;
            layout = mLayout;
        }

        if (layout == null) {
            return;
        }

        layout = createLayout(layout.getText());

        synchronized (LOCK) {
            if (isCurrentHtml(html)) {
                if (mVisible) {
                    mLayout = layout;
                }

                mSize = new Size(layout.getWidth(), layout.getHeight());

                if (getMeasuredWidth() != mSize.width || getMeasuredHeight() != mSize.height) {
                    post(this::requestLayout);
                } else {
                    postInvalidate();
                }
            }
        }
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);

        if (mHtml != null && mVisible != (visibility == VISIBLE)) {
            synchronized (LOCK) {
                if (mVisible) {
                    mLayout = null;
                } else if (mSize != null) {
                    layoutThreadPool.execute(this::updateLayout);
                }
                mVisible = !mVisible;
            }
        }
    }

    /**
     * 更新布局。
     */
    private void updateLayout() {
        final String html = mHtml;
        final Layout layout = createLayout(html);

        synchronized (LOCK) {
            if (mVisible && isCurrentHtml(html)) {
                mLayout = layout;

                postInvalidate();
            }
        }
    }

    /**
     * 计算文本的显示大小。
     */
    private void calculateSize() {
        mLayout = createLayout(mHtml);

        mSize = new Size(mLayout.getWidth(), mLayout.getHeight());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isInEditMode()) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            if (mHtml != null) {
                mLayout = createLayout(Html.fromHtml(mHtml));
                setMeasuredDimension(mLayout.getWidth(), mLayout.getHeight());
            }
            return;
        }

        if (mHtml == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else if (getMeasuredWidth() == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            calculateSize();

            setMeasuredDimension(mSize.width, mSize.height);
        } else {
            synchronized (LOCK) {
                if (mSize == null) {
                    try {
                        LOCK.wait();//等待大小计算完成
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            setMeasuredDimension(mSize.width, mSize.height);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mLayout != null) {
            mLayout.draw(canvas);
        }
    }

    //TODO 应该在无引用时取消未完成的加载
    private static class FutureDrawable extends ColorDrawable implements Drawable.Callback {
        private static DrawableCache drawableCache = new DrawableCache();
        private static DrawableSizeCache sizeCache = new DrawableSizeCache();

        private final Executor threadPool;

        private final Reference<RichTextView> containerReference;

        private Drawable currentDrawable = new ColorDrawable(0x55CCCCCC);

        public FutureDrawable(RichTextView container, Executor executor, String url) {
            containerReference = new WeakReference<>(container);

            threadPool = executor;

            call(sizeCache.get(url), size -> updateBounds(container, size.width, size.height));

            loadImage(url);
        }

        private void updateBounds(RichTextView container, int width, int height) {
            final int containerWidth = container.getMeasuredWidth();

            width = ViewUtils.getDimension(container.getContext(), COMPLEX_UNIT_DIP, width);
            height = ViewUtils.getDimension(container.getContext(), COMPLEX_UNIT_DIP, height);

            if (width > containerWidth) {
                int w = Math.min(containerWidth, width);
                height = height * w / width;
                width = w;

                setBounds(0, 0, width, height);
            } else if (width > containerWidth / 2) {
                setBounds(0, 0, containerWidth, height);
            } else {
                setBounds(0, 0, width, height);
            }

            currentDrawable.setBounds(0, 0, width, height);
        }

        private void loadImage(String url) {
            threadPool.execute(() -> call(drawableCache.get(url), data -> setData(url, data)));
        }

        private void setData(String url, byte[] data) {
            RichTextView container = containerReference.get();
            if (container == null) {
                return;
            }

            Drawable drawable = BitmapUtils.createDrawable(data);
            if (drawable == null) {
                return;
            }

            App.post(() -> setDrawable(container, drawable));

            sizeCache.put(url,
                    BitmapUtils.getOriginalWidth(drawable), BitmapUtils.getOriginalHeight(drawable));
        }

        private void setDrawable(RichTextView container, Drawable drawable) {
            currentDrawable = drawable;

            updateBounds(container,
                    BitmapUtils.getOriginalWidth(drawable), BitmapUtils.getOriginalHeight(drawable));

            drawable.setCallback(this);

            container.requestRefresh();
        }

        @Override
        public void draw(Canvas canvas) {
            currentDrawable.draw(canvas);
        }

        @Override
        public void invalidateDrawable(Drawable who) {
            call(containerReference.get(), View::invalidate);
        }

        @Override
        public void scheduleDrawable(Drawable who, Runnable what, long when) {}

        @Override
        public void unscheduleDrawable(Drawable who, Runnable what) {}
    }

    private static class DrawableCache extends LruCache<String, byte[]> {
        public DrawableCache() {
            super((int) Runtime.getRuntime().maxMemory() / 16);
        }

        @Override
        protected int sizeOf(String key, byte[] value) {
            return key.length() * 16 + value.length;
        }

        @Override
        protected byte[] create(String url) {
            try {
                URLConnection conn = new URL(url).openConnection();
                conn.setConnectTimeout(1_000);
                conn.setReadTimeout(10_000);
                return ByteStreams.toByteArray(conn.getInputStream());
            } catch (IOException | OutOfMemoryError e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * 图片大小信息的缓存容器。
     */
    private static class DrawableSizeCache extends LruCache<String, Size> {
        public DrawableSizeCache() {
            super(500 * 1024);
        }

        public void put(String key, int width, int height) {
            if (get(key) == null) {
                put(key, new Size(width, height));
            }
        }

        @Override
        protected int sizeOf(String key, Size value) {
            return key.length() * 16 + 2 * 32;
        }
    }

    /**
     * 图片大小信息。
     */
    private static class Size {
        public final int width;
        public final int height;

        public Size(int w, int h) {
            width = w;
            height = h;
        }
    }
}
