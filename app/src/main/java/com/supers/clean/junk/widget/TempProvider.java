package com.supers.clean.junk.widget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.supers.clean.junk.R;
import com.supers.clean.junk.activity.CoolingActivity;
import com.supers.clean.junk.modle.CpuTempReader;

/**
 * Created by Ivy on 2017/3/28.
 */

public class TempProvider extends AutoUpdateWidgetProvider {

    public static final String TEMP_PROVIDER_ACTION = "app.eosmobi.action.widget.temp";

    private RemoteViews rv;

    @Override
    public Intent launcherService(Context context) {
        return new Intent(context, TempService.class);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        //最后一个widget被从屏幕移除
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        //widget添加到屏幕上执行
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        if (TextUtils.equals(TEMP_PROVIDER_ACTION, action)) {
            if (rv == null) {
                rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_temp);
            }
            CpuTempReader.getCPUTemp(new CpuTempReader.TemperatureResultCallback() {
                @Override
                public void callbackResult(CpuTempReader.ResultCpuTemperature result) {
                    int cpuTemp;
                    if (result != null) {
                        cpuTemp = (int) result.getTemperature();
                    } else {
                        cpuTemp = 40;
                    }
                    Intent intent = new Intent(context, CoolingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    rv.setOnClickPendingIntent(R.id.img_temp, pendingIntent);
                    rv.setImageViewBitmap(R.id.img_temp, getBitmap(context, cpuTemp));
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
                    ComponentName cn = new ComponentName(context.getApplicationContext(), TempProvider.class);
                    appWidgetManager.updateAppWidget(cn, rv);
                }
            });

        }

    }

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        //刷新的时候执行widget
        //remoteView  AppWidgetManager
 /*
         * 构造pendingIntent发广播，onReceive()根据收到的广播，更新
         */
        Log.e("rqy", "TempProvider--onUpdate");
        if (rv == null) {
            rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_temp);
        }
        CpuTempReader.getCPUTemp(new CpuTempReader.TemperatureResultCallback() {
            @Override
            public void callbackResult(CpuTempReader.ResultCpuTemperature result) {
                int cpuTemp;
                if (result != null) {
                    cpuTemp = (int) result.getTemperature();
                } else {
                    cpuTemp = 40;
                }
                rv.setImageViewBitmap(R.id.img_temp, getBitmap(context, cpuTemp));
                Intent intent = new Intent(context, CoolingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                rv.setOnClickPendingIntent(R.id.img_temp, pendingIntent);
                appWidgetManager.updateAppWidget(appWidgetIds, rv);
            }
        });
    }

    public Bitmap getBitmap(Context context, int actualTemp) {
        int temp = actualTemp;
        if (temp > 60) {
            temp = 60;
        }
        Bitmap background = BitmapFactory.decodeResource(context.getResources(), R.mipmap.bg);

        Bitmap bg = background.copy(Bitmap.Config.ARGB_8888, true);
        int mWidth = bg.getWidth();
        int mHeight = bg.getHeight();
        background.recycle();
        Canvas canvas = new Canvas(bg);

        Bitmap shanxing = BitmapFactory.decodeResource(context.getResources(), R.mipmap.shanxing);
        int mShanxingWidth = shanxing.getWidth();
        int mShanxingHeight = shanxing.getHeight();
        Rect mShanxingSrcRect = new Rect(0, 0, mShanxingWidth, mShanxingHeight);
        int distance = (mWidth - mShanxingWidth) / 2;
        int distance1 = (mHeight - mShanxingHeight) / 2;
        Rect mShanxingDestRect = new Rect(distance, distance1, (mWidth - mShanxingWidth) / 2 + mShanxingWidth, (mHeight - mShanxingHeight) / 2 + mShanxingHeight);
        canvas.drawBitmap(shanxing, mShanxingSrcRect, mShanxingDestRect, new Paint());

        //draw over arc
        Paint arcPaint = new Paint();
        arcPaint.setStrokeWidth(33f);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setColor(Color.parseColor("#e8f1f3"));
        arcPaint.setAntiAlias(true);

        float startAngle01 = temp * 4 + 150;
        float sweepAngle01 = 395 - startAngle01;
        RectF rectF = new RectF();
        rectF.left = 31.5f / 2 + 12;
        rectF.top = 31.5f / 2 + 12;
        rectF.right = rectF.left + mShanxingWidth - 31.5f;
        rectF.bottom = rectF.top + mShanxingHeight - 31.5f;
        canvas.drawArc(rectF, startAngle01, sweepAngle01, false, arcPaint);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap jiantou = BitmapFactory.decodeResource(context.getResources(), R.mipmap.jiantou);
        canvas.save();
        canvas.rotate(temp * 4, jiantou.getWidth() / 2, jiantou.getHeight() / 2);
        canvas.drawBitmap(jiantou, 0, 0, paint);
        canvas.restore();

        Bitmap zhongbiao = BitmapFactory.decodeResource(context.getResources(), R.mipmap.zhongbiao);
        canvas.drawBitmap(zhongbiao, 0, 0, new Paint());

        Paint textPaint = new Paint();
        textPaint.setColor(Color.parseColor("#3cb728"));
        textPaint.setTextSize(40);
        textPaint.setStyle(Paint.Style.FILL);
        //该方法即为设置基线上那个点究竟是left,center,还是right  这里我设置为center
        textPaint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom

        int baseLineY = (int) (mHeight / 2 - top / 2 - bottom / 2);//基线中间点的y轴计算公式
        String text = actualTemp + "℃";
        canvas.drawText(text, mWidth / 2, baseLineY, textPaint);

        return bg;
    }

}
