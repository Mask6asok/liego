package com.mask.liego.entity;

import java.sql.Timestamp;

public class GoodsPublish {
    private int pubid;
    private int gid;
    private Timestamp pubTime;
    private int display;

    public GoodsPublish() {
    }

    public GoodsPublish(int gid, Timestamp pubTime) {
        this.gid = gid;
        this.pubTime = pubTime;
        this.display = 1;
    }

    public int getPubid() {
        return pubid;
    }

    public void setPubid(int pubid) {
        this.pubid = pubid;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public Timestamp getPubTime() {
        return pubTime;
    }

    public void setPubTime(Timestamp pubTime) {
        this.pubTime = pubTime;
    }

    public int getDisplay() {
        return display;
    }

    public void setDisplay(int display) {
        this.display = display;
    }
}
