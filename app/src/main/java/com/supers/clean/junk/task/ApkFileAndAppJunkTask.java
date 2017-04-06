package com.supers.clean.junk.task;

import android.content.Context;
import android.text.TextUtils;


import com.supers.clean.junk.R;
import com.supers.clean.junk.entity.JunkInfo;
import com.supers.clean.junk.util.CommonUtil;
import com.supers.clean.junk.util.MemoryManager;

import java.io.File;
import java.util.ArrayList;

public class ApkFileAndAppJunkTask extends Thread {
    private String path;
    private Context mContext;
    private FileTaskListener mFileTaskListener;
    private long logSize = 0;
    private long apkSize = 0;

    public ApkFileAndAppJunkTask(Context context, FileTaskListener fileTaskListener) {
        mContext = context;
        mFileTaskListener = fileTaskListener;
    }

    @Override
    public void run() {
        super.run();
        loadData();
    }


    void loadData() {
        path = MemoryManager.getPhoneInSDCardPath();
        if (path == null) {
            path = "/storage/";
        }
        File rootPath = new File(path);
        File[] dirs = rootPath.listFiles();
        searchDirs(dirs);
    }

    private void searchDirs(final File[] file) {
        ArrayList<JunkInfo> apkList = new ArrayList<>();
        ArrayList<JunkInfo> logList = new ArrayList<>();
        if (file != null) {
            for (File f : file) {
                searchFile(f, apkList, logList);
            }
        }
        if (mFileTaskListener != null) {
            mFileTaskListener.loadFinish(apkSize, logSize, apkList, logList);
        }
    }

    // ???????????
    private void searchFile(File file, ArrayList<JunkInfo> list, ArrayList<JunkInfo> logList) {
        if (file == null || !file.exists() || !file.canRead()) {
            return;
        }
        if (!file.isDirectory()) {
            if (file.length() <= 0) {
                return;
            }
            sepreateFile(file, list, logList);
        } else {
            File[] files = file.listFiles();
            if (files == null || file.length() == 0) {
                return;
            }
            int count = 0;
            for (File f : files) {
                count++;
                if (count > 20) {
                    continue;
                }
                searchFile(f, list, logList);
            }
        }
    }

    // ?????????
    private void sepreateFile(File file, ArrayList<JunkInfo> list, ArrayList<JunkInfo> logList) {
        // TODO Auto-generated method stub
        if (file.isFile()) {
            String name = file.getName();
            int dotIndex = name.lastIndexOf(".");
            if (dotIndex < 0) {
                return;
            }
            String end = name.substring(dotIndex + 1, name.length()).toLowerCase();
            if (TextUtils.isEmpty(end)) {
                return;
            }
            if ("apk".equals(end)) {
                JunkInfo fileListInfo = new JunkInfo(file.getAbsolutePath(),
                        file.getName(), file.lastModified(),
                        true, CommonUtil.getApkIcon(mContext, file.getAbsolutePath()),
                        file.length());
                apkSize += file.length();
                list.add(fileListInfo);
            }
            if ("log".equals(end)) {
                JunkInfo fileListInfo = new JunkInfo(file.getAbsolutePath(),
                        file.getName(), file.lastModified(),
                        true, mContext.getResources().getDrawable(R.mipmap.log_file),
                        file.length());
                logSize += file.length();
                logList.add(fileListInfo);
            }
        }
    }


    public interface FileTaskListener {
        void loadFinish(long apkSize, long logSize, ArrayList<JunkInfo> apkList, ArrayList<JunkInfo> logList);
    }
}
