package com.ds.arthas.logistics.entity;

/**
 * Created by Administrator on 2016/8/15 0015.
 */
public class UserInfoEntity {

    /**
     * uid : 1
     * username : a1
     * nick : 小a
     * phone : 111
     * working : 1
     */

    private String uid;
    private String username;
    private String nick;
    private String phone;
    private String working;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWorking() {
        return working;
    }

    public void setWorking(String working) {
        this.working = working;
    }
}
