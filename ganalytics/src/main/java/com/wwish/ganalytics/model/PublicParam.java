package com.wwish.ganalytics.model;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.wwish.ganalytics.db.entity.OverDraw;
import com.wwish.ganalytics.utils.Constant;
import com.wwish.ganalytics.utils.DeviceUtils;
import com.wwish.ganalytics.utils.GMSharedPreference;
import com.wwish.ganalytics.utils.PubParamSharedPreference;


/**
 * Created by gaocaili on 2017/6/5.
 */

public class PublicParam {

    /**
     * 公参1
     * @param mContext
     * @param param
     */
    public static void setPubParam1(Context mContext, CommonParam param){
        param.dt  = DeviceUtils.isPad(mContext)? "2" : "1";
        param.os  = DeviceUtils.getDeviceSoftwareVersion(mContext);
        param.osv = DeviceUtils.getDeviceType();
        param.sv  = Constant.VERSION_GANALYTICS/*DeviceUtils.getSdkVersion(mContext)*/;
        param.aid = DeviceUtils.getPackageName(mContext);
        param.pmc = DeviceUtils.getDeviceMac(mContext);
        param.pb  = Build.BRAND;
        param.pm  = Build.MODEL;
        param.pi = DeviceUtils.getDeviceId(mContext);
        param.o   = DeviceUtils.getDeviceSimProviderName(mContext);

        //从sp中获取的
        GMSharedPreference sp = new GMSharedPreference(mContext.getApplicationContext(), GMSharedPreference.SharedPreferenceName.pubSp);

        param.av  = sp.getStringValue(GMSharedPreference.SharePreferenceKeyEnum.av,"");
        param.ic  = sp.getStringValue(GMSharedPreference.SharePreferenceKeyEnum.ic,"");
        param.ll  = sp.getStringValue(GMSharedPreference.SharePreferenceKeyEnum.ll,"");
//        param.s   = sp.getStringValue(GMSharedPreference.SharePreferenceKeyEnum.s,"");
        param.sr  = sp.getStringValue(GMSharedPreference.SharePreferenceKeyEnum.sr,"");
        param.c  = sp.getStringValue(GMSharedPreference.SharePreferenceKeyEnum.c,"");
    }

    /**
     * 公参2
     * @param mContext
     * @param param
     */
    public static void setPubParam2(Context mContext, CommonParam param){
        param.st = System.currentTimeMillis();
        param.nt = DeviceUtils.getDeviceNetType(mContext);
        //从sp中获取的
        PubParamSharedPreference sp = new PubParamSharedPreference(mContext.getApplicationContext());
        String cid = sp.getCid();
//        第一次数据上传时创建，后续不修改
        if(TextUtils.isEmpty(cid)){
            cid = DeviceUtils.getDeviceIdParam(mContext);
            sp.setCid(cid);
        }
        param.cid = cid;

        String appkey = sp.getAk();
        if(TextUtils.isEmpty(appkey)){
            appkey = DeviceUtils.getAppKey(mContext);
        }
        param.ak = appkey;
        param.c  = sp.getC();
        param.__gmz = getH5Params(mContext);
    }

//    app|{ic}|{c}|{站点}|{推荐信息}|{搜索信息}|{广告信息}|{cid}
//    示例：app|A001|sem_baidu_cpc1_国美|dc-18|-|-|-|e9eaa457-6ff5-4a7b-9c1e-d8effdcf9fe.1498529174911
    public static String getH5Params(Context context){
        //从sp中获取的
        PubParamSharedPreference sp = new PubParamSharedPreference(context.getApplicationContext());
        StringBuilder sb = new StringBuilder();
        sb.append("app");
        sb.append("|" + (TextUtils.isEmpty(sp.getIc()) ? "-" : sp.getIc()));//安装渠道
        sb.append("|" + (TextUtils.isEmpty(sp.getC()) ? "-" : sp.getC()));//打开渠道
        String appKey = DeviceUtils.getAppKey(context);
        sb.append("|" + (TextUtils.isEmpty(appKey) ? "-" : appKey));//站点
        sb.append("|-");//推荐信息
        sb.append("|-");//搜索信息
        sb.append("|-");//广告信息
        sb.append("|" + (TextUtils.isEmpty(sp.getCid())?"-":sp.getCid()));
        return sb.toString();
    }


}
