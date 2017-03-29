package com.supers.clean.junk.activity;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;

import com.supers.clean.junk.R;
import com.supers.clean.junk.modle.CommonUtil;
import com.supers.clean.junk.modle.CpuTempReader;
import com.supers.clean.junk.modle.PhoneManager;
import com.supers.clean.junk.modle.PreData;
import com.supers.clean.junk.modle.entity.Contents;


public class NotifactionService extends Service {
    Handler myHandler;
    private PhoneManager phoneManager;
    private NotificationManager mNotifyManager;
    private Notification notification_ram, notification_cooling, notification_junk;
    private RemoteViews remoteView_1;
    private Notification notification_1;

    private MyApplication cleanApplication;
    private Intent notifyIntentMain, notifyIntentRam, notifyIntentCooling, notifyIntentFlash, notifyIntentJunkRam;

    private Bitmap bitmap_progress;
    private Canvas canvas;
    private Paint paint_1;
    private int pointX = CommonUtil.dp2px(29) / 2;
    private RectF oval;
    private int cpuTemp;

    private long lastTotalRxBytes = 0; // 最后缓存的字节数
    private long lastTimeStamp = 0; // 当前缓存时间

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
        phoneManager = PhoneManager.getPhoneManage(this);
        initIntent();
        bitmap_progress = Bitmap.createBitmap(CommonUtil.dp2px(29), CommonUtil.dp2px(29), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap_progress);
        paint_1 = new Paint();
        paint_1.setAntiAlias(true);
        paint_1.setStrokeCap(Paint.Cap.ROUND);
        paint_1.setStrokeWidth(CommonUtil.dp2px(1));
        paint_1.setStyle(Paint.Style.STROKE);
        paint_1.setColor(getResources().getColor(R.color.white_40));
        oval = new RectF(0 + CommonUtil.dp2px(2), -pointX + CommonUtil.dp2px(2), pointX
                * 2 - CommonUtil.dp2px(2), pointX - CommonUtil.dp2px(2));
        canvas.save();
        canvas.translate(0, pointX);
        canvas.rotate(135, pointX, 0);
        canvas.drawArc(oval, 0, 270, false, paint_1);
        paint_1.setColor(this.getResources().getColor(R.color.white_100));
        changZhuTongzhi();

    }

    private void initIntent() {
        notifyIntentMain = new Intent(this, MainActivity.class);
        notifyIntentMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notifyIntentRam = new Intent(this, RamAvtivity.class);
        notifyIntentRam.putExtra("from", "notifi");
        notifyIntentRam.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notifyIntentCooling = new Intent(this, CoolingActivity.class);
        notifyIntentCooling.putExtra("from", "notifi");
        notifyIntentCooling.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notifyIntentFlash = new Intent(this, TranslateActivity.class);
        notifyIntentFlash.putExtra("from", "notifi");
        notifyIntentFlash.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notifyIntentJunkRam = new Intent(this, JunkAndRamActivity.class);
        notifyIntentJunkRam.putExtra("from", "notifi");
        notifyIntentJunkRam.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
        myHandler.removeCallbacks(runnableW);
        myHandler.post(runnableW);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void changZhuTongzhi() {
        Notification.Builder mBuilder = new Notification.Builder(this);
        remoteView_1 = new RemoteViews(getPackageName(),
                R.layout.layout_notification);
        canvas.drawArc(oval, 0, 270 * CommonUtil.getMemory(this) / 100, false, paint_1);
        remoteView_1.setImageViewBitmap(R.id.notifi_memory, bitmap_progress);
        remoteView_1.setTextViewText(R.id.norifi_memory_text, CommonUtil.getMemory(this) + "%");
        int requestCode = (int) SystemClock.uptimeMillis();
        PendingIntent pendIntentMain = PendingIntent.getActivity(this, requestCode,
                notifyIntentMain, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendIntentRam = PendingIntent.getActivity(this, requestCode,
                notifyIntentRam, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendIntentCooling = PendingIntent.getActivity(this, requestCode,
                notifyIntentCooling, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendIntentFlash = PendingIntent.getActivity(this, requestCode,
                notifyIntentFlash, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendIntentJunkRam = PendingIntent.getActivity(this, requestCode,
                notifyIntentJunkRam, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteView_1.setOnClickPendingIntent(R.id.notifi_icon, pendIntentMain);
        remoteView_1.setOnClickPendingIntent(R.id.notifi_ram, pendIntentRam);
        remoteView_1.setOnClickPendingIntent(R.id.notifi_cooling, pendIntentCooling);
        remoteView_1.setOnClickPendingIntent(R.id.notifi_flash, pendIntentFlash);
        remoteView_1.setOnClickPendingIntent(R.id.notifi_junk_ram, pendIntentJunkRam);
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

    private Runnable runnableW = new Runnable() {
        public void run() {
            long nowTotalRxBytes = getTotalRxBytes(); // 获取当前数据总量
            long nowTimeStamp = System.currentTimeMillis(); // 当前时间
            // kb/s
            long speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp == lastTimeStamp ? nowTimeStamp : nowTimeStamp
                    - lastTimeStamp));// 毫秒转换
//            tv.setText(String.valueOf(speed) + "b/s");
            String type = phoneManager.getPhoneNetworkType();
            if (type.equals("WIFI")) {
                remoteView_1.setImageViewResource(R.id.notifi_network_type, R.mipmap.notifi_wifi);
            } else if (type.equals("MOBILE")) {
                remoteView_1.setImageViewResource(R.id.notifi_network_type, R.mipmap.notifi_liuliang);
            } else {
                remoteView_1.setImageViewResource(R.id.notifi_network_type, R.drawable.translate);
            }
            remoteView_1.setTextViewText(R.id.notifi_network_sudu, CommonUtil.getFileSizeWifi(speed));
            mNotifyManager.notify(154, notification_1);
            lastTimeStamp = nowTimeStamp;
            lastTotalRxBytes = nowTotalRxBytes;
            myHandler.postDelayed(this, 2000);
        }
    };

    private long getTotalRxBytes() {
        // 得到整个手机的流量值  
        return TrafficStats.getUidRxBytes(getApplicationInfo().uid) == TrafficStats.UNSUPPORTED ? 0
                : (TrafficStats.getTotalRxBytes());//
        // // 得到当前应用的流量值  
        // return TrafficStats.getUidRxBytes(getApplicationInfo().uid) ==  
        // TrafficStats.UNSUPPORTED ? 0 : (TrafficStats  
        // .getUidRxBytes(getApplicationInfo().uid) / 1024);// 转为KB  

    }

    private void update() {
        //cpu温度
        CpuTempReader.getCPUTemp(new CpuTempReader.TemperatureResultCallback() {
            @Override
            public void callbackResult(CpuTempReader.ResultCpuTemperature result) {
                if (result != null) {
                    cpuTemp = (int) result.getTemperature();
                } else {
                    cpuTemp = 40;
                }
                Log.e("notifi", "cpuTemp=" + cpuTemp);
                remoteView_1.setTextViewText(R.id.notifi_cpu, cpuTemp + "℃");
            }
        });
        int memory = CommonUtil.getMemory(this);
        if (memory > 70) {
            paint_1.setColor(this.getResources().getColor(R.color.app_color_third));
        } else {
            paint_1.setColor(this.getResources().getColor(R.color.white_100));
        }
        canvas.drawArc(oval, 0, 270 * memory / 100, false, paint_1);
        remoteView_1.setImageViewBitmap(R.id.notifi_memory, bitmap_progress);
        remoteView_1.setTextViewText(R.id.norifi_memory_text, memory + "%");
        mNotifyManager.notify(154, notification_1);
        long time = System.currentTimeMillis();
        if (PreData.getDB(this, Contents.TONGZHI_SWITCH, true)) {
            int hh = Integer.parseInt(CommonUtil.getStrTimeHH(time));
            //ram
            if (hh >= 6 && hh < 12 && PreData.getDB(this, Contents.KEY_TONGZHI_ZAO_RAM, true)) {
                PreData.putDB(NotifactionService.this, Contents.KEY_TONGZHI_ZHONG_RAM, true);
                PreData.putDB(NotifactionService.this, Contents.KEY_TONGZHI_WAN_RAM, true);
                if (memory > 80) {
                    tonghzi_Ram();
                    mNotifyManager.notify(101, notification_ram);
                    PreData.putDB(NotifactionService.this, Contents.KEY_TONGZHI_ZAO_RAM, false);
                }
            } else if (hh >= 12 && hh < 18 && PreData.getDB(this, Contents.KEY_TONGZHI_ZHONG_RAM, true)) {
                PreData.putDB(NotifactionService.this, Contents.KEY_TONGZHI_ZAO_RAM, true);
                PreData.putDB(NotifactionService.this, Contents.KEY_TONGZHI_WAN_RAM, true);
                if (memory > 80) {
                    tonghzi_Ram();
                    mNotifyManager.notify(101, notification_ram);
                    PreData.putDB(NotifactionService.this, Contents.KEY_TONGZHI_ZHONG_RAM, false);
                }
            } else if (hh >= 18 && PreData.getDB(this, Contents.KEY_TONGZHI_WAN_RAM, true)) {
                PreData.putDB(NotifactionService.this, Contents.KEY_TONGZHI_ZAO_RAM, true);
                PreData.putDB(NotifactionService.this, Contents.KEY_TONGZHI_ZHONG_RAM, true);
                if (memory > 80) {
                    tonghzi_Ram();
                    mNotifyManager.notify(101, notification_ram);
                    PreData.putDB(NotifactionService.this, Contents.KEY_TONGZHI_WAN_RAM, false);
                }
            }
            //cooling
            if (hh >= 6 && hh < 12 && PreData.getDB(this, Contents.KEY_TONGZHI_ZAO_COOLING, true)) {
                PreData.putDB(NotifactionService.this, Contents.KEY_TONGZHI_ZHONG_COOLING, true);
                PreData.putDB(NotifactionService.this, Contents.KEY_TONGZHI_WAN_COOLING, true);
                if (cpuTemp > 50) {
                    tonghzi_cooling();
                    mNotifyManager.notify(101, notification_cooling);
                    PreData.putDB(NotifactionService.this, Contents.KEY_TONGZHI_ZAO_COOLING, false);
                }
            } else if (hh >= 12 && hh < 18 && PreData.getDB(this, Contents.KEY_TONGZHI_ZHONG_COOLING, true)) {
                PreData.putDB(NotifactionService.this, Contents.KEY_TONGZHI_ZAO_COOLING, true);
                PreData.putDB(NotifactionService.this, Contents.KEY_TONGZHI_WAN_COOLING, true);
                if (cpuTemp > 50) {
                    tonghzi_cooling();
                    mNotifyManager.notify(101, notification_cooling);
                    PreData.putDB(NotifactionService.this, Contents.KEY_TONGZHI_ZHONG_COOLING, false);
                }
            } else if (hh >= 18 && PreData.getDB(this, Contents.KEY_TONGZHI_WAN_COOLING, true)) {
                PreData.putDB(NotifactionService.this, Contents.KEY_TONGZHI_ZAO_COOLING, true);
                PreData.putDB(NotifactionService.this, Contents.KEY_TONGZHI_ZHONG_COOLING, true);
                if (cpuTemp > 50) {
                    tonghzi_cooling();
                    mNotifyManager.notify(101, notification_cooling);
                    PreData.putDB(NotifactionService.this, Contents.KEY_TONGZHI_WAN_COOLING, false);
                }
            }
            //junk
            long laji_size = cleanApplication.getCacheSize() + cleanApplication.getApkSize() + cleanApplication.getUnloadSize()
                    + cleanApplication.getLogSize() + cleanApplication.getDataSize() + cleanApplication.getRamSize();
            if (hh >= 6 && hh < 12 && PreData.getDB(this, Contents.KEY_TONGZHI_ZAO_JUNK, true)) {
                PreData.putDB(NotifactionService.this, Contents.KEY_TONGZHI_ZHONG_JUNK, true);
                PreData.putDB(NotifactionService.this, Contents.KEY_TONGZHI_WAN_JUNK, true);
                if (laji_size > 200 * 1024 * 1024) {
                    tonghzi_junk();
                    mNotifyManager.notify(101, notification_junk);
                    PreData.putDB(NotifactionService.this, Contents.KEY_TONGZHI_ZAO_JUNK, false);
                }
            } else if (hh >= 12 && hh < 18 && PreData.getDB(this, Contents.KEY_TONGZHI_ZHONG_JUNK, true)) {
                PreData.putDB(NotifactionService.this, Contents.KEY_TONGZHI_ZAO_JUNK, true);
                PreData.putDB(NotifactionService.this, Contents.KEY_TONGZHI_WAN_JUNK, true);
                if (laji_size > 200 * 1024 * 1024) {
                    tonghzi_junk();
                    mNotifyManager.notify(101, notification_junk);
                    PreData.putDB(NotifactionService.this, Contents.KEY_TONGZHI_ZHONG_JUNK, false);
                }
            } else if (hh >= 18 && PreData.getDB(this, Contents.KEY_TONGZHI_WAN_JUNK, true)) {
                PreData.putDB(NotifactionService.this, Contents.KEY_TONGZHI_ZAO_JUNK, true);
                PreData.putDB(NotifactionService.this, Contents.KEY_TONGZHI_ZHONG_JUNK, true);
                if (laji_size > 200 * 1024 * 1024) {
                    tonghzi_junk();
                    mNotifyManager.notify(101, notification_junk);
                    PreData.putDB(NotifactionService.this, Contents.KEY_TONGZHI_WAN_JUNK, false);
                }
            }
//            int dd = Integer.parseInt(CommonUtil.getStrTimedd(time));
//            if (dd % 3 == 0 && hh == 18 && PreData.getDB(this, Contents.KEY_FILE_SAN, true)) {
//
//                if (laji_size > 0 & cleanApplication.isSaomiaoSuccess()) {
//                    tonghzi_junk();
//                    mNotifyManager.notify(101, notification_ram);
//                    PreData.putDB(NotifactionService.this, Contents.KEY_FILE_SAN, false);
//                }
//            } else if (hh != 18) {
//                PreData.putDB(NotifactionService.this, Contents.KEY_FILE_SAN, true);
//            }
        }

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void tonghzi_Ram() {
        Notification.Builder mBuilder = new Notification.Builder(this);
        RemoteViews remoteView = new RemoteViews(getPackageName(),
                R.layout.layout_tongzhi_ram);
        int requestCode = (int) SystemClock.uptimeMillis();
        PendingIntent pendIntent = PendingIntent.getActivity(this, requestCode,
                notifyIntentRam, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContent(remoteView);
        mBuilder.setContentIntent(pendIntent);
        mBuilder.setAutoCancel(true);
        mBuilder.setOngoing(false);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setSmallIcon(R.mipmap.notification_title);
        notification_ram = mBuilder.build();
        notification_ram.defaults = Notification.DEFAULT_SOUND;
        notification_ram.flags = Notification.FLAG_AUTO_CANCEL;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void tonghzi_cooling() {
        Notification.Builder mBuilder = new Notification.Builder(this);
        RemoteViews remoteView = new RemoteViews(getPackageName(),
                R.layout.layout_tongzhi_cooling);
        int requestCode = (int) SystemClock.uptimeMillis();
        PendingIntent pendIntent = PendingIntent.getActivity(this, requestCode,
                notifyIntentCooling, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContent(remoteView);
        mBuilder.setContentIntent(pendIntent);
        mBuilder.setAutoCancel(true);
        mBuilder.setOngoing(false);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setSmallIcon(R.mipmap.notification_title);
        notification_cooling = mBuilder.build();
        notification_cooling.defaults = Notification.DEFAULT_SOUND;
        notification_cooling.flags = Notification.FLAG_AUTO_CANCEL;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void tonghzi_junk() {
        Notification.Builder mBuilder = new Notification.Builder(this);
        RemoteViews remoteView = new RemoteViews(getPackageName(),
                R.layout.layout_tongzhi_junk);
        int requestCode = (int) SystemClock.uptimeMillis();
        PendingIntent pendIntent = PendingIntent.getActivity(this, requestCode,
                notifyIntentJunkRam, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContent(remoteView);
        mBuilder.setContentIntent(pendIntent);
        mBuilder.setAutoCancel(true);
        mBuilder.setOngoing(false);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setSmallIcon(R.mipmap.notification_title);
        notification_junk = mBuilder.build();
        notification_junk.defaults = Notification.DEFAULT_SOUND;
        notification_junk.flags = Notification.FLAG_AUTO_CANCEL;
    }


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        if (!PreData.getDB(this, Contents.TONGZHILAN_SWITCH, true)) {
            mNotifyManager.cancel(154);
        }
        myHandler.removeCallbacks(runnable);
        myHandler.removeCallbacks(runnableW);
        mNotifyManager.cancel(155);
        super.onDestroy();
    }
}
