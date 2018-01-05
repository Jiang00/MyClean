package com.upupup.clean.imageclean;

import android.media.ExifInterface;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Comparator;

/**
 * Created by renqingyou on 2017/4/26.
 */

public class ImageInfo {

    public String name;

    public String path;

    public String tag_datetime; //拍摄时间

    public String width;

    public String height;

    public String sourceHashCode;

    public boolean isNormal;

    public int avgPixel;

    public long fileSize;

    public long originId;

    public long rowId;
    public String restoreFilePath;  //从回收站恢复文件的路径
    public String backFilePath;     //备份的文件路径

    public ImageInfo() {
    }

    public ImageInfo(long rowId, String restoreFilePath, String backFilePath) {
        this.rowId = rowId;
        this.restoreFilePath = restoreFilePath;
        this.backFilePath = backFilePath;
    }

    public ImageInfo(String path, long originId, String name, long fileSize) {
        this.path = path;
        this.name = name;
        this.fileSize = fileSize;
        this.originId = originId;
        try {
            ExifInterface exif = new ExifInterface(path);
            width = exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
            height = exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
            tag_datetime = exif.getAttribute(ExifInterface.TAG_DATETIME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "ImageInfo{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", tag_datetime='" + tag_datetime + '\'' +
                ", width='" + width + '\'' +
                ", height='" + height + '\'' +
                ", sourceHashCode='" + sourceHashCode + '\'' +
                ", isNormal=" + isNormal +
                ", avgPixel=" + avgPixel +
                ", fileSize=" + fileSize +
                ", rowId=" + rowId +
                ", restoreFilePath='" + restoreFilePath + '\'' +
                ", backFilePath='" + backFilePath + '\'' +
                '}';
    }

    static class ImageFilter implements FileFilter {

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String tmp = f.getName().toLowerCase();
            if (tmp.endsWith(".jpg") || tmp.endsWith(".jpeg")) {
                try {
                    ExifInterface exif = new ExifInterface(f.getPath());
                    if (exif.getAttribute(ExifInterface.TAG_DATETIME) == null) {
                        return false;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
                return true;
            }
            return false;
        }
    }

    static class ImageComparator implements Comparator<ImageInfo> {
        @Override
        public int compare(ImageInfo lhs, ImageInfo rhs) {
            return lhs.tag_datetime.compareToIgnoreCase(rhs.tag_datetime);
        }
    }


}
