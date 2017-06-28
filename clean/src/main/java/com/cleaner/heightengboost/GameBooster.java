package com.cleaner.heightengboost;


import android.app.Activity;

import com.cleaner.util.Util;
import com.cleaner.util.Constant;
import com.cleaner.util.LoadManager;
import com.cleaner.util.DataPre;

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
            if (DataPre.getDB(context, Constant.GBOOST_LUN, true)) {
                DataPre.putDB(context, Constant.GBOOST_LUN, false);
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
