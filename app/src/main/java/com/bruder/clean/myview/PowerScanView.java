package com.bruder.clean.myview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import com.bruder.clean.junk.R;


public class PowerScanView extends View {

    private Handler mHandler;

    private Rect src;
    private Rect dst;

    private Bitmap battery;
    private Bitmap cover;
    private Bitmap line;

    private static final int INTERVAL = 40;

    private static final int INVALIDATE_LENGTH = 40;

    private int starX = 0;

    private int count = 0;

    private int curPos = 0;

    private int insertPos = 0;

    private int lineLeftMargin = 0;
    private int batteryLeftMargin = 0;
    private int topMargin = 0;

    private int height;
    private int width;

    public PowerScanView(Context context) {
        super(context);
        init(context);
    }

    public PowerScanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PowerScanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void destory() {
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
        }

        if (battery != null) {
            battery.recycle();
        }
        if (cover != null) {
            cover.recycle();
        }
        if (line != null) {
            line.recycle();
        }
    }

    private void init(Context context) {
        mHandler = new Handler();
        battery = BitmapFactory.decodeResource(getResources(), R.mipmap.cooling_2);
        cover = BitmapFactory.decodeResource(getResources(), R.mipmap.cooling_3);
        line = BitmapFactory.decodeResource(getResources(), R.mipmap.cooling_line);
        count = battery.getHeight() / INTERVAL + 1;//INTERVAL = 40

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        width = windowManager.getDefaultDisplay().getWidth();
        height = windowManager.getDefaultDisplay().getHeight();

        topMargin = (height - cover.getHeight()) / 2;//640 - 350 /2 = 145
        lineLeftMargin = (width - line.getWidth()) / 2;//0
        batteryLeftMargin = (width - cover.getWidth()) / 2;//0
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawBitmap(battery, null, new Rect(0, topMargin, width, topMargin + battery.getHeight()), null);
        src = new Rect(0, 0, cover.getWidth(), starX);
        dst = new Rect(0, topMargin, width, starX + topMargin);
        canvas.drawBitmap(cover, src, dst, null);
        canvas.drawBitmap(line, null, new Rect(0, starX - line.getHeight() + topMargin, width, starX + topMargin), null);
        drawScan();
    }

    private void drawScan() {
        if (curPos > count) {
            starX -= INTERVAL;
            if (starX <= 0) {
                starX = 0;
            }
            if (insertPos > count) {
                curPos = 0;
                insertPos = 0;
                times++;
                if (scanEndListener != null) {
                    scanEndListener.scanTimes(times);
                }
            }
            insertPos++;
        } else {
            starX += INTERVAL;
            if (starX > battery.getHeight()) {
                starX = battery.getHeight();
                times++;
                if (scanEndListener != null) {
                    scanEndListener.scanTimes(times);
                }
            }
        }

        curPos++;

        mHandler.postDelayed(mRunnable, INVALIDATE_LENGTH);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            postInvalidate();
        }
    };

    private int times = 0;
    private IScanEndListener scanEndListener;

    public void setScanCallBack(IScanEndListener listener) {
        this.scanEndListener = listener;
    }

    public interface IScanEndListener {
        void scanTimes(int times);
    }

}
