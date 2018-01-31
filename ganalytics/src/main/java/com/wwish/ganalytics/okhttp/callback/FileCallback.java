package com.wwish.ganalytics.okhttp.callback;



import java.io.File;

import com.wwish.ganalytics.okhttp.convert.FileConvert;
import okhttp3.Response;

/**
 * ================================================
 * 描    述：文件的回调下载进度监听
 * ================================================
 */
public abstract class FileCallback extends AbsCallback<File> {

    private FileConvert convert;    //文件转换类

    public FileCallback() {
        this(null);
    }

    public FileCallback(String destFileName) {
        this(null, destFileName);
    }

    public FileCallback(String destFileDir, String destFileName) {
        convert = new FileConvert(destFileDir, destFileName);
        convert.setCallback(this);
    }

    @Override
    public File convertSuccess(Response response) throws Exception {
        File file = convert.convertSuccess(response);
        response.close();
        return file;
    }
}