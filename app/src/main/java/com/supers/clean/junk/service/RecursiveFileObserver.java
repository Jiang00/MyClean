package com.supers.clean.junk.service;

import android.content.Context;
import android.os.FileObserver;
import android.support.v4.util.ArrayMap;
import android.util.Log;


import com.android.clean.db.CleanDBHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

/**
 * Created by ${} on 2017/8/16.
 */

public class RecursiveFileObserver extends FileObserver {
    Map<String, SingleFileObserver> mObservers;
    String mPath;
    int mMask;
    Context context;

    public RecursiveFileObserver(Context context, String path) {
        this(path, ALL_EVENTS);
        this.context = context;
    }

    public RecursiveFileObserver(String path, int mask) {
        super(path, mask);
        mPath = path;
        mMask = mask;
    }

    @Override
    public void startWatching() {
        if (mObservers != null)
            return;
        mObservers = new ArrayMap<>();
        Stack stack = new Stack();
        stack.push(mPath);

        while (!stack.isEmpty()) {
            String temp = (String) stack.pop();
            mObservers.put(temp, new SingleFileObserver(temp, mMask));
            File path = new File(temp);
            File[] files = path.listFiles();
            if (null == files)
                continue;
            for (File f : files) {
                // 递归监听目录
                if (f.isDirectory() && !f.getName().equals(".") && !f.getName()
                        .equals("..")) {
                    stack.push(f.getAbsolutePath());
                }
            }
        }
        Iterator<String> iterator = mObservers.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            mObservers.get(key).startWatching();
        }
    }

    @Override
    public void stopWatching() {
        if (mObservers == null)
            return;

        Iterator<String> iterator = mObservers.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            mObservers.get(key).stopWatching();
        }
        mObservers.clear();
        mObservers = null;
    }

    @Override
    public void onEvent(int event, String path) {
        int el = event & FileObserver.ALL_EVENTS;
        switch (el) {
            case FileObserver.CREATE:

                File file = new File(path);
                if (file.isDirectory()) {
                    Stack stack = new Stack();
                    stack.push(path);
                    while (!stack.isEmpty()) {
                        String temp = (String) stack.pop();
                        if (mObservers.containsKey(temp)) {
                            continue;
                        } else {
                            SingleFileObserver sfo = new SingleFileObserver(temp, mMask);
                            sfo.startWatching();
                            mObservers.put(temp, sfo);
                        }
                        File tempPath = new File(temp);
                        File[] files = tempPath.listFiles();
                        if (null == files)
                            continue;
                        for (File f : files) {
                            // 递归监听目录
                            if (f.isDirectory() && !f.getName().equals(".") && !f.getName()
                                    .equals("..")) {
                                stack.push(f.getAbsolutePath());

                            }
                        }
                    }
                } else {
                    Log.e("RacentFile", file.getPath());
                    CleanDBHelper.getInstance(context).addItem(CleanDBHelper.TableType.RacentFile, file.getPath());
                }
                break;
        }


    }

    public long getFileSizes(File f) {

        FileChannel s = null;
        try {
            if (f.exists()) {
                FileInputStream fis = new FileInputStream(f);
                s = fis.getChannel();
            }
            return s.size();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return 0;

        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        } finally {
            if (null != s) {
                try {
                    s.close();
                } catch (IOException e) {
                }
            }
        }

    }

    class SingleFileObserver extends FileObserver {
        String mPath;

        public SingleFileObserver(String path) {
            this(path, ALL_EVENTS);
            mPath = path;
        }

        public SingleFileObserver(String path, int mask) {
            super(path, mask);
            mPath = path;
        }

        @Override
        public void onEvent(int event, String path) {
            if (path != null) {
                String newPath = mPath + "/" + path;
                RecursiveFileObserver.this.onEvent(event, newPath);
            }
        }
    }
}

