package com.supers.clean.junk.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.android.clean.util.MemoryManager;


/**
 * Created by ${} on 2017/8/16.
 */

public class MyFileObserver extends Service {
    RecursiveFileObserver fileObserver;
    Handler handler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };
        fileObserver = new RecursiveFileObserver(this, MemoryManager.getPhoneInSDCardPath());
        fileObserver.startWatching();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fileObserver.stopWatching();
    }
}