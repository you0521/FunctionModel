package com.ljkj.common.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

/**
 * Glide图片加载工具类
 *
 * @author zhangbiao
 */
public class ImageLoaderManager {

    /**
     * 默认加载方式 网页
     *
     * @param context   上下文对象
     * @param url       加载Url
     * @param imageView 展示View控件
     */
    public static void loadImage(Context context, String url, ImageView imageView) {
        //根据自己的项目需求去搭建
        // .skipMemoryCache(true);  关闭内存缓存
        //DiskCacheStrategy.NONE 磁盘不缓存
        //Glide.get(this).clearMemory();清除所有内存缓存
        //Glide.get(this).clearDiskCache(); 清除所有磁盘缓存
        //Glide.with(this).clear(imageView); 清除单个缓存
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()//图片按比例缩放
                .priority(Priority.HIGH)//加载优先级
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存策略既缓存原始图片，又缓存转化后的图片
                .dontAnimate();//不显示动画效果

        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())//动画渐变
                .into(imageView);
    }

    /**
     * 带有占位图片 错误图片
     *
     * @param context         上下文
     * @param url             加载Url
     * @param preloadingImage 占位图
     * @param errorImage      加载错误动画
     * @param imageView       展示View
     */
    public static void loadImage(Context context, String url, int preloadingImage, int errorImage, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(preloadingImage)//预加载图片
                .error(errorImage)//加载失败显示图片
                .priority(Priority.HIGH)//优先级
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存策略
                .dontAnimate();
        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())//动画渐变
                .into(imageView);

    }

    /**
     * 加载本地图片文件
     *
     * @param context   上下文
     * @param file      file文件
     * @param imageView 展示View
     */
    public static void loadFileImage(Context context, File file, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .skipMemoryCache(true)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .dontAnimate();

        Glide.with(context)
                .load(file)
                .apply(requestOptions)
                .into(imageView);
    }

    /**
     * 加载本地图片  带有占位图  带有错误图片
     *
     * @param context         上下文
     * @param file            file文件
     * @param preloadingImage 展位图
     * @param errorImage      错误图片
     * @param imageView       展示view
     */
    public static void loadFileImage(Context context, File file, int preloadingImage, int errorImage, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .skipMemoryCache(true)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(preloadingImage)
                .error(errorImage)
                .dontAnimate();
        Glide.with(context)
                .load(file)
                .apply(requestOptions)
                .into(imageView);

    }

    /**
     * 本地资源图片加载
     *
     * @param context    上下文
     * @param resourceId 资源文件
     * @param imageView  展示view
     */
    public static void loadResourceImage(Context context, int resourceId, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
                .priority(Priority.HIGH)
                .centerCrop()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE);

        Glide.with(context)
                .load(resourceId)
                .apply(requestOptions)
                .into(imageView);
    }

    /**
     * 本地资源文件加载  带有占位图  带有错误图片
     *
     * @param context         上下文
     * @param resourceId      资源文件
     * @param preloadingImage 占位图片
     * @param errorImage      错误的图片
     * @param imageView       展示view
     */
    public static void loadResourceImage(Context context, int resourceId, int preloadingImage, int errorImage, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .skipMemoryCache(true)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(preloadingImage)
                .error(errorImage)
                .dontAnimate();
        Glide.with(context)
                .load(resourceId)
                .apply(requestOptions)
                .into(imageView);
    }

    /**
     * 加载圆形图片
     *
     * @param context   上下文
     * @param url       图片地址
     * @param imageView 展示view
     */
    public static void loadCircleImage(Context context, String url, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
                .priority(Priority.HIGH)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CircleCrop());

        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

    /**
     * 加载圆角图片
     *
     * @param context   上下文
     * @param url       图片网址
     * @param imageView 展示view
     * @param radius    圆角大小
     */
    public static void loadRoundImage(Context context, String url, ImageView imageView, int radius) {
        RequestOptions requestOptions = new RequestOptions()
                .priority(Priority.HIGH)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transforms(new CenterCrop(), new RoundedCorners(radius));

        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

    /**
     * 加载图片指定大小
     *
     * @param context   上下文
     * @param url       图片地址
     * @param imageView 展示view
     * @param width     图片宽度
     * @param height    图片高度
     */
    public static void loadSizeImage(Context context, String url, ImageView imageView, int width, int height) {
        RequestOptions requestOptions = new RequestOptions()
                .priority(Priority.HIGH)
                .override(width, height)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

    /**
     * 加载高斯模糊
     *
     * @param context   上下文
     * @param url       加载地址
     * @param imageView 展示view
     * @param radius    模糊级数 最大25
     */
    public static void loadBlurImage(Context context, String url, ImageView imageView, int radius) {
        RequestOptions requestOptions = new RequestOptions()
                .override(300)
                .transforms(new BlurTransformation(radius));

        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

    /**
     * 加载gif图
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadGifImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .into(imageView);
    }

}
