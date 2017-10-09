package com.supers.clean.junk.util;

import android.content.Context;

import com.android.clean.util.Constant;
import com.android.clean.util.PreData;


/**
 * Created by ${} on 2017/9/18.
 */

public class BadgerCount {
    public static void setCount(Context context) {
        int badger = 0;
        if (PreData.getDB(context, Constant.HONG_RAM, true)) {
            badger++;
        }
        if (PreData.getDB(context, Constant.HONG_JUNK, true)) {
            badger++;
        }
        if (PreData.getDB(context, Constant.HONG_CALL, true)) {
            badger++;
        }
        if (PreData.getDB(context, Constant.HONG_PRI, true)) {
            badger++;
        }
        if (PreData.getDB(context, Constant.HONG_NET, true)) {
            badger++;
        }
        if (PreData.getDB(context, Constant.HONG_NOTIFI, true)) {
            badger++;
        }
        if (PreData.getDB(context, Constant.HONG_FILE, true)) {
            badger++;
        }
        if (PreData.getDB(context, Constant.HONG_MANAGER, true)) {
            badger++;
        }
        if (PreData.getDB(context, Constant.HONG_DEEP, true)) {
            badger++;
        }
        if (PreData.getDB(context, Constant.HONG_PHOTO, true)) {
            badger++;
        }
        if (PreData.getDB(context, Constant.HONG_GBOOST, true)) {
            badger++;
        }
//        ShortcutBadger.applyCount(context, badger);
    }
}
