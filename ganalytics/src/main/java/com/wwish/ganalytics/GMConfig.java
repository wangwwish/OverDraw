package com.wwish.ganalytics;



public class GMConfig {
     String appkey;//appkey
     boolean isCloseAnalytics;//是否关闭埋点 true:关闭 false：打开
     boolean isShowLogger = true;//是否打印log true：打印log
     boolean isDebug = true;//测试环境生产环境 true:测试环境

    public static class Builder{
        GMConfig config ;
        public Builder(){
            config = new GMConfig();
        }

        public Builder setAppkey(String appkey){
            config.appkey = appkey;
            return this;
        }

        public Builder setDebug(boolean isDebug){
            config.isDebug = isDebug;
            return this;
        }

        public Builder setShowLogger(boolean isShowLogger){
            config.isShowLogger = isShowLogger;
            return this;
        }

        public Builder setCloseAnalytics(boolean isClose){
            config.isCloseAnalytics = isClose;
            return this;
        }

        public GMConfig build(){
            return config;
        }
    }
}
