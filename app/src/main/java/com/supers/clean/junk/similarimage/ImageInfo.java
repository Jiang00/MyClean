package com.supers.clean.junk.similarimage;

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

    public ImageInfo() {
    }

    public ImageInfo(String path, String name, long fileSize) {
        this.path = path;
        this.name = name;
        this.fileSize = fileSize;
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
            return rhs.tag_datetime.compareToIgnoreCase(lhs.tag_datetime);
        }
    }


}
