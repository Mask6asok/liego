package com.mask.liego.entity;

public class UserInfo {
    private int uid;
    private String username;
    private String sno;
    private String phone;
    private double balance;

    public UserInfo() {

    }

    public UserInfo(String username, String sno, String phone) {
        this.username = username;
        this.sno = sno;
        this.phone = phone;
        this.balance = 0.00d;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
