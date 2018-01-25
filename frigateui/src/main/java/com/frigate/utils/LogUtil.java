package com.frigate.utils;

import android.util.Log;

/**
 * Created by zhy on 15/11/18.
 */
public class LogUtil {
    public static boolean debug = false;
    private static final String TAG = "AUTO_LAYOUT";

    public static void e(String msg) {
        if (debug) {
            Log.e(TAG, msg);
        }
    }
}
