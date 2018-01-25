package com.frigate.parser;

import android.text.TextUtils;

/**
 * Created by renqingyou on 2017/2/16.
 */

public class Command {
    public static final String DO = "do:";
    public static final String GO = "go:";

    /**
     * ILLEGAL COMMAND
     */
    public static final int TYPE_ILLEGAL_COMMAND = -1;
    /**
     * DO COMMAND
     */
    public static final int TYPE_DO = 0;
    /**
     * GO COMMAND
     */
    public static final int TYPE_GO = 1;

    public static int getCommandType(String action) {
        if (TextUtils.isEmpty(action)) {
            return TYPE_ILLEGAL_COMMAND;
        }
        String trimAction = action.trim();
        if (trimAction.startsWith(DO)) {
            return TYPE_DO;
        } else if (trimAction.startsWith(GO)) {
            return TYPE_GO;
        }
        return TYPE_ILLEGAL_COMMAND;
    }

    public static String getCommandAction(int commandType, String action) {
        if (TextUtils.isEmpty(action)){
            return null;
        }
        String trimAction = action.trim();
        if (commandType == TYPE_DO) {
            return trimAction.replaceFirst(DO, "");
        } else if (commandType == TYPE_GO) {
            return trimAction.replaceFirst(GO, "");
        }
        return null;
    }

    public static String getCommandAction(String action) {
        return getCommandAction(getCommandType(action), action);
    }


}
