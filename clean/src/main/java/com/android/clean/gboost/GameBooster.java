package com.android.clean.gboost;


import android.app.Activity;

import com.android.clean.util.Util;
import com.android.clean.util.Constant;
import com.android.clean.util.LoadManager;
import com.android.clean.util.PreData;

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
            if (PreData.getDB(context, Constant.GBOOST_LUN, true)) {
                PreData.putDB(context, Constant.GBOOST_LUN, false);
                String data = Util.readFileFromAssets(context, "raw/gboost.json");
                JSONObject jsonObject = new JSONObject(data);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    String pkg = (String) jsonArray.get(i);
                    if (LoadManager.getInstance(context).isPkgInstalled(pkg)) {
                        list.add(pkg);
                    }
                }
            }
        } catch (Exception e) {
        }


        return list;
    }


}
