package com.fraumobi.call.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;


import com.fraumobi.call.entries.Contact;
import com.fraumobi.call.entries.RejectInfo;

import java.util.ArrayList;

/**
 * Created by Ivy on 2017/4/26.
 */

public class Database extends SQLiteOpenHelper {

    public static final String BATTERY_DB = "contacts.db";
    public static final int DB_VERSION = 1;

    private static final String TAG = "BatteryDBHelper";

    public static final String CONTACT_ID = "contact_id";
    public static final String CONTACT_NAME = "contact_name";
    public static final String CONTACT_NUMBER = "contact_number";
    public static final String REJECT_DATE = "reject_date";
    public static final String REJECT_DIRECT = "reject_direct";
    public static final String COLUMN_CALL_DIRECTION = "call_direction";
    public static final String COLUMN_CALL_DATE = "call_date";
    public static final String COLUMN_CALL_DURATION = "call_duration";
    public static final String COLUMN_CALL_RECORD_PATH = "call_record_path";

    private static Database instance = null;

    public synchronized static Database getInstance(Context context) {
        if (instance == null) {
            return new Database(context);
        }
        return instance;
    }

    private Database(Context context) {
        super(context, BATTERY_DB, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    public boolean tableIsExist(SQLiteDatabase db, String tabName) {
        boolean result = false;
        if (tabName == null) {
            return false;
        }
        Cursor cursor;
        try {
            String sql = "select count(*) as c from sqlite_master where type ='table' and name ='" + tabName.trim() + "' ";
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<String> getAllTable(SQLiteDatabase db) {
        ArrayList<String> tables = new ArrayList<>();
        Cursor cursor = db.rawQuery("select name from sqlite_master where type='table' order by name", null);
        if (cursor == null) {
            return tables;
        }
        while (cursor.moveToNext()) {
            //遍历出表名
            String name = cursor.getString(0);
            tables.add(name);
        }
        cursor.close();
        return tables;
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

    public void deleteTable(SQLiteDatabase db, String tableName) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
    }

    public void createTableContacts(SQLiteDatabase db, String tableName) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                CONTACT_ID + " TEXT, " +
                CONTACT_NAME + " TEXT, " +
                CONTACT_NUMBER + " TEXT " + ");");
    }

    public void insertDataIntoTableContacts(SQLiteDatabase db, String tableName, Contact contact) {
        try {
            String sql = "insert into " + tableName + "(" + CONTACT_ID + "," + CONTACT_NAME + "," + CONTACT_NUMBER + ")values(?,?,?)";
            SQLiteStatement stat = db.compileStatement(sql);
            stat.bindString(1, contact._id);
            stat.bindString(2, contact.name);
            stat.bindString(3, contact.phoneNum);
            try {
                stat.executeInsert();
                Log.e("call", contact.phoneNum + "==");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception E) {
            E.printStackTrace();
        }
    }

    public ArrayList<Contact> getDataFromTableContacts(SQLiteDatabase db, String tableName) {
        ArrayList<Contact> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select contact_id,contact_name,contact_number" +
                    " from " + tableName, null);
            if (cursor == null) {
                return list;
            }
            while (cursor.moveToNext()) {
                list.add(new Contact(cursor.getString(0), cursor.getString(1), cursor.getString(2), false, false));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    public void deleteDataFromTableContacts(SQLiteDatabase db, String tableName, Contact info) {
        try {
            db.execSQL("delete from " + tableName + " where contact_id=?", new String[]{info._id});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAllDataFromContacts(SQLiteDatabase db, String tableName) {
        try {
            db.execSQL("delete from " + tableName);
        } catch (Exception e) {

        }
    }

    public boolean checkDataFromTableContacts(SQLiteDatabase db, String tableName, String phoneNum) {
        if (phoneNum == null) {
            return false;
        }
        try {
            Cursor cursor = db.rawQuery("select * from " + tableName + " where " + CONTACT_NUMBER + "=?", new String[]{phoneNum});
            if (cursor == null) {
                return false;
            } else {
                if (cursor.getCount() > 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void createTableReject(SQLiteDatabase db, String tableName) {
        db.execSQL("create table if not exists " + tableName + " (" +
                CONTACT_NAME + " TEXT, " +
                CONTACT_NUMBER + " TEXT, " +
                REJECT_DATE + " TEXT, " +
                REJECT_DIRECT + " TEXT " + ");");
    }

    public void insertDataToTableReject(SQLiteDatabase db, String tableName, RejectInfo info) {
        try {
            String sql = "insert into " + tableName + "(" +
                    CONTACT_NAME + "," + CONTACT_NUMBER + "," + REJECT_DATE + "," + REJECT_DIRECT + ")values(?,?,?,?)";
            SQLiteStatement stat = db.compileStatement(sql);
            stat.bindString(1, info.name);
            stat.bindString(2, info.phoneNum);
            stat.bindString(3, info.date);
            stat.bindString(4, info.direct);
            stat.executeInsert();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<RejectInfo> getDataFromTableReject(SQLiteDatabase db, String tableName) {
        ArrayList<RejectInfo> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select contact_name,contact_number,reject_date,reject_direct from " + tableName + " order by reject_date DESC", null);
            if (cursor == null) {
                return list;
            }
            while (cursor.moveToNext()) {
                list.add(new RejectInfo(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    public void deleteDataFromTableReject(SQLiteDatabase db, String tableName, RejectInfo info) {
        try {
            db.execSQL("delete from " + tableName + " where contact_name=? and contact_number=? and reject_date=? and reject_direct=?", new String[]{info.name, info.phoneNum, info.date, info.direct});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createTableRecord(SQLiteDatabase db, String tableName) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                CONTACT_NAME + " TEXT, " +
                CONTACT_NUMBER + " TEXT, " +
                COLUMN_CALL_DIRECTION + " TEXT, " +
                COLUMN_CALL_DATE + " TEXT, " +
                COLUMN_CALL_DURATION + " TEXT, " +
                COLUMN_CALL_RECORD_PATH + " TEXT " +
                ");");
    }


    public void deleteItemFromTableRecord(SQLiteDatabase db, String tableName, String path) {
        try {
            db.execSQL("delete from " + tableName + " where " + COLUMN_CALL_RECORD_PATH + "=?", new String[]{path});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
