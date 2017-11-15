package com.supers.clean.junk.presenter;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.android.clean.util.Util;
import com.supers.clean.junk.R;
import com.supers.clean.junk.activity.FloatActivity;
import com.supers.clean.junk.activity.FloatAnimationActivity;
import com.supers.clean.junk.customeview.FloatDiView;
import com.supers.clean.junk.customeview.FloatHuoView;
import com.supers.clean.junk.customeview.FloatStateView;

import java.lang.reflect.Field;

/**
 * 浮窗管理者的创建(单例)
 * <p>
 * 1 私有化构造行数
 * 2 创建静态的返回浮窗管理类的方法
 * <p>
 * Created by chengyuan on 16/8/12.
 */
public class FloatStateManager {
    private final Vibrator vibrator;
    private final int size_1;
    private Context context;
    private WindowManager wm; // 通过这个windowManager来操控浮窗体的显示和隐藏以及位置的改变

    private FloatStateView circleView;
    FloatHuoView huoView;
    private FloatDiView diView;
    //    private FloatMenuView menuView;
    private WindowManager.LayoutParams params, params_di;
    Handler myHandler;
    boolean added = false;
    boolean remove = false;
    boolean isAnimation = false;

    private View.OnTouchListener circleViewTouchListener = new View.OnTouchListener() {
        private float startX;
        private float startY;
        private float x0;
        private float y0;
        private int paramx, paramy;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = event.getRawX();
                    startY = event.getRawY();
                    x0 = event.getRawX();
                    y0 = event.getRawY();
                    params.alpha = 1;
                    paramx = params.x;
                    paramy = params.y;

                    break;
                case MotionEvent.ACTION_MOVE:
                    int end = (int) event.getRawX();
                    int endY = (int) event.getRawY();
                    float dx = end - startX;
                    float dy = endY - startY;
                    if (Math.abs(end - x0) < 50 && Math.abs(endY - y0) < 50) {
                        break;
                    }
                    params.x += dx;
                    params.y += dy;
                    if (!remove) {
                        remove = true;
                        try {
                            wm.addView(diView, params_di);
                        } catch (Exception e) {
                        }
                        circleView.setDragState(FloatStateView.STATE_NORMAL);
                        try {
                            wm.updateViewLayout(circleView, params);
                        } catch (Exception e) {
                        }
                    } else {
                        try {
                            wm.updateViewLayout(circleView, params);
                        } catch (Exception e) {
                        }
                    }
                    if (params.y + circleView.height >= getScreenHeight() - diView.bitmap_height &&
                            (params.x + circleView.width > (getScreenWidth() - diView.bitmap_width) / 2 && params.x < (getScreenWidth() + diView.bitmap_width) / 2)) {
                        isAnimation = true;
                        vibrator.vibrate(1000);
                    } else {
                        isAnimation = false;
                        vibrator.cancel();
                    }
                    startX = (int) event.getRawX();
                    startY = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_UP:
                    vibrator.cancel();
                    float endX = event.getRawX();
                    remove = false;
                    try {
                        wm.removeView(diView);
                    } catch (Exception e) {
                    }
                    if (isAnimation) {
                        try {
                            wm.removeView(circleView);
                            added = false;
                        } catch (Exception e) {
                        }
                        Intent intent = new Intent(context, FloatAnimationActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
                        try {
                            pendingIntent.send(); // 监听到Home键按下后立即调用startActivity启动Activity会有5s延迟
                        } catch (PendingIntent.CanceledException e) {
                            e.printStackTrace();
                        }
//                        context.startActivity(intent);
                        params.x = paramx;
                        params.y = paramy;
                        if (paramx > getScreenWidth() / 2) {
                            circleView.setDragState(FloatStateView.STATE_RIGHT);
                        } else {
                            circleView.setDragState(FloatStateView.STATE_LEFT);
                        }

                    } else {
                        if (endX > getScreenWidth() / 2) {
                            circleView.setDragState(FloatStateView.STATE_RIGHT);
                            params.x = getScreenWidth() - size_1;
                        } else {
                            circleView.setDragState(FloatStateView.STATE_LEFT);
                            params.x = 0;
                        }
                        try {
                            wm.updateViewLayout(circleView, params);
                        } catch (Exception e) {
                        }
                        params.alpha = 0.4f;
                        myHandler.postDelayed(new Runnable() {
                            public void run() {
                                if (added)
                                    try {
                                        wm.updateViewLayout(circleView, params);
                                    } catch (Exception e) {
                                    }
                            }
                        }, 2000);
                    }
                    if (Math.abs(endX - x0) > 50) {
                        return true;
                    } else {
                        return false;
                    }
                default:
                    break;
            }
            return false;
        }
    };

    public int getScreenWidth() {
        return wm.getDefaultDisplay().getWidth();
    }

    public int getScreenHeight() {
        return wm.getDefaultDisplay().getHeight();
    }

    /**
     * 状态栏的高度
     *
     * @return
     */
    public int getStatusHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object o = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = (Integer) field.get(0);
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void upDate(int pratent) {
        circleView.upDate(pratent);
    }

    private FloatStateManager(final Context context) {
        this.context = context.getApplicationContext();

        size_1 = context.getResources().getDimensionPixelOffset(R.dimen.d30);
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        circleView = new FloatStateView(context);
        huoView = new FloatHuoView(context);
        circleView.setOnTouchListener(circleViewTouchListener);
        if (myHandler == null) {
            myHandler = new Handler(Looper.getMainLooper());
        }
        circleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FloatActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
                try {
                    pendingIntent.send(); // 监听到Home键按下后立即调用startActivity启动Activity会有5s延迟
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }
//                context.startActivity(intent);
//                removeWindowsView();
            }
        });
        diView = new FloatDiView(context);
        showFloatDi();
    }

    private static FloatStateManager instance;

    public static FloatStateManager getInstance(Context context) {
        if (instance == null) {
            instance = new FloatStateManager(context);
        }
        return instance;
    }

    /**
     * 展示浮窗小球到窗口上
     */
    public void showFloatCircleView() {

        if (params == null) {
            params = new WindowManager.LayoutParams();
            params.width = size_1;
            params.height = size_1;
            params.gravity = Gravity.TOP | Gravity.LEFT;
            params.x = Util.dp2px(0);
            params.y = Util.dp2px(400);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                params.type = WindowManager.LayoutParams.TYPE_TOAST;
            } else {
                params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
            }
            params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
            params.format = PixelFormat.RGBA_8888;
        }
    }

    /**
     * 展示浮窗小球到窗口上
     */
    public void showFloatDi() {
        if (params_di == null) {
            params_di = new WindowManager.LayoutParams();
            params_di.width = WindowManager.LayoutParams.MATCH_PARENT;
            params_di.height = WindowManager.LayoutParams.MATCH_PARENT;
            params_di.gravity = Gravity.BOTTOM | Gravity.CENTER;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                params_di.type = WindowManager.LayoutParams.TYPE_PHONE;
            } else {
                params_di.type = WindowManager.LayoutParams.TYPE_PHONE;
            }
            params_di.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
            params_di.format = PixelFormat.RGBA_8888;
        }
    }

    public void addWindowsView() {
        if (!added && wm != null) {
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        wm.addView(circleView, params);
                        params.alpha = 0.4f;
                        added = true;
                    } catch (Exception e) {
                        Log.e("rqy", "exception " + e.getMessage());
                    }
                }
            });
        }
    }

    public void removeWindowsView() {
        if (added && wm != null) {
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        wm.removeView(circleView);
                        added = false;
                    } catch (Exception e) {
                        Log.e("rqy", "exception" + e.getMessage());
                    }
                }
            });
        }
    }


}
