package com.supers.clean.junk.activity;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.RemoteViews;

import com.supers.clean.junk.R;
import com.supers.clean.junk.modle.CommonUtil;
import com.supers.clean.junk.modle.PreData;
import com.supers.clean.junk.modle.entity.Contents;


public class NotifactionService extends Service {
    Handler myHandler;
    private NotificationManager mNotifyManager;
    private Notification notification_2;
    private RemoteViews remoteView_2;
    private RemoteViews remoteView_1;
    private Notification notification_1;

    private MyApplication cleanApplication;

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        if (myHandler == null)
            myHandler = new Handler();
        cleanApplication = (MyApplication) getApplication();
        changZhuTongzhi();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub

        if (intent != null && intent.getAction().equals("notification")) {
            onstart();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void onCancle() {
        if (mNotifyManager != null)
            mNotifyManager.cancel(154);
    }

    private void onstart() {
        myHandler.removeCallbacks(runnable);
        myHandler.postDelayed(runnable, 30000);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void changZhuTongzhi() {
        Notification.Builder mBuilder = new Notification.Builder(this);
        remoteView_1 = new RemoteViews(getPackageName(),
                R.layout.layout_notification);
        remoteView_1.setTextViewText(R.id.tv_memory, CommonUtil.getMemory(this) + "%");
        int requestCode = (int) SystemClock.uptimeMillis();
        Intent notifyIntent1 = new Intent(this, RamAvtivity.class);
        notifyIntent1.putExtra("from", "notifi");
        notifyIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notifyIntent1.setAction("notification");
        PendingIntent pendIntent1 = PendingIntent.getActivity(this, requestCode,
                notifyIntent1, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteView_1.setOnClickPendingIntent(R.id.ll_jiasu, pendIntent1);
        mBuilder.setContent(remoteView_1);
        mBuilder.setAutoCancel(false);
        mBuilder.setOngoing(true);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setSmallIcon(R.mipmap.notification_title);
        notification_1 = mBuilder.build();
        notification_1.flags = Notification.FLAG_INSISTENT;
        notification_1.flags |= Notification.FLAG_ONGOING_EVENT;
        // 表明在点击了通知栏中的"清除通知"后，此通知不清除， 经常与FLAG_ONGOING_EVENT一起使用
        notification_1.flags |= Notification.FLAG_NO_CLEAR;
        notification_1.priority = Notification.PRIORITY_MAX;
        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyManager.notify(154, notification_1);
    }

    private Runnable runnable = new Runnable() {
        public void run() {
            update();
            myHandler.postDelayed(this, 30000);
        }
    };

    private void update() {
        int memory = CommonUtil.getMemory(this);
        remoteView_1.setTextViewText(R.id.tv_memory, memory + "%");
        if (memory > 80) {
            remoteView_1.setTextColor(R.id.tv_memory, 0xfffa4f48);
        } else {
            remoteView_1.setTextColor(R.id.tv_memory, 0xffffa92b);
        }
        mNotifyManager.notify(154, notification_1);
        long time = System.currentTimeMillis();
        if (PreData.getDB(this, Contents.TONGZHI_SWITCH, true)) {
            int hh = Integer.parseInt(CommonUtil.getStrTimeHH(time));
            if (hh >= 6 && hh < 12 && PreData.getDB(this, Contents.KEY_TONGZHI_ZAO, true)) {
                PreData.putDB(NotifactionService.this, Contents.KEY_TONGZHI_ZHONG, true);
                PreData.putDB(NotifactionService.this, Contents.KEY_TONGZHI_WAN, true);
                if (memory > 80) {
                    tonghzi_1();
                    remoteView_2.setTextViewText(R.id.electricity, getText(R.string.notification_1));
                    remoteView_2.setTextColor(R.id.tv_memory, 0xfffa4f48);
                    remoteView_2.setTextViewText(R.id.tv_memory, memory + "%");
                    mNotifyManager.notify(101, notification_2);
                    PreData.putDB(NotifactionService.this, Contents.KEY_TONGZHI_ZAO, false);
                }
            } else if (hh >= 12 && hh < 18 && PreData.getDB(this, Contents.KEY_TONGZHI_ZHONG, true)) {
                PreData.putDB(NotifactionService.this, Contents.KEY_TONGZHI_ZAO, true);
                PreData.putDB(NotifactionService.this, Contents.KEY_TONGZHI_WAN, true);
                if (memory > 80) {
                    tonghzi_1();
                    remoteView_2.setTextViewText(R.id.electricity, getText(R.string.notification_1));
                    remoteView_2.setTextColor(R.id.tv_memory, 0xfffa4f48);
                    remoteView_2.setTextViewText(R.id.tv_memory, memory + "%");
                    mNotifyManager.notify(101, notification_2);
                    PreData.putDB(NotifactionService.this, Contents.KEY_TONGZHI_ZHONG, false);
                }
            } else if (hh >= 18 && PreData.getDB(this, Contents.KEY_TONGZHI_WAN, true)) {
                PreData.putDB(NotifactionService.this, Contents.KEY_TONGZHI_ZAO, true);
                PreData.putDB(NotifactionService.this, Contents.KEY_TONGZHI_ZHONG, true);
                if (memory > 80) {
                    tonghzi_1();
                    remoteView_2.setTextViewText(R.id.electricity, getText(R.string.notification_1));
                    remoteView_2.setTextColor(R.id.tv_memory, 0xfffa4f48);
                    remoteView_2.setTextViewText(R.id.tv_memory, memory + "%");
                    mNotifyManager.notify(101, notification_2);
                    PreData.putDB(NotifactionService.this, Contents.KEY_TONGZHI_WAN, false);
                }
            }
            int dd = Integer.parseInt(CommonUtil.getStrTimedd(time));
            if (dd % 3 == 0 && hh == 18 && PreData.getDB(this, Contents.KEY_FILE_SAN, true)) {
                long laji_size = cleanApplication.getCacheSize() + cleanApplication.getApkSize() + cleanApplication.getUnloadSize()
                        + cleanApplication.getLogSize() + cleanApplication.getDataSize() + cleanApplication.getRamSize();
                if (laji_size > 0 & cleanApplication.isSaomiaoSuccess()) {
                    tonghzi_2();
                    remoteView_2.setTextViewText(R.id.electricity, getText(R.string.notification_2));
                    remoteView_2.setTextColor(R.id.tv_memory, 0xfffa4f48);
                    remoteView_2.setTextViewText(R.id.tv_memory, CommonUtil.getFileSize(laji_size));
                    mNotifyManager.notify(101, notification_2);
                    PreData.putDB(NotifactionService.this, Contents.KEY_FILE_SAN, false);
                }
            } else if (hh != 18) {
                PreData.putDB(NotifactionService.this, Contents.KEY_FILE_SAN, true);
            }
        }

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void tonghzi_1() {
        Notification.Builder mBuilder = new Notification.Builder(this);
        remoteView_2 = new RemoteViews(getPackageName(),
                R.layout.layout_notification);
        int requestCode = (int) SystemClock.uptimeMillis();
        Intent notifyIntent = new Intent(this, RamAvtivity.class);
        notifyIntent.putExtra("from", "notifi");
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendIntent = PendingIntent.getActivity(this, requestCode,
                notifyIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContent(remoteView_2);
        mBuilder.setContentIntent(pendIntent);
        mBuilder.setAutoCancel(true);
        mBuilder.setOngoing(false);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setSmallIcon(R.mipmap.notification_title);
        notification_2 = mBuilder.build();
        notification_2.defaults = Notification.DEFAULT_SOUND;
        notification_2.flags = Notification.FLAG_AUTO_CANCEL;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void tonghzi_2() {
        Notification.Builder mBuilder = new Notification.Builder(this);
        remoteView_2 = new RemoteViews(getPackageName(),
                R.layout.layout_notification);
        int requestCode = (int) SystemClock.uptimeMillis();
        Intent notifyIntent = new Intent(this, JunkAndRamActivity.class);
        notifyIntent.putExtra("from", "notifi");
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendIntent = PendingIntent.getActivity(this, requestCode,
                notifyIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContent(remoteView_2);
        mBuilder.setContentIntent(pendIntent);
        mBuilder.setAutoCancel(true);
        mBuilder.setOngoing(false);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setSmallIcon(R.mipmap.notification_title);
        notification_2 = mBuilder.build();
        notification_2.defaults = Notification.DEFAULT_SOUND;
        notification_2.flags = Notification.FLAG_AUTO_CANCEL;
    }


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        if (!PreData.getDB(this, Contents.TONGZHILAN_SWITCH, true)) {
            mNotifyManager.cancel(154);
        }
        myHandler.removeCallbacks(runnable);
        mNotifyManager.cancel(155);
        super.onDestroy();
    }
}
