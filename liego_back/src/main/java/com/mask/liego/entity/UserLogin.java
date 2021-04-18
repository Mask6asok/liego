package com.mask.liego.entity;

public class UserLogin {
    private int uid;
    private String username;
    private String password;
    private int authority;

    public UserLogin() {

    }

    public UserLogin(String username, String password) {
        this.username = username;
        this.password = password;
        this.authority = 1;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAuthority() {
        return authority;
    }

    public void setAuthority(int authority) {
        this.authority = authority;
    }
}
