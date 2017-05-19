package com.supers.clean.junk.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.android.clean.util.CommonUtil;
import com.supers.clean.junk.activity.UnloadActivity;
import com.supers.clean.junk.util.Constant;
import com.supers.clean.junk.util.PreData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by on 2017/2/6.
 */

public class UnloadBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (!PreData.getDB(context, Constant.KEY_UNLOAD, true)) {
            return;
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            context.startActivity(new Intent(context, UnloadActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("packageName", packageName));
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ArrayList<String> gboost_names = PreData.getNameList(context, Constant.GBOOST_LIST);
                    try {
                        String data = CommonUtil.readFileFromAssets(context, "raw/gboost.json");
                        JSONObject jsonObject = new JSONObject(data);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String pkg = (String) jsonArray.get(i);
                            if (!gboost_names.contains(pkg)) {
                                PreData.addName(context, pkg, Constant.GBOOST_LIST);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}

