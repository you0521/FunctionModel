package com.ljkj.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Gson相关工具类
 * @author zhangbiao
 */
public class GsonUtils {
    private static final Gson GSON = createGson(true);

    private static final Gson GSON_NO_NULLS = createGson(false);

    private GsonUtils() {
        throw new UnsupportedOperationException("Do not need instantiate!");
    }

    /**
     * 获取 Gson 对象
     */
    public static Gson getGson() {
        return getGson(true);
    }

    /**
     * 获取 Gson 对象
     */
    public static Gson getGson(final boolean serializeNulls) {
        return serializeNulls ? GSON_NO_NULLS : GSON;
    }

    /**
     *  对象转 Json 串
     */
    public static String toJson(final Object object) {
        return toJson(object, true);
    }

    /**
     *  对象转 Json 串
     */
    public static String toJson(final Object object, final boolean includeNulls) {
        return includeNulls ? GSON.toJson(object) : GSON_NO_NULLS.toJson(object);
    }

    /**
     *  对象转 Json 串
     */
    public static String toJson(final Object src, final Type typeOfSrc) {
        return toJson(src, typeOfSrc, true);
    }

    /**
     *  对象转 Json 串
     */
    public static String toJson(final Object src, final Type typeOfSrc, final boolean includeNulls) {
        return includeNulls ? GSON.toJson(src, typeOfSrc) : GSON_NO_NULLS.toJson(src, typeOfSrc);
    }

    /**
     * Json 串转对象
     */
    public static <T> T fromJson(final String json, final Class<T> type) {
        return GSON.fromJson(json, type);
    }

    /**
     * Json 串转对象
     */
    public static <T> T fromJson(final String json, final Type type) {
        return GSON.fromJson(json, type);
    }

    /**
     * Json 串转对象
     */
    public static <T> T fromJson(final Reader reader, final Class<T> type) {
        return GSON.fromJson(reader, type);
    }

    /**
     * Json 串转对象
     */
    public static <T> T fromJson(final Reader reader, final Type type) {
        return GSON.fromJson(reader, type);
    }
    /**
     *  获取链表类型
     */
    public static Type getListType(final Type type) {
        return TypeToken.getParameterized(List.class, type).getType();
    }
    /**
     *  获取集合类型
     */

    public static Type getSetType(final Type type) {
        return TypeToken.getParameterized(Set.class, type).getType();
    }

    /**
     * 获取字典类型
     */
    public static Type getMapType(final Type keyType, final Type valueType) {
        return TypeToken.getParameterized(Map.class, keyType, valueType).getType();
    }

    /**
     * 获取数组类型
     */
    public static Type getArrayType(final Type type) {
        return TypeToken.getArray(type).getType();
    }

    /**
     *  获取类型
     */
    public static Type getType(final Type rawType, final Type... typeArguments) {
        return TypeToken.getParameterized(rawType, typeArguments).getType();
    }

    /**
     * 创建Gson对象
     */
    private static Gson createGson(final boolean serializeNulls) {
        final GsonBuilder builder = new GsonBuilder();
        if (serializeNulls) builder.serializeNulls();
        return builder.create();
    }
}
