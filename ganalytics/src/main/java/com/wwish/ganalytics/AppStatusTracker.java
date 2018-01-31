package com.wwish.ganalytics;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;


import com.wwish.ganalytics.operation.Shake;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



/**
 * SDK內部接口
 */

final class AppStatusTracker implements Application.ActivityLifecycleCallbacks {

    private static volatile AppStatusTracker mInstance;
    private int mLauncherType;
    private Application mGlobalApplication;
    private ExecutorService mExecutorService;
    private boolean isLockScreen = true;//true：亮 false：暗
    private int mCount;
    private int mBuildNewPid ;


    private AppStatusTracker(Application application){
        mGlobalApplication = application;
        mExecutorService = Executors.newSingleThreadExecutor();
    }

    public static AppStatusTracker getInstance(Application application){
        if(mInstance == null){
            synchronized (AppStatusTracker.class){
                if(mInstance == null) {
                    mInstance = new AppStatusTracker(application);
                }
            }
        }
        return mInstance;
    }

    public void registerGMActivityLifecycleCallbacks(){
        mGlobalApplication.registerActivityLifecycleCallbacks(this);
    }

    public void unRegisterGMActivityLifecycleCallbacks(){
        mGlobalApplication.unregisterActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        mBuildNewPid = -1;
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Shake.getInstance(activity).init();
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {
        OverDrawView.printActivityOverDrawCounter(activity);
        Shake.getInstance(activity).cance();
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }


}
