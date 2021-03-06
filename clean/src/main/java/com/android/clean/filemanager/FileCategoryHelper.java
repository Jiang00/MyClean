package com.android.clean.filemanager;/*
 * Copyright (c) 2010-2011, The MiCode Open Source Community (www.micode.net)
 *
 * This file is part of FileExplorer.
 *
 * FileExplorer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FileExplorer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SwiFTP.  If not, see <http://www.gnu.org/licenses/>.
 */

import android.content.Context;
import android.database.Cursor;
import android.media.MediaFile;
import android.net.Uri;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Files;
import android.provider.MediaStore.Files.FileColumns;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video;
import android.util.Log;

import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Iterator;

public class FileCategoryHelper {
    public static final int COLUMN_ID = 0;

    public static final int COLUMN_PATH = 1;

    public static final int COLUMN_SIZE = 2;

    public static final int COLUMN_DATE = 3;

    private static final String LOG_TAG = "FileCategoryHelper";

    public enum FileCategory {
        All, Music, Video, Picture, Doc, Zip, Apk, Other, Word, Txt, Pdf, Log
    }

    private static String APK_EXT = "apk";
    private static String THEME_EXT = "mtz";
    private static String LOG_EXT = "log";
    private static String[] ZIP_EXTS = new String[]{
            "zip", "rar"
    };

    public HashMap<FileCategory, FilenameExtFilter> filters = new HashMap<>();

    public HashMap<FileCategory, Integer> categoryNames = new HashMap<>();

   /* static {
        categoryNames.put(FileCategory.All, R.string.category_all);
        categoryNames.put(FileCategory.Music, R.string.category_music);
        categoryNames.put(FileCategory.Video, R.string.category_video);
        categoryNames.put(FileCategory.Picture, R.string.category_picture);
        categoryNames.put(FileCategory.Theme, R.string.category_theme);
        categoryNames.put(FileCategory.Doc, R.string.category_document);
        categoryNames.put(FileCategory.Zip, R.string.category_zip);
        categoryNames.put(FileCategory.Apk, R.string.category_apk);
        categoryNames.put(FileCategory.Other, R.string.category_other);
        categoryNames.put(FileCategory.Favorite, R.string.category_favorite);
    }*/

    public FileCategory[] sCategories = new FileCategory[]{
            FileCategory.Music, FileCategory.Video, FileCategory.Picture,
            FileCategory.Doc, FileCategory.Zip, FileCategory.Apk, FileCategory.Other, FileCategory.Log
    };

    private FileCategory mCategory;

    private Context mContext;

    public FileCategoryHelper(Context context) {
        mContext = context.getApplicationContext();

        mCategory = FileCategory.All;
    }

    public FileCategory getCurCategory() {
        return mCategory;
    }

    public void setCurCategory(FileCategory c) {
        mCategory = c;
    }

    public int getCurCategoryNameResId() {
        return categoryNames.get(mCategory);
    }


    public FilenameFilter getFilter() {
        return filters.get(mCategory);
    }

    private HashMap<FileCategory, CategoryInfo> mCategoryInfo = new HashMap<>();

    public HashMap<FileCategory, CategoryInfo> getCategoryInfos() {
        return mCategoryInfo;
    }

    public CategoryInfo getCategoryInfo(FileCategory fc) {
        if (mCategoryInfo.containsKey(fc)) {
            return mCategoryInfo.get(fc);
        } else {
            CategoryInfo info = new CategoryInfo();
            mCategoryInfo.put(fc, info);
            return info;
        }
    }

    public static class CategoryInfo {
        public long count;

        public long size;
    }

    private void setCategoryInfo(FileCategory fc, long count, long size) {
        CategoryInfo info = mCategoryInfo.get(fc);
        if (info == null) {
            info = new CategoryInfo();
            mCategoryInfo.put(fc, info);
        }
        info.count = count;
        info.size = size;
    }


    private static String buildDocSelection() {
        StringBuilder selection = new StringBuilder();
        Iterator<String> iter = Util.sDocMimeTypesSet.iterator();
        while (iter.hasNext()) {
            selection.append("(" + FileColumns.MIME_TYPE + "=='" + iter.next() + "') OR ");
        }
        return selection.substring(0, selection.lastIndexOf(")") + 1);
    }

    private static String buildWordSelection() {
        StringBuilder selection = new StringBuilder();
        Iterator<String> iter = Util.sWordMimeTypesSet.iterator();
        while (iter.hasNext()) {
            selection.append("(" + FileColumns.MIME_TYPE + "=='" + iter.next() + "') OR ");
        }
        return selection.substring(0, selection.lastIndexOf(")") + 1);
    }

    private static String buildZipSelection() {
        StringBuilder selection = new StringBuilder();
        Iterator<String> iter = Util.sZipFileMimeType.iterator();
        while (iter.hasNext()) {
            selection.append("(" + FileColumns.MIME_TYPE + "=='" + iter.next() + "') OR ");
        }
        return selection.substring(0, selection.lastIndexOf(")") + 1);
    }

    private static String buildSelectionByCategory(FileCategory cat) {
        String selection = null;
        switch (cat) {
            case Doc:
                selection = buildDocSelection();
                break;
            case Word:
                selection = buildWordSelection();
                break;
            case Txt:
                selection = "(" + FileColumns.MIME_TYPE + " == '" + Util.sTxtMimeTypesSet + "')";
                break;
            case Pdf:
                selection = "(" + FileColumns.MIME_TYPE + " == '" + Util.sPdfMimeTypesSet + "')";
                break;
            case Zip:
//                selection = "(" + FileColumns.MIME_TYPE + " == '" + Util.sZipFileMimeType + "')";
                selection = buildZipSelection();
                break;
            case Apk:
                selection = FileColumns.DATA + " LIKE '%.apk'";
                break;
            case Log:
                selection = FileColumns.DATA + " LIKE '%.log'";
                break;
            default:
                selection = null;
        }
        return selection;
    }

    public static Uri getContentUriByCategory(FileCategory cat) {
        Uri uri;
        String volumeName = "external";
        switch (cat) {
            case Doc:
            case Zip:
            case Apk:
            case Word:
            case Txt:
            case Log:
            case Pdf:
                uri = Files.getContentUri(volumeName);
                break;
            case Music:
                uri = Audio.Media.getContentUri(volumeName);
                break;
            case Video:
                uri = Video.Media.getContentUri(volumeName);
                break;
            case Picture:
                uri = Images.Media.getContentUri(volumeName);
                break;
            default:
                uri = null;
        }
        return uri;
    }

    private static String buildSortOrder(FileSortHelper.SortMethod sort) {
        String sortOrder = null;
        switch (sort) {
            case name:
                sortOrder = FileColumns.TITLE + " asc";
                break;
            case size:
                sortOrder = FileColumns.SIZE + " desc";
                break;
            case date:
                sortOrder = FileColumns.DATE_MODIFIED + " desc";
                break;
            case type:
                sortOrder = FileColumns.MIME_TYPE + " asc, " + FileColumns.TITLE + " asc";
                break;
        }
        return sortOrder;
    }

    public Cursor query(FileCategory fc, FileSortHelper.SortMethod sort) {
        Uri uri = getContentUriByCategory(fc);
        String selection = buildSelectionByCategory(fc);
        String sortOrder = buildSortOrder(sort);

        if (uri == null) {
            Log.e(LOG_TAG, "invalid uri, category:" + fc.name());
            return null;
        }

        String[] columns = new String[]{
                FileColumns._ID, FileColumns.DATA, FileColumns.SIZE, FileColumns.DATE_MODIFIED
        };
        try {
            return mContext.getContentResolver().query(uri, columns, selection, null, sortOrder);
        } catch (Exception e) {
            return null;
        }

    }

    public void refreshCategoryInfo() {
        // clear
        for (FileCategory fc : sCategories) {
            setCategoryInfo(fc, 0, 0);
        }

        // query database
        String volumeName = "external";

        Uri uri = Audio.Media.getContentUri(volumeName);
        refreshMediaCategory(FileCategory.Music, uri);

        uri = Video.Media.getContentUri(volumeName);
        refreshMediaCategory(FileCategory.Video, uri);

        //uri = Images.Media.getContentUri(volumeName);
        // refreshMediaCategory(FileCategory.Picture, uri);

        uri = Files.getContentUri(volumeName);//
        // refreshMediaCategory(FileCategory.Theme, uri);
        refreshMediaCategory(FileCategory.Doc, uri);
        refreshMediaCategory(FileCategory.Zip, uri);
        refreshMediaCategory(FileCategory.Apk, uri);
        refreshMediaCategory(FileCategory.Zip, uri);
    }

    private boolean refreshMediaCategory(FileCategory fc, Uri uri) {
        String[] columns = new String[]{
                "COUNT(*)", "SUM(_size)"
        };
        Cursor c = mContext.getContentResolver().query(uri, columns, buildSelectionByCategory(fc), null, null);
        if (c == null) {
            Log.e(LOG_TAG, "fail to query uri:" + uri);
            return false;
        }

        if (c.moveToNext()) {
            setCategoryInfo(fc, c.getLong(0), c.getLong(1));
            Log.v(LOG_TAG, "Retrieved " + fc.name() + " info >>> count:" + c.getLong(0) + " size:" + c.getLong(1));
            c.close();
            return true;
        }

        return false;
    }

    public static FileCategory getCategoryFromPath(String path) {
        MediaFile.MediaFileType type = MediaFile.getFileType(path);
        if (type != null) {
            if (MediaFile.isAudioFileType(type.fileType)) return FileCategory.Music;
            if (MediaFile.isVideoFileType(type.fileType)) return FileCategory.Video;
            if (MediaFile.isImageFileType(type.fileType)) return FileCategory.Picture;
            if (Util.sDocMimeTypesSet.contains(type.mimeType)) return FileCategory.Doc;
        }

        int dotPosition = path.lastIndexOf('.');
        if (dotPosition < 0) {
            return FileCategory.Other;
        }

        String ext = path.substring(dotPosition + 1);
        if (ext.equalsIgnoreCase(APK_EXT)) {
            return FileCategory.Apk;
        }

        if (ext.equalsIgnoreCase(LOG_EXT)) {
            return FileCategory.Log;
        }

        if (matchExts(ext, ZIP_EXTS)) {
            return FileCategory.Zip;
        }

        return FileCategory.Other;
    }

    private static boolean matchExts(String ext, String[] exts) {
        for (String ex : exts) {
            if (ex.equalsIgnoreCase(ext))
                return true;
        }
        return false;
    }


    public void refreshCategory() {
        long startTime = System.currentTimeMillis();
        Log.e("rqy", "refreshCategory--startTime=" + startTime);
        Util.SDCardInfo sdCardInfo = Util.getSDCardInfo();
        if (sdCardInfo != null) {
            Log.e("rqy", "sdcard total:" + sdCardInfo.total);
            Log.e("rqy", "sdcard total:" + Util.convertStorage(sdCardInfo.total));
            Log.e("rqy", "sdcard free:" + Util.convertStorage(sdCardInfo.free));
        }

        refreshCategoryInfo();

        // the other category size should include those files didn't get scanned.
        long size = 0;
        for (FileCategory fc : sCategories) {
            CategoryInfo categoryInfo = getCategoryInfos().get(fc);
            Log.e("rqy", "fc=" + fc + ",categoryInfo.count=" + categoryInfo.count);
            // other category size should be set separately with calibration
            if (fc == FileCategory.Other)
                continue;
            Log.e("rqy", "fc=" + fc + ",categoryInfo.size=" + categoryInfo.size);
            size += categoryInfo.size;
        }

        if (sdCardInfo != null) {
            long otherSize = sdCardInfo.total - sdCardInfo.free - size;
            Log.e("rqy", "otherSize=" + otherSize);
        }

        long endTime = System.currentTimeMillis();
        Log.e("rqy", "refreshCategory--time=" + (endTime - startTime));

        Cursor cursor = query(FileCategoryHelper.FileCategory.Music, FileSortHelper.SortMethod.size);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Log.e("rqy", "COLUMN_ID=" + cursor.getLong(FileCategoryHelper.COLUMN_ID));
                Log.e("rqy", "COLUMN_PATH=" + cursor.getString(FileCategoryHelper.COLUMN_PATH));
                Log.e("rqy", "File_name=" + Util.getNameFromFilepath(cursor.getString(FileCategoryHelper.COLUMN_PATH)));
                Log.e("rqy", "COLUMN_SIZE=" + cursor.getString(FileCategoryHelper.COLUMN_SIZE));
                Log.e("rqy", "COLUMN_DATE=" + cursor.getString(FileCategoryHelper.COLUMN_DATE));
            } while (cursor.moveToNext());

        }

    }
}
