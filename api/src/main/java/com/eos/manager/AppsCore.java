package com.eos.manager;

import android.content.Context;

/**
 * Created by superjoy on 2014/9/1.
 */
public class AppsCore {

    public static final int INFO_PATH = 2;


    public static String ROOT;

    public static boolean init(Context c, String ROOT) {
        AppsCore.ROOT = ROOT;

        return true;
    }
}
