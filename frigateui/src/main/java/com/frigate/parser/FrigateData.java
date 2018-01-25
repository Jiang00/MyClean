package com.frigate.parser;

/**
 * Created by renqingyou on 2017/2/15.
 */

public class FrigateData {

    public static final String TAG = "tag";
    public static final String CLICK = "click";
    public static final String LONG_CLICK = "long_click";
    public static final String TYPE = "type";

    public String tag;
    public String click;
    public String long_click;
    public String type;

    @Override
    public String toString() {
        return "FrigateData{" +
                "tag='" + tag + '\'' +
                ", click='" + click + '\'' +
                ", long_click='" + long_click + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public FrigateData() {

    }

    public FrigateData(String tag, String type, String click, String long_click) {
        this.tag = tag;
        this.type = type;
        this.click = click;
        this.long_click = long_click;
    }

}

