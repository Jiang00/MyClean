package com.supers.clean.junk.util;

import android.content.Context;

import com.android.clean.util.Constant;
import com.android.clean.util.PreData;

import me.leolin.shortcutbadger.ShortcutBadger;


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
        if (PreData.getDB(context, Constant.HONG_MANAGER, true)) {
            badger++;
        }
        ShortcutBadger.applyCount(context, badger);
    }
}
