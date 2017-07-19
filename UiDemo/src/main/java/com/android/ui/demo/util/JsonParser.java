package com.android.ui.demo.util;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {

    public static JSONObject getSpecifyJsonObject(Context context, String data, String tag){
        if (data == null || tag == null || context == null) {
            return null;
        }
        int index = 0;
        try {
            index = Utils.getCurrentCrossIndex(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            JSONObject parent = new JSONObject(data);
            JSONArray array = parent.optJSONArray(tag);
            int length = array.length() - 1;
            if (index > length) {
                index = length;
            }
            JSONObject object = array.optJSONObject(index);
            if (index >= length) {
                index = 0;
            } else {
                ++index;
            }
            try {
                Utils.updateCrossIndex(context, index);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return object;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}
