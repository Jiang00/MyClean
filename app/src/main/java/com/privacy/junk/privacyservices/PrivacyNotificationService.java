package com.privacy.junk.privacyservices;

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

import com.privacy.clean.core.CleanManager;
import com.privacy.clean.cleannotification.NotificationCallBack;
import com.privacy.clean.cleannotification.NotificationInfo;
import com.privacy.clean.utilsprivacy.MyUtils;
import com.privacy.clean.utilsprivacy.PreData;
import com.privacy.junk.R;
import com.privacy.junk.activityprivacy.PrivacyBatteriesActivity;
import com.privacy.junk.activityprivacy.MainActivity;
import com.privacy.junk.activityprivacy.PrivacyMemoryAvtivity;
import com.privacy.junk.activityprivacy.MyApplication;
import com.privacy.junk.activityprivacy.MyNotifingActivityPrivacy;
import com.privacy.junk.activityprivacy.PrivacyRubbishAndRamActivity;
import com.privacy.junk.activityprivacy.PrivacySucceedActivity;
import com.privacy.junk.toolsprivacy.PrivacyGetCpuTempReader;
import com.privacy.junk.toolsprivacy.MyConstantPrivacy;
import com.privacy.junk.toolsprivacy.PhonesManager;
import com.privacy.junk.toolsprivacy.SetAdUtilPrivacy;

import java.util.ArrayList;


public class PrivacyNotificationService extends Service {
    Handler myHandler;
    private PhonesManager phoneManager;
    private NotificationManager mNotifyManager;
    private Notification notification_ram, notification_cooling, notification_junk, notification_two_day, notification_gboost;
    private RemoteViews remoteView_1, remoteViewNotifi;
    private Notification notification_1;
    private Notification notification_notifi;

    private MyApplication cleanApplication;
    private Intent notifyIntentMain, notifyIntentRam, notifyIntentCooling, notifyIntentJunkRam, notifyIntentNotifi, notifyIntentGBoost;

    private Bitmap bitmap_progress;
    private Paint paint_1;
    private Paint paint1;
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
        cleanApplication = (MyApplication) getApplication();
        phoneManager = PhonesManager.getPhoneManage(this);
        initIntent();
        bitmap_progress = Bitmap.createBitmap(getResources().getDimensionPixelOffset(R.dimen.d33),
                getResources().getDimensionPixelOffset(R.dimen.d33), Bitmap.Config.ARGB_8888);//里面A代表Alpha，R表示red，G表示green，B表示blue.

        paint_1 = new Paint();
        paint_1.setAntiAlias(true);
        paint_1.setStrokeCap(Paint.Cap.ROUND);
        paint_1.setStrokeWidth(MyUtils.dp2px(2));
        paint_1.setStyle(Paint.Style.STROKE);
        pointX = getResources().getDimensionPixelOffset(R.dimen.d33) / 2;
        // oval = new RectF(0 + MyUtils.dp2px(5), -pointX + MyUtils.dp2px(5), pointX
//                * 2 - MyUtils.dp2px(5), pointX - MyUtils.dp2px(5));

        oval = new RectF(0, 0,
                getResources().getDimensionPixelOffset(R.dimen.d30), getResources().getDimensionPixelOffset(R.dimen.d30));

        oval.left = getResources().getDimensionPixelSize(R.dimen.d3);
        oval.top = getResources().getDimensionPixelSize(R.dimen.d3);
        changZhuTongzhi();
        tonghzi_notifi();
        CleanManager.getInstance(this).addNotificationCallBack(notificationCallBack);
    }

    NotificationCallBack notificationCallBack = new NotificationCallBack() {
        @Override
        public void notificationChanged(ArrayList<NotificationInfo> notificationList) {
            if (notificationList != null && notificationList.size() != 0) {
                tonghzi_notifi();
                LinearLayout view = (LinearLayout) LayoutInflater.from(PrivacyNotificationService.this).inflate(R.layout.layout_linear, null);
                for (NotificationInfo info : notificationList) {
                    ImageView imageView = new ImageView(PrivacyNotificationService.this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MyUtils.dp2px(15), MyUtils.dp2px(15));
                    layoutParams.rightMargin = MyUtils.dp2px(1);
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

    public Canvas getCanvas2() {
        paint1 = new Paint();
        paint1.setColor(ContextCompat.getColor(this, R.color.A5));
        paint1.setAntiAlias(true);
        paint1.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.d2));
        paint1.setStyle(Paint.Style.STROKE);

        Canvas canvas = new Canvas(bitmap_progress);
        canvas.save();
        return canvas;
    }

    private void initIntent() {
        notifyIntentMain = new Intent(this, MainActivity.class);
        notifyIntentMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notifyIntentRam = new Intent(this, PrivacyMemoryAvtivity.class);
        notifyIntentRam.putExtra("from", "notifi");
        notifyIntentRam.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notifyIntentCooling = new Intent(this, PrivacyBatteriesActivity.class);
        notifyIntentCooling.putExtra("from", "notifi");
        notifyIntentCooling.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notifyIntentJunkRam = new Intent(this, PrivacyRubbishAndRamActivity.class);
        notifyIntentJunkRam.putExtra("from", "notifi");
        notifyIntentJunkRam.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notifyIntentNotifi = new Intent(this, MyNotifingActivityPrivacy.class);
        notifyIntentNotifi.putExtra("from", "notifi");
        notifyIntentNotifi.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notifyIntentGBoost = new Intent(this, PrivacySucceedActivity.class);
        notifyIntentGBoost.putExtra("from", "gboost");
        notifyIntentGBoost.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction().equals("notification")) {
            onstart();
        }
        if (intent != null && intent.getAction().equals("gboost")) {
            tonghzi_gboost();
            mNotifyManager.notify(101, notification_gboost);
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
        int memory = MyUtils.getMemory(this);
        paint_1.setColor(ContextCompat.getColor(this, R.color.B3));

        Canvas canvas = getCanvas2();
        canvas.drawArc(oval, 90, 360, false, paint1);

        if (memory < 30) {
            paint1.setColor(ContextCompat.getColor(this, R.color.A1));
        } else if (memory >= 30 && memory < 80) {
            paint1.setColor(ContextCompat.getColor(this, R.color.A12));
        } else {
            paint1.setColor(ContextCompat.getColor(this, R.color.A3));
        }
        canvas.drawArc(oval, 135, 360 * memory / 100, false, paint1);
        remoteView_1.setImageViewBitmap(R.id.notifi_memory, bitmap_progress);


        if (MyUtils.getMemory(this) < 30) {
            remoteView_1.setTextColor(R.id.norifi_memory_text, ContextCompat.getColor(this, R.color.A1));
        } else if (MyUtils.getMemory(this) >= 30 && MyUtils.getMemory(this) < 80) {
            remoteView_1.setTextColor(R.id.norifi_memory_text, ContextCompat.getColor(this, R.color.A12));
        } else {
            remoteView_1.setTextColor(R.id.norifi_memory_text, ContextCompat.getColor(this, R.color.A3));
        }
        remoteView_1.setTextViewText(R.id.norifi_memory_text, MyUtils.getMemory(this) + "%");
        int requestCode = (int) SystemClock.uptimeMillis();
        PendingIntent pendIntentMain = PendingIntent.getActivity(this, requestCode,
                notifyIntentMain, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendIntentRam = PendingIntent.getActivity(this, requestCode,
                notifyIntentRam, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendIntentCooling = PendingIntent.getActivity(this, requestCode,
                notifyIntentCooling, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendIntentJunkRam = PendingIntent.getActivity(this, requestCode,
                notifyIntentJunkRam, PendingIntent.FLAG_UPDATE_CURRENT);

        notifyIntentJunkRam.putExtra("from2", "twoday");

        remoteView_1.setOnClickPendingIntent(R.id.notifi_icon, pendIntentMain);
        remoteView_1.setOnClickPendingIntent(R.id.notifi_ram, pendIntentRam);
        remoteView_1.setOnClickPendingIntent(R.id.notifi_cooling, pendIntentCooling);
        remoteView_1.setOnClickPendingIntent(R.id.notifi_junk_ram, pendIntentJunkRam);
//        remoteView_1.setOnClickPendingIntent(R.id.notifi_network_type, pendIntentJunkRam);
        mBuilder.setContent(remoteView_1);
        mBuilder.setAutoCancel(false);
        mBuilder.setOngoing(true);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setSmallIcon(R.mipmap.notify_icon);
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
                remoteView_1.setImageViewResource(R.id.notifi_network_type, R.mipmap.notifi_wifi1);
            } else if (type.equals("MOBILE")) {
                remoteView_1.setImageViewResource(R.id.notifi_network_type, R.mipmap.notifi_liuliang);
            } else {
                remoteView_1.setImageViewResource(R.id.notifi_network_type, R.mipmap.notifi_wifi);
            }
            remoteView_1.setTextViewText(R.id.notifi_network_sudu, MyUtils.convertStorageWifi(speed));
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


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void tonghzi_junk(long laji_size) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        RemoteViews remoteView = new RemoteViews(getPackageName(),
                R.layout.layout_tongzhi_junk);
        int requestCode = (int) SystemClock.uptimeMillis();

        PendingIntent pendIntent = PendingIntent.getActivity(this, requestCode,
                notifyIntentJunkRam, PendingIntent.FLAG_CANCEL_CURRENT);
        notifyIntentJunkRam.putExtra("from2", "junk");

        if (laji_size > 200 && laji_size < 300) {
            remoteView.setTextColor(R.id.tv_memory, ContextCompat.getColor(this, R.color.A12));
        } else {
            remoteView.setTextColor(R.id.tv_memory, ContextCompat.getColor(this, R.color.A3));
        }
        remoteView.setTextViewText(R.id.tv_memory, laji_size + "MB");

        mBuilder.setContent(remoteView);
        mBuilder.setContentIntent(pendIntent);
        mBuilder.setAutoCancel(true);
        mBuilder.setOngoing(false);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setSmallIcon(R.mipmap.notify_icon);
        notification_junk = mBuilder.build();
        notification_junk.defaults = Notification.DEFAULT_SOUND;
        notification_junk.flags = Notification.FLAG_AUTO_CANCEL;
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
        mBuilder.setSmallIcon(R.mipmap.notify_icon);
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
        if (cpuTemp > 50 && cpuTemp < 80) {
            remoteView.setTextColor(R.id.tv_memory, ContextCompat.getColor(this, R.color.A12));
        } else {
            remoteView.setTextColor(R.id.tv_memory, ContextCompat.getColor(this, R.color.A3));
        }
        remoteView.setTextViewText(R.id.tv_memory, cpuTemp + "℃");

        mBuilder.setContent(remoteView);
        mBuilder.setContentIntent(pendIntent);
        mBuilder.setAutoCancel(true);
        mBuilder.setOngoing(false);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setSmallIcon(R.mipmap.notify_icon);
        notification_cooling = mBuilder.build();
        notification_cooling.defaults = Notification.DEFAULT_SOUND;
        notification_cooling.flags = Notification.FLAG_AUTO_CANCEL;
    }

    private void update() {

        final int memory = MyUtils.getMemory(this);
        //cpu温度
        PrivacyGetCpuTempReader.getCPUTemp(new PrivacyGetCpuTempReader.TemperatureResultCallback() {
            @Override
            public void callbackResult(PrivacyGetCpuTempReader.ResultCpuTemperature result) {
                if (result != null) {
                    cpuTemp = (int) result.getTemperature();
                } else {
                    cpuTemp = 40;
                }
                Log.e("notifi", "cpuTemp=" + cpuTemp);
                if (cpuTemp < 30) {
                    remoteView_1.setTextColor(R.id.notifi_cpu, ContextCompat.getColor(PrivacyNotificationService.this, R.color.A1));
                    remoteView_1.setTextViewText(R.id.notifi_cpu, cpuTemp + "℃");
                } else if (cpuTemp >= 30 && cpuTemp < 80) {
                    remoteView_1.setTextColor(R.id.notifi_cpu, ContextCompat.getColor(PrivacyNotificationService.this, R.color.A12));
                    remoteView_1.setTextViewText(R.id.notifi_cpu, cpuTemp + "℃");
                } else {
                    remoteView_1.setTextColor(R.id.notifi_cpu, ContextCompat.getColor(PrivacyNotificationService.this, R.color.A3));
                    remoteView_1.setTextViewText(R.id.notifi_cpu, cpuTemp + "℃");
                }
                paint_1.setColor(ContextCompat.getColor(PrivacyNotificationService.this, R.color.A19));
                Canvas canvas = getCanvas2();
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//清除之前画的
                canvas.drawArc(oval, 0, 360, false, paint1);
                if (memory < 30) {
                    paint1.setColor(ContextCompat.getColor(PrivacyNotificationService.this, R.color.A1));
                } else if (memory >= 30 && memory < 80) {
                    paint1.setColor(ContextCompat.getColor(PrivacyNotificationService.this, R.color.A12));
                } else {
                    paint1.setColor(ContextCompat.getColor(PrivacyNotificationService.this, R.color.A3));
                }
                canvas.drawArc(oval, 135, 360 * memory / 100, false, paint1);
                remoteView_1.setImageViewBitmap(R.id.notifi_memory, bitmap_progress);

                if (MyUtils.getMemory(PrivacyNotificationService.this) < 30) {
                    remoteView_1.setTextColor(R.id.norifi_memory_text, ContextCompat.getColor(PrivacyNotificationService.this, R.color.A1));
                } else if (MyUtils.getMemory(PrivacyNotificationService.this) >= 30 && MyUtils.getMemory(PrivacyNotificationService.this) < 80) {
                    remoteView_1.setTextColor(R.id.norifi_memory_text, ContextCompat.getColor(PrivacyNotificationService.this, R.color.A12));
                } else {
                    remoteView_1.setTextColor(R.id.norifi_memory_text, ContextCompat.getColor(PrivacyNotificationService.this, R.color.A3));
                }
                remoteView_1.setTextViewText(R.id.norifi_memory_text, memory + "%");
                mNotifyManager.notify(102, notification_1);
            }
        });
        long time = System.currentTimeMillis();
        if (PreData.getDB(this, MyConstantPrivacy.TONGZHI_SWITCH, true)) {
            int hh = Integer.parseInt(MyUtils.getStrTimeHH(time));
            //ram
            if (hh >= 6 && hh < 12 && PreData.getDB(this, MyConstantPrivacy.KEY_TONGZHI_ZAO_RAM, true)) {
                PreData.putDB(PrivacyNotificationService.this, MyConstantPrivacy.KEY_TONGZHI_ZHONG_RAM, true);
                PreData.putDB(PrivacyNotificationService.this, MyConstantPrivacy.KEY_TONGZHI_WAN_RAM, true);
                if (memory > 70) {
                    tonghzi_Ram();
                    mNotifyManager.notify(101, notification_ram);
                    SetAdUtilPrivacy.track("通知栏", "内存通知", "展示", 1);
                    PreData.putDB(PrivacyNotificationService.this, MyConstantPrivacy.KEY_TONGZHI_ZAO_RAM, false);
                }
                return;
            } else if (hh >= 12 && hh < 18 && PreData.getDB(this, MyConstantPrivacy.KEY_TONGZHI_ZHONG_RAM, true)) {
                PreData.putDB(PrivacyNotificationService.this, MyConstantPrivacy.KEY_TONGZHI_ZAO_RAM, true);
                PreData.putDB(PrivacyNotificationService.this, MyConstantPrivacy.KEY_TONGZHI_WAN_RAM, true);
                if (memory > 70) {
                    tonghzi_Ram();
                    mNotifyManager.notify(101, notification_ram);
                    SetAdUtilPrivacy.track("通知栏", "内存通知", "展示", 1);
                    PreData.putDB(PrivacyNotificationService.this, MyConstantPrivacy.KEY_TONGZHI_ZHONG_RAM, false);
                }
                return;
            } else if (hh >= 18 && PreData.getDB(this, MyConstantPrivacy.KEY_TONGZHI_WAN_RAM, true)) {
                PreData.putDB(PrivacyNotificationService.this, MyConstantPrivacy.KEY_TONGZHI_ZAO_RAM, true);
                PreData.putDB(PrivacyNotificationService.this, MyConstantPrivacy.KEY_TONGZHI_ZHONG_RAM, true);
                if (memory > 70) {
                    tonghzi_Ram();
                    mNotifyManager.notify(101, notification_ram);
                    SetAdUtilPrivacy.track("通知栏", "内存通知", "展示", 1);
                    PreData.putDB(PrivacyNotificationService.this, MyConstantPrivacy.KEY_TONGZHI_WAN_RAM, false);
                }
                return;
            }
            //cooling
            if (hh >= 6 && hh < 12 && PreData.getDB(this, MyConstantPrivacy.KEY_TONGZHI_ZAO_COOLING, true)) {
                PreData.putDB(PrivacyNotificationService.this, MyConstantPrivacy.KEY_TONGZHI_ZHONG_COOLING, true);
                PreData.putDB(PrivacyNotificationService.this, MyConstantPrivacy.KEY_TONGZHI_WAN_COOLING, true);
                if (cpuTemp > 50) {
                    tonghzi_cooling();
                    mNotifyManager.notify(101, notification_cooling);
                    SetAdUtilPrivacy.track("通知栏", "降温通知", "展示", 1);
                    PreData.putDB(PrivacyNotificationService.this, MyConstantPrivacy.KEY_TONGZHI_ZAO_COOLING, false);
                }
                return;
            } else if (hh >= 12 && hh < 18 && PreData.getDB(this, MyConstantPrivacy.KEY_TONGZHI_ZHONG_COOLING, true)) {
                PreData.putDB(PrivacyNotificationService.this, MyConstantPrivacy.KEY_TONGZHI_ZAO_COOLING, true);
                PreData.putDB(PrivacyNotificationService.this, MyConstantPrivacy.KEY_TONGZHI_WAN_COOLING, true);
                if (cpuTemp > 50) {
                    tonghzi_cooling();
                    mNotifyManager.notify(101, notification_cooling);
                    SetAdUtilPrivacy.track("通知栏", "降温通知", "展示", 1);
                    PreData.putDB(PrivacyNotificationService.this, MyConstantPrivacy.KEY_TONGZHI_ZHONG_COOLING, false);
                }
                return;
            } else if (hh >= 18 && PreData.getDB(this, MyConstantPrivacy.KEY_TONGZHI_WAN_COOLING, true)) {
                PreData.putDB(PrivacyNotificationService.this, MyConstantPrivacy.KEY_TONGZHI_ZAO_COOLING, true);
                PreData.putDB(PrivacyNotificationService.this, MyConstantPrivacy.KEY_TONGZHI_ZHONG_COOLING, true);
                if (cpuTemp > 50) {
                    tonghzi_cooling();
                    mNotifyManager.notify(101, notification_cooling);
                    SetAdUtilPrivacy.track("通知栏", "降温通知", "展示", 1);
                    PreData.putDB(PrivacyNotificationService.this, MyConstantPrivacy.KEY_TONGZHI_WAN_COOLING, false);
                }
                return;
            }
            //junk
            long laji_size = CleanManager.getInstance(this).getApkSize() + CleanManager.getInstance(this).getCacheSize() + CleanManager.getInstance(this).getUnloadSize() + CleanManager.getInstance(this).getLogSize()
                    + CleanManager.getInstance(this).getDataSize() + CleanManager.getInstance(this).getRamSize();

            if (hh >= 6 && hh < 12 && PreData.getDB(this, MyConstantPrivacy.KEY_TONGZHI_ZAO_JUNK, true)) {
                PreData.putDB(PrivacyNotificationService.this, MyConstantPrivacy.KEY_TONGZHI_ZHONG_JUNK, true);
                PreData.putDB(PrivacyNotificationService.this, MyConstantPrivacy.KEY_TONGZHI_WAN_JUNK, true);
                if (laji_size > 200 * 1024 * 1024) {
                    tonghzi_junk(laji_size);
                    mNotifyManager.notify(101, notification_junk);
                    SetAdUtilPrivacy.track("通知栏", "垃圾通知", "展示", 1);
                    PreData.putDB(PrivacyNotificationService.this, MyConstantPrivacy.KEY_TONGZHI_ZAO_JUNK, false);
                }
            } else if (hh >= 12 && hh < 18 && PreData.getDB(this, MyConstantPrivacy.KEY_TONGZHI_ZHONG_JUNK, true)) {
                PreData.putDB(PrivacyNotificationService.this, MyConstantPrivacy.KEY_TONGZHI_ZAO_JUNK, true);
                PreData.putDB(PrivacyNotificationService.this, MyConstantPrivacy.KEY_TONGZHI_WAN_JUNK, true);
                if (laji_size > 200 * 1024 * 1024) {
                    tonghzi_junk(laji_size);
                    mNotifyManager.notify(101, notification_junk);
                    SetAdUtilPrivacy.track("通知栏", "垃圾通知", "展示", 1);
                    PreData.putDB(PrivacyNotificationService.this, MyConstantPrivacy.KEY_TONGZHI_ZHONG_JUNK, false);
                }
            } else if (hh >= 18 && PreData.getDB(this, MyConstantPrivacy.KEY_TONGZHI_WAN_JUNK, true)) {
                PreData.putDB(PrivacyNotificationService.this, MyConstantPrivacy.KEY_TONGZHI_ZAO_JUNK, true);
                PreData.putDB(PrivacyNotificationService.this, MyConstantPrivacy.KEY_TONGZHI_ZHONG_JUNK, true);
                if (laji_size > 200 * 1024 * 1024) {
                    tonghzi_junk(laji_size);
                    mNotifyManager.notify(101, notification_junk);
                    SetAdUtilPrivacy.track("通知栏", "垃圾通知", "展示", 1);
                    PreData.putDB(PrivacyNotificationService.this, MyConstantPrivacy.KEY_TONGZHI_WAN_JUNK, false);
                }
            }
            long clean_two_day = PreData.getDB(PrivacyNotificationService.this, MyConstantPrivacy.KEY_CLEAN_TIME, 0l);
            if (MyUtils.millTransFate(time - clean_two_day) > 2) {
                tonghzi_two_day();
                mNotifyManager.notify(101, notification_two_day);
                SetAdUtilPrivacy.track("通知栏", "两天唤醒", "展示", 1);
                PreData.putDB(this, MyConstantPrivacy.KEY_CLEAN_TIME, System.currentTimeMillis());
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
        mBuilder.setSmallIcon(R.mipmap.notify_icon);
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

        // // 得到当前应用的流量值
        // return TrafficStats.getUidRxBytes(getApplicationInfo().uid) ==
        // TrafficStats.UNSUPPORTED ? 0 : (TrafficStats
        // .getUidRxBytes(getApplicationInfo().uid) / 1024);// 转为KB

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
        mBuilder.setSmallIcon(R.mipmap.notify_icon);
        notification_gboost = mBuilder.build();
        notification_gboost.defaults = Notification.DEFAULT_SOUND;
        notification_gboost.flags = Notification.FLAG_AUTO_CANCEL;
    }

    public Bitmap screenShot(View view, int size) {
        if (null == view) {
            throw new IllegalArgumentException("parameter can't be null.");
        }
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, MyUtils.dp2px(16) * (size > 8 ? 8 : size), MyUtils.dp2px(15));
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
        mBuilder.setSmallIcon(R.mipmap.notify_icon);
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
        if (!PreData.getDB(this, MyConstantPrivacy.TONGZHILAN_SWITCH, true)) {
            mNotifyManager.cancel(102);
        }
        myHandler.removeCallbacks(runnableW);
        CleanManager.getInstance(this).removeNotificatioCallBack(notificationCallBack);
        super.onDestroy();
    }
}
