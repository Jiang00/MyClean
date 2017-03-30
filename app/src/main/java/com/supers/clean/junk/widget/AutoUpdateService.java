package com.supers.clean.junk.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by renqingyou on 2017/3/30.
 */

public abstract class AutoUpdateService extends Service {
    private static final int ALARM_DURATION = 5 * 60 * 1000; // service 自启间隔
    private static final int UPDATE_DURATION = 2 * 1000;     // Widget 更新间隔
    private static final int UPDATE_MESSAGE = 1000;

    /**
     * widget被移除，停止service
     */
    public static final String SERVICE_NEED_STOP = "need_stop";

    private boolean isNeedStop = false;

    // 更新 Widget 的 Handler
    private Handler updateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_MESSAGE:
                    if (isNeedStop) {
                        stopSelf();
                        break;
                    }
                    updateWidget();
                    Message message = updateHandler.obtainMessage();
                    message.what = UPDATE_MESSAGE;
                    updateHandler.sendMessageDelayed(message, UPDATE_DURATION);
                    break;
                default:
                    break;
            }
        }

    };


    /**
     * 创建自启的Service，每隔{@link AutoUpdateService#ALARM_DURATION}启动
     */
    public abstract Intent createAlarmIntent(Context context);


    /**
     * 定时更新widget{@link AutoUpdateService#UPDATE_DURATION}
     */
    public abstract void updateWidget();


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("rqy", "AutoUpdateService--onCreate");
        Message message = updateHandler.obtainMessage(UPDATE_MESSAGE);
        updateHandler.sendMessageDelayed(message, UPDATE_DURATION);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isNeedStop = intent.getBooleanExtra(SERVICE_NEED_STOP, false);
        if (isNeedStop) {
            stopSelf();
            return START_STICKY_COMPATIBILITY;
        }
        Intent bootService = createAlarmIntent(this);
        if (bootService == null) {
            throw new Error("boot service name not be null");
        }
        // 每个 ALARM_DURATION 自启一次
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(getBaseContext(), 0,
                bootService, PendingIntent.FLAG_UPDATE_CURRENT);

        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + ALARM_DURATION, pendingIntent);

        return START_STICKY_COMPATIBILITY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
