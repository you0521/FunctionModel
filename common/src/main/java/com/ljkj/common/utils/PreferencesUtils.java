package com.ljkj.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.SharedPreferencesCompat;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;

/**
 * SharedPreferences 的工具类
 * @author zhangbiao
 */
public class PreferencesUtils {
    /**
     * Sp 的文件名   根据自己的项目需要进行更改名字
     */
    public static String FILLNAME = "demo";

    /**
     * 存入某个 key 对应的 value 值
     *
     * @param key       要存入的键
     * @param value     存入键对应的值
     */
    public static void put(String key, Object value) {
        SharedPreferences sp = ActivityUtils.getApp().getSharedPreferences(FILLNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        if (value instanceof String) {
            edit.putString(key, (String) value);
        } else if (value instanceof Integer) {
            edit.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            edit.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            edit.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            edit.putLong(key, (Long) value);
        }
        SharedPreferencesCompat.EditorCompat.getInstance().apply(edit);
    }

    /**
     * 得到某个 key 对应的值
     *
     * @param key       要取数据的键
     * @param defValue     没有读取到数据情况下  设置的默认值
     * @return  null
     */
    public static Object get(String key, Object defValue) {
        SharedPreferences sp = ActivityUtils.getApp().getSharedPreferences(FILLNAME, Context.MODE_PRIVATE);
        if (defValue instanceof String) {
            return sp.getString(key, (String) defValue);
        } else if (defValue instanceof Integer) {
            return sp.getInt(key, (Integer) defValue);
        } else if (defValue instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defValue);
        } else if (defValue instanceof Float) {
            return sp.getFloat(key, (Float) defValue);
        } else if (defValue instanceof Long) {
            return sp.getLong(key, (Long) defValue);
        }
        return null;
    }

    /**
     * 返回所有数据
     *
     * @return  所有数据的 Map 集合
     */
    public static Map<String, ?> getAll() {
        SharedPreferences sp = ActivityUtils.getApp().getSharedPreferences(FILLNAME, Context.MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * 移除某个 key 值已经对应的值
     *
     * @param key   要移除数据对应的键
     */
    public static void remove(String key) {
        SharedPreferences sp = ActivityUtils.getApp().getSharedPreferences(FILLNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.remove(key);
        SharedPreferencesCompat.EditorCompat.getInstance().apply(edit);
    }

    /**
     * 清除所有内容
     *
     */
    public static void clear() {
        SharedPreferences sp = ActivityUtils.getApp().getSharedPreferences(FILLNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.clear();
        SharedPreferencesCompat.EditorCompat.getInstance().apply(edit);
    }





    /**
     * 存入字符串
     *
     * @param context 上下文
     * @param key     字符串的键
     * @param value   字符串的值
     */
    public static void putString(Context context, String key, String value) {
        SharedPreferences sp = ActivityUtils.getApp().getSharedPreferences(FILLNAME, Context.MODE_PRIVATE);
        //存入数据
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 获取字符串
     *
     * @param context 上下文
     * @param key     字符串的键
     * @return 得到的字符串
     */
    public static String getString(Context context, String key) {
        SharedPreferences sp =ActivityUtils.getApp().getSharedPreferences(FILLNAME, Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }
    public static void removeString(String key){
        SharedPreferences sp = ActivityUtils.getApp().getSharedPreferences(FILLNAME, Context.MODE_PRIVATE);
        sp.edit().remove(key).commit();

    }
    /**
     * 存储Map集合
     * @param key 键
     * @param map 存储的集合
     * @param <K> 指定Map的键
     * @param <V> 指定Map的值
     */
    public static <K extends Serializable, V extends Serializable> void putMap(String key, Map<K, V> map)
    {
        try {
            put(ActivityUtils.getApp(), key, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <K extends Serializable, V extends Serializable> Map<K, V> getMap(String key)
    {
        try {
            return (Map<K, V>) get(ActivityUtils.getApp(), key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**存储对象*/
    private static void put(Context context, String key, Object obj)
            throws IOException
    {
        if (obj == null) {//判断对象是否为空
            return;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos  = null;
        oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        // 将对象放到OutputStream中
        // 将对象转换成byte数组，并将其进行base64编码
        String objectStr = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
        baos.close();
        oos.close();

        putString(context, key, objectStr);
    }

    /**获取对象*/
    private static Object get(Context context, String key)
            throws IOException, ClassNotFoundException
    {
        String wordBase64 = getString(context, key);
        // 将base64格式字符串还原成byte数组
        if (TextUtils.isEmpty(wordBase64)) { //不可少，否则在下面会报java.io.StreamCorruptedException
            return null;
        }
        byte[]  objBytes = Base64.decode(wordBase64.getBytes(), Base64.DEFAULT);
        ByteArrayInputStream bais     = new ByteArrayInputStream(objBytes);
        ObjectInputStream ois      = new ObjectInputStream(bais);
        // 将byte数组转换成product对象
        Object obj = ois.readObject();
        bais.close();
        ois.close();
        return obj;
    }
}
