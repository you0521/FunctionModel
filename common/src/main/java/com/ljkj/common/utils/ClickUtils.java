package com.ljkj.common.utils;


import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.view.View;

import com.ljkj.common.R;

/**
 * 点击相关工具类
 *
 * @author 张标
 */
public class ClickUtils {

    private static long lastClickTime = 0;//上次点击的时间
    private static int spaceTime = 1000;//时间间隔
    private final static long INTERNAL_TIME = 1000;

    /**
     * 快速点击
     *
     * @return
     */
    public static boolean isFastClick() {

        long currentTime = System.currentTimeMillis();//当前系统时间
        boolean isAllowClick;//是否允许点击

        if (currentTime - lastClickTime > spaceTime) {

            isAllowClick = false;

        } else {
            isAllowClick = true;

        }

        lastClickTime = currentTime;
        return isAllowClick;
    }


    /**
     * 点击事件防抖
     * 点击事件是否无效
     */
    public static boolean isInvalidClick(@NonNull View target) {
        return isInvalidClick(target, INTERNAL_TIME);
    }

    /**
     * 点击事件防抖
     *
     * @param target
     * @param internalTime
     * @return
     */
    public static boolean isInvalidClick(@NonNull View target, @IntRange(from = 0) long internalTime) {
        long curTimeStamp = System.currentTimeMillis();
        long lastClickTimeStamp = 0;
        Object o = target.getTag(R.id.last_click_time);
        if (o == null) {
            target.setTag(R.id.last_click_time, curTimeStamp);
            return false;
        }
        lastClickTimeStamp = (Long) o;
        boolean isInvalid = curTimeStamp - lastClickTimeStamp < internalTime;
        if (!isInvalid)
            target.setTag(R.id.last_click_time, curTimeStamp);
        return isInvalid;
    }
}
