package com.wwish.ganalytics.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by wangwei-ds10 on 2018/1/25.
 */
@Entity
public class OverDraw {
    @Id(autoincrement = true)
    private Long id;
    //页面名字
    @Property(nameInDb = "name")
    String name;
    //页面过度渲染值
    @Property(nameInDb = "over")
    String over;
    //插入时间
    @Property(nameInDb = "t")
    private long t;
    //应用版本
    @Property(nameInDb = "version")
    private String version;
    //是否上报过
    @Property(nameInDb = "upload")
    private boolean upload;

    @Generated(hash = 2134824156)
    public OverDraw(Long id, String name, String over, long t, String version,
            boolean upload) {
        this.id = id;
        this.name = name;
        this.over = over;
        this.t = t;
        this.version = version;
        this.upload = upload;
    }

    @Generated(hash = 827110852)
    public OverDraw() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOver() {
        return over;
    }

    public void setOver(String over) {
        this.over = over;
    }

    public long getT() {
        return t;
    }

    public void setT(long t) {
        this.t = t;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isUpload() {
        return upload;
    }

    public void setUpload(boolean upload) {
        this.upload = upload;
    }

    public boolean getUpload() {
        return this.upload;
    }
}
