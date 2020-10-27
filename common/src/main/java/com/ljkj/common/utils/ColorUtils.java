package com.ljkj.common.utils;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

/**
 * 颜色相关工具类
 *
 * @author zhangbiao
 */
public class ColorUtils {
    /**
     * 不需要实例化本类
     */
    private ColorUtils() {
        throw new UnsupportedOperationException("Do not need instantiate!");
    }

    /**
     * 获取颜色
     *
     * @param id 资源id
     * @return 返回颜色
     */
    public static int getColor(@ColorRes int id) {
        return ContextCompat.getColor(ActivityUtils.getApp(), id);
    }

    /**
     * 设置颜色的透明度
     *
     * @param color 颜色资源id
     * @param alpha 透明度
     * @return 返回带有透明度的色值
     */
    public static int setAlphaComponent(@ColorInt final int color,
                                        @IntRange(from = 0x0, to = 0xFF) int alpha) {
        return (color & 0x00ffffff) | (alpha << 24);
    }


    /**
     * 设置颜色的透明度 （0-1）
     *
     * @param color 颜色资源id
     * @param alpha 透明度
     * @return 返回带有透明度的色值
     */
    public static int setAlphaComponent(@ColorInt int color,
                                        @FloatRange(from = 0, to = 1) float alpha) {
        return (color & 0x00ffffff) | ((int) (alpha * 255.0f + 0.5f) << 24);
    }

    /**
     * 颜色串转颜色值
     *
     * @param colorString 颜色值（比如：#000000）
     * @return
     */
    public static int string2Int(@NonNull String colorString) {
        return Color.parseColor(colorString);
    }

    /**
     * 颜色值转 RGB 串
     *
     * @param colorInt
     * @return
     */
    public static String int2RgbString(@ColorInt int colorInt) {
        colorInt = colorInt & 0x00ffffff;
        String color = Integer.toHexString(colorInt);
        while (color.length() < 6) {
            color = "0" + color;
        }
        return "#" + color;
    }

    /**
     * 颜色值转 ARGB 串
     *
     * @param colorInt
     * @return
     */
    public static String int2ArgbString(@ColorInt final int colorInt) {
        String color = Integer.toHexString(colorInt);
        while (color.length() < 6) {
            color = "0" + color;
        }
        while (color.length() < 8) {
            color = "f" + color;
        }
        return "#" + color;
    }

    /**
     * 获取随机的颜色
     *
     * @return
     */
    public static int getRandomColor() {
        return getRandomColor(true);
    }

    /**
     * 获取随机的颜色
     *
     * @param supportAlpha
     * @return
     */
    public static int getRandomColor(final boolean supportAlpha) {
        int high = supportAlpha ? (int) (Math.random() * 0x100) << 24 : 0xFF000000;
        return high | (int) (Math.random() * 0x1000000);
    }
}
