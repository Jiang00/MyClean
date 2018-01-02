package com.security.cleaner.myview;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class DeepWidgetContainer extends FrameLayout implements OnClickListener {

    public static final int MATCH_PARENT = WindowManager.LayoutParams.MATCH_PARENT;
    public static final int WRAP_CONTENT = WindowManager.LayoutParams.WRAP_CONTENT;
    public static final int LANDSCAPE = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
    public static final int PORTRAIT = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    public static final int BEHIND = ActivityInfo.SCREEN_ORIENTATION_BEHIND;

    public static Handler widgetHandler = null;


    WindowManager.LayoutParams lp;
    private WindowManager wm;
    boolean movable;
    int screenHeight;
    public Context context;

    public DeepWidgetContainer(Context context, Builder builder) {
        // TODO Auto-generated constructor stub
        super(context);
        this.context = context;
        this.movable = builder.movable;
        int type = builder.type;
        int flag = builder.flag;
        if (!builder.blockInput) {
            flag |= WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        }
        if (!builder.blockTouch) {
            flag = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        }
        lp = new WindowManager.LayoutParams(builder.width, builder.height,
                type, flag, PixelFormat.TRANSPARENT);
        lp.gravity = builder.gravity;
        lp.screenOrientation = builder.orientation;

        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        screenHeight = wm.getDefaultDisplay().getHeight();


        setOnClickListener(this);

        if (Build.VERSION.SDK_INT > 18) {
            setFitsSystemWindows(true);
            setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        } else {
            setFitsSystemWindows(true);
            setSystemUiVisibility(View.INVISIBLE);

        }
    }

    public LayoutParams makeLayoutParams(int width, int height, int gravity) {
        return new LayoutParams(width, height, gravity);
    }

    public static class Builder {
        boolean movable = false;
        boolean blockTouch = true;
        boolean blockInput = true;
        int orientation = BEHIND;
        int gravity = Gravity.NO_GRAVITY;
        int width = MATCH_PARENT;
        int height = MATCH_PARENT;

        int type = Build.VERSION.SDK_INT >= 19 ? (Build.VERSION.SDK_INT < 24 ? WindowManager.LayoutParams.TYPE_TOAST : WindowManager.LayoutParams.TYPE_SYSTEM_ERROR)
                : WindowManager.LayoutParams.TYPE_TOAST;
        //        int flag = Build.VERSION.SDK_INT >= 19 ? (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS|WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
//                : 512;
        int flag = Build.VERSION.SDK_INT >= 19 ? (WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                : 512 | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
//
//        int type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
//        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;

        public Builder setMovable(boolean movable) {
            this.movable = movable;
            return this;
        }

        public Builder setBlockTouch(boolean blockTouch) {
            this.blockTouch = blockTouch;
            return this;
        }

        public Builder setBlockInput(boolean blockInput) {
            this.blockInput = blockInput;
            return this;
        }


        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public DeepWidgetContainer build(Context context) {
            return new DeepWidgetContainer(context, this);
        }

        public Builder setOrientation(int orientation) {
            this.orientation = orientation;
            return this;
        }

        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    public synchronized void addToWindow() {
        if (wm != null) {
            try {
                wm.addView(this, lp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void removeFromWindow() {
        if (wm != null) {
            try {
                wm.removeView(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        // TODO Auto-generated method stub
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                break;
            case KeyEvent.KEYCODE_MENU:
                break;
            case KeyEvent.KEYCODE_HOME:
                break;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        return movable;
    }

    public static void setHandler(Handler handler) {
        widgetHandler = handler;
    }


}
