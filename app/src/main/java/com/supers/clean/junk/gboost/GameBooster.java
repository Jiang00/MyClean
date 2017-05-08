package com.supers.clean.junk.gboost;


import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;

import com.supers.clean.junk.util.CommonUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by renqingyou on 2017/5/8.
 */

public class GameBooster {

    public static ArrayList<String> getInstalledGameList(Context context) {
        ArrayList<String> pkgs = new ArrayList<>();
        try {
            String data = readFileFromAssets(context, "raw/gboost.json");
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++) {
                String pkg = (String) jsonArray.get(i);
                if (CommonUtil.isPkgInstalled(pkg, context.getPackageManager())) {
                    pkgs.add(pkg);
                    Log.e("rqy", "getInstalledGameList--pkg=" + pkg);
                }
            }
        } catch (Exception e) {
            Log.e("rqy", "error message--" + e.getMessage());
        }
        return pkgs;
    }

    public static String readFileFromAssets(Context context, String fileName) throws IOException, IllegalArgumentException {
        if (null == context || TextUtils.isEmpty(fileName)) {
            throw new IllegalArgumentException("bad arguments!");
        }
        AssetManager assetManager = context.getAssets();
        InputStream input = assetManager.open(fileName);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = input.read(buffer)) != -1) {
            output.write(buffer, 0, length);
        }
        output.close();
        input.close();
        return output.toString();
    }

}
