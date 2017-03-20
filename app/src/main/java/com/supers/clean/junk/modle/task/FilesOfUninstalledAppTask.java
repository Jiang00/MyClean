package com.supers.clean.junk.modle.task;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Environment;


import com.supers.clean.junk.R;
import com.supers.clean.junk.modle.CommonUtil;
import com.supers.clean.junk.modle.entity.JunkInfo;
import com.supers.clean.junk.modle.CopyDbManager;
import com.supers.clean.junk.modle.MemoryManager;

import java.io.File;
import java.util.ArrayList;


public class FilesOfUninstalledAppTask extends SimpleTask {

    private static final String TAG = "FilesOfUninstalledAppTask";

    @SuppressWarnings("unchecked")
    public FilesOfUninstalledAppTask(Context context, SimpleTaskListener simpleTaskListener) {
        super(context, simpleTaskListener, TAG);
    }

    @Override
    void loadData() {
        long allSize = 0;
        ArrayList<JunkInfo> dataList = new ArrayList<>();
        CopyDbManager.copyDB(mContext, "clearpath.db");
        try {
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Environment.getDataDirectory() + "/data/"
                    + mContext.getPackageName() + "/" + "clearpath.db", null);
            //SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase( context.getFilesDir() + "clearpath.db", null);
            String sql = "select * from softdetail";
            Cursor c = db.rawQuery(sql, null);
            String sdpath = MemoryManager.getPhoneInSDCardPath();
            while (c.moveToNext()) {
                String apkname = c.getString(c.getColumnIndex("apkname"));
                if (CommonUtil.isPkgInstalled(apkname, pm)) {
                    continue;
                }
                String filepath = c.getString(c.getColumnIndex("filepath"));

                filepath = sdpath + filepath;

                File file = new File(filepath);

                if (file.exists()) {

                    Drawable icon;
                    try {
                        icon = pm.getApplicationIcon(apkname);
                    } catch (NameNotFoundException e) {
                        icon = mContext.getResources().getDrawable(
                                R.mipmap.icon);
                    }
                    String softChinesename = c.getString(c
                            .getColumnIndex("softChinesename"));
                    long fileSize = getFileSize(file);
                    JunkInfo info = new JunkInfo(icon, filepath, softChinesename, apkname,
                            true, fileSize);
                    allSize += fileSize;
                    dataList.add(info);
                    if (mSimpleTaskListener != null) {
                        mSimpleTaskListener.loading(info, fileSize);
                    }
                }
                if (isCancelTask) {
                    if (mSimpleTaskListener != null) {
                        mSimpleTaskListener.cancelLoading();
                    }
                    break;
                }
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mSimpleTaskListener != null) {
            mSimpleTaskListener.finishLoading(allSize, dataList);
        }
    }

    private long getFileSize(File file) {
        long size = 0;

        if (file.isDirectory()) {
            File fileList[] = file.listFiles();
            if (fileList != null) {


                for (int i = 0; i < fileList.length; i++) {
                    if (fileList[i].isDirectory()) {
                        size = size + getFileSize(fileList[i]);
                    } else {
                        size = size + fileList[i].length();
                    }
                }
            }
        } else {
            size += file.length();
        }
        return size;
    }
}