package com.security.manager;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.security.manager.lib.BaseApp;
import com.security.manager.lib.Utils;

import java.io.File;

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
