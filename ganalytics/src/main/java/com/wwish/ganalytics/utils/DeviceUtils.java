package com.wwish.ganalytics.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.view.Display;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.List;
import java.util.UUID;

/**
 * Created by wangwei-ds10 on 2018/1/26.
 */

public class DeviceUtils {
    /**
     * 获取不到的类型返回值
     */
    public static String NO_INFO = "unknown";
    public static final String YES = "1";       //是
    public static final String NO = "2";        //否

    public static final String NETWORK_TYPE_WIFI    = "WIFI";
    public static final String NETWORK_TYPE_4G      = "4G";
    public static final String NETWORK_TYPE_3G      = "3G";
    public static final String NETWORK_TYPE_2G      = "2G";

    /**
     * 判断是手机还是平板
     * @param context
     * @return
     */
    public static boolean isPad(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * 设备网络类型
     *
     * @param context
     * @return
     */
    public static String getDeviceNetType(Context context) {
        String deviceSimNetType = NO_INFO;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info == null || !info.isConnectedOrConnecting())
            return deviceSimNetType;

        switch (info.getType()) {
            case ConnectivityManager.TYPE_WIFI:
            case ConnectivityManager.TYPE_WIMAX:
            case ConnectivityManager.TYPE_ETHERNET:
                deviceSimNetType = "WIFI";
                break;
            case ConnectivityManager.TYPE_MOBILE:
                switch (info.getSubtype()) {
                    case TelephonyManager.NETWORK_TYPE_LTE:  // 4G
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                        deviceSimNetType = "4G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS: // 3G
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        deviceSimNetType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_GPRS: // 2G
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                        deviceSimNetType = "2G";
                        break;
                }
                break;
        }
        return deviceSimNetType;
    }

    /**
     * 设备mac地址
     *
     * @return
     */
    public static String getDeviceMac(Context context) {
        String macSerial = null;
        String str = "";
        try {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }
        return macSerial == null ? NO_INFO : macSerial;
    }


    /**
     * 唯一的设备ID：<br/>
     * 如果是GSM网络，返回IMEI；如果是CDMA网络，返回MEID<br/>
     * 需要权限：android.permission.READ_PHONE_STATE
     *
     * @return null if device ID is not available.
     */
    public static String getDeviceId(Context ctx) {
        try{
            TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            if(tm == null){
                return NO_INFO;
            }
            String deviceId = tm.getDeviceId();
            if(TextUtils.isEmpty(deviceId)){
                return NO_INFO;
            }
            return deviceId;
        }catch (Exception e){
            return NO_INFO;
        }

    }

    /**
     * 返回移动终端的软件版本：<br/>
     * 例如：Android 版本。<br/>
     *
     * @return null if the software version is not available.
     */
    public static String getDeviceSoftwareVersion(Context ctx) {
        String deviceSoftwareVersion = Build.VERSION.RELEASE;
        return deviceSoftwareVersion == null ? NO_INFO : deviceSoftwareVersion;
    }

    /**
     * 获取手机型号
     * 分辨率为960x640，屏幕为3.5英寸，960的平方加上640的平方等于1331200，开根号得出1153，再除以3.5等于329，这就是iphone4的PPI。
     */
    public static String getDeviceType() {
        return Build.MODEL;
    }

    public static String getDisplayAndPPI(Context context){
        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics( metrics );
        int nDeviceHeight = (int)(metrics.heightPixels * metrics.density);  // 569 * 1.5 = 853.5
        int nDeviceWidth = (int)(metrics.widthPixels * metrics.density);    //  320 * 1.5 = 480
        String wh = nDeviceWidth + "*" + nDeviceHeight;

        double diagonalPixels = Math.sqrt(Math.pow(metrics.widthPixels, 2) + Math.pow(metrics.heightPixels, 2));
        double screenSize = diagonalPixels / (160 * metrics.density);//屏幕尺寸

        int ppi = (int) ((int)diagonalPixels/screenSize);
        return wh + "|" + ppi;
    }

    /**
     * 服务商名称 simOperatorName
     *
     * @param ctx
     * @return
     */
    public static String getDeviceSimProviderName(Context ctx) {

        try {
            TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            String providersName ;
//        String simOperatorName = tm.getSimOperatorName();//网络服务商：CMCC
            // 返回唯一的用户ID;就是这张卡的编号神马的
            String IMSI = tm.getSubscriberId();     //460078102671888
            // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
            if (TextUtils.isEmpty(IMSI)) {
                return NO_INFO;
            }
            if (IMSI.startsWith("46000") || IMSI.startsWith("46002") || IMSI.startsWith("46007")) {
                providersName = "中国移动";
            } else if (IMSI.startsWith("46001")) {
                providersName = "中国联通";
            } else if (IMSI.startsWith("46003")) {
                providersName = "中国电信";
            } else {
                providersName = NO_INFO;
            }
            return providersName;
        }catch (Exception e){
        }

        return NO_INFO;
    }

    public static String getSdkVersion(Context context){
        int currentVersionCode = 0;
        String appVersionName = "";
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            appVersionName = info.versionName; // 版本名
            currentVersionCode = info.versionCode; // 版本号
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appVersionName + "" + currentVersionCode;
    }

    public static String getPackageName(Context context){
        //当前应用pid
        int pid = android.os.Process.myPid();
        //任务管理类
        ActivityManager manager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        //遍历所有应用
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        if(infos == null){
            return "";
        }
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (info.pid == pid)//得到当前应用
                return info.processName;//返回包名
        }
        return "";
    }

    /**
     * 获取手机唯一识别码
     * UUID+设备号序列号 唯一识别码（不可变）
     **/
    public static String getPhoneUUID(Context context) {
        try {
            final TelephonyManager tm = getTelephonyManager(context);

            final String tmDevice, tmSerial, tmPhone, androidId;

            tmDevice = "" + tm.getDeviceId();

            tmSerial = "" + tm.getSimSerialNumber();

            androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

            UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());

            String uniqueId = deviceUuid.toString();

            return uniqueId;
        }catch (Exception e){
            return "";
        }


    }

    public static String getDeviceIdParam(Context context){
        return getPhoneUUID(context) + "." + System.currentTimeMillis();
    }

    public static TelephonyManager getTelephonyManager(Context context) {
        return (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }


    /**
     * appkey
     * @param context
     * @return
     */
    public static String getAppKey(Context context){
        ApplicationInfo appInfo = null;
        try {
            appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);

            if(appInfo != null && appInfo.metaData != null){
                return appInfo.metaData.getString("GMCLICK_APPKEY");
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "";
    }

    // 获取android当前可用内存大小
    public static String getAvailMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存
        return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
    }

    public static String getTotalMemory(Context context) {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;

        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

            arrayOfString = str2.split("\\s+");

            initial_memory = Long.valueOf(arrayOfString[1]).longValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();

        } catch (IOException e) {
        }
        return Formatter.formatFileSize(context, initial_memory);// Byte转换为KB或者MB，内存大小规格化
    }
}
