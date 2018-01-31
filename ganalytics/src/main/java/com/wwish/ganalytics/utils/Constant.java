package com.wwish.ganalytics.utils;

/**
 * Created by wangwei-ds10 on 2018/1/26.
 */

public class Constant {
    public static final String VERSION_GANALYTICS = "3.0.13-plus";
    public static final String HOST_TEST = "http://test-gb.mobile.gomeplus.com";
    public static final String HOST_PRD  = "https://gb.mobile.gomeplus.com";
    //测试环境地址
    public static final String REQUEST_URL_TEST     = HOST_TEST + "/app_log";
    //正式环境地址
    public static final String REQUEST_URL_PRD      = HOST_PRD + "/app_log";


    /**
     * 启动类型
     */
    public static final int INSTALL_FIRST               = 1;
    public static final int OPEN_FIRST                  = 2;
    public static final int BACKGROUND_TO_FOREGROUND    = 3;
    public static final int SLEEP_OPEN                  = 4;


    public static final String SDK_D_E_S_KEY        = "!G0Me@ANALYTICS*";

    // 可视化埋点功能最低API版本
    public static final int GMClick_SUPPORTED_MIN_API = 14;
}
