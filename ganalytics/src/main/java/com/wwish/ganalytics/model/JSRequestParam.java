package com.wwish.ganalytics.model;

/**
 * Created by gaocaili on 2017/12/15.
 * native 跳转到js需要的参数
 */

public class JSRequestParam {
    public String dt;//设备型号 1：手机 2：pad
    public String os;//移动终端的软件版本
    public String osv;//获取手机型号Build.MODEL;
    public String sr;//screen_resolution: 实际分辨率+ppi
    public String av;//app_version: 内部+外部版本
    public String ic;//install_channel：安装渠道
    public String aid;//app_id: app包名 和上面参数合并
    public String pmc;//设备mac地址
    public String pb;//Build.BRAND
    public String pm;//Build.MODEL
    public String pi;//tm.getDeviceId()
    public String ll;//longitude and latitude经纬度 分隔符用|
    public String o;//服务商名称 tm.getSubscriberId()
    public String nt;//网络类型

    public String __gmz;//h5公参

    //pv信息
    public String sid;// session_id
    public String pid;//pageid
    public String cn;//类全路径
    public long     t;//pv发生的事件
    public String uid;
    public String lpid;//pageid
    public String lcn;//类全路径
    public long     lst;//pv发生的事件

}
