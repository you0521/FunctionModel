package com.ljkj.demo;

import android.support.multidex.MultiDexApplication;
import com.ljkj.common.utils.ActivityUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MyApp extends MultiDexApplication {
    public static MyApp myApp;

    public static MyApp getInstance() {
        return myApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApp = this;
        ActivityUtils.initialize(this);

        closeAndroidPDialog();

    }

    /**
     * 解决Android9.0 文明上网
     */
    private void closeAndroidPDialog() {
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
