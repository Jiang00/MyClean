package com.supers.clean.junk.widget;

import android.app.ActivityManager;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.ColorStateList;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.supers.clean.junk.R;
import com.supers.clean.junk.activity.SuccessActivity;
import com.supers.clean.junk.modle.CommonUtil;

import java.util.List;
import java.util.Timer;

/**
 * Created by Ivy on 2017/3/28.
 */

public class WidgetService extends Service {
    private Timer mTimer;
    private Handler handler;
    private KillBackg killBackg;
    private int memory, memory_kill;
    private RemoteViews rv;
    private AppWidgetManager manager;
    private ComponentName cn;
    private boolean isRuning;
    private ColorStateList green, yellow, red;
    private Thread kill, runing;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        rv = new RemoteViews(getPackageName(), R.layout.layout_widget_ram);
        manager = AppWidgetManager.getInstance(getApplicationContext());
        cn = new ComponentName(getApplicationContext(), WidgetProvider.class);
        green = WidgetService.this.getResources().getColorStateList(R.color.widget_green);
        yellow = WidgetService.this.getResources().getColorStateList(R.color.widget_yellow);
        red = WidgetService.this.getResources().getColorStateList(R.color.widget_red);
        killBackg = new KillBackg() {
            @Override
            public void killS(int memory, long M) {
                WidgetService.this.memory_kill = memory;
            }
        };
        kill = new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
        runing = new Thread(new Runnable() {
            @Override
            public void run() {
                isRuning = true;
                handler.removeCallbacks(runnable);
                int memory_chushi = 80;
                for (int i = memory_chushi; i > 0; i -= 3) {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();

                    }
                    if (i < 0) {
                        i = 0;
                    }
                    rv.setProgressBar(R.id.widget_progress, 100, i, false);
                    if (i <= 40) {
                        rv.setProgressTintList(R.id.widget_progress, green);
                    } else if (i <= 80) {
                        rv.setProgressTintList(R.id.widget_progress, yellow);
                    } else {
                        rv.setProgressTintList(R.id.widget_progress, red);
                    }
                    rv.setTextViewText(R.id.widget_text, i + "% " + getText(R.string.widget_ram).toString());
                    manager.updateAppWidget(cn, rv);
                    Log.e("thread", "t=" + CommonUtil.getStrTime(System.currentTimeMillis()));
//                    sendBroadcast(new Intent("app.eosmobi.action.widget.ram").putExtra("memory", i));
                }
//                killAll(WidgetService.this);
                for (int i = 0; i <= memory; i += 3) {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (i > memory)
                        i = memory;
                    if (i <= 40) {
                        rv.setProgressTintList(R.id.widget_progress, green);
                    } else if (i <= 80) {
                        rv.setProgressTintList(R.id.widget_progress, yellow);
                    } else {
                        rv.setProgressTintList(R.id.widget_progress, red);
                    }
                    rv.setProgressBar(R.id.widget_progress, 100, i, false);
                    rv.setTextViewText(R.id.widget_text, i + "% " + getText(R.string.widget_ram).toString());
                    manager.updateAppWidget(cn, rv);
//                    sendBroadcast(new Intent("app.eosmobi.action.widget.ram").putExtra("memory", i));
                    Log.e("thread", "a=" + CommonUtil.getStrTime(System.currentTimeMillis()));
                }
                handler.postDelayed(runnable, 1000);
                isRuning = false;
                Log.e("thread", "s=" + CommonUtil.getStrTime(System.currentTimeMillis()));
            }
        });
//        mTimer = new Timer();
//        mTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                updata();
//            }
//        }, 0, 1000);
        handler.removeCallbacks(runnable);
        handler.post(runnable);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sendBroadcast(new Intent("app.eosmobi.action.widget.ram").putExtra("memory", memory = CommonUtil.getMemory(WidgetService.this)));
            handler.postDelayed(runnable, 1000);
        }
    };
    boolean isxiao = true;
    Runnable runnableT = new Runnable() {
        @Override
        public void run() {
            if (isxiao) {
                isRuning = true;
                memory -= 3;
                if (memory < 2) {
                    memory = 0;
                    isxiao = false;
                }
            } else {
                memory += 3;
                if (memory > memory_kill) {
                    memory = memory_kill;
                    isxiao = true;
                    isRuning = false;
                }
            }

            rv.setProgressBar(R.id.widget_progress, 100, memory, false);
            if (memory <= 40) {
                rv.setProgressTintList(R.id.widget_progress, green);
            } else if (memory <= 80) {
                rv.setProgressTintList(R.id.widget_progress, yellow);
            } else {
                rv.setProgressTintList(R.id.widget_progress, red);
            }
            rv.setTextViewText(R.id.widget_text, memory + "% ");
            manager.updateAppWidget(cn, rv);
            if (isRuning) {
                handler.postDelayed(runnableT, 20);
            } else {
                startActivity(new Intent(WidgetService.this, SuccessActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("size", 0));
                handler.postDelayed(runnable, 1000);
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isRuning) {
            updata();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void updata() {
        handler.removeCallbacks(runnable);
        handler.post(runnableT);
        new Thread(new Runnable() {
            @Override
            public void run() {
                killAll(WidgetService.this);
            }
        }).start();
//        kill.start();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                isRuning = true;
//                rv = new RemoteViews(getPackageName(), R.layout.layout_widget_ram);
//                handler.removeCallbacks(runnable);
//                int memory_chushi = memory;
//                killAll(WidgetService.this);
//                for (int i = memory_chushi; i >= 0; i -= 3) {
//                    try {
//                        Thread.sleep(20);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    if (i < 3) {
//                        i = 0;
//                    }
//                    rv.setProgressBar(R.id.widget_progress, 100, i, false);
//                    if (i <= 40) {
//                        rv.setProgressTintList(R.id.widget_progress, green);
//                    } else if (i <= 80) {
//                        rv.setProgressTintList(R.id.widget_progress, yellow);
//                    } else {
//                        rv.setProgressTintList(R.id.widget_progress, red);
//                    }
//                    rv.setTextViewText(R.id.widget_text, i + "% ");
//                    manager.updateAppWidget(cn, rv);
//                    Log.e("thread", "t=" + CommonUtil.getStrTime(System.currentTimeMillis()));
////                    sendBroadcast(new Intent("app.eosmobi.action.widget.ram").putExtra("memory", i));
//                }
//                for (int i = 0; i <= memory; i += 3) {
//                    try {
//                        Thread.sleep(20);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    if (i > memory)
//                        i = memory;
//                    if (i <= 40) {
//                        rv.setProgressTintList(R.id.widget_progress, green);
//                    } else if (i <= 80) {
//                        rv.setProgressTintList(R.id.widget_progress, yellow);
//                    } else {
//                        rv.setProgressTintList(R.id.widget_progress, red);
//                    }
//                    rv.setProgressBar(R.id.widget_progress, 100, i, false);
//                    rv.setTextViewText(R.id.widget_text, i + "% ");
//                    manager.updateAppWidget(cn, rv);
////                    sendBroadcast(new Intent("app.eosmobi.action.widget.ram").putExtra("memory", i));
//                    Log.e("thread", "a=" + CommonUtil.getStrTime(System.currentTimeMillis()));
//                }
//                handler.postDelayed(runnable, 1000);
//                isRuning = false;
//                Thread.currentThread().interrupt();
//                Log.e("thread", "s=" + CommonUtil.getStrTime(System.currentTimeMillis()));
//            }
//        }).start();
    }

    public void up() {

    }

    public void killAll(Context context) {
        final ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<PackageInfo> installedPackages = context.getPackageManager().getInstalledPackages(0);
        final long beforeMem = getAvailMemory(am);
        for (PackageInfo packageInfo : installedPackages) {
            if (packageInfo.packageName.equals(context.getPackageName())) {
                continue;
            }
            am.killBackgroundProcesses(packageInfo.packageName);
        }
        final long afterMem = getAvailMemory(am);
        final long M = (afterMem - beforeMem);
        if (killBackg != null) {
            killBackg.killS(CommonUtil.getMemory(context), M);
        }
    }

    private long getAvailMemory(ActivityManager am) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存
        return (mi.availMem);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer = null;
    }

    interface KillBackg {
        void killS(int memory, long M);
    }
}
