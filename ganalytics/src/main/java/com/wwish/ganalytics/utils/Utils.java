package com.wwish.ganalytics.utils;

import android.content.Context;
import android.os.Environment;

import com.wwish.ganalytics.okhttp.utils.Logger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okio.Buffer;
import okio.BufferedSink;
import okio.GzipSink;
import okio.Okio;

/**
 * Created by wangwei-ds10 on 2018/1/26.
 */

public class Utils {
    public static final String TAG = "OverDrawView";

    public static void copy(String path, String copyPath) throws IOException {
        File filePath = new File(path);
        DataInputStream read ;
        DataOutputStream write;
        if(filePath.isDirectory()){
            File[] list = filePath.listFiles();
            for(int i=0; i<list.length; i++){
                String newPath = path + File.separator + list[i].getName();
                String newCopyPath = copyPath + File.separator + list[i].getName();
                File newFile = new File(copyPath);
                if(!newFile.exists()){
                    Logger.e("","copyPath:" + copyPath + " : "+ newFile.mkdirs());
                }
                copy(newPath, newCopyPath);
            }
        }else if(filePath.isFile()){
            read = new DataInputStream(
                    new BufferedInputStream(new FileInputStream(path)));

            write = new DataOutputStream(
                    new BufferedOutputStream(new FileOutputStream(copyPath)));
            byte [] buf = new byte[1024*512];
            while(read.read(buf) != -1){
                write.write(buf);
            }
            read.close();
            write.close();
        }else{
            System.out.println("请输入正确的文件名或路径名");
        }
    }

    /**
     * sessionid是否过期 ,超过1天视为过期
     */
    public static boolean isOutofSession(long preTime) {
        long days = Math.abs((System.currentTimeMillis() - preTime))
                / (1000 * 60 * 60 * 24);
        return  days >= 1;
    }

    /**
     * 1. 启动类型为1时生成新sessionId；
     * 2. 启动类型为3和4 时，判断sessionid创建时间是否超过一天（超过24点，即：day变为day+1时认为过期），过期则生成新sessionId；
     * @return
     */
    public static String  getTimeSecondFrom2011() {
        Date dt = new Date();

        long beginTime = 0;
        try {
            SimpleDateFormat formater = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = formater.parse("2011-01-01 00:00:00");
            beginTime = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long time = dt.getTime() - beginTime;

        return String.valueOf(time / 1000);
    }

    public static Buffer gzip(String data) throws IOException {
        Buffer result = new Buffer();
        BufferedSink gzipSink = Okio.buffer(new GzipSink(result));
        gzipSink.writeUtf8(data);
        gzipSink.close();
        return result;
    }


    public static void asynCopy(final Context context){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String packageName = DeviceUtils.getPackageName(context);
                    Utils.copy("/data/data/" + packageName, Environment.getExternalStorageDirectory().toString()+"/" +packageName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
