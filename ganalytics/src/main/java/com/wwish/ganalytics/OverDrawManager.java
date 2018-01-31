package com.wwish.ganalytics;

import android.app.Application;
import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.wwish.ganalytics.okhttp.OkHttp;

/**
 * Created by wangwei-ds10 on 2018/1/25.
 */

public final class OverDrawManager {
    private volatile static OverDrawManager mInstance;

    private ExecutorService mExcutor;
    private Context mContext;
    private boolean isNeedNewPid = true;
    private volatile String mCurrentPageId;
    private volatile boolean isClose;//true:关闭埋点sdk


    //当前activity是否上报了pv
    boolean mUploadCurrentActivityPv = false;




    private OverDrawManager(Context context) {
        mContext = context.getApplicationContext();

        mExcutor = Executors.newSingleThreadExecutor();

    }

    public static OverDrawManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (OverDrawManager.class) {
                if (mInstance == null) {
                    mInstance = new OverDrawManager(context);
                }
            }
        }
        return mInstance;
    }

    public void init(Application context) {

        AppStatusTracker.getInstance(context).registerGMActivityLifecycleCallbacks();
        OkHttp.getInstance().initOkhttp(context);
    }
}
