package com.mask.liego.entity;

import java.sql.Time;
import java.sql.Timestamp;

public class MsgInfo {
    private int mid;
    private int gid;
    private int uid;
    private String msg;
    private Timestamp mtime;

    public MsgInfo() {

    }

    public MsgInfo(int gid, int uid, String msg) {
        this.gid = gid;
        this.uid = uid;
        this.msg = msg;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
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

    public Timestamp getMtime() {
        return mtime;
    }

    public void setMtime(Timestamp mtime) {
        this.mtime = mtime;
    }

}
