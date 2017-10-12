package com.bruder.clean.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;


public final class PreData {

    private PreData() {
    }

    // ****************** SharedPreference Start ******************//
    private static int _putCount = 0;
    private static SharedPreferences db = null;

    private static SharedPreferences getDB(Context context) {
        if (db == null && context != null)
            db = context.getSharedPreferences(Constant.SHARED_FILE, 0);
        return db;
    }

    public static void saveDB(Context context) {
        SharedPreferences db = getDB(context);
        if (db != null) {
            saveDB(db.edit(), true);
        }
    }


    private static void saveDB(SharedPreferences.Editor e, boolean force) {
        if (e != null) {
            if (force) {
                e.commit();
                _putCount = 0;
            } else {
                if (_putCount++ >= 9) {
                    e.commit();
                    _putCount = 0;
                } else {
                    if (Build.VERSION.SDK_INT >= 9) {
                        e.apply();
                    } else {
                        e.commit();
                        _putCount = 0;
                    }
                }
            }
        }
    }

    public static <T> void putDB(Context context, String key, T value) {
        try {
            SharedPreferences db = getDB(context);
            if (db != null) {
                SharedPreferences.Editor e = db.edit();
                if (value instanceof String) {
                    e.putString(key, (String) value);
                } else if (value instanceof Integer) {
                    e.putInt(key, (Integer) value);
                } else if (value instanceof Boolean) {
                    e.putBoolean(key, (Boolean) value);
                } else if (value instanceof Long) {
                    e.putLong(key, (Long) value);
                } else if (value instanceof Float) {
                    e.putFloat(key, (Float) value);
                }
                saveDB(e, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean removeDB(Context context, String key) {
        try {
            SharedPreferences db = getDB(context);
            if (db != null) {
                SharedPreferences.Editor e = db.edit();
                e.remove(key);
                saveDB(e, false);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean hasDB(Context context, String key) {
        SharedPreferences db = getDB(context);
        if (db != null) {
            return db.contains(key);
        }
        return false;
    }

    public static int getDB(Context context, String key, int defValue) {
        SharedPreferences db = getDB(context);
        if (db != null) {
            if (db.contains(key)) {
                return db.getInt(key, defValue);
            } else {
                db.edit().putInt(key, defValue).commit();
            }
        }
        return defValue;
    }

    public static long getDB(Context context, String key, long defValue) {
        SharedPreferences db = getDB(context);
        if (db != null) {
            if (db.contains(key)) {
                return db.getLong(key, defValue);
            } else {
                db.edit().putLong(key, defValue).commit();
            }
        }
        return defValue;
    }

    public static String getDB(Context context, String key, String defValue) {
        SharedPreferences db = getDB(context);
        if (db != null) {
            if (db.contains(key)) {
                return db.getString(key, defValue);
            } else {
                db.edit().putString(key, defValue).commit();
            }
        }
        return defValue;
    }

    public static boolean getDB(Context context, String key, boolean defValue) {
        SharedPreferences db = getDB(context);
        if (db != null) {
            if (db.contains(key)) {
                return db.getBoolean(key, defValue);
            } else {
                db.edit().putBoolean(key, defValue).commit();
            }
        }
        return defValue;
    }


}
