package com.mask.liego.entity;

public class GoodsInfo {
    private int gid;
    private String gname;
    private int gtype;
    private int uid;
    private double price;
    private int count;
    private int descriptionID;

    public GoodsInfo() {

    }

    public GoodsInfo(String gname, int gtype, int uid, double price, int count, int descriptionID) {
        this.gname = gname;
        this.gtype = gtype;
        this.uid = uid;
        this.price = price;
        this.count = count;
        this.descriptionID = descriptionID;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public int getGtype() {
        return gtype;
    }

    public void setGtype(int gtype) {
        this.gtype = gtype;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getDescriptionID() {
        return descriptionID;
    }

    public void setDescriptionID(int descriptionID) {
        this.descriptionID = descriptionID;
    }
}
