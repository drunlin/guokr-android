package com.github.drunlin.guokr;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.VisibleForTesting;

import com.github.drunlin.guokr.model.PreferenceModel;
import com.github.drunlin.guokr.module.AppModule;
import com.github.drunlin.guokr.module.tool.Injector;
import com.github.drunlin.guokr.module.tool.ViewLifecycle;
import com.github.drunlin.guokr.module.tool.ViewLifecycleImpl;
import com.github.drunlin.guokr.util.JavaUtil.Converter;

import java.util.LinkedList;

import static android.content.res.Configuration.UI_MODE_NIGHT_MASK;
import static android.content.res.Configuration.UI_MODE_NIGHT_YES;
import static android.support.v7.app.AppCompatDelegate.MODE_NIGHT_NO;
import static android.support.v7.app.AppCompatDelegate.MODE_NIGHT_YES;
import static android.support.v7.app.AppCompatDelegate.getDefaultNightMode;
import static android.support.v7.app.AppCompatDelegate.setDefaultNightMode;
import static com.github.drunlin.guokr.util.JavaUtil.call;

/**
 * @author drunlin@outlook.com
 */
public class App extends Application implements Application.ActivityLifecycleCallbacks {
    private static final Handler HANDLER = new Handler();

    @VisibleForTesting
    static Converter<Object, ViewLifecycle> viewLifecycleConverter = ViewLifecycleImpl::new;

    private static App instance;

    /**所有启动的Activity列表。*/
    private final LinkedList<Activity> activities = new LinkedList<>();

    /**当前正在显示的Activity。*/
    private Activity runningActivity;

    /**
     * 获取该类的实例。
     * @return
     */
    public static App getContext() {
        return instance;
    }

    /**
     * 获取依赖注入器。
     * @return
     */
    public static Injector getInjector() {
        return AppModule.injector;
    }

    /**
     * 创建ViewLifecycle对象。
     * @param view
     * @return
     */
    public static ViewLifecycle createLifecycle(Object view) {
        return viewLifecycleConverter.convert(view);
    }

    /**
     * @see Handler#post(Runnable)
     * @param r
     * @return
     */
    public static boolean post(Runnable r) {
        return HANDLER.post(r);
    }

    /**
     * @see Handler#postDelayed(Runnable, long)
     * @param r
     * @param delayMillis
     * @return
     */
    public static boolean postDelayed(Runnable r, long delayMillis) {
        return HANDLER.postDelayed(r, delayMillis);
    }

    public App() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        setTheme(getInjector().get(PreferenceModel.class).isNightMode());

        registerActivityLifecycleCallbacks(this);
    }

    /**
     * 设置全局的主题。
     * @param nightMode
     */
    public void setTheme(boolean nightMode) {
        int mode = getDefaultNightMode();
        if (nightMode ? mode != MODE_NIGHT_YES : mode == MODE_NIGHT_YES) {
            setDefaultNightMode(nightMode ? MODE_NIGHT_YES : MODE_NIGHT_NO);

            call(runningActivity, Activity::recreate);
        }
    }

    /**
     * 剩余的可用内存。
     * @return
     */
    public long freeMemory() {
        final Runtime runtime = Runtime.getRuntime();
        return runtime.maxMemory() - runtime.totalMemory() + runtime.freeMemory();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        activities.add(activity);

        releaseActivity();
    }

    /**
     * 在内存不足时回收旧的Activity来释放内存。
     */
    private void releaseActivity() {
        if (freeMemory() > 1024 * 1024) {
            postDelayed(this::releaseActivity, 1_000);
        } else if (activities.size() > 2)  {
            activities.remove(1).finish();

            postDelayed(this::releaseActivity, 16);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        runningActivity = activity;

        int mode = activity.getResources().getConfiguration().uiMode & UI_MODE_NIGHT_MASK;
        if (getDefaultNightMode() == MODE_NIGHT_YES
                ? (mode != UI_MODE_NIGHT_YES) : (mode == UI_MODE_NIGHT_YES)) {
            activity.recreate();
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (runningActivity == activity) {
            runningActivity = null;
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        activities.remove(activity);
    }

    @Override
    public void onActivityResumed(Activity activity) {}

    @Override
    public void onActivityPaused(Activity activity) {}

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}
}
