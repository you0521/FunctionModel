package com.ljkj.common.permission.base;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.ljkj.common.permission.request.EasyPermissions;
import com.ljkj.common.permission.request.SimplePermissions;


/**
 * Activity权限基类
 * @author zhangbiao
 */

public abstract class PermissionActivity extends AppCompatActivity {

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SimplePermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
