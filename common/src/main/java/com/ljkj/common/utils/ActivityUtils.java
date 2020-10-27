package com.ljkj.common.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;

import butterknife.internal.Utils;

/**
 * 管理Activity
 *
 * @author zhangbiao
 */
public class ActivityUtils {
    private static final Handler UTIL_HANDLER = new Handler(Looper.getMainLooper());
    @SuppressLint("StaticFieldLeak")
    private static Application mApplication;
    /* 当前处于栈顶的 Activity */
    private static WeakReference<Activity> mTopActivityWeakRef;
    /* 存储所有存活的 Activity */
    private static List<Activity> mActivityList = new LinkedList<Activity>();
    /* Activity 的生命周期 */
    private static Application.ActivityLifecycleCallbacks mLifecycleCallback = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            // activity 被创建时，向 Activity 列表中添加 activity
            mActivityList.add(activity);
            setTopActivityWeakRef(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            // activity 被销毁时，从 Activity 列表中移除 activity
            mActivityList.remove(activity);
        }
    };

    private ActivityUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 初始化工具类
     *
     * @param app 应用对象
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void initialize(@NonNull final Application app) {
        mApplication = app;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (mActivityList == null) mActivityList = new LinkedList<Activity>();
            app.registerActivityLifecycleCallbacks(mLifecycleCallback);
        }
    }

    /**
     * 获取应用对象
     *
     * @return Application
     */
    public static Application getApp() {
        if (mApplication != null) {
            return mApplication;
        }
        throw new NullPointerException("you should initialize first");
    }

    /**
     * 设置栈顶的 Activity
     *
     * @param activity
     */
    private static void setTopActivityWeakRef(Activity activity) {
        if (mTopActivityWeakRef == null || mTopActivityWeakRef.get() == null || !mTopActivityWeakRef.get().equals(activity)) {
            mTopActivityWeakRef = new WeakReference<>(activity);
        }
    }

    /**
     * 获取栈顶的 Activity
     *
     * @return Activity
     */
    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static Activity getTopActivity() {
        if (mTopActivityWeakRef != null && mTopActivityWeakRef.get() != null) {
            return mTopActivityWeakRef.get();
        } else if (mActivityList != null && mActivityList.size() > 0) {
            return mActivityList.get(mActivityList.size() - 1);
        }
        return null;
    }

    /**
     * 获取 Activity 列表
     *
     * @return List<Activity>
     */
    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static List<Activity> getActivityList() {
        return mActivityList;
    }

    /**
     * 移除指定的 Activity
     *
     * @param activity
     */
    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void removeActivity(Activity activity) {
        if (mActivityList != null) {
            if (activity != null && !activity.isFinishing()) {
                activity.finish();
            }
            mActivityList.remove(activity);
        }
    }

    //这是删除所有Activity的
    public static void removeAllActivity() {
        List<Activity> activityList = getActivityList();
        for (Activity activity : activityList) {
            activity.finish();
        }
        mActivityList.removeAll(activityList);
    }

    /**
     * 移除指定的 Activity
     *
     * @param cls
     */
    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void removeActivity(Class<?> cls) {
        if (mActivityList != null) {
            for (int i = 0, size = mActivityList.size(); i < size; i++) {
                Activity activity = mActivityList.get(i);
                if (activity.getClass().getName().equals(cls.getName())) {
                    if (!activity.isFinishing()) {
                        activity.finish();
                    }
                    mActivityList.remove(i);
                    break;
                }
            }
        }
    }

    /**
     * 键盘造成的内存泄漏解决
     * @param window
     */
    public static void fixSoftInputLeaks(final Window window) {
        InputMethodManager imm =
                (InputMethodManager) ActivityUtils.getApp().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        String[] leakViews = new String[]{"mLastSrvView", "mCurRootView", "mServedView", "mNextServedView"};
        for (String leakView : leakViews) {
            try {
                Field leakViewField = InputMethodManager.class.getDeclaredField(leakView);
                if (leakViewField == null) continue;
                if (!leakViewField.isAccessible()) {
                    leakViewField.setAccessible(true);
                }
                Object obj = leakViewField.get(imm);
                if (!(obj instanceof View)) continue;
                View view = (View) obj;
                if (view.getRootView() == window.getDecorView().getRootView()) {
                    leakViewField.set(imm, null);
                }
            } catch (Throwable ignore) {/**/}
        }
    }
    static <T> Task<T> doAsync(final Task<T> task) {
        Executors.newFixedThreadPool(3).execute(task);
        return task;
    }
    /**
     * 子线程更新ui
     * @param runnable
     */
    public static void runOnUiThread(final Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        } else {
            UTIL_HANDLER.post(runnable);
        }
    }

    /**
     * 设置时间更新参数
     * @param runnable
     * @param delayMillis
     */
    public static void runOnUiThreadDelayed(final Runnable runnable, long delayMillis) {
        UTIL_HANDLER.postDelayed(runnable, delayMillis);
    }
    public abstract static class Task<Result> implements Runnable {

        private static final int NEW         = 0;
        private static final int COMPLETING  = 1;
        private static final int CANCELLED   = 2;
        private static final int EXCEPTIONAL = 3;

        private volatile int state = NEW;

        abstract Result doInBackground();

        private Callback<Result> mCallback;

        public Task(final Callback<Result> callback) {
            mCallback = callback;
        }

        @Override
        public void run() {
            try {
                final Result t = doInBackground();

                if (state != NEW) return;
                state = COMPLETING;
                UTIL_HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        mCallback.onCall(t);
                    }
                });
            } catch (Throwable th) {
                if (state != NEW) return;
                state = EXCEPTIONAL;
            }
        }

        public void cancel() {
            state = CANCELLED;
        }

        public boolean isDone() {
            return state != NEW;
        }

        public boolean isCanceled() {
            return state == CANCELLED;
        }
    }

    public interface Callback<T> {
        void onCall(T data);
    }
}
