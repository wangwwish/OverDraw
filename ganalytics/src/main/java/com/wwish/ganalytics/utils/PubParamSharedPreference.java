package com.wwish.ganalytics.utils;

import android.content.Context;
import android.text.TextUtils;

import static com.wwish.ganalytics.utils.GMSharedPreference.SharePreferenceKeyEnum.sid;
import static com.wwish.ganalytics.utils.GMSharedPreference.SharePreferenceKeyEnum.sidTime;


final public class PubParamSharedPreference {

    public static final int VALUE_FIRST_INSTALL                 = 1;
    public static final int VALUE_FIRST_INSTALL_NO              = -1;
    public static final int VALUE_FIRST_INSTALL_DEFAULT         = 0;

    public static final int VALUE_EXIT_YES                      = 1;
    public static final int VALUE_EXIT_NO                       = -1;

    private Context mContext;
    private GMSharedPreference mSp;

    public PubParamSharedPreference(Context context){
        mContext = context.getApplicationContext();
        mSp = new GMSharedPreference(mContext, GMSharedPreference.SharedPreferenceName.pubSp);
    }

    /**
     * 内部+外部版本
     * @param av
     */
    public void setAv(String av){
        mSp.put(GMSharedPreference.SharePreferenceKeyEnum.av, av);
    }

    /**
     *安装渠道
     * @param inc
     */
    public void setIc(String inc){
        mSp.put(GMSharedPreference.SharePreferenceKeyEnum.ic, inc);
    }

    public String getIc(){
        return mSp.getStringValue(GMSharedPreference.SharePreferenceKeyEnum.ic,"");
    }

    /**
     * appkey
     * @param a
     */
    public void setAk(String a){
        mSp.putStringValue(GMSharedPreference.SharePreferenceKeyEnum.ak,a);
    }

    public String getAk(){
        return mSp.getStringValue(GMSharedPreference.SharePreferenceKeyEnum.ak,"");
    }
    /**
     * 打开渠道
     * @param c
     */
    public void setC(String c){
        mSp.put(GMSharedPreference.SharePreferenceKeyEnum.c, c);
    }

    public String getC(){
        return mSp.getStringValue(GMSharedPreference.SharePreferenceKeyEnum.c,"");
    }

    public void setMac(String c){
        mSp.put(GMSharedPreference.SharePreferenceKeyEnum.mac, c);
    }

    public String getMac(){
        return mSp.getStringValue(GMSharedPreference.SharePreferenceKeyEnum.mac,"");
    }

    /**
     * 经纬度 lon|lat 竖线分割
     * @param ll
     */
    public void setLl(String ll){
        mSp.put(GMSharedPreference.SharePreferenceKeyEnum.ll, ll);
    }

    /**
     * 基站
     */
//    public void setS(String st){
//        mSp.put(GMSharedPreference.SharePreferenceKeyEnum.s, st);
//    }

    public void setUid(String u){
        mSp.put(GMSharedPreference.SharePreferenceKeyEnum.uid, u);
    }

    public String getUid(){
        return mSp.getStringValue(GMSharedPreference.SharePreferenceKeyEnum.uid,"");
    }

    /**
     * sessionid
     * @param s
     */
    public void setSid(String s){
        mSp.put(sid, s);
    }

    public String getSid(){
        String s = mSp.getStringValue(sid,"");
        if(TextUtils.isEmpty(s)){
            s = Utils.getTimeSecondFrom2011();
            setSid(s);
            setSidTime(System.currentTimeMillis());
        }
        return mSp.getStringValue(sid,"");
    }

    public void setSidTime(long s){
        mSp.putLongValue(sidTime, s);
    }

    public long getSidTime(){
        return mSp.getLongValue(sidTime,0);
    }

    public String getCid(){
        return mSp.getStringValue(GMSharedPreference.SharePreferenceKeyEnum.cid,"");
    }

    public void setCid(String mcid){
        mSp.putStringValue(GMSharedPreference.SharePreferenceKeyEnum.cid,mcid);
    }

    public void setFInstall(int fInstall){
        mSp.putIntValue(GMSharedPreference.SharePreferenceKeyEnum.f_install,fInstall);
    }

    public int getFInstall(){
        return mSp.getIntValue(GMSharedPreference.SharePreferenceKeyEnum.f_install,VALUE_FIRST_INSTALL_DEFAULT);
    }

    public void setIsExit(boolean exit){
        mSp.putBooleanValue(GMSharedPreference.SharePreferenceKeyEnum.isExit,exit);
    }

    public boolean getIsExit(){
        return mSp.getBooleanValue(GMSharedPreference.SharePreferenceKeyEnum.isExit,false);
    }

    public String getResoution(){
        return mSp.getStringValue(GMSharedPreference.SharePreferenceKeyEnum.sr,"");
    }

    public void setResoution(String s){
        mSp.putStringValue(GMSharedPreference.SharePreferenceKeyEnum.sr,s);
    }

    public void setMemory(String s){
        mSp.putStringValue(GMSharedPreference.SharePreferenceKeyEnum.m,s);
    }

    public String getMemory(){
        return mSp.getStringValue(GMSharedPreference.SharePreferenceKeyEnum.m,"");
    }

    public void setPageId(String pageId){
        mSp.putStringValue(GMSharedPreference.SharePreferenceKeyEnum.pageid,pageId);
    }

    public String getPageId(){
        return mSp.getStringValue(GMSharedPreference.SharePreferenceKeyEnum.pageid,"");
    }
}
