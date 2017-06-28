package com.bruder.clean.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.cleaner.sqldb.CleanDBHelper;
import com.cleaner.util.DataPre;
import com.cleaner.util.Util;
import com.bruder.clean.activity.UnloadingActivity;
import com.bruder.clean.util.Constant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by on 2017/2/6.
 */

public class UnloadingBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (!DataPre.getDB(context, Constant.KEY_UNLOAD, true)) {
            return;
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            context.startActivity(new Intent(context, UnloadingActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("packageName", packageName));
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

