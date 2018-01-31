package com.wwish.ganalytics.okhttp.exception;

/**
 * ================================================
 * 描    述：异常封装
 * ================================================
 */
public class OkHttpException extends Exception {

    public static OkHttpException INSTANCE(String errorMsg, String errorCode) {
        return new OkHttpException(errorMsg,errorCode);
    }

    public String errorCode;
    public String errorMsg;

    public OkHttpException(String detailMessage, String errorCode) {
        super(detailMessage);
        this.errorCode = errorCode;
        this.errorMsg = detailMessage;
    }

}