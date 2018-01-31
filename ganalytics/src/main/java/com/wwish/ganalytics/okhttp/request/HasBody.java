package com.wwish.ganalytics.okhttp.request;


import java.io.File;
import java.util.List;

import com.wwish.ganalytics.okhttp.model.HttpParams;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * ================================================
 * 描    述：表示当前请求是否具有请求体
 * ================================================
 */
public interface HasBody<R> {
    R requestBody(RequestBody requestBody);

    R params(String key, File file);

    R addFileParams(String key, List<File> files);

    R addFileWrapperParams(String key, List<HttpParams.FileWrapper> fileWrappers);

    R params(String key, File file, String fileName);

    R params(String key, File file, String fileName, MediaType contentType);

    R upString(String string);

    R upJson(String json);

    R upBytes(byte[] bs);
}