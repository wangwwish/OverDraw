package com.wwish.ganalytics.operation;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;

import com.wwish.ganalytics.GMUploadManager;
import com.wwish.ganalytics.db.entity.OverDraw;
import com.wwish.ganalytics.model.DataParam;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.SENSOR_SERVICE;
import static android.content.Context.VIBRATOR_SERVICE;

/**
 * Created by wangwei-ds10 on 2018/1/26.
 */

public class Shake implements SensorEventListener {
    private volatile static Shake instance;
    Activity activity;
    private SensorManager sensorManager;
    private Sensor maccelerometersensor;
    private Vibrator mVibrator;//手机震动
    //记录摇动状态
    private boolean isShake = false;

    private Shake(Activity activity){
        this.activity=activity;
    }

    public static Shake getInstance(Activity activity) {
        if (instance == null) {
            synchronized (Shake.class) {
                if (instance == null) {
                    instance = new Shake(activity);
                }
            }
        }
        return instance;
    }

    public void init(){
        //获取Vibrator震动服务
        mVibrator = (Vibrator) activity.getSystemService(VIBRATOR_SERVICE);
        //获取 SensorManager 负责管理传感器
        sensorManager = ((SensorManager) activity.getSystemService(SENSOR_SERVICE));
        if (sensorManager != null) {
            //获取加速度传感器
            maccelerometersensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (maccelerometersensor != null) {
                sensorManager.registerListener(this, maccelerometersensor, SensorManager.SENSOR_DELAY_UI);
            }
        }
    }

    public void cance(){
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int type = sensorEvent.sensor.getType();

        if (type == Sensor.TYPE_ACCELEROMETER) {
            //获取三个方向值
            float[] values = sensorEvent.values;
            float x = values[0];
            float y = values[1];
            float z = values[2];

            if ((Math.abs(x) > 17 || Math.abs(y) > 17 || Math
                    .abs(z) > 17) && !isShake) {
                GMUploadManager gmUploadManager = new GMUploadManager(activity);
                //todo
                gmUploadManager.batchUploadDb(GMUploadManager.UPLOAD_TYPE_OVERDRAW,1,buildPvInfo());
                System.out.print("shake......................................................");
                isShake = true;
            }
        }

    }

    private DataParam buildPvInfo(){




        return new DataParam();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
