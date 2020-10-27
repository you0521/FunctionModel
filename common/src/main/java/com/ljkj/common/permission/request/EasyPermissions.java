package com.ljkj.common.permission.request;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.SparseArray;

import com.ljkj.common.permission.bean.PermissionInfo;
import com.ljkj.common.permission.call.OnRequestCallBack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by HJQ on 2017-5-9.
 *
 * 通过回调接口请求权限
 */

public final class EasyPermissions {
    /**
     * 不能被实例化
     */
    private EasyPermissions() {}

    /**
     * 请求权限，不需要指定请求码，请求结果通过回调接口方式实现
     *
     * @param activity          Activity对象
     * @param call              用于接收结果的回调
     * @param permissions       需要请求的权限组，可变参数类型
     */
    public static void request(Activity activity, OnRequestCallBack call, String... permissions) {
        request((Object) activity, call, permissions);
    }

/*    *//**
     * 请求权限，不需要指定请求码，请求结果通过回调接口方式实现
     *
     * @param fragment          Fragment对象
     * @param callBack              用于接收结果的回调
     * @param permissions       需要请求的权限组，可变参数类型
     *//*
    public static void request(Fragment fragment,OnRequestCallBack callBack,String... permissions){
        request((Object)fragment,callBack,permissions);

    }*/


    /**
     * 请求权限，不需要指定请求码，请求结果通过回调接口方式实现
     *
     * @param fragment          v4包下的Fragment对象
     * @param call              用于接收结果的回调
     * @param permissions       需要请求的权限组，可变参数类型
     */
    public static void request(Fragment fragment, OnRequestCallBack call, String... permissions) {
        request((Object) fragment, call, permissions);
    }

    private final static SparseArray mContainer = new SparseArray<>();

    private static long requestTime;

    public static void request(Object object, OnRequestCallBack call, String... permissions) {

        PermissionUtils.checkObject(object);

        PermissionUtils.isEmptyPermissions(permissions);

        if (call == null) throw new NullPointerException("权限请求回调接口必须要实现");

        int requestCode;

        //请求码随机生成，避免随机产生之前的请求码，必须进行循环判断
        do {
            //requestCode = new Random().nextInt(65535);//Studio编译的APK请求码必须小于65536
            requestCode = new Random().nextInt(255);//Eclipse编译的APK请求码必须小于256
        } while (mContainer.get(requestCode) != null);

        if (PermissionUtils.getFailPermissions(PermissionUtils.getActivity(object), permissions).length == 0) {
            //证明权限已经全部授予过
            call.hasPermission(PermissionUtils.arrayConversion(permissions, true, false, false));
        } else {
            //将当前的请求码和对象添加到集合中
            mContainer.put(requestCode, call);
            //将Activity或Fragment对象保存到集合中
            mContainer.put(requestCode + 1, object);
            //记录本次请求时间
            requestTime = System.currentTimeMillis();
            //申请没有授予过的权限
            PermissionUtils.requestPermissions(object, permissions, requestCode);
        }
    }

    /**
     * 在Activity或Fragment中的同名同参方法调用此方法
     */
    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        //根据请求码取出的对象为空，就直接返回不处理
        if (mContainer.get(requestCode) == null || mContainer.get(requestCode + 1) == null) return;

        OnRequestCallBack call = (OnRequestCallBack) mContainer.get(requestCode);
        Object object = mContainer.get(requestCode + 1);

        String[] succeedPermissions = PermissionUtils.getSucceedPermissions(permissions, grantResults);
        String[] failPermissions = PermissionUtils.getFailPermissions(permissions, grantResults);


        //证明有部分或者全部权限被成功授予
        if (succeedPermissions.length != 0) {
            call.hasPermission(PermissionUtils.arrayConversion(succeedPermissions, true, false, false));
        }

        //证明有部分或者全部权限被拒绝授予
        if (failPermissions.length != 0) {
            List<PermissionInfo> infos = new ArrayList<>();
            for (String permission : failPermissions) {
                infos.add(new PermissionInfo().setName(permission).setGranted(false)
                        .setPermanent(PermissionUtils.checkPermissionPermanentDenied(PermissionUtils.getActivity(object), permission))
                        .setExplain(ActivityCompat.shouldShowRequestPermissionRationale(PermissionUtils.getActivity(object), permission)));
            }
            //如果拒绝的时间过快证明是系统自动拒绝
            call.noPermission(infos, System.currentTimeMillis() - requestTime < 200);
        }

        //权限回调结束后要删除集合中的对象，避免重复请求
        mContainer.remove(requestCode);
        mContainer.remove(requestCode + 1);
    }
}
