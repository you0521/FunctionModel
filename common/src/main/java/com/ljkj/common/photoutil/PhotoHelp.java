package com.ljkj.common.photoutil;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoHelp {
    /**
     * 获取相机权限，打开相机
     *
     * @param context
     * @param permissionRequestCode
     * @param filePath
     * @param cameraRequestCode
     */
    public static void applyForCameraPermission(Activity context, int permissionRequestCode, String filePath, int cameraRequestCode) {
        //使用兼容库就无需判断系统版本
        int hasWriteStoragePermission = ContextCompat.checkSelfPermission(context.getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int hasCameraStoragePermission = ContextCompat.checkSelfPermission(context.getApplication(), Manifest.permission.CAMERA);
        if (hasWriteStoragePermission == PackageManager.PERMISSION_GRANTED && hasCameraStoragePermission == PackageManager.PERMISSION_GRANTED) {
            //拥有权限，执行操作
            showCameraAction(context, filePath, cameraRequestCode);
        } else {
            //没有权限，向用户请求权限
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, permissionRequestCode);
        }
    }

    /**
     * 获取相机权限，打开相机
     *
     * @param permissionRequestCode
     * @param filePath
     * @param cameraRequestCode
     */
    public static void applyForCameraPermission(android.support.v4.app.Fragment fragment,
                                                int permissionRequestCode, String filePath, int cameraRequestCode) {
        //使用兼容库就无需判断系统版本
        int hasWriteStoragePermission = ContextCompat.checkSelfPermission(fragment.getActivity().getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int hasCameraStoragePermission = ContextCompat.checkSelfPermission(fragment.getActivity().getApplication(), Manifest.permission.CAMERA);
        if (hasWriteStoragePermission == PackageManager.PERMISSION_GRANTED && hasCameraStoragePermission == PackageManager.PERMISSION_GRANTED) {
            //拥有权限，执行操作
            showCameraAction(fragment, filePath, cameraRequestCode);
        } else {
            //没有权限，向用户请求权限
            ActivityCompat.requestPermissions(fragment.getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, permissionRequestCode);
        }
    }

    /**
     * 相机的权限回调
     *
     * @param context
     * @param grantResults
     * @param permissionRequestCode
     * @param filePath
     * @param cameraRequestCode
     */
    public static void cameraPermissionResult(Activity context, int[] grantResults,
                                              int permissionRequestCode, String filePath, int cameraRequestCode) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            filePath = PhotoHelp.getFilePath();
            PhotoHelp.applyForCameraPermission(context, permissionRequestCode, filePath, cameraRequestCode);
        } else {
            //ToastHelp.showShort(context, "您有权限为同意");
        }
    }

    /**
     * 相机的权限回调
     *
     * @param grantResults
     * @param permissionRequestCode
     * @param filePath
     * @param cameraRequestCode
     */
    public static void cameraPermissionResult(android.support.v4.app.Fragment fragment, int[] grantResults,
                                              int permissionRequestCode, String filePath, int cameraRequestCode) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            filePath = PhotoHelp.getFilePath();
            PhotoHelp.applyForCameraPermission(fragment, permissionRequestCode, filePath, cameraRequestCode);
        } else {
            //ToastHelp.showShort(fragment.getContext(), "您有权限为同意");
        }
    }

    /**
     * 跳转相机
     *
     * @param context
     * @param filePath
     * @param cameraRequestCode
     */
    public static void showCameraAction(Activity context, String filePath, int cameraRequestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            ContentValues contentValues = new ContentValues(2);

            contentValues.put(MediaStore.Images.Media.DATA, filePath);

            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            Uri mPhotoUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);

            context.startActivityForResult(intent, cameraRequestCode);
        }
    }

    /**
     * 跳转相机
     *
     * @param filePath
     * @param cameraRequestCode
     */
    public static void showCameraAction(android.support.v4.app.Fragment fragment, String
            filePath, int cameraRequestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(fragment.getContext().getPackageManager()) != null) {
            ContentValues contentValues = new ContentValues(2);

            contentValues.put(MediaStore.Images.Media.DATA, filePath);

            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            Uri mPhotoUri = fragment.getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);

            fragment.startActivityForResult(intent, cameraRequestCode);
        }
    }

    /**
     * 获取相册权限，打开相册
     *
     * @param activity
     * @param xiangCePermissionResultCode
     * @param xiangCeRequestCode
     */
    public static void autoObtainStoragePermission(Activity activity,
                                                   int xiangCePermissionResultCode, int xiangCeRequestCode) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, xiangCePermissionResultCode);
        } else {
            PhotoUtils.openPic(activity, xiangCeRequestCode);
        }

    }


    /**
     * 获取相册权限，打开相册
     *
     * @param xiangCePermissionResultCode
     * @param xiangCeRequestCode
     */
    public static void autoObtainStoragePermission(android.support.v4.app.Fragment fragment,
                                                   int xiangCePermissionResultCode, int xiangCeRequestCode) {
        if (ContextCompat.checkSelfPermission(fragment.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(fragment.getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, xiangCePermissionResultCode);
        } else {
            PhotoUtils.openPic(fragment, xiangCeRequestCode);
        }

    }

    /**
     * 相册的权限回调
     *
     * @param activity
     * @param grantResults
     * @param xiangCeResultCode
     */
    public static void xiangCePermissionResult(Activity activity, int[] grantResults,
                                               int xiangCeResultCode) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            PhotoUtils.openPic(activity, xiangCeResultCode);
        } else {
            //ToastHelp.showShort(activity, "请允许打操作SDCard！！");
        }
    }

    /**
     * 相册的权限回调
     *
     * @param fragment
     * @param grantResults
     * @param xiangCeResultCode
     */
    public static void xiangCePermissionResult(android.support.v4.app.Fragment fragment, int[] grantResults,
                                               int xiangCeResultCode) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            PhotoUtils.openPic(fragment, xiangCeResultCode);
        } else {
            //ToastHelp.showShort(fragment.getContext(), "请允许打操作SDCard！！");
        }
    }


    /**
     * 获取相册选取的图片
     *
     * @param context
     * @param data
     * @return
     */
    public static Bitmap xiangCeResult(Context context, Intent data) {
        if (PhotoUtils.hasSdcard()) {
            Uri newUri = Uri.parse(PhotoUtils.getPath(context, data.getData()));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                newUri = FileProvider.getUriForFile(context, "com.ljkj.demo.myFileProvider", new File(newUri.getPath()));
            Bitmap bitmap = PhotoUtils.getBitmapFromUri(newUri, context);
            if (bitmap != null) {
                return bitmap;
            } else {
                return null;
            }

        } else {
            //ToastHelp.showShort(context, "设备没有SD卡！");
        }
        return null;
    }

    /**
     * 获取文件路径
     *
     * @return
     */
    public static String getFilePath() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //  File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStorageDirectory();
        String files = storageDir.getAbsolutePath() + "/" + "照片";
        //新建一个File，传入文件夹目录
        File file = new File(files);
        //判断文件夹是否存在，如果不存在就创建，否则不创建
        if (!file.exists()) {
            //通过file的mkdirs()方法创建目录中包含却不存在的文件夹
            file.mkdirs();

            //要保存照片的绝对路径
        }
        String filePath = file + "/" + imageFileName + ".jpg";
        return filePath;
    }


    /**
     * 把Bitmap 转file
     *
     * @param bitmap
     * @param filepath
     */
    public static File saveBitmapFile(Bitmap bitmap, String filepath) {
        Bitmap comp = comp(bitmap);

        File file = new File(filepath);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            comp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 采样压缩图片
     *
     * @param image
     * @return
     */
    public static Bitmap comp(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 750f;//这里设置高度为800f
        float ww = 1080f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return bitmap;//压缩好比例大小后再进行质量压缩
    }
}
