package com.easy.junk.easybroadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.easy.clean.easydb.CleanDBHelper;
import com.easy.clean.easyutils.MyUtils;
import com.easy.clean.easyutils.PreData;
import com.easy.junk.easyactivity.UnloadingResidualActivity;
import com.easy.junk.easytools.MyConstant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by on 2017/2/6.
 */

public class UnloadResidualBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (!PreData.getDB(context, MyConstant.UNLOAD_SWITCH, true)) {
            return;
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            context.startActivity(new Intent(context, UnloadingResidualActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("packageName", packageName));
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ArrayList<String> gboost_names = CleanDBHelper.getInstance(context).getWhiteList(CleanDBHelper.TableType.GameBoost);
                    try {
                        String data = MyUtils.readFileFromAssets(context, "raw/gboost.json");
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

