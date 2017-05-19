package com.android.clean.whitelist;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.android.clean.util.CommonUtil;

import java.util.ArrayList;

/**
 * Created by renqingyou on 2017/5/19.
 */


public class WhiteListHelper extends SQLiteOpenHelper {
    public static final String BATTERY_DB = "whitelist.db";
    public static final int DB_VERSION = 1;

    public static final String PKG_NAME = "pkg_name";

    public static final String WHITELIST_TABLE_NAME = "whitelist";

    private static final String TAG = "WhiteListHelper";

    private static WhiteListHelper whiteListHelper;

    private Context mContext;

    private WhiteListHelper(Context context) {
        super(context.getApplicationContext(), BATTERY_DB, null, DB_VERSION);
        mContext = context.getApplicationContext();
    }

    public synchronized static WhiteListHelper getInstance(Context context) {
        if (whiteListHelper == null) {
            whiteListHelper = new WhiteListHelper(context);
        }
        return whiteListHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            //
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            //clearDB(db);
        }
    }

    private void createRecyclerTable() {
        getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS " + WHITELIST_TABLE_NAME + " (" +
                PKG_NAME + " TEXT NOT NULL, " +
                "PRIMARY KEY (" + PKG_NAME + ") " +
                ");");
    }

    private void deleteTable(SQLiteDatabase db, String tableName) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
    }

    private boolean deleteItem(String pkgName) {
        String sql = "delete from " + WHITELIST_TABLE_NAME + "  where " + PKG_NAME + "=" + pkgName;
        try {
            return getWritableDatabase().compileStatement(sql).executeUpdateDelete() != -1;
        } catch (Exception e) {
            Log.e(TAG, "deleteItemFromFavor Error pkgName=" + pkgName);
        }
        return false;
    }

    private boolean addItem(String pkgName) {
        createRecyclerTable();
        String sql = "insert into " + WHITELIST_TABLE_NAME + "(" + PKG_NAME + ")" +
                "values(?)";
        //db.beginTransaction();
        try {
            SQLiteStatement stat = getWritableDatabase().compileStatement(sql);
            stat.bindString(1, pkgName);
            return stat.executeInsert() != -1;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "addItemToFavor Error tableName=" + WHITELIST_TABLE_NAME + pkgName);
        }
//        db.setTransactionSuccessful();
//        db.endTransaction();
        return false;
    }


    public ArrayList<String> getWhiteList() {
        //先删除过期文件

        ArrayList<String> whiteList = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = getWritableDatabase().rawQuery("select " + PKG_NAME + " from " + WHITELIST_TABLE_NAME, null);
            if (cursor == null || cursor.getCount() == 0) {
                return whiteList;
            }
            cursor.moveToFirst();
            do {
                //遍历出表名
                String pkgName = cursor.getString(0);
                if (!CommonUtil.isPkgInstalled(pkgName, mContext.getPackageManager())) {
                    deleteItem(pkgName);
                    continue;
                }
                whiteList.add(pkgName);
            } while (cursor.moveToNext());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("rqy", "e.message=" + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return whiteList;
    }

}

