package com.vater.clean.game;


import android.app.Activity;

import com.vater.clean.util.Util;
import com.vater.clean.util.Constant;
import com.vater.clean.util.LoadManager;
import com.vater.clean.util.PreData;

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
