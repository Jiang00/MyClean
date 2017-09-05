package com.icleaner.clean.goodgame;


import android.app.Activity;

import com.icleaner.clean.utils.MyUtils;
import com.icleaner.clean.utils.MyConstant;
import com.icleaner.clean.utils.LoadManager;
import com.icleaner.clean.utils.PreData;

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
            if (PreData.getDB(context, MyConstant.GBOOST_LUN, true)) {
                PreData.putDB(context, MyConstant.GBOOST_LUN, false);
                String data = MyUtils.readFileFromAssets(context, "raw/gboost.json");
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