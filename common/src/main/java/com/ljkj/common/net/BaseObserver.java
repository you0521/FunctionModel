package com.ljkj.common.net;

import android.util.Log;


import com.ljkj.common.base.BaseView;
import com.ljkj.common.utils.LogUtils;
import com.ljkj.common.utils.ToastUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * BaseObserver 基类
 * @param <T>
 */
public class BaseObserver<T> implements Observer<BaseEntity<T>> {
    private OnSuccessAndFaultListener mOnSuccessAndFaultListener;
    private BaseView baseView;

    public BaseObserver(BaseView baseView, OnSuccessAndFaultListener mOnSuccessAndFaultListener) {
        this.mOnSuccessAndFaultListener = mOnSuccessAndFaultListener;
        this.baseView = baseView;
    }

    @Override
    public void onSubscribe(Disposable d) {
        baseView.showProgressDialog();
    }

    @Override
    public void onNext(BaseEntity<T> tBaseEntity) {
        baseView.dimissProgressDialog();
        if (tBaseEntity.getCode() == 401) {
           // baseView.error401();
        }
        if (tBaseEntity.isSuccess())
            mOnSuccessAndFaultListener.onSuccees(tBaseEntity);
        else
            mOnSuccessAndFaultListener.onCodeError(tBaseEntity);

    }

    @Override
    public void onError(Throwable e) {
        baseView.dimissProgressDialog();
        try {

            if (e instanceof SocketTimeoutException) {//请求超时
                baseView.errorMessage("请求超时");
                //baseView.errorMessage("请求超时");
            } else if (e instanceof ConnectException) {//网络连接超时
                //mOnSuccessAndFaultListener.onFailure("网络连接超时");
                // baseView.errorMessage("网络连接超时");
                baseView.errorMessage("网络连接超时");
            } else if (e instanceof SSLHandshakeException) {//安全证书异常
                //mOnSuccessAndFaultListener.onFailure("安全证书异常");
                baseView.errorMessage("安全证书异常");
            } else if (e instanceof HttpException) {//请求的地址不存在
                int code = ((HttpException) e).code();
                if (code == 504) {
                    //baseView.errorMessage("网络异常，请检查您的网络状态");
                    baseView.errorMessage("网络异常，请检查您的网络状态");
                    //mOnSuccessAndFaultListener.onFailure("网络异常，请检查您的网络状态");
                } else if (code == 404) {
                    baseView.errorMessage("请求的地址不存在");
                    // mOnSuccessAndFaultListener.onFailure("请求的地址不存在");
                } else {
                    baseView.errorMessage("请求失败");
                    //mOnSuccessAndFaultListener.onFailure("请求失败");
                }
            } else if (e instanceof UnknownHostException) {//域名解析失败
                baseView.errorMessage("域名解析失败");
                // mOnSuccessAndFaultListener.onFailure("域名解析失败");
            } else {
                ToastUtils.show(e.getMessage());
                //mOnSuccessAndFaultListener.onFailure("error:" + e.getMessage());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        } finally {
            LogUtils.e("OnSuccessAndFaultSub", "error:" + e.getMessage());

        }
    }

    @Override
    public void onComplete() {

    }
}
