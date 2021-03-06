package com.supers.clean.junk.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

import com.android.clean.core.CleanManager;
import com.android.clean.notification.NotificationCallBack;
import com.android.clean.notification.NotificationInfo;
import com.supers.clean.junk.R;
import com.supers.clean.junk.activity.CoolingActivity;
import com.supers.clean.junk.activity.FloatAnimationActivity;
import com.supers.clean.junk.activity.JunkAndRamActivity;
import com.supers.clean.junk.activity.MainActivity;
import com.supers.clean.junk.activity.NotifiActivity;
import com.supers.clean.junk.activity.NotifiCpuActivity;
import com.supers.clean.junk.activity.RamAvtivity;
import com.supers.clean.junk.activity.SuccessActivity;
import com.supers.clean.junk.activity.TranslateActivity;
import com.android.clean.util.Util;
import com.supers.clean.junk.util.AdUtil;
import com.android.clean.util.Constant;
import com.supers.clean.junk.util.BadgerCount;
import com.supers.clean.junk.util.CpuTempReader;
import com.supers.clean.junk.util.PhoneManager;
import com.android.clean.util.PreData;

import java.util.ArrayList;


public class NotificationService extends Service {
    Handler myHandler;
    private PhoneManager phoneManager;
    private NotificationManager mNotifyManager;
    private Notification notification_ram, notification_cooling, notification_junk, notification_two_day, notification_gboost;
    private RemoteViews remoteView_1, remoteViewNotifi;
    private Notification notification_1;
    private Notification notification_notifi;

    private Intent notifyIntentMain, notifyIntentRam, notifyIntentCooling, notifyIntentFlash, notifyIntentJunkRam, notifyIntentNotifi, notifyIntentGBoost;

    private Bitmap bitmap_progress;
    private Paint paint_1;
    private int pointX = Util.dp2px(29) / 2;
    private RectF oval;
    private int cpuTemp;
    private int num;

    private long lastTotalRxBytes = 0; // 最后缓存的字节数
    private long lastTimeStamp = 0; // 当前缓存时间

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        long time = System.currentTimeMillis();
        Log.e("loading_time", "notificraet0");
        if (myHandler == null)
            myHandler = new Handler();
        phoneManager = PhoneManager.getPhoneManage(this);
        initIntent();
        bitmap_progress = Bitmap.createBitmap(Util.dp2px(29), Util.dp2px(29), Bitmap.Config.ARGB_8888);
        paint_1 = new Paint();
        paint_1.setAntiAlias(true);
        paint_1.setStrokeCap(Paint.Cap.ROUND);
        paint_1.setStrokeWidth(Util.dp2px(1));
        paint_1.setStyle(Paint.Style.STROKE);
        oval = new RectF(0 + Util.dp2px(2), -pointX + Util.dp2px(2), pointX
                * 2 - Util.dp2px(2), pointX - Util.dp2px(2));
        changZhuTongzhi();
        tonghzi_notifi();
        CleanManager.getInstance(this).addNotificationCallBack(notificationCallBack);
        Log.e("loading_time", "loaingonResume" + (System.currentTimeMillis() - time));
    }

    NotificationCallBack notificationCallBack = new NotificationCallBack() {
        @Override
        public void notificationChanged(ArrayList<NotificationInfo> notificationList) {
            if (notificationList != null && notificationList.size() != 0) {
                tonghzi_notifi();
                LinearLayout view = (LinearLayout) LayoutInflater.from(NotificationService.this).inflate(R.layout.layout_linear, null);
                for (NotificationInfo info : notificationList) {
                    ImageView imageView = new ImageView(NotificationService.this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Util.dp2px(15), Util.dp2px(15));
                    layoutParams.rightMargin = Util.dp2px(1);
                    imageView.setLayoutParams(layoutParams);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setImageDrawable(info.icon);
                    view.addView(imageView);
                }
                remoteViewNotifi.setTextViewText(R.id.num, notificationList.size() + " ");
                remoteViewNotifi.setImageViewBitmap(R.id.ll_notifi, screenShot(view, notificationList.size()));
                mNotifyManager.notify(103, notification_notifi);
            } else {
                mNotifyManager.cancel(103);
            }
        }
    };


    public Canvas getCanvas() {
        Canvas canvas = new Canvas(bitmap_progress);
        canvas.save();
        canvas.translate(0, pointX);
        canvas.rotate(135, pointX, 0);
        canvas.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR);
        return canvas;
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
        notifyIntentNotifi = new Intent(this, NotifiActivity.class);
        notifyIntentNotifi.putExtra("from", "notifi");
        notifyIntentNotifi.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notifyIntentGBoost = new Intent(this, SuccessActivity.class);
        notifyIntentGBoost.putExtra("from", "gboost");
        notifyIntentGBoost.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && "notification".equals(intent.getStringExtra("from"))) {
            onstart();
        }
        if (intent != null && "gboost".equals(intent.getStringExtra("from"))) {
            tonghzi_gboost();
            mNotifyManager.cancel(101);
            mNotifyManager.notify(101, notification_gboost);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void onstart() {
        myHandler.removeCallbacks(runnableW);
        myHandler.postAtTime(runnableW, 2000);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void changZhuTongzhi() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        remoteView_1 = new RemoteViews(getPackageName(),
                R.layout.layout_notification);
        int memory = Util.getMemory(this);
        paint_1.setColor(ContextCompat.getColor(this, R.color.white_40));
        Canvas canvas = getCanvas();
        canvas.drawArc(oval, 0, 270, false, paint_1);
        if (memory > 70) {
            paint_1.setColor(ContextCompat.getColor(this, R.color.A2));
        } else {
            paint_1.setColor(ContextCompat.getColor(this, R.color.white_100));
        }
        canvas.drawArc(oval, 0, 270 * memory / 100, false, paint_1);

        remoteView_1.setImageViewBitmap(R.id.notifi_memory, bitmap_progress);
        remoteView_1.setTextViewText(R.id.norifi_memory_text, Util.getMemory(this) + "%");
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
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setSmallIcon(R.mipmap.notification_title);
        notification_1 = mBuilder.build();
        notification_1.flags = Notification.FLAG_INSISTENT;
        notification_1.flags |= Notification.FLAG_ONGOING_EVENT;
        // 表明在点击了通知栏中的"清除通知"后，此通知不清除， 经常与FLAG_ONGOING_EVENT一起使用
        notification_1.flags |= Notification.FLAG_NO_CLEAR;
        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotifyManager.notify(102, notification_1);
    }

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
            remoteView_1.setTextViewText(R.id.notifi_network_sudu, Util.convertStorageWifi(speed));
            num++;
            if (num >= 10) {
                num = 0;
                update();
            } else {
                mNotifyManager.notify(102, notification_1);
            }
            lastTimeStamp = nowTimeStamp;
            lastTotalRxBytes = nowTotalRxBytes;
            myHandler.postDelayed(this, 2000);
        }
    };

    private long getTotalRxBytes() {
        // 得到整个手机的流量值
        try {
            return TrafficStats.getUidRxBytes(getApplicationInfo().uid) == TrafficStats.UNSUPPORTED ? 0
                    : (TrafficStats.getTotalRxBytes());//
        } catch (Exception e) {
            return 0;
        }
    }

    private void update() {

        final int memory = Util.getMemory(this);
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
                paint_1.setColor(ContextCompat.getColor(NotificationService.this, R.color.white_40));
                Canvas canvas = getCanvas();
                canvas.drawArc(oval, 0, 270, false, paint_1);
                if (memory > 70) {
                    paint_1.setColor(ContextCompat.getColor(NotificationService.this, R.color.A2));
                } else {
                    paint_1.setColor(ContextCompat.getColor(NotificationService.this, R.color.white_100));
                }
                canvas.drawArc(oval, 0, 270 * memory / 100, false, paint_1);
                remoteView_1.setImageViewBitmap(R.id.notifi_memory, bitmap_progress);
                remoteView_1.setTextViewText(R.id.norifi_memory_text, memory + "%");
                mNotifyManager.notify(102, notification_1);
            }
        });
        long time = System.currentTimeMillis();
        if (PreData.getDB(this, Constant.TAN_COOLING, true)) {
            int hh = Integer.parseInt(Util.getStrTimeHH(time));
            //cooling
            if (hh >= 6 && hh < 12 && PreData.getDB(this, Constant.KEY_TAN_ZAO_COOLING, true)) {
                PreData.putDB(NotificationService.this, Constant.KEY_TAN_ZHONG_COOLING, true);
                PreData.putDB(NotificationService.this, Constant.KEY_TAN_WAN_COOLING, true);
                if (cpuTemp > 50) {
                    AdUtil.track("通知栏", "降温弹窗", "展示", 1);
                    PreData.putDB(NotificationService.this, Constant.KEY_TAN_ZAO_COOLING, false);
                    Intent intent = new Intent(this, NotifiCpuActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
                    try {
                        pendingIntent.send(); // 监听到Home键按下后立即调用startActivity启动Activity会有5s延迟
                    } catch (PendingIntent.CanceledException e) {
                        e.printStackTrace();
                    }
                }
            } else if (hh >= 12 && hh < 18 && PreData.getDB(this, Constant.KEY_TAN_ZHONG_COOLING, true)) {
                PreData.putDB(NotificationService.this, Constant.KEY_TAN_ZAO_COOLING, true);
                PreData.putDB(NotificationService.this, Constant.KEY_TAN_WAN_COOLING, true);
                if (cpuTemp > 50) {
                    AdUtil.track("通知栏", "降温弹窗", "展示", 1);
                    PreData.putDB(NotificationService.this, Constant.KEY_TAN_ZHONG_COOLING, false);
                    Intent intent = new Intent(this, NotifiCpuActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
                    try {
                        pendingIntent.send(); // 监听到Home键按下后立即调用startActivity启动Activity会有5s延迟
                    } catch (PendingIntent.CanceledException e) {
                        e.printStackTrace();
                    }
                }
            } else if (hh >= 18 && PreData.getDB(this, Constant.KEY_TAN_WAN_COOLING, true)) {
                PreData.putDB(NotificationService.this, Constant.KEY_TAN_ZAO_COOLING, true);
                PreData.putDB(NotificationService.this, Constant.KEY_TAN_ZHONG_COOLING, true);
                if (cpuTemp > 50) {
                    AdUtil.track("通知栏", "降温弹窗", "展示", 1);
                    PreData.putDB(NotificationService.this, Constant.KEY_TAN_WAN_COOLING, false);
                    Intent intent = new Intent(this, NotifiCpuActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
                    try {
                        pendingIntent.send(); // 监听到Home键按下后立即调用startActivity启动Activity会有5s延迟
                    } catch (PendingIntent.CanceledException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (PreData.getDB(this, Constant.TONGZHI_SWITCH, true)) {
            int hh = Integer.parseInt(Util.getStrTimeHH(time));
            //cooling
            if (hh >= 6 && hh < 12 && PreData.getDB(this, Constant.KEY_TONGZHI_ZAO_COOLING, true)) {
                PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_ZHONG_COOLING, true);
                PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_WAN_COOLING, true);
                if (cpuTemp > 50) {
                    tonghzi_cooling();
                    mNotifyManager.cancel(101);
                    mNotifyManager.notify(101, notification_cooling);
                    AdUtil.track("通知栏", "降温通知", "展示", 1);
                    PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_ZAO_COOLING, false);
                    PreData.putDB(NotificationService.this, Constant.HONG_COOLING, true);
                    BadgerCount.setCount(NotificationService.this);
                }
                return;
            } else if (hh >= 12 && hh < 18 && PreData.getDB(this, Constant.KEY_TONGZHI_ZHONG_COOLING, true)) {
                PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_ZAO_COOLING, true);
                PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_WAN_COOLING, true);
                if (cpuTemp > 50) {
                    tonghzi_cooling();
                    mNotifyManager.cancel(101);
                    mNotifyManager.notify(101, notification_cooling);
                    AdUtil.track("通知栏", "降温通知", "展示", 1);
                    PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_ZHONG_COOLING, false);
                    PreData.putDB(NotificationService.this, Constant.HONG_COOLING, true);
                    BadgerCount.setCount(NotificationService.this);
                }
                return;
            } else if (hh >= 18 && PreData.getDB(this, Constant.KEY_TONGZHI_WAN_COOLING, true)) {
                PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_ZAO_COOLING, true);
                PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_ZHONG_COOLING, true);
                if (cpuTemp > 50) {
                    tonghzi_cooling();
                    mNotifyManager.cancel(101);
                    mNotifyManager.notify(101, notification_cooling);
                    AdUtil.track("通知栏", "降温通知", "展示", 1);
                    PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_WAN_COOLING, false);
                    PreData.putDB(NotificationService.this, Constant.HONG_COOLING, true);
                    BadgerCount.setCount(NotificationService.this);
                }
                return;
            }
            //ram
            if (hh >= 6 && hh < 12 && PreData.getDB(this, Constant.KEY_TONGZHI_ZAO_RAM, true)) {
                PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_ZHONG_RAM, true);
                PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_WAN_RAM, true);
                if (memory > 80) {
                    tonghzi_Ram();
                    mNotifyManager.cancel(101);
                    mNotifyManager.notify(101, notification_ram);
                    AdUtil.track("通知栏", "内存通知", "展示", 1);
                    PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_ZAO_RAM, false);
                    PreData.putDB(NotificationService.this, Constant.HONG_RAM, true);
                    BadgerCount.setCount(NotificationService.this);
                }
                return;
            } else if (hh >= 12 && hh < 18 && PreData.getDB(this, Constant.KEY_TONGZHI_ZHONG_RAM, true)) {
                PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_ZAO_RAM, true);
                PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_WAN_RAM, true);
                if (memory > 80) {
                    tonghzi_Ram();
                    mNotifyManager.cancel(101);
                    mNotifyManager.notify(101, notification_ram);
                    AdUtil.track("通知栏", "内存通知", "展示", 1);
                    PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_ZHONG_RAM, false);
                    PreData.putDB(NotificationService.this, Constant.HONG_RAM, true);
                    BadgerCount.setCount(NotificationService.this);
                }
                return;
            } else if (hh >= 18 && PreData.getDB(this, Constant.KEY_TONGZHI_WAN_RAM, true)) {
                PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_ZAO_RAM, true);
                PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_ZHONG_RAM, true);
                if (memory > 80) {
                    tonghzi_Ram();
                    mNotifyManager.cancel(101);
                    mNotifyManager.notify(101, notification_ram);
                    AdUtil.track("通知栏", "内存通知", "展示", 1);
                    PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_WAN_RAM, false);
                    PreData.putDB(NotificationService.this, Constant.HONG_RAM, true);
                    BadgerCount.setCount(NotificationService.this);
                }
                return;
            }

            //junk
            long laji_size = CleanManager.getInstance(this).getApkSize() + CleanManager.getInstance(this).getCacheSize() + CleanManager.getInstance(this).getUnloadSize() + CleanManager.getInstance(this).getLogSize()
                    + CleanManager.getInstance(this).getDataSize() + CleanManager.getInstance(this).getRamSize();

            if (hh >= 6 && hh < 12 && PreData.getDB(this, Constant.KEY_TONGZHI_ZAO_JUNK, true)) {
                PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_ZHONG_JUNK, true);
                PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_WAN_JUNK, true);
                if (laji_size > 200 * 1024 * 1024) {
                    tonghzi_junk();
                    mNotifyManager.cancel(101);
                    mNotifyManager.notify(101, notification_junk);
                    AdUtil.track("通知栏", "垃圾通知", "展示", 1);
                    PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_ZAO_JUNK, false);
                    PreData.putDB(NotificationService.this, Constant.HONG_JUNK, true);
                    BadgerCount.setCount(NotificationService.this);
                }
            } else if (hh >= 12 && hh < 18 && PreData.getDB(this, Constant.KEY_TONGZHI_ZHONG_JUNK, true)) {
                PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_ZAO_JUNK, true);
                PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_WAN_JUNK, true);
                if (laji_size > 200 * 1024 * 1024) {
                    tonghzi_junk();
                    mNotifyManager.cancel(101);
                    mNotifyManager.notify(101, notification_junk);
                    AdUtil.track("通知栏", "垃圾通知", "展示", 1);
                    PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_ZHONG_JUNK, false);
                    PreData.putDB(NotificationService.this, Constant.HONG_JUNK, true);
                    BadgerCount.setCount(NotificationService.this);
                }
            } else if (hh >= 18 && PreData.getDB(this, Constant.KEY_TONGZHI_WAN_JUNK, true)) {
                PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_ZAO_JUNK, true);
                PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_ZHONG_JUNK, true);
                if (laji_size > 200 * 1024 * 1024) {
                    tonghzi_junk();
                    mNotifyManager.cancel(101);
                    mNotifyManager.notify(101, notification_junk);
                    AdUtil.track("通知栏", "垃圾通知", "展示", 1);
                    PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_WAN_JUNK, false);
                    PreData.putDB(NotificationService.this, Constant.HONG_JUNK, true);
                    BadgerCount.setCount(NotificationService.this);
                }
            }
            long clean_two_day = PreData.getDB(NotificationService.this, Constant.KEY_CLEAN_TIME, 0l);
            if (Util.millTransFate(time - clean_two_day) > 2) {
                tonghzi_two_day();
                mNotifyManager.cancel(101);
                mNotifyManager.notify(101, notification_two_day);
                AdUtil.track("通知栏", "两天唤醒", "展示", 1);
                PreData.putDB(this, Constant.KEY_CLEAN_TIME, System.currentTimeMillis());
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void tonghzi_Ram() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
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
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
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
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void tonghzi_two_day() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        RemoteViews remoteView = new RemoteViews(getPackageName(),
                R.layout.layout_two_day);
        int requestCode = (int) SystemClock.uptimeMillis();
        PendingIntent pendIntent = PendingIntent.getActivity(this, requestCode,
                notifyIntentJunkRam, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContent(remoteView);
        mBuilder.setContentIntent(pendIntent);
        mBuilder.setAutoCancel(true);
        mBuilder.setOngoing(false);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setSmallIcon(R.mipmap.notification_title);
        notification_two_day = mBuilder.build();
        notification_two_day.defaults = Notification.DEFAULT_SOUND;
        notification_two_day.flags = Notification.FLAG_AUTO_CANCEL;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void tonghzi_gboost() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        RemoteViews remoteView = new RemoteViews(getPackageName(),
                R.layout.layout_tongzhi_gboost);
        int requestCode = (int) SystemClock.uptimeMillis();
        PendingIntent pendIntent = PendingIntent.getActivity(this, requestCode,
                notifyIntentGBoost, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContent(remoteView);
        mBuilder.setContentIntent(pendIntent);
        mBuilder.setAutoCancel(true);
        mBuilder.setOngoing(false);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setSmallIcon(R.mipmap.notification_title);
        notification_gboost = mBuilder.build();
        notification_gboost.defaults = Notification.DEFAULT_SOUND;
        notification_gboost.flags = Notification.FLAG_AUTO_CANCEL;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void tonghzi_notifi() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        int requestCode = (int) SystemClock.uptimeMillis();
        remoteViewNotifi = new RemoteViews(getPackageName(),
                R.layout.layout_tongzhi_notifi);

        PendingIntent pendIntent = PendingIntent.getActivity(this, requestCode,
                notifyIntentNotifi, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(pendIntent);
        mBuilder.setContent(remoteViewNotifi);
        mBuilder.setAutoCancel(false);
        mBuilder.setOngoing(true);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setSmallIcon(R.mipmap.notifi_small);
        notification_notifi = mBuilder.build();
        notification_notifi.flags = Notification.FLAG_INSISTENT;
        notification_notifi.flags |= Notification.FLAG_ONGOING_EVENT;
        // 表明在点击了通知栏中的"清除通知"后，此通知不清除， 经常与FLAG_ONGOING_EVENT一起使用
        notification_notifi.flags |= Notification.FLAG_NO_CLEAR;
//        notification_notifi.priority = Notification.PRIORITY_MAX;
    }

    public Bitmap screenShot(View view, int size) {
        if (null == view) {
            throw new IllegalArgumentException("parameter can't be null.");
        }
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, Util.dp2px(16) * (size > 8 ? 8 : size), Util.dp2px(15));
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        if (!PreData.getDB(this, Constant.TONGZHILAN_SWITCH, true)) {
            mNotifyManager.cancel(102);
        }
        myHandler.removeCallbacks(runnableW);
        CleanManager.getInstance(this).removeNotificatioCallBack(notificationCallBack);
        super.onDestroy();
    }
}
