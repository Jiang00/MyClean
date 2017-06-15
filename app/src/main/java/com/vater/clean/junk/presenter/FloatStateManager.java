package com.vater.clean.junk.presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.vater.clean.util.Util;
import com.vater.clean.junk.jiemian.XuanfuActivity;
import com.vater.clean.junk.myview.FloatStateView;

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
    private Context context;
    private WindowManager wm; // 通过这个windowManager来操控浮窗体的显示和隐藏以及位置的改变

    private FloatStateView circleView;
    //    private FloatMenuView menuView;
    private WindowManager.LayoutParams params;
    Handler myHandler;
    boolean added = false;

    private View.OnTouchListener circleViewTouchListener = new View.OnTouchListener() {
        private float startX;
        private float startY;
        private float x0;
        private float y0;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = event.getRawX();
                    startY = event.getRawY();
                    x0 = event.getRawX();
                    y0 = event.getRawY();
                    params.alpha = 1;
                    break;
                case MotionEvent.ACTION_MOVE:
                    int end = (int) event.getRawX();
                    int endY = (int) event.getRawY();
                    float dx = end - startX;
                    float dy = endY - startY;
                    if (Math.abs(end - x0) < 50 && Math.abs(endY - y0) < 50) {
                        break;
                    }
                    circleView.setDragState(FloatStateView.STATE_NORMAL);
                    params.x += dx;
                    params.y += dy;
                    try {
                        wm.updateViewLayout(circleView, params);
                    } catch (Exception e) {
                    }
                    startX = (int) event.getRawX();
                    startY = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_UP:
                    float endX = event.getRawX();
                    if (endX > getScreenWidth() / 2) {
                        params.x = getScreenWidth() - circleView.width;
                        circleView.setDragState(FloatStateView.STATE_RIGHT);
                    } else {
                        params.x = 0;
                        circleView.setDragState(FloatStateView.STATE_LEFT);
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

    private FloatStateManager(final Context context) {
        this.context = context.getApplicationContext();
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        circleView = new FloatStateView(context);
        circleView.setOnTouchListener(circleViewTouchListener);
        if (myHandler == null) {
            myHandler = new Handler(Looper.getMainLooper());
        }
        circleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, XuanfuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
//                removeWindowsView();
            }
        });

    }

    private static FloatStateManager instance;



    public int getScreenWidth() {
        return wm.getDefaultDisplay().getWidth();
    }

    public int getScreenHeight() {
        return wm.getDefaultDisplay().getHeight();
    }
    public static FloatStateManager getInstance(Context context) {
        if (instance == null) {
            instance = new FloatStateManager(context);
        }
        return instance;
    }

    public void upDate(int pratent) {
        circleView.upDate(pratent);
    }


    /**
     * 展示浮窗小球到窗口上
     */
    public void showFloatCircleView() {
        if (params == null) {
            params = new WindowManager.LayoutParams();
            params.width = circleView.width;
            params.height = circleView.height;
            params.gravity = Gravity.TOP | Gravity.LEFT;
            params.x = getScreenWidth();
            params.y = Util.dp2px(350);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                params.type = WindowManager.LayoutParams.TYPE_TOAST;
            } else {
                params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
            }
            params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
            params.format = PixelFormat.RGBA_8888;
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
