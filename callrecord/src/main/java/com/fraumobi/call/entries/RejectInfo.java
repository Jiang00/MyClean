package com.fraumobi.call.entries;

/**
 * Created by Ivy on 2017/4/27.
 */

public class RejectInfo {

    public String name;
    public String phoneNum;
    public String date;
    public String direct;
    public boolean isChecked;

    public RejectInfo(String name, String number, String date, String direct) {
        this.name = name;
        this.phoneNum = number;
        this.date = date;
        this.direct = direct;
    }

    @Override
    public String toString() {
        return "name=" + name + " phoneNum=" + phoneNum + " date=" + date + " direct=" + direct;
    }

}
