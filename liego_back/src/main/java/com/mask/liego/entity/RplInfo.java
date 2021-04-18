package com.mask.liego.entity;

import java.sql.Timestamp;

public class RplInfo {
    private int rid;
    private int mid;
    private int uid;
    private String msg;
    private Timestamp rtime;

    public RplInfo() {

    }

    public RplInfo(int mid, int uid, String msg) {
        this.mid = mid;
        this.uid = uid;
        this.msg = msg;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Timestamp getRtime() {
        return rtime;
    }

    public void setRtime(Timestamp rtime) {
        this.rtime = rtime;
    }
}
