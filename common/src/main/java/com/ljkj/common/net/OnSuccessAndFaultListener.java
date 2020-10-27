package com.ljkj.common.net;

public interface OnSuccessAndFaultListener<T> {
    //成功监听
    void onSuccees(BaseEntity<T> t);
    //成功错误码错误的监听
    void onCodeError(BaseEntity<T> t);
}
