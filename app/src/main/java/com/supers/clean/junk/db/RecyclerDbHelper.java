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

    public static final long RECYCLER_AUTO_DELETE_INTERVAL = 7 * 24 * 60 * 60 * 1000;

    private static RecyclerDbHelper recyclerDBHelper;

    private Context mContext;

    private RecyclerDbHelper(Context context) {
        super(context.getApplicationContext(), BATTERY_DB, null, DB_VERSION);
        mContext = context.getApplicationContext();
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

    private void createRecyclerTable() {
        getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS " + RECYCLER_TABLE_NAME + " (" +
                BACKUP_FILE_DIRECTORY + " TEXT NOT NULL, " +
                RESTORE_FILE_PATH + " TEXT NOT NULL, " +
                "PRIMARY KEY (" + RESTORE_FILE_PATH + ") " +
                ");");
    }

    private void deleteTable(SQLiteDatabase db, String tableName) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
    }

    private int deleteItem(ImageInfo imageInfo) {
        String sql = "delete from " + RECYCLER_TABLE_NAME + "  where rowid=" + imageInfo.rowId;
        try {
            return getWritableDatabase().compileStatement(sql).executeUpdateDelete();
        } catch (Exception e) {
            Log.e(TAG, "deleteItemFromFavor Error tableName=" + RECYCLER_TABLE_NAME + imageInfo);
        }
        return -1;
    }

    private long addItem(String path, String recyclerTime) {
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

    private String getRecyclerTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(new Date());
        return date;
    }

    private String getRecyclerDirectory(String recyclerTime) {
        return Environment.getExternalStorageDirectory() + "/eosbackup/" + recyclerTime + "/";
    }

    public ArrayList<ImageInfo> getRecyclerImageList() {
        Log.e("rqy", "getRecyclerImageList");
        //先删除过期文件
        deleteOverDateRecyclerFile();

        ArrayList<ImageInfo> imageInfos = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = getWritableDatabase().rawQuery("select rowid," + RESTORE_FILE_PATH + "," + BACKUP_FILE_DIRECTORY + " from " + RECYCLER_TABLE_NAME, null);
            if (cursor == null || cursor.getCount() == 0) {
                return imageInfos;
            }
            Log.e("rqy", "cursor coucnt = " + cursor.getCount());
            cursor.moveToFirst();
            do {
                //遍历出表名
                long rowId = cursor.getLong(0);
                String restorePath = cursor.getString(1);
                String fileDic = cursor.getString(2);
                String backFilePath = fileDic + "img_" + rowId;

                ImageInfo imageInfo = new ImageInfo(rowId, restorePath, backFilePath);
                Log.e("rqy", "getRecyclerImageList " + imageInfo);
                File file = new File(backFilePath);
                if (!file.exists() || file.isDirectory()) {
                    deleteItem(imageInfo);
                } else {
                    imageInfos.add(imageInfo);
                }
            } while (cursor.moveToNext());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("rqy", "e.message=" + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return imageInfos;
    }

    private void deleteOverDateRecyclerFile() {
        File file = new File(Environment.getExternalStorageDirectory() + "/eosbackup");
        if (file != null && file.exists()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                String name = files[i].getName();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = simpleDateFormat.parse(name);
                    if (System.currentTimeMillis() > date.getTime() + RECYCLER_AUTO_DELETE_INTERVAL) {
                        FileUtils.deleteFile(files[i].getAbsolutePath());
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }
    }

    public boolean putImageToRecycler(ImageInfo imageInfo) {
        Log.e("rqy", "putImageToRecycler--" + imageInfo);
        if (imageInfo == null) {
            return false;
        }
        File file = new File(imageInfo.path);
        if (!file.exists() || file.isDirectory()) {
            Log.e("rqy", "putImageToRecycler: file not exist or is directory, " + imageInfo.path);
            return false;
        }
        String recyclerTime = RecyclerDbHelper.getInstance(mContext).getRecyclerTime();
        long rowId = RecyclerDbHelper.getInstance(mContext).addItem(imageInfo.path, recyclerTime);
        Log.e("rqy", "putImageToRecycler: rowId= " + rowId);
        if (rowId < 0) {
            return false;
        }
        String result = FileUtils.copyFileToRecycler(imageInfo.path, RecyclerDbHelper.getInstance(mContext).getRecyclerDirectory(recyclerTime), "img_" + rowId);
        if (result == null) {
            Log.e("rqy", "copyFileToRecycler:error");
            RecyclerDbHelper.getInstance(mContext).deleteItem(imageInfo);
            return false;
        } else {
            RecyclerDbHelper.getInstance(mContext).deleteItem(imageInfo);
            FileUtils.deleteFile(imageInfo.path);
        }
        return true;
    }

    public boolean restoreImageFromRecycler(ImageInfo imageInfo) {
        if (imageInfo == null || imageInfo.backFilePath == null || imageInfo.restoreFilePath == null) {
            return false;
        }

        int index = imageInfo.restoreFilePath.lastIndexOf("/");
        if (index == -1) {
            return false;
        }
        String directory = imageInfo.restoreFilePath.substring(0, index);
        String name = imageInfo.restoreFilePath.substring(index + 1, imageInfo.restoreFilePath.length());
        String result = FileUtils.copyFileToRecycler(imageInfo.backFilePath, directory, name);

        Log.e("rqy", "result=" + result);
        if (result != null) {
            int success = deleteItem(imageInfo);

            FileUtils.deleteFile(imageInfo.backFilePath);

            Log.e("rqy", "success=" + success);
            return true;
        }
        return false;
    }

    public boolean deleteImageFromRecyler(ImageInfo imageInfo) {
        if (imageInfo == null || imageInfo.backFilePath == null) {
            return false;
        }
        File file = new File(imageInfo.backFilePath);
        if (!file.exists() || file.isDirectory()) {
            return false;
        }
        boolean success = file.delete();
        if (success) {
            return (deleteItem(imageInfo)) >= 0;
        }
        return false;
    }


}
