package com.security.mcleaner.photo;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;

/**
 * Created by on 2017/5/3.
 */

public class MediaScanner {

    private MediaScannerConnection mediaScanConn = null;
    private PhotoSannerClient client = null;
    private String filePath = null;
    private String fileType = null;
    private static MediaScanner mediaScanner = null;

    /**
     * 然后调用MediaScanner.scanFile("/sdcard/2.mp3");
     */

    public MediaScanner(Context context) {
        // 创建MusicSannerClient
        if (client == null) {
            client = new PhotoSannerClient();
        }
        mediaScanConn = new MediaScannerConnection(context, client);
    }

    public static MediaScanner getInstanc(Context context) {
        if (mediaScanner == null) {
            mediaScanner = new MediaScanner(context);
        }
        return mediaScanner;
    }

    private class PhotoSannerClient implements
            MediaScannerConnection.MediaScannerConnectionClient {

        public void onMediaScannerConnected() {

            if (filePath != null) {
                mediaScanConn.scanFile(filePath, fileType);
            }

            filePath = null;
            fileType = null;
        }

        public void onScanCompleted(String path, Uri uri) {
            // TODO Auto-generated method stub
            mediaScanConn.disconnect();
        }

    }

    /**
     * 扫描文件标签信息
     *
     * @param fileType 文件类型 eg: audio/mp3 media/* application/ogg
     */

    public void scanFile(String filepath, String fileType) {
        this.filePath = filepath;
        this.fileType = fileType;
        // 连接之后调用MusicSannerClient的onMediaScannerConnected()方法
        mediaScanConn.connect();
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

}