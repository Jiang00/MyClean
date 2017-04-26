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
import android.text.TextUtils;
import android.util.Log;

import com.supers.clean.junk.util.CommonUtil;

/**
 * Created by renqingyou on 2017/3/30.
 */

public abstract class AutoUpdateService extends Service {

    public static final String SERVICE_UPDATE = "SERVICE_UPDATE";
    public static final String WIDGET_UPDATE = "WIDGET_UPDATE";
    public static final String UPDATE_WIDGET_ACTION = "UPDATE_WIDGET_ACTION";

    private static final int DEFAULT_SERVICE_UPDATE_DURATION = 5 * 60 * 1000; // service 自启间隔
    private static final int DEFAULT_WIDGET_UPDATE_DURATION = 30 * 1000;     // Widget 更新间隔

    private static final int SERVICE_UPDATE_MESSAGE = 1000;

    private int widget_update_duration = DEFAULT_WIDGET_UPDATE_DURATION;
    private int service_update_duration = DEFAULT_SERVICE_UPDATE_DURATION;
    private String update_widget_action = null;

    /**
     * widget被移除，停止service
     */
    public static final String STOP_UPDATE_WIDGET = "stop_update_widget";
    public static final String START_UPDATE_WIDGET = "start_update_widget";

    private boolean isStopUpdateWidget = false;

    // 更新 Widget 的 Handler
    private Handler updateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SERVICE_UPDATE_MESSAGE:
                    CommonUtil.log("rqy", "update widget--broadcast action=" + update_widget_action);
                    if (isStopUpdateWidget) {
                        removeMessages(SERVICE_UPDATE_MESSAGE);
                        stopSelf();
                        break;
                    }
                    if (!TextUtils.isEmpty(update_widget_action)) {
                        sendBroadcast(new Intent(update_widget_action));
                    }
                    Message message = updateHandler.obtainMessage();
                    message.what = SERVICE_UPDATE_MESSAGE;
                    updateHandler.sendMessageDelayed(message, widget_update_duration);
                    break;
                default:
                    break;
            }
        }

    };


    /**
     * 创建自启的Service，每隔{@link AutoUpdateService#DEFAULT_SERVICE_UPDATE_DURATION}启动
     */
    public abstract Intent createAlarmIntent(Context context);


    private Intent alarmIntent;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("rqy", "AutoUpdateService--onCreate");
        Intent alarmIntent = createAlarmIntent(this);
        if (alarmIntent == null) {
            throw new Error("boot service name not be null");
        }
        update_widget_action = alarmIntent.getStringExtra(UPDATE_WIDGET_ACTION);
        service_update_duration = alarmIntent.getIntExtra(SERVICE_UPDATE, DEFAULT_SERVICE_UPDATE_DURATION);
        widget_update_duration = alarmIntent.getIntExtra(WIDGET_UPDATE, DEFAULT_WIDGET_UPDATE_DURATION);

        startUpdate();
    }

    private void stopUpdate() {
        updateHandler.removeMessages(SERVICE_UPDATE_MESSAGE);
    }

    private void startUpdate() {
        Message message = updateHandler.obtainMessage(SERVICE_UPDATE_MESSAGE);
        updateHandler.sendMessageDelayed(message, widget_update_duration);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("rqy", "AutoUpdateService--onStartCommand");

        isStopUpdateWidget = intent.getBooleanExtra(STOP_UPDATE_WIDGET, false);
        if (isStopUpdateWidget) {
            stopUpdate();
            return START_NOT_STICKY;
        } else {
            boolean start_update = intent.getBooleanExtra(START_UPDATE_WIDGET, false);
            if (start_update) {
                updateHandler.removeMessages(SERVICE_UPDATE_MESSAGE);
                startUpdate();
            }
        }

        if (alarmIntent == null) {
            alarmIntent = createAlarmIntent(this);
        }
        // 每个 ALARM_DURATION 自启一次
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(getBaseContext(), 0,
                alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + service_update_duration, pendingIntent);

        return START_STICKY_COMPATIBILITY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
