package com.ljkj.common.base;

/**
 * Presenter基类
 * @author zhangbiao
 */
public interface BasePresenter<T>{
    //绑定View
    void attachView(T t);
    //销毁View
    void detachView();
}
