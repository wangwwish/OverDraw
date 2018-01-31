package com.wwish.ganalytics.okhttp.convert;

import android.os.Environment;
import android.text.TextUtils;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.wwish.ganalytics.okhttp.OkHttp;
import com.wwish.ganalytics.okhttp.callback.AbsCallback;
import com.wwish.ganalytics.okhttp.utils.HttpUtils;
import com.wwish.ganalytics.okhttp.utils.Logger;
import okhttp3.Response;

/**
 * ================================================
 * 描    述：字符串的转换器
 * ================================================
 */
public class FileConvert implements Converter<File> {

    public static final String DM_TARGET_FOLDER = File.separator + "download" + File.separator; //下载目标文件夹

    private String destFileDir;     //目标文件存储的文件夹路径
    private String destFileName;    //目标文件存储的文件名
    private AbsCallback callback;    //下载回调

    public FileConvert() {
        this(null);
    }

    public FileConvert(String destFileName) {
        this(Environment.getExternalStorageDirectory() + DM_TARGET_FOLDER, destFileName);
    }

    public FileConvert(String destFileDir, String destFileName) {
        this.destFileDir = destFileDir;
        this.destFileName = destFileName;
    }

    public void setCallback(AbsCallback callback) {
        this.callback = callback;
    }

    @Override
    public File convertSuccess(Response value) throws Exception {
        if (TextUtils.isEmpty(destFileDir)) destFileDir = Environment.getExternalStorageDirectory() + DM_TARGET_FOLDER;
        if (TextUtils.isEmpty(destFileName)) destFileName = HttpUtils.getNetFileName(value, value.request().url().toString());

        File dir = new File(destFileDir);
        if (!dir.exists()) dir.mkdirs();
        File file = new File(dir, destFileName);
        if (file.exists()) file.delete();

        long lastRefreshUiTime = 0;  //最后一次刷新的时间
        long lastWriteBytes = 0;     //最后一次写入字节数据

        InputStream is = null;
        byte[] buf = new byte[2048];
        FileOutputStream fos = null;
        try {
            is = value.body().byteStream();
            final long total = value.body().contentLength();
            long sum = 0;
            int len;
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                sum += len;
                fos.write(buf, 0, len);

                //下载进度回调
                if (callback != null) {
                    final long finalSum = sum;
                    long curTime = System.currentTimeMillis();
                    //每200毫秒刷新一次数据
                    if (curTime - lastRefreshUiTime >= OkHttp.REFRESH_TIME || finalSum == total) {
                        //计算下载速度
                        long diffTime = (curTime - lastRefreshUiTime) / 1000;
                        if (diffTime == 0) diffTime += 1;
                        long diffBytes = finalSum - lastWriteBytes;
                        final long networkSpeed = diffBytes / diffTime;
                        OkHttp.getInstance().getDelivery().post(new Runnable() {
                            @Override
                            public void run() {
                                callback.downloadProgress(finalSum, total, finalSum * 1.0f / total, networkSpeed);   //进度回调的方法
                            }
                        });

                        lastRefreshUiTime = System.currentTimeMillis();
                        lastWriteBytes = finalSum;
                    }
                }
            }
            fos.flush();
            return file;
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
                Logger.e(e);
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
                Logger.e(e);
            }
        }
    }
}