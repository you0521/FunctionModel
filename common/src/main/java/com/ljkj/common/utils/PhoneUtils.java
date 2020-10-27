package com.ljkj.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresPermission;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import static com.ljkj.common.permission.bean.Permission.READ_PHONE_STATE;

/**
 * 手机相关类
 *
 * @author zhangbiao
 */
public class PhoneUtils {
    private PhoneUtils() {
        throw new UnsupportedOperationException("Do not need instantiate!");
    }

    /**
     * 判断是否为手机
     *
     * @return
     */
    public static boolean isPhone() {
        TelephonyManager tm = getTelephonyManager();
        return tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
    }

    /**
     * 获取设备码
     *
     * @return
     */
    public static String getDeviceId() {
        if (Build.VERSION.SDK_INT >= 29) {
            return "";
        }
        TelephonyManager tm = getTelephonyManager();
        @SuppressLint("MissingPermission") String deviceId = tm.getDeviceId();
        if (!TextUtils.isEmpty(deviceId)) return deviceId;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("MissingPermission") String imei = tm.getImei();
            if (!TextUtils.isEmpty(imei)) return imei;
            @SuppressLint("MissingPermission") String meid = tm.getMeid();
            return TextUtils.isEmpty(meid) ? "" : meid;
        }
        return "";
    }

    /**
     * 获取序列号
     *
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getSerial() {

        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? Build.getSerial() : Build.SERIAL;
    }

    /**
     * 获取 IMEI 码
     *
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getIMEI() {
        return getImeiOrMeid(true);
    }

    /**
     * 获取MEID
     *
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getMEID() {
        return getImeiOrMeid(false);
    }

    @SuppressLint("HardwareIds")
    @RequiresPermission(READ_PHONE_STATE)
    public static String getImeiOrMeid(boolean isImei) {
        if (Build.VERSION.SDK_INT >= 29) {
            return "";
        }
        TelephonyManager tm = getTelephonyManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (isImei) {
                return getMinOne(tm.getImei(0), tm.getImei(1));
            } else {
                return getMinOne(tm.getMeid(0), tm.getMeid(1));
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String ids = getSystemPropertyByReflect(isImei ? "ril.gsm.imei" : "ril.cdma.meid");
            if (!TextUtils.isEmpty(ids)) {
                String[] idArr = ids.split(",");
                if (idArr.length == 2) {
                    return getMinOne(idArr[0], idArr[1]);
                } else {
                    return idArr[0];
                }
            }

            String id0 = tm.getDeviceId();
            String id1 = "";
            try {
                Method method = tm.getClass().getMethod("getDeviceId", int.class);
                id1 = (String) method.invoke(tm,
                        isImei ? TelephonyManager.PHONE_TYPE_GSM
                                : TelephonyManager.PHONE_TYPE_CDMA);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            if (isImei) {
                if (id0 != null && id0.length() < 15) {
                    id0 = "";
                }
                if (id1 != null && id1.length() < 15) {
                    id1 = "";
                }
            } else {
                if (id0 != null && id0.length() == 14) {
                    id0 = "";
                }
                if (id1 != null && id1.length() == 14) {
                    id1 = "";
                }
            }
            return getMinOne(id0, id1);
        } else {
            String deviceId = tm.getDeviceId();
            if (isImei) {
                if (deviceId != null && deviceId.length() >= 15) {
                    return deviceId;
                }
            } else {
                if (deviceId != null && deviceId.length() == 14) {
                    return deviceId;
                }
            }
        }
        return "";
    }

    /**
     * @param s0
     * @param s1
     * @return
     */
    private static String getMinOne(String s0, String s1) {
        boolean empty0 = TextUtils.isEmpty(s0);
        boolean empty1 = TextUtils.isEmpty(s1);
        if (empty0 && empty1) return "";
        if (!empty0 && !empty1) {
            if (s0.compareTo(s1) <= 0) {
                return s0;
            } else {
                return s1;
            }
        }
        if (!empty0) return s0;
        return s1;
    }

    private static String getSystemPropertyByReflect(String key) {
        try {
            @SuppressLint("PrivateApi")
            Class<?> clz = Class.forName("android.os.SystemProperties");
            Method getMethod = clz.getMethod("get", String.class, String.class);
            return (String) getMethod.invoke(clz, key, "");
        } catch (Exception e) {/**/}
        return "";
    }

    /**
     * 获取IMSI
     *
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getIMSI() {
        return getTelephonyManager().getSubscriberId();
    }

    /**
     * 获取移动终端类型
     *
     * @return
     */
    public static int getPhoneType() {
        TelephonyManager tm = getTelephonyManager();
        return tm.getPhoneType();
    }

    /**
     * 判断 sim 卡是否准备好
     *
     * @return
     */
    public static boolean isSimCardReady() {
        TelephonyManager tm = getTelephonyManager();
        return tm.getSimState() == TelephonyManager.SIM_STATE_READY;
    }

    /**
     * 获取 Sim 卡运营商名称
     *
     * @return
     */
    public static String getSimOperatorName() {
        TelephonyManager tm = getTelephonyManager();
        return tm.getSimOperatorName();
    }

    /**
     * 获取 Sim 卡运营商名称
     *
     * @return
     */
    public static String getSimOperatorByMnc() {
        TelephonyManager tm = getTelephonyManager();
        String operator = tm.getSimOperator();
        if (operator == null) return "";
        switch (operator) {
            case "46000":
            case "46002":
            case "46007":
            case "46020":
                return "中国移动";
            case "46001":
            case "46006":
            case "46009":
                return "中国联通";
            case "46003":
            case "46005":
            case "46011":
                return "中国电信";
            default:
                return operator;
        }
    }

    /**
     * 跳转到拨号页
     *
     * @param phoneNumber
     * @return
     */
    public static boolean dial(final String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        if (isIntentAvailable(intent)) {
            ActivityUtils.getApp().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            return true;
        }
        return false;
    }

    /**
     * 拨打电话
     *
     * @param phoneNumber
     * @return
     */
    public static boolean call(final String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        if (isIntentAvailable(intent)) {
            ActivityUtils.getApp().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            return true;
        }
        return false;
    }

    /**
     * 跳至发送短信界面
     *
     * @param phoneNumber
     * @param content
     * @return
     */
    public static boolean sendSms(final String phoneNumber, final String content) {
        Uri uri = Uri.parse("smsto:" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        if (isIntentAvailable(intent)) {
            intent.putExtra("sms_body", content);
            ActivityUtils.getApp().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            return true;
        }
        return false;
    }

    private static TelephonyManager getTelephonyManager() {
        return (TelephonyManager) ActivityUtils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
    }

    private static boolean isIntentAvailable(final Intent intent) {
        return ActivityUtils.getApp()
                .getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
                .size() > 0;
    }
}
