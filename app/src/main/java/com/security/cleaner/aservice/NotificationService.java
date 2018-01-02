package com.security.cleaner.aservice;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

import com.security.cleaner.activity.JunkFileActivity;
import com.security.cleaner.activity.LajiAndRamActivity;
import com.security.cleaner.activity.LoadingActivity;
import com.security.cleaner.activity.MainActivity;
import com.security.cleaner.activity.NotificationActivity;
import com.security.cleaner.activity.SuccessActivity;
import com.security.cleaner.utils.ASwitchControl;
import com.security.cleaner.utils.AdUtil;
import com.security.cleaner.utils.Constant;
import com.security.cleaner.utils.PhoneManager;
import com.security.cleaner.utils.RCpuTempReader;
import com.security.mcleaner.manager.CleanManager;
import com.security.mcleaner.notification.NotificationCallBack;
import com.security.mcleaner.notification.NotificationInfo;
import com.security.cleaner.R;
import com.security.cleaner.activity.CpuCoolingActivity;
import com.security.cleaner.activity.ARamAvtivity;
import com.security.mcleaner.mutil.Util;
import com.security.mcleaner.mutil.PreData;

import java.util.ArrayList;


public class NotificationService extends Service {
    private static final String FLASH_ACTION = "action.flash.clean";
    Handler myHandler;
    private RemoteViews remoteView_1, remoteViewNotifi;
    private Notification notification_1;
    private Notification notification_notifi;
    private PhoneManager phoneManager;
    private NotificationManager mNotifyManager;
    private Notification notification_ram, notification_cooling, notification_junk, notification_two_day, notification_gboost;

    private Intent notifyIntentLoading, notifyIntentRam, notifyIntentCooling, notifyIntentFlash, notifyIntentJunkRam, notifyIntentNotifi, notifyIntentGBoost;

    private Bitmap bitmap_progress;
    private Paint paint_1;
    private int pointX;
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
        if (myHandler == null)
            myHandler = new Handler();
        phoneManager = PhoneManager.getPhoneManage(this);
        initIntent();
        bitmap_progress = Bitmap.createBitmap(getResources().getDimensionPixelOffset(R.dimen.d33),
                getResources().getDimensionPixelOffset(R.dimen.d33), Bitmap.Config.ARGB_8888);
        paint_1 = new Paint();
        paint_1.setAntiAlias(true);
        paint_1.setStrokeCap(Paint.Cap.ROUND);
        paint_1.setStrokeWidth(Util.dp2px(2));
        paint_1.setStyle(Paint.Style.STROKE);
        pointX = getResources().getDimensionPixelOffset(R.dimen.d33) / 2;
        oval = new RectF(0 + Util.dp2px(3), -pointX + Util.dp2px(3), pointX
                * 2 - Util.dp2px(3), pointX - Util.dp2px(3));
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FLASH_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);
        changZhuTongzhi();
        tonghzi_notifi();
        CleanManager.getInstance(this).addNotificationCallBack(notificationCallBack);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean iskai = ASwitchControl.switchFlashlight();
            remoteView_1.setImageViewResource(R.id.notifi_shou, iskai ? R.mipmap.notifi_shou_2 : R.mipmap.notifi_shou_1);
            mNotifyManager.notify(102, notification_1);
        }
    };

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


    private void initIntent() {
        notifyIntentRam = new Intent(this, ARamAvtivity.class);
        notifyIntentRam.putExtra("from", "notifi");
        notifyIntentRam.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notifyIntentLoading = new Intent(this, MainActivity.class);
        notifyIntentLoading.putExtra("from", "notifi");
        notifyIntentLoading.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notifyIntentCooling = new Intent(this, CpuCoolingActivity.class);
        notifyIntentCooling.putExtra("from", "notifi");
        notifyIntentCooling.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notifyIntentJunkRam = new Intent(this, JunkFileActivity.class);
        notifyIntentJunkRam.putExtra("from", "notifi");
        notifyIntentJunkRam.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notifyIntentFlash = new Intent(FLASH_ACTION);
        notifyIntentFlash.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notifyIntentNotifi = new Intent(this, NotificationActivity.class);
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
        if (intent != null && TextUtils.equals("notification", intent.getStringExtra("from"))) {
            onstart();
        }
        if (intent != null && TextUtils.equals("gboost", intent.getStringExtra("from"))) {
//            tonghzi_gboost();
//            mNotifyManager.notify(101, notification_gboost);
        }
        return super.onStartCommand(intent, flags, startId);
    }


    private void onstart() {
        myHandler.removeCallbacks(runnableW);
        myHandler.postAtTime(runnableW, 2000);
    }

    private void onCancle() {
        if (mNotifyManager != null)
            mNotifyManager.cancel(102);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void changZhuTongzhi() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        remoteView_1 = new RemoteViews(getPackageName(),
                R.layout.layout_notification);
        int memory = Util.getMemory(this);
        paint_1.setColor(ContextCompat.getColor(this, R.color.white_50));
        Canvas canvas = getCanvas();
        canvas.drawArc(oval, 0, 360, false, paint_1);
        if (memory > 70) {
            paint_1.setColor(ContextCompat.getColor(this, R.color.A2));
        } else {
            paint_1.setColor(ContextCompat.getColor(this, R.color.A5));
        }
        canvas.drawArc(oval, 0, 360 * memory / 100, false, paint_1);

        remoteView_1.setImageViewBitmap(R.id.notifi_memory, bitmap_progress);
        remoteView_1.setTextViewText(R.id.norifi_memory_text, Util.getMemory(this) + "%");
        int requestCode = (int) SystemClock.uptimeMillis();
        PendingIntent pendIntentRam = PendingIntent.getActivity(this, requestCode,
                notifyIntentRam, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendIntentLoading = PendingIntent.getActivity(this, requestCode,
                notifyIntentLoading, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendIntentCooling = PendingIntent.getActivity(this, requestCode,
                notifyIntentCooling, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendIntentJunkRam = PendingIntent.getActivity(this, requestCode,
                notifyIntentJunkRam, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendIntentFlash = PendingIntent.getBroadcast(this, requestCode,
                notifyIntentFlash, PendingIntent.FLAG_UPDATE_CURRENT);
        notifyIntentJunkRam.putExtra("from2", "twoday");
        remoteView_1.setOnClickPendingIntent(R.id.notifi_main, pendIntentLoading);
        remoteView_1.setOnClickPendingIntent(R.id.notifi_ram, pendIntentRam);
        remoteView_1.setOnClickPendingIntent(R.id.notifi_cooling, pendIntentCooling);
        remoteView_1.setOnClickPendingIntent(R.id.notifi_junk_ram, pendIntentJunkRam);
        remoteView_1.setOnClickPendingIntent(R.id.notifi_flash, pendIntentFlash);
        remoteView_1.setImageViewResource(R.id.notifi_shou, ASwitchControl.getSwitchFlashlight() ? R.mipmap.notifi_shou_2 : R.mipmap.notifi_shou_1);

        mBuilder.setContent(remoteView_1);
        mBuilder.setAutoCancel(false);
        mBuilder.setOngoing(true);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setSmallIcon(R.mipmap.loading_icon);
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
            update();
            myHandler.postDelayed(this, 20000);
        }
    };


    private void update() {

        final int memory = Util.getMemory(this);
        //cpu温度
        RCpuTempReader.getCPUTemp(new RCpuTempReader.TemperatureResultCallback() {
            @Override
            public void callbackResult(RCpuTempReader.ResultCpuTemperature result) {
                if (result != null) {
                    cpuTemp = (int) result.getTemperature();
                } else {
                    cpuTemp = 40;
                }
            }
        });
        paint_1.setColor(ContextCompat.getColor(NotificationService.this, R.color.white_50));
        Canvas canvas = getCanvas();
        canvas.drawArc(oval, 0, 360, false, paint_1);
        if (memory > 70) {
            paint_1.setColor(ContextCompat.getColor(NotificationService.this, R.color.A2));
        } else {
            paint_1.setColor(ContextCompat.getColor(NotificationService.this, R.color.A5));
        }
        canvas.drawArc(oval, 0, 360 * memory / 100, false, paint_1);
        remoteView_1.setImageViewBitmap(R.id.notifi_memory, bitmap_progress);
        remoteView_1.setTextViewText(R.id.norifi_memory_text, memory + "%");
        mNotifyManager.notify(102, notification_1);

        long time = System.currentTimeMillis();
        if (PreData.getDB(this, Constant.TONGZHI_SWITCH, true)) {
            int hh = Integer.parseInt(Util.getStrTimeHH(time));
            //ram
            if (hh >= 6 && hh < 12 && PreData.getDB(this, Constant.KEY_TONGZHI_ZAO_RAM, true)) {
                PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_ZHONG_RAM, true);
                PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_WAN_RAM, true);
                if (memory > 80) {
                    tonghzi_Ram();
                    mNotifyManager.notify(101, notification_ram);
                    AdUtil.track("通知栏", "内存通知", "展示", 1);
                    PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_ZAO_RAM, false);
                }
                return;
            } else if (hh >= 12 && hh < 18 && PreData.getDB(this, Constant.KEY_TONGZHI_ZHONG_RAM, true)) {
                PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_ZAO_RAM, true);
                PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_WAN_RAM, true);
                if (memory > 80) {
                    tonghzi_Ram();
                    mNotifyManager.notify(101, notification_ram);
                    AdUtil.track("通知栏", "内存通知", "展示", 1);
                    PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_ZHONG_RAM, false);
                }
                return;
            } else if (hh >= 18 && PreData.getDB(this, Constant.KEY_TONGZHI_WAN_RAM, true)) {
                PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_ZAO_RAM, true);
                PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_ZHONG_RAM, true);
                if (memory > 80) {
                    tonghzi_Ram();
                    mNotifyManager.notify(101, notification_ram);
                    AdUtil.track("通知栏", "内存通知", "展示", 1);
                    PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_WAN_RAM, false);
                }
                return;
            }
            //cooling
            if (hh >= 6 && hh < 12 && PreData.getDB(this, Constant.KEY_TONGZHI_ZAO_COOLING, true)) {
                PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_ZHONG_COOLING, true);
                PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_WAN_COOLING, true);
                if (cpuTemp > 50) {
                    tonghzi_cooling();
                    mNotifyManager.notify(101, notification_cooling);
                    AdUtil.track("通知栏", "降温通知", "展示", 1);
                    PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_ZAO_COOLING, false);
                }
                return;
            } else if (hh >= 12 && hh < 18 && PreData.getDB(this, Constant.KEY_TONGZHI_ZHONG_COOLING, true)) {
                PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_ZAO_COOLING, true);
                PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_WAN_COOLING, true);
                if (cpuTemp > 50) {
                    tonghzi_cooling();
                    mNotifyManager.notify(101, notification_cooling);
                    AdUtil.track("通知栏", "降温通知", "展示", 1);
                    PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_ZHONG_COOLING, false);
                }
                return;
            } else if (hh >= 18 && PreData.getDB(this, Constant.KEY_TONGZHI_WAN_COOLING, true)) {
                PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_ZAO_COOLING, true);
                PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_ZHONG_COOLING, true);
                if (cpuTemp > 50) {
                    tonghzi_cooling();
                    mNotifyManager.notify(101, notification_cooling);
                    AdUtil.track("通知栏", "降温通知", "展示", 1);
                    PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_WAN_COOLING, false);
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
                    mNotifyManager.notify(101, notification_junk);
                    AdUtil.track("通知栏", "垃圾通知", "展示", 1);
                    PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_ZAO_JUNK, false);
                }
            } else if (hh >= 12 && hh < 18 && PreData.getDB(this, Constant.KEY_TONGZHI_ZHONG_JUNK, true)) {
                PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_ZAO_JUNK, true);
                PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_WAN_JUNK, true);
                if (laji_size > 200 * 1024 * 1024) {
                    tonghzi_junk();
                    mNotifyManager.notify(101, notification_junk);
                    AdUtil.track("通知栏", "垃圾通知", "展示", 1);
                    PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_ZHONG_JUNK, false);
                }
            } else if (hh >= 18 && PreData.getDB(this, Constant.KEY_TONGZHI_WAN_JUNK, true)) {
                PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_ZAO_JUNK, true);
                PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_ZHONG_JUNK, true);
                if (laji_size > 200 * 1024 * 1024) {
                    tonghzi_junk();
                    mNotifyManager.notify(101, notification_junk);
                    AdUtil.track("通知栏", "垃圾通知", "展示", 1);
                    PreData.putDB(NotificationService.this, Constant.KEY_TONGZHI_WAN_JUNK, false);
                }
            }
            long clean_two_day = PreData.getDB(NotificationService.this, Constant.KEY_CLEAN_TIME, 0l);
            if (Util.millTransFate(time - clean_two_day) > 2) {
                tonghzi_two_day();
                mNotifyManager.notify(101, notification_two_day);
                AdUtil.track("通知栏", "两天唤醒", "展示", 1);
                PreData.putDB(this, Constant.KEY_CLEAN_TIME, System.currentTimeMillis());
            }
        }

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void tonghzi_two_day() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        RemoteViews remoteView = new RemoteViews(getPackageName(),
                R.layout.layout_two_day);
        int requestCode = (int) SystemClock.uptimeMillis();
        PendingIntent pendIntent = PendingIntent.getActivity(this, requestCode,
                notifyIntentJunkRam, PendingIntent.FLAG_CANCEL_CURRENT);
        notifyIntentJunkRam.putExtra("from2", "twoday");
        mBuilder.setContent(remoteView);
        mBuilder.setContentIntent(pendIntent);
        mBuilder.setAutoCancel(true);
        mBuilder.setOngoing(false);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setSmallIcon(R.mipmap.loading_icon);
        notification_two_day = mBuilder.build();
        notification_two_day.defaults = Notification.DEFAULT_SOUND;
        notification_two_day.flags = Notification.FLAG_AUTO_CANCEL;
    }

    private long getTotalRxBytes() {
        // 得到整个手机的流量值
        try {
            return TrafficStats.getUidRxBytes(getApplicationInfo().uid) == TrafficStats.UNSUPPORTED ? 0
                    : (TrafficStats.getTotalRxBytes());//
        } catch (Exception e) {
            return 0;
        }
    }

    public Canvas getCanvas() {
        Canvas canvas = new Canvas(bitmap_progress);
        canvas.save();
        canvas.translate(0, pointX);
        canvas.rotate(90, pointX, 0);
        canvas.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR);
        return canvas;
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
        mBuilder.setSmallIcon(R.mipmap.loading_icon);
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
        mBuilder.setSmallIcon(R.mipmap.loading_icon);
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
        notifyIntentJunkRam.putExtra("from2", "junk");
        mBuilder.setContent(remoteView);
        mBuilder.setContentIntent(pendIntent);
        mBuilder.setAutoCancel(true);
        mBuilder.setOngoing(false);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setSmallIcon(R.mipmap.loading_icon);
        notification_junk = mBuilder.build();
        notification_junk.defaults = Notification.DEFAULT_SOUND;
        notification_junk.flags = Notification.FLAG_AUTO_CANCEL;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void tonghzi_gboost() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        RemoteViews remoteView = new RemoteViews(getPackageName(),
                R.layout.layout_tongzhi_gboost);
        int requestCode = (int) SystemClock.uptimeMillis();
        PendingIntent pendIntent = PendingIntent.getActivity(this, requestCode,
                notifyIntentGBoost, PendingIntent.FLAG_CANCEL_CURRENT);
        notifyIntentGBoost.putExtra("from", "gboost");
        mBuilder.setContent(remoteView);
        mBuilder.setContentIntent(pendIntent);
        mBuilder.setAutoCancel(true);
        mBuilder.setOngoing(false);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setSmallIcon(R.mipmap.loading_icon);
        notification_gboost = mBuilder.build();
        notification_gboost.defaults = Notification.DEFAULT_SOUND;
        notification_gboost.flags = Notification.FLAG_AUTO_CANCEL;
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
        mBuilder.setSmallIcon(R.mipmap.loading_icon);
        notification_notifi = mBuilder.build();
        notification_notifi.flags = Notification.FLAG_INSISTENT;
        notification_notifi.flags |= Notification.FLAG_ONGOING_EVENT;
        // 表明在点击了通知栏中的"清除通知"后，此通知不清除， 经常与FLAG_ONGOING_EVENT一起使用
        notification_notifi.flags |= Notification.FLAG_NO_CLEAR;
//        notification_notifi.priority = Notification.PRIORITY_MAX;
    }


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        if (!PreData.getDB(this, Constant.TONGZHILAN_SWITCH, true)) {
            mNotifyManager.cancel(102);
        }
        unregisterReceiver(broadcastReceiver);
        myHandler.removeCallbacks(runnableW);
        CleanManager.getInstance(this).removeNotificatioCallBack(notificationCallBack);
        super.onDestroy();
    }
}
