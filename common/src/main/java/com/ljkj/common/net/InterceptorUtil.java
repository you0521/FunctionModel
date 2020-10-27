package com.ljkj.common.net;

import android.text.TextUtils;


import com.ljkj.common.utils.LogUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class InterceptorUtil {
    //日志拦截器
    public static HttpLoggingInterceptor LogInterceptor() {
        return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                LogUtils.i("-------", message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY);//设置打印数据的级别
    }

    //网络拦截器
    public static Interceptor HeaderInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request mRequest = chain.request();
                Response response = chain.proceed(mRequest);
                //如果服务器不带请求头，添加请求头
                if (TextUtils.isEmpty(response.cacheControl().toString())) {
                    return response.newBuilder().addHeader("Cache-Control", "max-age = 60*60*24").build();
                }
                //返回response 对象
                return response;
            }
        };
    }
}