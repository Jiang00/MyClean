package com.easy.module.charge.saver.protectview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.easy.module.charge.saver.R;

/**
 * 电池动画
 */

public class CharingView extends View {
    Paint paint;
    Paint paint_L;
    Context context;
    int height;
    int xianL;
    int level;
    int round;
    Bitmap bitmap1, bitmap2, bitmap3, bitmap4, bitmap5, bitmap6;

    public CharingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }


    private void init() {
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.white_20));
        paint.setAntiAlias(true);
        paint_L = new Paint();
        paint_L.setColor(ContextCompat.getColor(context, R.color.white_20));
        paint_L.setAntiAlias(true);
//        paint.setStyle(Paint.Style.FILL);
        round = getResources().getDimensionPixelOffset(R.dimen.d8);
        paint.setStrokeWidth(round);
        paint_L.setStrokeWidth(getResources().getDimensionPixelOffset(R.dimen.d5));
        paint_L.setColor(Color.parseColor("#3dcf55"));
        height = getResources().getDimensionPixelOffset(R.dimen.d26);
        xianL = getResources().getDimensionPixelOffset(R.dimen.d100);
        bitmap1 = BitmapFactory.decodeResource(getResources(), R.mipmap.charging_2);
        bitmap2 = BitmapFactory.decodeResource(getResources(), R.mipmap.charging_3);
        bitmap3 = BitmapFactory.decodeResource(getResources(), R.mipmap.charging_4);
        bitmap4 = BitmapFactory.decodeResource(getResources(), R.mipmap.charging_5);
        bitmap5 = BitmapFactory.decodeResource(getResources(), R.mipmap.charging_6);
        bitmap6 = BitmapFactory.decodeResource(getResources(), R.mipmap.charging_7);
        float scaleWidth = (float) height / bitmap1.getWidth();
        float scaleHeight = (float) height / bitmap1.getHeight();
        // 取得想要缩放的matrix參數
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        bitmap1 = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.charging_2), 0, 0, bitmap1.getWidth(), bitmap1.getHeight(), matrix, true);
        bitmap2 = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.charging_3), 0, 0, bitmap2.getWidth(), bitmap2.getHeight(), matrix, true);
        bitmap3 = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.charging_4), 0, 0, bitmap3.getWidth(), bitmap3.getHeight(), matrix, true);
        bitmap4 = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.charging_5), 0, 0, bitmap4.getWidth(), bitmap4.getHeight(), matrix, true);
        bitmap5 = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.charging_6), 0, 0, bitmap5.getWidth(), bitmap5.getHeight(), matrix, true);
        bitmap6 = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.charging_7), 0, 0, bitmap6.getWidth(), bitmap6.getHeight(), matrix, true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(xianL * 2 + height, height);
    }

    boolean zhuangtai;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                postInvalidate();
            }
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(ContextCompat.getColor(context, R.color.white_100));
        int src = canvas.saveLayer(0, 0, xianL * 2 + height, height, paint, Canvas.ALL_SAVE_FLAG);
        paint.setColor(ContextCompat.getColor(context, R.color.white_20));
//        canvas.drawLine(xianL, height / 2, (xianL - height / 2) * 2, height / 2, paint);
        canvas.drawCircle(height / 2, height / 2, height / 2, paint);
        canvas.drawCircle(xianL + height / 2, height / 2, height / 2, paint);
        canvas.drawCircle(xianL * 2 + height / 2, height / 2, height / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
        paint.setAntiAlias(false);
        canvas.drawLine(height / 2, height / 2, xianL * 2 + height / 2, height / 2, paint);
        paint.setXfermode(null);
        canvas.restoreToCount(src);

        int src1 = canvas.saveLayer(0, 0, xianL * 2 + height, height, paint_L, Canvas.ALL_SAVE_FLAG);
//        canvas.drawLine(xianL, height / 2, (xianL - height / 2) * 2, height / 2, paint);
        if (level <= 20) {
            zhuangtai = !zhuangtai;
            if (zhuangtai) {
                canvas.drawBitmap(bitmap1, 0, 0, paint_L);
            } else {
                canvas.drawBitmap(bitmap2, 0, 0, paint_L);
            }
        } else if (level <= 80) {
            canvas.drawLine(height / 2, height / 2, xianL + height / 2, height / 2, paint_L);
            canvas.drawBitmap(bitmap1, 0, 0, paint_L);
            zhuangtai = !zhuangtai;
            if (zhuangtai) {
                canvas.drawBitmap(bitmap3, xianL, 0, paint_L);
            } else {
                canvas.drawBitmap(bitmap4, xianL, 0, paint_L);
            }
        } else {
            canvas.drawLine(height / 2, height / 2, xianL * 2 + height / 2, height / 2, paint_L);
            canvas.drawBitmap(bitmap1, 0, 0, paint_L);
            canvas.drawBitmap(bitmap3, 0, xianL, paint_L);
            zhuangtai = !zhuangtai;
            if (zhuangtai) {
                canvas.drawBitmap(bitmap5, xianL * 2, 0, paint_L);
            } else {
                canvas.drawBitmap(bitmap6, xianL * 2, 0, paint_L);
            }
        }
        canvas.restoreToCount(src1);
        handler.removeMessages(1);
        handler.sendEmptyMessageDelayed(1, 1000 / 2);

    }

    public void isCharging(boolean isCharging) {
        if (isCharging) {
            handler.sendEmptyMessage(1);
        } else {
            handler.removeMessages(1);
        }
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void dectory() {
        if (bitmap1 != null && !bitmap1.isRecycled()) {
            bitmap1.recycle();
            bitmap1 = null;
        }
        if (bitmap2 != null && !bitmap2.isRecycled()) {
            bitmap2.recycle();
            bitmap2 = null;
        }
        if (bitmap3 != null && !bitmap3.isRecycled()) {
            bitmap3.recycle();
            bitmap3 = null;
        }
        if (bitmap4 != null && !bitmap4.isRecycled()) {
            bitmap4.recycle();
            bitmap4 = null;
        }
        if (bitmap5 != null && !bitmap5.isRecycled()) {
            bitmap5.recycle();
            bitmap5 = null;
        }
        if (bitmap6 != null && !bitmap6.isRecycled()) {
            bitmap6.recycle();
            bitmap6 = null;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
