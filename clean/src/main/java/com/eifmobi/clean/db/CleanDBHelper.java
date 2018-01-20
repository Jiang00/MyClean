package com.eifmobi.clean.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.eifmobi.clean.util.LoadManager;

import java.util.ArrayList;

/**
 * Created by renqingyou on 2017/5/19.
 */

public class CleanDBHelper extends SQLiteOpenHelper {
    public static final String CLEAN_DB = "clean.db";
    public static final int DB_VERSION = 1;

    public static final String PKG_NAME = "pkg_name";

    public static final String RAM_CLEAR_TABLE_NAME = "ram";

    public static final String NOTIFICATION_CLEAR_TABLE_NAME = "notification";

    public static final String GAME_BOOST_TABLE_NAME = "game_boost";

    private static final String TAG = "CleanDBHelper";

    private static CleanDBHelper cleanDBHelper;

    private Context mContext;

    public enum TableType {
        Ram, Notification, GameBoost
    }

    private CleanDBHelper(Context context) {
        super(context.getApplicationContext(), CLEAN_DB, null, DB_VERSION);
        mContext = context.getApplicationContext();
    }

    public synchronized static CleanDBHelper getInstance(Context context) {
        if (cleanDBHelper == null) {
            cleanDBHelper = new CleanDBHelper(context);
        }
        return cleanDBHelper;
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

    private void createTable(TableType tableType) {
        String table_name = getTableName(tableType);
        getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS " + table_name + " (" +
                PKG_NAME + " TEXT NOT NULL, " +
                "PRIMARY KEY (" + PKG_NAME + ") " +
                ");");
    }

    public String getTableName(TableType tableType) {
        String table_name = RAM_CLEAR_TABLE_NAME;
        if (tableType == TableType.Notification) {
            table_name = NOTIFICATION_CLEAR_TABLE_NAME;
        } else if (tableType == TableType.GameBoost) {
            table_name = GAME_BOOST_TABLE_NAME;
        }
        return table_name;
    }


    private void deleteTable(TableType tableType) {
        String tableName = getTableName(tableType);
        getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + tableName);
    }

    public boolean deleteItem(TableType tableType, String pkgName) {

        String table_name = getTableName(tableType);
        String sql = "delete from " + table_name + " where " + PKG_NAME + "='" + pkgName + "'";
        try {
            return getWritableDatabase().compileStatement(sql).executeUpdateDelete() != -1;
        } catch (Exception e) {
            Log.e(TAG, "deleteItem Error sql=" + sql + "--" + e.getMessage());
        }
        return false;
    }

    public boolean addItem(TableType tableType, String pkgName) {
        createTable(tableType);
        String table_name = getTableName(tableType);
        String sql = "insert into " + table_name + "(" + PKG_NAME + ")" +
                "values(?)";
        //db.beginTransaction();
        Log.e("rqy", "--addItem--sql=" + sql);
        try {
            SQLiteStatement stat = getWritableDatabase().compileStatement(sql);
            stat.bindString(1, pkgName);
            return stat.executeInsert() != -1;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "addItem Error sql=" + sql + "--" + e.getMessage());
        }
//        db.setTransactionSuccessful();
//        db.endTransaction();
        return false;
    }


    public ArrayList<String> getWhiteList(TableType tableType) {
        String table_name = getTableName(tableType);
        //先删除过期文件

        ArrayList<String> whiteList = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = getWritableDatabase().rawQuery("select " + PKG_NAME + " from " + table_name, null);
            if (cursor == null || cursor.getCount() == 0) {
                return whiteList;
            }
            cursor.moveToFirst();
            do {
                //遍历出表名
                String pkgName = cursor.getString(0);
                if (!LoadManager.getInstance(mContext).isPkgInstalled(pkgName)) {
                    deleteItem(tableType, pkgName);
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

