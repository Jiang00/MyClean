package com.supers.clean.junk.entity;

/**
 * Created by renqingyou on 2017/6/5.
 */

public class SmsInfo {
    public String id;
    public String address;

    public SmsInfo() {
    }

    public SmsInfo(String id, String address) {
        this.id = id;
        this.address = address;
    }

    @Override
    public String toString() {
        return "SmsInfo{" +
                "id='" + id + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
