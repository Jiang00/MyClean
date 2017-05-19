package com.supers.clean.junk.customeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.supers.clean.junk.R;
import com.android.clean.util.CommonUtil;

/**
 * Created by 98614 on 2017/4/20.
 */

public class FileRoundView extends View {

    private int size;
    private long apk_size;
    private long zip_size;
    private long doc_size;
    private long music_size;
    private long video_size;
    private long other_size;
    private long all_size;
    private float lint_whith = CommonUtil.dp2px(29);
    private RectF rectF;
    private Paint paint_apk;
    private Paint paint_zip;
    private Paint paint_doc;
    private Paint paint_music;
    private Paint paint_video;
    private Paint paint_other;
    Context context;

    public FileRoundView(Context context) {
        this(context, null);
    }

    public FileRoundView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FileRoundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        paint_apk = new Paint();
        paint_apk.setAntiAlias(true);
        paint_apk.setStrokeWidth(lint_whith);
        paint_apk.setStyle(Paint.Style.STROKE);
        paint_apk.setColor(ContextCompat.getColor(context, R.color.file_apk));

        paint_zip = new Paint();
        paint_zip.setAntiAlias(true);
        paint_zip.setStrokeWidth(lint_whith);
        paint_zip.setStyle(Paint.Style.STROKE);
        paint_zip.setColor(ContextCompat.getColor(context, R.color.file_zip));

        paint_doc = new Paint();
        paint_doc.setAntiAlias(true);
        paint_doc.setStrokeWidth(lint_whith);
        paint_doc.setStyle(Paint.Style.STROKE);
        paint_doc.setColor(ContextCompat.getColor(context, R.color.file_txt));
        paint_music = new Paint();
        paint_music.setAntiAlias(true);
        paint_music.setStrokeWidth(lint_whith);
        paint_music.setStyle(Paint.Style.STROKE);
        paint_music.setColor(ContextCompat.getColor(context, R.color.file_music));
        paint_video = new Paint();
        paint_video.setAntiAlias(true);
        paint_video.setStrokeWidth(lint_whith);
        paint_video.setStyle(Paint.Style.STROKE);
        paint_video.setColor(ContextCompat.getColor(context, R.color.file_video));
        paint_other = new Paint();
        paint_other.setAntiAlias(true);
        paint_other.setStrokeWidth(lint_whith);
        paint_other.setStyle(Paint.Style.STROKE);
        paint_other.setColor(ContextCompat.getColor(context, R.color.file_qita));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        size = w >= h ? h : w;
        setMeasuredDimension(size, size);
        rectF = new RectF(0 + lint_whith / 2, 0 + lint_whith / 2, size - lint_whith / 2, size - lint_whith / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float start1 = 0;
        float start2 = apk_size == 0 ? 0 : (0 + apk_size) * 360 / all_size;
        float start3 = (0 + apk_size + zip_size) == 0 ? 0 : (0 + apk_size + zip_size) * 360 / all_size;
        float start4 = (0 + apk_size + zip_size + doc_size) == 0 ? 0 : (0 + apk_size + zip_size + doc_size) * 360 / all_size;
        float start5 = (0 + apk_size + zip_size + doc_size + music_size) == 0 ? 0 : (0 + apk_size + zip_size + doc_size + music_size) * 360 / all_size;
        float start6 = (0 + apk_size + zip_size + doc_size + music_size + video_size) == 0 ? 0 : (0 + apk_size + zip_size + doc_size + music_size + video_size) * 360 / all_size;
        float start7 = (0 + apk_size + zip_size + doc_size + music_size + video_size + other_size) == 0 ? 0 : (0 + apk_size + zip_size + doc_size + music_size + video_size + other_size) * 360 / all_size;
        Log.e("draw", start1 + "=" + start2 + "=" + start3 + "=" + start4 + "=" + start5 + "=" + start6 + "=" + start7);
//        canvas.translate(-size/2,-size/2);
        canvas.drawArc(rectF, 0, 360, false, paint_other);
        if (all_size == 0) {
            return;
        }
        canvas.drawArc(rectF, start1, start2 + 1, false, paint_apk);
        canvas.drawArc(rectF, start2, start3 - start2 + 1, false, paint_zip);
        canvas.drawArc(rectF, start3, start4 - start3 + 1, false, paint_doc);
        canvas.drawArc(rectF, start4, start5 - start4 + 1, false, paint_music);
        canvas.drawArc(rectF, start5, start6 - start5 + 1, false, paint_video);
        canvas.drawArc(rectF, start6, start7 - start6, false, paint_other);
    }

    public void setProgress(final Handler handler, final long apkSize, final long zipSize, final long docSize, final long musicSize, final long videoSize, final long otherSize) {
        all_size = apkSize + zipSize + docSize + musicSize + videoSize + otherSize;
        final long kedu = all_size / 360 * 2;
        apk_size = 0;
        zip_size = 0;
        doc_size = 0;
        music_size = 0;
        video_size = 0;
        other_size = 0;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int count = 0;
                apk_size += kedu;
                zip_size += kedu;
                doc_size += kedu;
                music_size += kedu;
                video_size += kedu;
                other_size += kedu;
                if (apk_size > apkSize) {
                    apk_size = apkSize;
                    count++;
                }
                if (zip_size > zipSize) {
                    zip_size = zipSize;
                    count++;
                }
                if (doc_size > docSize) {
                    doc_size = docSize;
                    count++;
                }
                if (music_size > musicSize) {
                    music_size = musicSize;
                    count++;
                }
                if (video_size > videoSize) {
                    video_size = videoSize;
                    count++;
                }
                if (other_size > otherSize) {
                    other_size = otherSize;
                    count++;
                }
                if (count < 6) {
                    handler.postDelayed(this, 10 * (count - 6) / 6);
                }
                postInvalidate();
            }
        };
        handler.post(runnable);
    }

}
