package com.ljkj.common.net;

import com.ljkj.common.utils.ActivityUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 创建Api请求工具类
 */
public class ApiUtils {
    private static ApiUtils apiUtils;
    private OkHttpClient okHttpClient;
    private Retrofit mRetrofit;
    private final int DEFAULT_TIME_OUT = 50;

    /**
     * 构造函数
     */
    public ApiUtils() {
        okHttpClient = getOkHttpClient();
        mRetrofit = getRetrofit(okHttpClient);
    }

    /**
     * okHttpClient的配置
     *
     * @return
     */
    private OkHttpClient getOkHttpClient() {
        //缓存文件夹
        File cacheFile = new File(ActivityUtils.getApp().getExternalCacheDir().toString(), "cache");
        //缓存大小为10M
        int cacheSize = 10 * 1024 * 1024;
        //创建一个缓存对象
        Cache cache = new Cache(cacheFile, cacheSize);
        //创建okHttpClicent对象
        //SSLSocketFactory sslSocketFactory = new SslContextFactory().getSslSocket().getSocketFactory();
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)//连接超时时间
                .readTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)//写操作超时时间
                .writeTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS) //读操作超时时间
                .addNetworkInterceptor(InterceptorUtil.HeaderInterceptor())//网络拦截器
                .addInterceptor(InterceptorUtil.LogInterceptor())//日志拦截器
                .retryOnConnectionFailure(true)//断线重连
                .cache(cache);//添加缓存信息
        return builder.build();
    }

    //设置配置Retrofit
    private Retrofit getRetrofit(OkHttpClient okHttpClient) {

        return new Retrofit.Builder()
                .baseUrl("")//创建一个url
                .client(okHttpClient)//配置okhttpClient
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//对rxJava2进行支持
                .addConverterFactory(GsonConverterFactory.create())//对gson进行支持
                .build();
    }

    //创建单例模式得到对象
    public static ApiUtils getInstance() {

        if (apiUtils == null) {
            synchronized (ApiUtils.class) {
                if (apiUtils == null){
                    apiUtils = new ApiUtils();
                }
            }
        }
            //返回对象
            return apiUtils;
    }

    //通过java反射获取对象
    public <T> T getService(Class<T> tClass) {
        return mRetrofit.create(tClass);
    }
    //获取业务
    public ApiService getApiService(){
        return getService(ApiService.class);
    }
}

