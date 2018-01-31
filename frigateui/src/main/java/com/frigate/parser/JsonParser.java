package com.frigate.parser;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * IVY (2016) All Rights Reserved.
 * Created by renqingyou on 11/08/16.
 */
public class JsonParser {
    public static String TAG = "jsonParser";
    /*public static JsonParser jsonParser;

    public static JsonParser getInstance() {
        if (jsonParser == null) {
            jsonParser = new JsonParser();
        }
        return jsonParser;
    }*/

    public static String getStrFromByte(byte[] responseBody) {
        String response = null;
        if (responseBody != null) {
            try {
                response = new String(responseBody, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    public static ArrayList<FrigateData> fromJson(String json) {
        ArrayList<FrigateData> frigateDataList = new ArrayList<>();
        if (!TextUtils.isEmpty(json)) {
            JSONArray jsonArray;
            try {
                JSONObject jsonObject = new JSONObject(json);
                jsonArray = jsonObject.getJSONArray("data");
            } catch (Exception e) {
                Log.e(TAG, "json parser error " + e.getMessage());
                return frigateDataList;
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    String tag = "";
                    if (jo.has(FrigateData.TAG)) {
                        tag = jo.getString(FrigateData.TAG);
                    }
                    String click = "";
                    if (jo.has(FrigateData.CLICK)) {
                        click = jo.getString(FrigateData.CLICK);
                    }
                    String type = "";
                    if (jo.has(FrigateData.TYPE)) {
                        type = jo.getString(FrigateData.TYPE);
                    }
                    String long_click = "";
                    if (jo.has(FrigateData.LONG_CLICK)) {
                        long_click = jo.getString(FrigateData.LONG_CLICK);
                    }
                    frigateDataList.add(new FrigateData(tag, type, click, long_click));
                } catch (Exception e) {
                    continue;
                }
            }

        }
        return frigateDataList;
    }
}
