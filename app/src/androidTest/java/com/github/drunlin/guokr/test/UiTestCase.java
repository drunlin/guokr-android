package com.github.drunlin.guokr.test;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import com.github.drunlin.guokr.test.util.Value;

import org.junit.Rule;
import org.junit.Test;

import static android.support.v7.app.AppCompatDelegate.MODE_NIGHT_NO;
import static android.support.v7.app.AppCompatDelegate.MODE_NIGHT_YES;
import static android.support.v7.app.AppCompatDelegate.getDefaultNightMode;
import static android.support.v7.app.AppCompatDelegate.setDefaultNightMode;

/**
 * UI测试的基类。
 *
 * @author drunlin@outlook.com
 */
public abstract class UiTestCase<T extends Activity> extends AsyncTestCase {
    /**
     * 一个Receiver的Action，用通知暂停的测试线程继续运行。
     * 通常是执行命令：adb shell am broadcast -a com.github.drunlin.ACTION_CONTINUE
     */
    public static final String ACTION_CONTINUE = "com.github.drunlin.ACTION_CONTINUE";

    /**全局的预览操作的开关。*/
    public static boolean GLOBE_PREVIEW_ENABLE = true;

    @Rule
    public ActivityTestRule<? extends T> activityTestRule;

    protected T activity;

    /**预览操作的开关。*/
    private boolean previewEnable = true;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            countDown();
        }
    };

    protected UiTestCase() {}

    public UiTestCase(Class<? extends T> activityClass) {
        onCreateTestRule(activityClass);
    }

    protected void onCreateTestRule(Class<? extends T> activityClass) {
        activityTestRule = new ActivityTestRule<>(activityClass);
    }

    @Override
    public void setUp() throws Throwable {
        activity = activityTestRule.getActivity();

        super.setUp();

        runOnUiThread(this::initOnUiThread);

        activity.registerReceiver(receiver, new IntentFilter(ACTION_CONTINUE));
    }

    protected void initOnUiThread() throws Throwable {}

    @Override
    public void tearDown() throws Throwable {
        activity.unregisterReceiver(receiver);

        super.tearDown();
    }

    /**
     * 暂停测试线程，预览显示效果。
     * @throws InterruptedException
     */
    protected void pauseToPreview() throws InterruptedException {
        if (GLOBE_PREVIEW_ENABLE && previewEnable) {
            await(1, Long.MAX_VALUE);
        }
    }

    /**
     * 暂停测试线程进行UI预览。
     * @throws Throwable
     */
    private void preview() throws Throwable {
        boolean preview = previewEnable;
        previewEnable = true;

        onPreview();
        pauseToPreview();

        previewEnable = preview;

        setDefaultNightMode(
                getDefaultNightMode() == MODE_NIGHT_YES ? MODE_NIGHT_NO : MODE_NIGHT_YES);
    }

    /**
     * 在预览中会调用的方法。如果需要预览操作，则重写该方法，并且不调用super.onPreview()。
     * @throws Throwable
     */
    protected void onPreview() throws Throwable {
        previewEnable = false;
    }

    @Test
    public void preview1() throws Throwable {
        preview();
    }

    @Test
    public void preview2() throws Throwable {
        preview();
    }

    /**
     * 等待主线程进入空闲状态。
     */
    protected void waitForIdleSync() {
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
    }

//    /**
//     * 用延迟的方式等待操作完成。
//     */
//    protected void waitForComplete() {
//        await(1, 500);
//    }

    /**
     * 在UI线程中运行。
     * @param callable
     * @throws Throwable
     */
    protected void runOnUiThread(final Callable callable) throws Throwable {
        final Value<Throwable> error = new Value<>();
        activityTestRule.runOnUiThread(() -> {
            try {
                callable.call();
            } catch (Throwable throwable) {
                //noinspection ThrowableResultOfMethodCallIgnored
                error.setValue(throwable);
            }
        });
        if (error.getValue() != null) {
            throw error.getValue();
        }
    }

    /**
     * 能够抛出异常的调用。
     */
    protected interface Callable {
        void call() throws Throwable;
    }
}
