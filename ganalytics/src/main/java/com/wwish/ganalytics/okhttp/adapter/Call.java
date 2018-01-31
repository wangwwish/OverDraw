package com.wwish.ganalytics.okhttp.adapter;


import com.wwish.ganalytics.okhttp.callback.AbsCallback;
import com.wwish.ganalytics.okhttp.model.Response;
import com.wwish.ganalytics.okhttp.request.BaseRequest;

/**
 * ================================================
 * 描    述：请求的包装类
 * ================================================
 */
public interface Call<T> {
    /** 同步执行 */
    Response<T> execute() throws Exception;

    /** 异步回调执行 */
    void execute(AbsCallback<T> callback);

    /** 是否已经执行 */
    boolean isExecuted();

    /** 取消 */
    void cancel();

    /** 是否取消 */
    boolean isCanceled();

    Call<T> clone();

    BaseRequest getBaseRequest();
}