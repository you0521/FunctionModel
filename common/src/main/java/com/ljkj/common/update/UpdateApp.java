package com.ljkj.common.update;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;

import com.ljkj.common.utils.ActivityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * App 更新软件
 */
public class UpdateApp {
    private UpdateApp() {
        throw new Error("Do not need instantiate!");
    }

    /**
     * 接收消息
     */
    private static Handler mUpdateProgressHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:
                    installAPK(Environment.getExternalStorageDirectory().getAbsolutePath(), ActivityUtils.getApp());
            }
        }
    };

    /**
     * 点击下载弹框
     */
    public static void showUpdateDialog(String url, String ver_introduction, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("检测到新版本");
        builder.setMessage("版本介绍:\n" + ver_introduction);
        builder.setPositiveButton("以后再说", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setNegativeButton("立即更新", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                downFile(url, context);
            }
        });
        builder.show();
    }

    public static void downFile(final String versionUrl, Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("下载中....");
        progressDialog.setProgress(0);
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient build = new OkHttpClient.Builder().build();
                Request request = new Request.Builder().url(versionUrl).build();
                build.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        int length = (int) response.body().contentLength();
                        InputStream inputStream = response.body().byteStream();
                        progressDialog.setMax(length);
                        FileOutputStream fos = null;
                        if (inputStream != null) {
                            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "demo.apk");
                            fos = new FileOutputStream(file);
                            byte[] buf = new byte[1024];
                            int ch;
                            int process = 0;
                            while ((ch = inputStream.read(buf)) != -1) {
                                fos.write(buf, 0, ch);
                                process += ch;
                                progressDialog.setProgress(process); // 实时更新进度了
                            }
                            if (fos != null) {
                                fos.flush();
                                fos.close();
                            }
                            //关闭dialog防止泄露
                            progressDialog.dismiss();
                            mUpdateProgressHandler.sendEmptyMessage(2);
                        }
                    }
                });
            }
        }).start();
    }

    /*
     * 下载到本地后执行安装
     */
    public static void installAPK(String mSavePath, Context context) {
        File apkFile = new File(mSavePath, "demo.apk");
        if (!apkFile.exists()) {
            return;
        }


        Intent intent = new Intent(Intent.ACTION_VIEW);
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, "com.ljkj.demo.myFileProvider", apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

}
