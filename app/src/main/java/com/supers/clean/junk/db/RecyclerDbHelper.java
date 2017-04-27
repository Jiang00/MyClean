package com.supers.clean.junk.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.Environment;
import android.util.Log;

import com.supers.clean.junk.filemanager.FileUtils;
import com.supers.clean.junk.similarimage.ImageInfo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by qingyou.ren on 2016/10/24.
 */

public class RecyclerDbHelper extends SQLiteOpenHelper {

    public static final String BATTERY_DB = "recycler.db";
    public static final int DB_VERSION = 1;
    public static final String BACKUP_FILE_DIRECTORY = "backup_dic";

    public static final String RESTORE_FILE_PATH = "file_path";

    public static final String RECYCLER_TABLE_NAME = "recycler";

    private static final String TAG = "RecyclerDbHelper";

    private static RecyclerDbHelper recyclerDBHelper;

    private Context mContext;

    private RecyclerDbHelper(Context context) {
        super(context.getApplicationContext(), BATTERY_DB, null, DB_VERSION);
        mContext = context;
    }

    public synchronized static RecyclerDbHelper getInstance(Context context) {
        if (recyclerDBHelper == null) {
            recyclerDBHelper = new RecyclerDbHelper(context);
        }
        return recyclerDBHelper;
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

    public void createRecyclerTable() {
        getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS " + RECYCLER_TABLE_NAME + " (" +
                BACKUP_FILE_DIRECTORY + " TEXT NOT NULL, " +
                RESTORE_FILE_PATH + " TEXT NOT NULL, " +
                "PRIMARY KEY (" + RESTORE_FILE_PATH + ") " +
                ");");
    }

    public void deleteTable(SQLiteDatabase db, String tableName) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
    }

    public void deleteItem(ImageInfo imageInfo) {
        String sql = "delete from " + RECYCLER_TABLE_NAME + "  where rowid=" + imageInfo.rowId;
        try {
            getWritableDatabase().compileStatement(sql).execute();
        } catch (Exception e) {
            Log.e(TAG, "deleteItemFromFavor Error tableName=" + RECYCLER_TABLE_NAME + imageInfo);
        }
    }

    public long addItem(String path, String recyclerTime) {
        createRecyclerTable();
        String sql = "insert into " + RECYCLER_TABLE_NAME + "(" + RESTORE_FILE_PATH + "," + BACKUP_FILE_DIRECTORY + ")" +
                "values(?,?)";
        //db.beginTransaction();
        try {
            SQLiteStatement stat = getWritableDatabase().compileStatement(sql);
            stat.bindString(1, path);
            stat.bindString(2, getRecyclerDirectory(recyclerTime));
            return stat.executeInsert();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "addItemToFavor Error tableName=" + RECYCLER_TABLE_NAME + path);
        }
//        db.setTransactionSuccessful();
//        db.endTransaction();
        return -1;
    }

    public ArrayList<ImageInfo> getRecyclerImageList() {
        ArrayList<ImageInfo> imageInfos = new ArrayList<>();
        try {
            Cursor cursor = getWritableDatabase().rawQuery("select rowid," + RESTORE_FILE_PATH + "," + BACKUP_FILE_DIRECTORY + " from " + RECYCLER_TABLE_NAME + " order by rowid", null);
            if (cursor == null) {
                return imageInfos;
            }
            while (cursor.moveToNext()) {
                //遍历出表名
                long rowId = cursor.getLong(0);
                String restorePath = cursor.getString(1);
                String fileDic = cursor.getString(2);
                String backFilePath = getRecyclerDirectory(fileDic) + "img_" + rowId;

                ImageInfo imageInfo = new ImageInfo(rowId, restorePath, backFilePath);
                File file = new File(backFilePath);
                if (!file.exists() || file.isDirectory()) {
                    deleteItem(imageInfo);
                } else {
                    imageInfos.add(imageInfo);
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageInfos;
    }

    private String getRecyclerTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:DD");
        String date = simpleDateFormat.format(new Date());
        return date;
    }

    private String getRecyclerDirectory(String recyclerTime) {
        return Environment.getExternalStorageDirectory() + "/eosbackup/" + recyclerTime + "/";
    }

    public boolean putImageToRecycler(Context context, ImageInfo imageInfo) {
        if (imageInfo == null) {
            return false;
        }
        File file = new File(imageInfo.path);
        if (!file.exists() || file.isDirectory()) {
            Log.v("rqy", "putImageToRecycler: file not exist or is directory, " + imageInfo.path);
            return false;
        }
        String recyclerTime = RecyclerDbHelper.getInstance(context).getRecyclerTime();
        long rowId = RecyclerDbHelper.getInstance(context).addItem(imageInfo.path, recyclerTime);
        if (rowId < 0) {
            return false;
        }
        String dest = RecyclerDbHelper.getInstance(context).getRecyclerDirectory(recyclerTime) + "img_" + rowId;
        String result = FileUtils.copyFile(imageInfo.path, dest);
        if (result == null) {
            RecyclerDbHelper.getInstance(context).deleteItem(imageInfo);
            return false;
        }
        return true;
    }

    public boolean restoreImageFromRecycler(Context context, ImageInfo imageInfo) {
        if (imageInfo == null || imageInfo.backFilePath == null || imageInfo.restoreFilePath == null) {
            return false;
        }
        String result = FileUtils.copyFile(imageInfo.backFilePath, imageInfo.restoreFilePath);
        if (result != null) {
            deleteItem(imageInfo);
            return true;
        }
        return false;
    }

}
