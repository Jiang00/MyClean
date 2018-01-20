package com.eifmobi.clean.junk.service;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;

import com.eifmobi.clean.db.CleanDBHelper;
import com.eifmobi.clean.junk.R;
import com.eifmobi.clean.util.Util;
import com.eifmobi.clean.junk.presenter.GetTopApp;
import com.eifmobi.clean.junk.presenter.FloatStateManager;
import com.eifmobi.clean.junk.util.Constant;
import com.eifmobi.clean.util.PreData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2017/3/7.
 */

public class FloatService extends Service {
    private PackageManager pm;
    private List<String> hmoes;
    private int count = 0;
    private ActivityManager am;
    private GetTopApp topApp;
    private LinearLayout mFullScreenCheckView;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    Handler myHandler;
    FloatStateManager manager;
    WindowManager mana;


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updata();
            myHandler.postDelayed(runnable, 2000);
        }
    };
    String runingGboost;

    private void updata() {
        if (count == 0) {
            count = 30;
            int memory = Util.getMemory(this);
            manager.upDate(memory);
        }
        count--;
        manager.upDate(Util.getMemory(FloatService.this));
        new Thread(new Runnable() {
            @Override
            public void run() {
                String pkg = topApp.execute();
//                if (hmoes.contains(pkg)) {
//                    manager.upDate(Util.getMemory(FloatService.this));
//                    manager.addWindowsView();
//                } else {
//                    manager.removeWindowsView();
//                }
                if (!TextUtils.equals(pkg, runingGboost)) {
                    runingGboost = pkg;
                    if (PreData.getDB(FloatService.this, Constant.TONGZHILAN_SWITCH, true)) {
                        ArrayList<String> gboost_names = CleanDBHelper.getInstance(FloatService.this).getWhiteList(CleanDBHelper.TableType.GameBoost);
                        if (gboost_names.contains(pkg)) {
                            Intent intent = new Intent(FloatService.this, NotificationService.class);
                            intent.putExtra("from", "gboost");
                            startService(intent);
                        }
                    }
                }
            }
        }).start();

    }

    public boolean isScreenOriatationPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (myHandler == null) {
            myHandler = new Handler();
        }

        if (pm == null)
            pm = getPackageManager();
        if (am == null)
            am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        topApp = new GetTopApp(this);
        hmoes = getLaunchers();
        manager.showFloatCircleView();
        myHandler.removeCallbacks(runnable);
        myHandler.postDelayed(runnable, 1000);
//        manager.addWindowsView();
        return START_REDELIVER_INTENT;
    }


    private void createFullScreenCheckView() {
        final LayoutParams layoutParams = new LayoutParams();
        layoutParams.type = LayoutParams.TYPE_PHONE;
        layoutParams.format = PixelFormat.TRANSPARENT;
        layoutParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.width = 1;
        layoutParams.height = LayoutParams.MATCH_PARENT;
        mFullScreenCheckView = (LinearLayout) View.inflate(this, R.layout.layout_linear, null);
        mFullScreenCheckView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {

                DisplayMetrics dm = new DisplayMetrics();
                mana.getDefaultDisplay().getMetrics(dm);
                int viewHeight = mFullScreenCheckView.getHeight();
                if (viewHeight == dm.widthPixels || viewHeight == dm.heightPixels) {
                    manager.removeWindowsView();
                } else {
                    manager.addWindowsView();
                }
            }

        });
        try {
            mana.addView(mFullScreenCheckView, layoutParams);
        } catch (Exception e) {
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        manager = FloatStateManager.getInstance(FloatService.this);
        mana = (WindowManager) FloatService.this.getSystemService(Context.WINDOW_SERVICE);
        createFullScreenCheckView();

    }

    public List<String> getLaunchers() {
        List<String> packageNames = new ArrayList<>();
        PackageManager packageManager = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        if (packageManager == null) {
            return packageNames;
        }
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resolveInfos) {
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            if (activityInfo != null) {
                packageNames.add(resolveInfo.activityInfo.processName);
                packageNames.add(resolveInfo.activityInfo.packageName);
                packageNames.add("com.miui.core");
            }
        }
        return packageNames;
    }

    @Override
    public void onDestroy() {
        myHandler.removeCallbacks(runnable);
        try {
            manager.removeWindowsView();
            mana.removeView(mFullScreenCheckView);
        } catch (Exception e) {
        }
        super.onDestroy();
    }
}