package com.wwish.ganalytics.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;



public class GMSharedPreference {

    private SharedPreferences mSp;
    private SharedPreferences.Editor mEditor;

    public enum SharedPreferenceName{
        pubSp
    }

    public static class SharePreferenceKeyEnum {
        public static final String sr = "sr";//screen_resolution: 实际分辨率+ppi
        public static final String av = "av";//app_version: 内部+外部版本
        public static final String ic = "ic";//install_channel：安装渠道
        public static final String aid = "aid";//app_id: app包名 和上面参数合并
        public static final String c = "c";//cmpid  打开渠道 ：push 微信
        public static final String ll = "ll";//longitude and latitude经纬度 分隔符用|
//        public static final String s = "s";//station 基站
        public static final String cid = "cid";//设备id uuid.时间戳 ，uuid通过plus的uuid生成方式获取
        public static final String sid = "sid";//session_id
        public static final String sidTime = "sidTime";
        public static final String uid = "uid";//用户id
        public static final String ak = "ak";//appkey
        public static final String m    ="m";//可用内存大小

        public static final String f_install = "f_ins";//第一次安装
        public static final String isExit = "isExit";//是否退出app了
        public static final String pageid = "pageid";//业务请求使用

        public static final String mac = "macAddress";//缓存mac地址，结账时出现anr

    }

    public GMSharedPreference(Context context, SharedPreferenceName name) {
        this.mSp = context.getSharedPreferences(name.toString(),
                Context.MODE_PRIVATE);
        this.mEditor = this.mSp.edit();
    }

    public void clear() {
        this.mEditor.clear();
    }

    public int getIntValue(String key, int defValue) {
        return mSp.getInt(key, defValue);
    }

    public void putIntValue(String key, int value) {
        mEditor.putInt(key, value);
        commit();
    }

    public boolean getBooleanValue(String key, boolean defValue) {
        return mSp.getBoolean(key, defValue);
    }

    public void putBooleanValue(String key, boolean value) {
        mEditor.putBoolean(key, value);
        commit();
    }

    public float getFloatValue(String key, float defValue) {
        return mSp.getFloat(key, defValue);
    }

    public void putFloatValue(String key, float value) {
        mEditor.putFloat(key, value);
        commit();
    }

    public long getLongValue(String key, long defValue) {
        return mSp.getLong(key, defValue);
    }

    public void putLongValue(String key, long value) {
        mEditor.putLong(key, value);
        commit();
    }

    public String getStringValue(String key, String defValue) {
        return mSp.getString(key, defValue);
    }

    public void putStringValue(String key, String value) {
        mEditor.putString(key, value);
        commit();
    }

    public void put(String key, int value) {
        mEditor.putInt(key, value);
        commit();
    }

    public void put(String key, String value) {
        mEditor.putString(key, value);
        commit();
    }

    public void remove(String key) {
        this.mEditor.remove(key);
        commit();
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void commit() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
//            mEditor.apply();
//        } else {
//            mEditor.commit();
//        }
        mEditor.commit();
    }
}
