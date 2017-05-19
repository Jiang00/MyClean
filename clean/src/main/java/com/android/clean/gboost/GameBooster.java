package com.android.clean.gboost;


import android.app.Activity;

import com.android.clean.util.CommonUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by renqingyou on 2017/5/8.
 */

public class GameBooster {

    public static ArrayList<String> getInstalledGameList(Activity context) {
        ArrayList<String> list = new ArrayList<>();
        try {
            String data = CommonUtil.readFileFromAssets(context, "raw/gboost.json");
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                String pkg = (String) jsonArray.get(i);
                if (CommonUtil.isPkgInstalled(pkg, context.getPackageManager())) {
                    list.add(pkg);
                }
            }
        } catch (Exception e) {
        }
        return list;
    }


}
