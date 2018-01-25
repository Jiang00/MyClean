package com.frigate.parser;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by renqingyou on 2017/2/15.
 */

public class FrigateManager {

    public static final String RAW_DIRECTORY = "raw";

    public static ArrayList<FrigateData> parseJson(Context context, String jsonName) {
        if (TextUtils.isEmpty(jsonName)) {
            return null;
        }
        if (!isExistJsonFile(context, jsonName)) {
            return null;
        }
        try {
            InputStream e = context.getAssets().open(RAW_DIRECTORY + "/" + jsonName);
            byte[] buffer = new byte[e.available()];
            e.read(buffer);
            e.close();
            String json = JsonParser.getStrFromByte(buffer);
            return JsonParser.fromJson(json);
        } catch (Exception e) {
            return null;
        }

    }

    public static boolean isExistJsonFile(Context context, String jsonName) {
        AssetManager assetManager = context.getAssets();
        String[] files = null;
        try {
            files = assetManager.list(RAW_DIRECTORY);
        } catch (IOException e) {
            Log.e("rqy", e.getMessage());
        }
        if (files == null) {
            return false;
        }
        int length = files.length;
        boolean hasFile = false;
        for (int i = 0; i < length; i++) {
            if (files[i].equals(jsonName)) {
                hasFile = true;
                break;
            }
        }
        return hasFile;

    }
}
