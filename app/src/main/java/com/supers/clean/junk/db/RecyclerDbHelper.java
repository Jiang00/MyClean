package com.supers.clean.junk.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;
import android.util.Log;

import com.supers.clean.junk.similarimage.ImageInfo;

import java.util.ArrayList;

/**
 * Created by qingyou.ren on 2016/10/24.
 */

public class RecyclerDbHelper extends SQLiteOpenHelper {

    public static final String BATTERY_DB = "favor.db";
    public static final int DB_VERSION = 1;
    public static final String COLUMN_INDEX = "favor_index";
    public static final String COLUMN_PKG = "pkg";

    public static final String FAVOR_TABLE_NAME = "favor_table";

    private static final String TAG = "FavorDBHelper";

    private static RecyclerDbHelper favorDBHelper;

    private Context mContext;

    private RecyclerDbHelper(Context context) {
        super(context.getApplicationContext(), BATTERY_DB, null, DB_VERSION);
        mContext = context;
    }

    public synchronized static RecyclerDbHelper getInstance(Context context) {
        if (favorDBHelper == null) {
            favorDBHelper = new RecyclerDbHelper(context);
        }
        return favorDBHelper;
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

    public void createFavorTable() {
        getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS " + FAVOR_TABLE_NAME + " (" +
                COLUMN_INDEX + " INTEGER NOT NULL, " +
                COLUMN_PKG + " TEXT NOT NULL, " +
                "PRIMARY KEY (" + COLUMN_INDEX + ") " +
                ");");
    }

    public void deleteTable(SQLiteDatabase db, String tableName) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
    }

    public void deleteItemFromFavor(ImageInfo circleItem) {
        String sql = "delete from " + FAVOR_TABLE_NAME + "  where " + COLUMN_INDEX + " = " + circleItem.path;
        try {
            getWritableDatabase().compileStatement(sql).execute();
        } catch (Exception e) {
            Log.e(TAG, "deleteItemFromFavor Error tableName=" + FAVOR_TABLE_NAME + "-index=" + circleItem.path + ",pkg=" + circleItem.path);
        }
    }

    public boolean addItem(ImageInfo circleItem) {
        createFavorTable();
        String sql = "insert into " + FAVOR_TABLE_NAME + "(" + COLUMN_INDEX + "," + COLUMN_PKG + ")" +
                "values(?,?)";
        //db.beginTransaction();
        try {
            SQLiteStatement stat = getWritableDatabase().compileStatement(sql);
            stat.bindLong(1, circleItem.fileSize);
            stat.bindString(2, circleItem.path);
            stat.execute();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "addItemToFavor Error tableName=" + FAVOR_TABLE_NAME + "-index=" + circleItem.path + ",pkg=" + circleItem.path);
            return false;
        }
//        db.setTransactionSuccessful();
//        db.endTransaction();
        return true;
    }

    public ArrayList<ImageInfo> getFavorList() {
        ArrayList<ImageInfo> circleItems = new ArrayList<>();
        try {
            Cursor cursor = getWritableDatabase().rawQuery("select " + COLUMN_INDEX + "," + COLUMN_PKG + " from " + FAVOR_TABLE_NAME + " order by " + COLUMN_INDEX, null);
            if (cursor == null) {
                return circleItems;
            }
            while (cursor.moveToNext()) {
                //遍历出表名
                int index = cursor.getInt(0);
                String pkg = cursor.getString(1);
                if (!TextUtils.isEmpty(pkg)) {
                    ImageInfo circleItem = new ImageInfo();
                    circleItems.add(circleItem);
                }

            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return circleItems;
    }


}
