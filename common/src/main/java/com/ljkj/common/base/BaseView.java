package com.ljkj.common.base;

/**
 * view基类
 * @author zhangbiao
 */
public interface BaseView {
    //显示加载动画
    void showProgressDialog();
    //隐藏加载动画
    void dimissProgressDialog();
    //错误码信息
    void errorMessage(String msg);
    //显示无数据页面
    //显示网络错误页面
    //显示数据错误加载错误页面


}
