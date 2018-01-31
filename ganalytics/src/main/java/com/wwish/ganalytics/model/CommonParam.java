package com.wwish.ganalytics.model;


public class CommonParam {
    //公参1
    public String dt;//设备型号 1：手机 2：pad
    public String os;//移动终端的软件版本
    public String osv;//获取手机型号Build.MODEL;
    public String sv;//埋点sdk版本号
    public String aid;//app_id: app包名 和上面参数合并
    public String pmc;//设备mac地址
    public String pb;//Build.BRAND
    public String pm;//Build.MODEL
    public String pi;//tm.getDeviceId()
    public String o;//服务商名称 tm.getSubscriberId()

    public String av;//app_version: 内部+外部版本
    public String ic;//install_channel：安装渠道
    public String ll;//longitude and latitude经纬度 分隔符用|
    public String s;//station 基站
    public String sr;//screen_resolution: 实际分辨率+ppi
    public String c;//cmpid  打开渠道 ：push 微信


    //参数2
    public String cid;//设备id uuid.时间戳 ，uuid通过plus的uuid生成方式获取
    public long   st;//当前时间
    public String ak;//appkey
    public String nt;//网络类型
    public String __gmz;//h5公参

}
