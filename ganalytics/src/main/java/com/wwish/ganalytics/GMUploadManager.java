package com.wwish.ganalytics;

import android.content.Context;

import com.google.gson.Gson;
import com.wwish.ganalytics.db.DBManager;
import com.wwish.ganalytics.db.entity.OverDraw;
import com.wwish.ganalytics.model.DataParam;
import com.wwish.ganalytics.model.DelCacheParam;
import com.wwish.ganalytics.model.PublicParam;
import com.wwish.ganalytics.okhttp.OkHttp;
import com.wwish.ganalytics.okhttp.callback.StringCallback;
import com.wwish.ganalytics.okhttp.exception.OkHttpException;
import com.wwish.ganalytics.okhttp.utils.Logger;
import com.wwish.ganalytics.utils.Constant;
import com.wwish.ganalytics.utils.DeviceUtils;
import com.wwish.ganalytics.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wangwei-ds10 on 2018/1/26.
 */

public class GMUploadManager {
    //上报数据类型
    public static final int UPLOAD_TYPE_OVERDRAW   = 1;

    //缓存数据上报的类型
    private static final int UPLOAD_CACHE_OVERDRAW   = 1;


    //批量上传的数量
    private static final int UPLOAD_BATCH_SIZE        = 50;

    //过期数据最大条数
    private static final int DELETE_EXPIRED_CACHE_SZIE  = 150;

    private ExecutorService mExcutor;
    private Context mContext;
    private Gson mGson;
    private String mRequest_url;
    public GMUploadManager( Context ct){
        mContext = ct;
        mGson = new Gson();
        mExcutor = Executors.newSingleThreadExecutor();
        mRequest_url = Constant.REQUEST_URL_TEST;
    }

    public void setIsDebugForHttp(boolean isDebugForHttp){
        if(isDebugForHttp){
            mRequest_url = Constant.REQUEST_URL_TEST;
        }else{
            mRequest_url = Constant.REQUEST_URL_PRD;
        }
        Logger.e("url:" + mRequest_url);
    }

    /**
     * 1、数据上传
     * @param uploadType
     * @param param
     */
    protected void preUpload(final int uploadType,final DataParam param){
        mExcutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String netType = DeviceUtils.getDeviceNetType(mContext);
                    if(uploadType == UPLOAD_TYPE_OVERDRAW){
                        if(netType.equals(DeviceUtils.NO_INFO)){//无网络
                            DBManager.getInstance(mContext).insertList(param.overDraws);
                            checkExpiredCache();
                        }else{
                            PublicParam.setPubParam1(mContext,param);
                            PublicParam.setPubParam2(mContext,param);
                            processUpload(uploadType,param);
                        }
                    }
                }catch (Exception e){

                }

            }
        });

    }

    /**
     * 删除过期数据
     */
    protected void checkExpiredCache(){
        mExcutor.execute(new Runnable() {
            @Override
            public void run() {
                try{
                    long overDrawCount = DBManager.getInstance(mContext).getDao(new OverDraw()).count();


                    if(overDrawCount > DELETE_EXPIRED_CACHE_SZIE){
                        DBManager.getInstance(mContext).deleteAll(OverDraw.class);


                        DelCacheParam param = new DelCacheParam();
                        PublicParam.setPubParam1(mContext,param);
                        PublicParam.setPubParam2(mContext,param);
                        param.et = 1;
                        param.overDraw = overDrawCount;
                        String json = mGson.toJson(param);
                        try {
                            Logger.e("数据溢出，clear！！！");
                            OkHttp.post(mRequest_url).upJson(json).execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }catch (Exception e){

                }

            }
        });

    }

    /**
     * 2、网络上传数据，失败存db
     * @param uploadType
     * @param param
     */
    private void processUpload(final int uploadType, final DataParam param){
        try{
            if(uploadType == UPLOAD_TYPE_OVERDRAW){
                uploadOverDraw(uploadType,param);
            }
        }catch (Exception e){

        }

    }

    /**
     * 3、批量网络上传
     * @param uploadType
     * @param param
     */
    public void batchUploadDb(final int uploadType,final int cacheType,final DataParam param){
        PublicParam.setPubParam1(mContext,param);
        String json = mGson.toJson(param);
        Logger.e(Utils.TAG,"上传db数据：" + json);
        try {
            OkHttp.post(mRequest_url).headers("Content-Encoding","gzip")
                    .upBytes(Utils.gzip(json).readByteArray())
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s) {
                            Logger.e("network success ，Thread:" + Thread.currentThread().getName() + " s :" + s);
                            List list = new ArrayList<>();
                            if(cacheType == UPLOAD_CACHE_OVERDRAW){
                                list = param.overDraws;
                            }

                            DBManager.getInstance(mContext).deleteList(list,new DBManager.DbCrudListener(){

                                @Override
                                public void doSuccess(Object object) {
                                    processUpload(uploadType,new DataParam());
                                }

                                @Override
                                public void doFail() {

                                }
                            });
                        }

                        @Override
                        public void onError(OkHttpException e) {
                            super.onError(e);
                            Logger.e("network fail ，Thread:"  + Thread.currentThread().getName());
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 3、网络上传Pv或者Launcher事件
     * @param uploadType
     * @param param
     */
    private void uploadOverDraw(final int uploadType, final DataParam param){
        PublicParam.setPubParam1(mContext,param);
        String json = mGson.toJson(param);
        Logger.e(Utils.TAG,"实时上传 " + uploadType + " 事件：" + json);
        try {
            OkHttp.post(mRequest_url).headers("Content-Encoding","gzip")
                    .upBytes(Utils.gzip(json).readByteArray())
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s) {
                            checkExpiredCache();
                        }

                        @Override
                        public void onError(OkHttpException e) {
                            super.onError(e);
                            if(uploadType == UPLOAD_TYPE_OVERDRAW) {
                                DBManager.getInstance(mContext).insertList(param.overDraws,new DBManager.DbCrudListener(){

                                    @Override
                                    public void doSuccess(Object object) {
                                        checkExpiredCache();
                                    }

                                    @Override
                                    public void doFail() {
                                        checkExpiredCache();
                                    }
                                });
                            }
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
