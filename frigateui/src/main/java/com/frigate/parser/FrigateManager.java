package com.frigate.parser;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;

import com.frigate.event.IFrigateEvent;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by renqingyou on 2017/2/15.
 */

public final class FrigateManager {

    private static final HashMap<String, ArrayList<IFrigateEvent>> frigateEventListenerMap = new HashMap<>();

    public static final String RAW_DIRECTORY = "raw";

    private static ArrayList<IFrigateEvent> _getFrigateEventListeners(String eventName) {
        ArrayList<IFrigateEvent> arr = frigateEventListenerMap.get(eventName);
        return arr == null ? new ArrayList<IFrigateEvent>() : arr;
    }

    public static void registerFrigateEventListener(String eventName, IFrigateEvent eventListener) {
        if (eventName != null && eventListener != null) {
            ArrayList<IFrigateEvent> arr = _getFrigateEventListeners(eventName);
            if (!arr.contains(eventListener))
                arr.add(eventListener);
            frigateEventListenerMap.put(eventName, arr);
        }
    }

    public static void removeFrigateEventListener(String eventName, IFrigateEvent eventListener) {
        if (eventName != null && eventListener != null) {
            ArrayList<IFrigateEvent> arr = _getFrigateEventListeners(eventName);
            if (arr.contains(eventListener))
                arr.remove(eventListener);
            frigateEventListenerMap.put(eventName, arr);
        }
    }

    public static void removeAllFrigateEventListener(String eventName) {
        if (eventName != null) {
            ArrayList<IFrigateEvent> arr = _getFrigateEventListeners(eventName);
            arr.clear();
            frigateEventListenerMap.put(eventName, arr);
        }
    }

    public static void dispatchFrigateEvent(String eventName, String...eventParams) {
        if (eventName != null) {
            ArrayList<IFrigateEvent> arr = _getFrigateEventListeners(eventName);
            if (arr.size() != 0) {
                for (IFrigateEvent listener : arr) {
                    listener.onFrigateEvent(eventName, eventParams);
                }
            }
        }
    }

    public static String parseJson(Context context, String jsonName) {
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
            return json;
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
