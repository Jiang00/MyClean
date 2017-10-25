/*
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

package com.supers.clean.junk.service;


import android.widget.ImageView;


import com.supers.clean.junk.R;

import java.util.HashMap;

public class FileIconHelper {

    private static final String LOG_TAG = "FileIconHelper";

    private static HashMap<ImageView, ImageView> imageFrames = new HashMap<ImageView, ImageView>();

    private static HashMap<String, Integer> fileExtToIcons = new HashMap<String, Integer>();


    static {
        addItem(new String[]{
                "mp3"
        }, R.mipmap.file_music_icon);
        addItem(new String[]{
                "wma"
        }, R.mipmap.file_txt_icon);
        addItem(new String[]{
                "wav"
        }, R.mipmap.file_txt_icon);
        addItem(new String[]{
                "mid"
        }, R.mipmap.file_txt_icon);
        addItem(new String[]{
                "apk"
        }, R.mipmap.file_apk_icon);
        addItem(new String[]{
                "mp4", "wmv", "mpeg", "m4v", "3gp", "3gpp", "3g2", "3gpp2", "asf"
        }, R.mipmap.file_video_icon);
        addItem(new String[]{
                "jpg", "jpeg", "gif", "png", "bmp", "wbmp"
        }, R.mipmap.file_video_icon);
        addItem(new String[]{
                "txt", "log", "xml", "ini", "lrc"
        }, R.mipmap.file_txt_icon);
        addItem(new String[]{
                "doc", "ppt", "docx", "pptx", "xsl", "xslx",
        }, R.mipmap.file_doc_icon);
        addItem(new String[]{
                "pdf"
        }, R.mipmap.file_pd_icon);
        addItem(new String[]{
                "zip"
        }, R.mipmap.file_zip_icon);
        addItem(new String[]{
                "mtz"
        }, R.mipmap.file_txt_icon);
        addItem(new String[]{
                "rar"
        }, R.mipmap.file_zip_icon);
    }


    private static void addItem(String[] exts, int resId) {
        if (exts != null) {
            for (String ext : exts) {
                fileExtToIcons.put(ext.toLowerCase(), resId);
            }
        }
    }

    public static int getFileIcon(String ext) {
        Integer i = fileExtToIcons.get(ext.toLowerCase());
        if (i != null) {
            return i.intValue();
        } else {
            return R.mipmap.file_txt_icon;
        }

    }

    public static void setIcon(String path, ImageView fileImage) {
        String extFromFilename = getExtFromFilename(path);
        int id = getFileIcon(extFromFilename);
        fileImage.setImageResource(id);

    }

    public static String getExtFromFilename(String filename) {
        int dotPosition = filename.lastIndexOf('.');
        if (dotPosition != -1) {
            return filename.substring(dotPosition + 1, filename.length());
        }
        return "";
    }


}
