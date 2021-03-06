package com.supers.clean.junk.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.android.clean.core.CleanManager;
import com.android.clean.db.CleanDBHelper;
import com.android.clean.entity.JunkInfo;
import com.android.clean.util.PreData;
import com.android.clean.util.Util;
import com.supers.clean.junk.activity.UnloadActivity;
import com.android.clean.util.Constant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by on 2017/2/6.
 */

public class UnloadBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
//        if (intent.getAction().equals(Intent.ACTION_PACKAGE_FULLY_REMOVED)) {
//            String packageName = intent.getData().getSchemeSpecificPart();
//            Log.e("broadcast", packageName + "1");
//        }
//        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
//            String packageName = intent.getData().getSchemeSpecificPart();
//            Log.e("broadcast", packageName + "2");
//        }
//        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
//            String packageName = intent.getData().getSchemeSpecificPart();
//            Log.e("broadcast", packageName + "3");
//        }
//        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
//            String packageName = intent.getData().getSchemeSpecificPart();
//            Log.e("broadcast", packageName + "4");
//        }
        if (!PreData.getDB(context, Constant.KEY_UNLOAD, true)) {
            return;
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_FULLY_REMOVED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            context.startActivity(new Intent(context, UnloadActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("packageName", packageName));
        }

        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ArrayList<String> gboost_names = CleanDBHelper.getInstance(context).getWhiteList(CleanDBHelper.TableType.GameBoost);
                    try {
                        String data = Util.readFileFromAssets(context, "raw/gboost.json");
                        JSONObject jsonObject = new JSONObject(data);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String pkg = (String) jsonArray.get(i);
                            if (!gboost_names.contains(pkg)) {
                                CleanDBHelper.getInstance(context).addItem(CleanDBHelper.TableType.Ram, pkg);
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