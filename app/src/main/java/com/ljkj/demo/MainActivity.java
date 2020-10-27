package com.ljkj.demo;


import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.ljkj.common.base.BaseActivity;
import com.ljkj.common.permission.bean.PermissionInfo;
import com.ljkj.common.permission.call.OnRequestCallBack;
import com.ljkj.common.permission.request.EasyPermissions;
import com.ljkj.common.permission.request.SimplePermissions;
import com.ljkj.common.photoutil.PhotoHelp;
import com.ljkj.common.photoutil.PhotoUtils;
import com.ljkj.common.update.UpdateApp;
import com.ljkj.common.utils.AnimationUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends BaseActivity {
    String[] perms = {Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.REQUEST_INSTALL_PACKAGES};
    @BindView(R.id.update_btn)
    Button updateBtn;
    @BindView(R.id.loadh5_btn)
    Button loadh5Btn;
    @BindView(R.id.loading_btn)
    Button loadingBtn;
    @BindView(R.id.take_photo)
    Button takePhoto;
    @BindView(R.id.open_camera)
    Button openCamera;
    //这是相册权限
    private final int STORAGE_PERMISSIONS_REQUEST_CODE = 100;
    //这是相册请求码
    private final int CODE_GALLERY_REQUEST = 200;
    //相机权限码
    private final int CAMERA_PERMISSIONS_REQUEST_CODE = 300;
    //相机请求码
    private final int CODE_CAMERA_REQUEST = 400;
    @BindView(R.id.iv)
    ImageView iv;
    private String filePath;


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        loadh5Btn.setText("加载页面");

        SimplePermissions.request(this,perms);
        EasyPermissions.request(this, new OnRequestCallBack() {
            @Override
            public void hasPermission(List<PermissionInfo> granted) {

            }

            @Override
            public void noPermission(List<PermissionInfo> denied, boolean quick) {

            }
        }, perms);
        AlphaAnimation alphaAnimation = AnimationUtils.getAlphaAnimation(0, 1);
        loadh5Btn.setAnimation(alphaAnimation);
    }

    @Override
    public void initLoad() {

    }


    @OnClick({R.id.update_btn, R.id.loadh5_btn, R.id.loading_btn, R.id.take_photo, R.id.open_camera})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.update_btn:
                UpdateApp.showUpdateDialog("https://download.dgstaticresources.net/fusion/android/app-c6-release.apk",
                        "1，添加删除信用卡接口\\r\\n2，添加vip认证\\r\\n3，区分自定义消费，一个小时不限制。\\r\\n4，添加放弃任务接口，小时内不生成。\\r\\n5，消费任务手动生成。"
                        , this);
                break;
            case R.id.loadh5_btn:
                break;
            case R.id.loading_btn:
                showProgressDialog();
                break;
            case R.id.take_photo:
                PhotoHelp.autoObtainStoragePermission(this, STORAGE_PERMISSIONS_REQUEST_CODE, CODE_GALLERY_REQUEST);
                break;
            case R.id.open_camera:
                filePath = PhotoHelp.getFilePath();
                PhotoHelp.applyForCameraPermission(this, CAMERA_PERMISSIONS_REQUEST_CODE, filePath, CODE_CAMERA_REQUEST);
                break;
        }
    }

    //权限的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST_CODE:
                filePath = PhotoHelp.getFilePath();
                PhotoHelp.cameraPermissionResult(this, grantResults, CAMERA_PERMISSIONS_REQUEST_CODE, filePath, CODE_CAMERA_REQUEST);
                break;
            case STORAGE_PERMISSIONS_REQUEST_CODE://调用系统相册申请Sdcard权限回调
                PhotoHelp.xiangCePermissionResult(this, grantResults, CODE_GALLERY_REQUEST);
                break;
        }
    }

    //相机相册的回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_CAMERA_REQUEST://拍照完成回调
                    Bitmap cameraBitmap = BitmapFactory.decodeFile(filePath);
                    iv.setImageBitmap(cameraBitmap);
                    break;
                case CODE_GALLERY_REQUEST://访问相册完成回调
                    Bitmap xiangCeBitmap = PhotoHelp.xiangCeResult(this, data);
                    String path = PhotoUtils.getPath(this, data.getData());
                    iv.setImageBitmap(xiangCeBitmap);

                    break;
            }
        }
    }

}
