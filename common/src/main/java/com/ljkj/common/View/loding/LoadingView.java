package com.ljkj.common.View.loding;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.ljkj.common.R;

/**
 * 自定义数据加载动画
 * @author zhangbiao
 */
public class LoadingView {
    private static AlertDialog mAlertDialog;
    private static LoadingIndicatorView pbLoad;

    /**
     * @param layout
     * @return 工具类尽量将方法全都设置成静态的static
     */
    public static View getView(int layout, Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(layout, null);
        return view;

    }


    public static void showLoadingDiaLog(Context context) {
        //打气筒
        View dialogView = View.inflate(context, R.layout.loading, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        pbLoad = dialogView.findViewById(R.id.pb_load);
        mAlertDialog = builder.create();
        mAlertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);//去除背景色
        mAlertDialog.setView(dialogView,0,0,0,0);
        pbLoad.show();
        mAlertDialog.show();
        WindowManager.LayoutParams params = mAlertDialog.getWindow().getAttributes();
        params.width = 400;
        params.height = 400 ;
        mAlertDialog.getWindow().setAttributes(params);

    }
    public static void dimissLoadingDialog(){
        pbLoad.hide();
        mAlertDialog.dismiss();
    }
}
