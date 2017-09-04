package com.supers.clean.junk.customeview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


import com.supers.clean.junk.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by Ivy on 2017/7/21.
 */

public class SnowView extends View {

    private Context mContext;
    private int screenWidth;
    private int screenHeight;

    public SnowView(Context context) {
        super(context);
        this.mContext = context;
        initScreen();
    }

    public SnowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initScreen();
    }

    public SnowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initScreen();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isShouldPlayAnimation) {
            return;
        }
        init();
        getSnow();
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            Snow object = (Snow) iterator.next();
            int posX = (object.X += object.translateX);
            int posY = (object.Y += object.translateY);
            mPaint.setAlpha(object.alpha);
            canvas.drawBitmap(snows.get(object.num), posX, posY, mPaint);
            if (posX <= 0 || posX > screenWidth || posY < 0) {
                iterator.remove();
            }
        }
        mHandler.sendEmptyMessageDelayed(0, 1000 / 50);
    }

    private int count = 1;

    public void setSnowCount(int count) {
        this.count = count;
    }

    private boolean isShouldPlayAnimation = false;

    public void playAni() {
        if (!isShouldPlayAnimation) {
            isShouldPlayAnimation = true;
        }
        postInvalidate();
    }

    public void cancelAni() {
        if (isShouldPlayAnimation) {
            isShouldPlayAnimation = false;
        }
        destroy();
    }

    public void destroy() {
        if (list != null) {
            list.clear();
        }
        if (mHandler != null) {
            mHandler.removeMessages(0);
        }
        if (snows != null) {
            snows.clear();
        }
        if (snow != null && !snow.isRecycled()) {
            snow.recycle();
        }
        if (snow1 != null && !snow1.isRecycled()) {
            snow1.recycle();
        }

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                postInvalidate();
            }
        }
    };

    private Bitmap snow;
    private Bitmap snow1;
    private List<Bitmap> snows;
    private Paint mPaint;

    private void init() {
        if (snows == null) {
            snows = new ArrayList<>();
        }
        if (snow == null) {
            snow = BitmapFactory.decodeResource(getResources(), R.mipmap.qiqiu1);
            snows.add(snow);
        }
        if (snow1 == null) {
            snow1 = BitmapFactory.decodeResource(getResources(), R.mipmap.qiqiu2);
            snows.add(snow1);
        }

        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
        }
    }

    private void initScreen() {
        screenWidth = WidgetUtil.getScreenWidth(mContext);
        screenHeight = WidgetUtil.getScreenHeight(mContext);
    }

    private ArrayList<Snow> list = new ArrayList<>();

    private void getSnow() {
        int size = list.size();
        int needSnowSize = count - size;
        for (int i = 0; i < needSnowSize; i++) {
            list.add(new Snow());
        }
    }

    private class Snow {
        public int X = 0;
        public int Y = 0;
        public int translateY = -10;
        public int translateX = -10;
        public int alpha;
        public int num;
        public float scale;

        private Random random;

        public Snow() {
            random = new Random();
            num = random.nextInt(2);
            Y = WidgetUtil.getScreenHeight(mContext);
            getAlpha();
            getScale();
            getDistanceX();
            getDistanceY();
            getX(WidgetUtil.getScreenWidth(mContext));
        }

        private void getAlpha() {
            this.alpha = random.nextInt(255);
            if (this.alpha <= 50) {
                this.alpha = 60;
            }
        }

        private void getScale() {
            this.scale = random.nextFloat();
        }

        private void getDistanceX() {
            this.translateX = -5 + random.nextInt(10);
        }

        private void getDistanceY() {
            this.translateY = -30 - random.nextInt(5);
        }

        private void getX(int maxWidth) {
            this.X = random.nextInt(maxWidth);
        }

    }

}
