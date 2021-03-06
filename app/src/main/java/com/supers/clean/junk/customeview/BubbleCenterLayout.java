package com.supers.clean.junk.customeview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


import com.supers.clean.junk.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class BubbleCenterLayout extends View {

    private List<Bubble> bubbles = new ArrayList<Bubble>();
    private Random random = new Random();//生成随机数
    private int width, height;
    private boolean starting = false;
    private boolean thread = true;
    private boolean isDestory = false;
    private Bitmap bitmap = null;
    private Bitmap dstBitmap;

    public BubbleCenterLayout(Context context) {
        super(context);
        init();
    }

    public BubbleCenterLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BubbleCenterLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.bubble_bai);
    }

    public void destroy() {
        starting = false;
        thread = false;

        if (bubbles != null) {
            bubbles.clear();
        }
        if (bitmap != null) {
            bitmap.recycle();
        }
        if (dstBitmap != null) {
            dstBitmap.recycle();
        }
        isDestory = true;
    }

    public void setParticleBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void pause() {
        thread = false;
//        if (bubbles != null) {
//            bubbles.clear();
//        }
    }

    public void reStart() {
        thread = true;
        starting = false;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();
        height = getHeight();
        if (!starting) {
            starting = true;
            new Thread() {
                public void run() {
                    while (starting && thread) {
                        Log.d("MyTest", "bubble Thread");
                        Bubble bubble = new Bubble();
                        float speedY = random.nextFloat() * 3 + 8;
                        float scale = random.nextFloat();
                        while (scale <= 0.7) {
                            scale = random.nextFloat();
                        }
                        bubble.setScale(scale);

                        if (bitmap != null && !bitmap.isRecycled()) {
                            try {
                                int width = (int) (bitmap.getWidth() * bubble.getScale());
                                int height = (int) (bitmap.getHeight() * bubble.getScale());
                                if (width <= 0 || height <= 0) {
                                    throw new RuntimeException();
                                }
                                dstBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
                                bubble.setBitmap(dstBitmap);
                            } catch (RuntimeException e) {
                                e.printStackTrace();
                            } catch (OutOfMemoryError error) {
                                bubble.setBitmap(null);
                            }
                        }
                        bubble.setRadius(bitmap.getWidth() / 2);
                        int b_w = random.nextInt(width) + bitmap.getWidth() / 2;
                        int b_h = random.nextInt(height) + bitmap.getWidth() / 2;
                        bubble.setX(b_w - bitmap.getWidth() / 2);
                        bubble.setY(b_h - bitmap.getWidth() / 2);
                        if (b_w > width / 2) {
                            if (b_h > height / 2) {
                                float line = (float) Math.sqrt((b_w - width / 2) * (b_w - width / 2) + (b_h - height / 2) * (b_h - height / 2));
                                if (line < getResources().getDimensionPixelSize(R.dimen.d109)) {
                                    bubble.setBitmap(null);
                                }
                                bubble.setSpeedX(-(speedY * (b_w - width / 2) / line));
                                bubble.setSpeedY(-(speedY * (b_h - height / 2) / line));
                            } else {
                                float line = (float) Math.sqrt((b_w - width / 2) * (b_w - width / 2) + (height / 2 - b_h) * (height / 2 - b_h));
                                if (line < getResources().getDimensionPixelSize(R.dimen.d109)) {
                                    bubble.setBitmap(null);
                                }
                                bubble.setSpeedX(-(speedY * (b_w - width / 2) / line));
                                bubble.setSpeedY((speedY * (height / 2 - b_h) / line));
                            }
                        } else {
                            if (b_h > height / 2) {
                                float line = (float) Math.sqrt((width / 2 - b_w) * (width / 2 - b_w) + (b_h - height / 2) * (b_h - height / 2));
                                if (line < getResources().getDimensionPixelSize(R.dimen.d109)) {
                                    bubble.setBitmap(null);
                                }
                                bubble.setSpeedX((speedY * (width / 2 - b_w) / line));
                                bubble.setSpeedY(-(speedY * (b_h - height / 2) / line));
                            } else {
                                float line = (float) Math.sqrt((width / 2 - b_w) * (width / 2 - b_w) + (height / 2 - b_h) * (height / 2 - b_h));
                                if (line < getResources().getDimensionPixelSize(R.dimen.d109)) {
                                    bubble.setBitmap(null);
                                }
                                bubble.setSpeedX((speedY * (width / 2 - b_w) / line));
                                bubble.setSpeedY((speedY * (height / 2 - b_h) / line));
                            }
                        }

                        bubbles.add(bubble);
                        try {
                            Thread.sleep(random.nextInt(2) * 100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }
        Paint paint = new Paint();

        paint.reset();
        paint.setColor(0X669999);//灰白色
        paint.setAlpha(180);//设置不透明度：透明为0，完全不透明为255
        List<Bubble> list = new ArrayList<Bubble>(bubbles);
        //依次绘制气泡
        for (Bubble bubble : list) {
            if (bubble == null) {
                continue;
            }
            //碰到上边界从数组中移除
            if (bubble.getY() + bubble.getRadius() > height / 2 && bubble.getY() + bubble.getRadius() + bubble.getSpeedY() <= height / 2) {
                bubbles.remove(bubble);
            } else if (bubble.getY() + bubble.getRadius() < height / 2 && bubble.getY() + bubble.getRadius() + bubble.getSpeedY() >= height / 2) {

                bubbles.remove(bubble);
            } else if (bubble.getX() + bubble.getRadius() < width / 2 && bubble.getX() + bubble.getRadius() + bubble.getSpeedX() >= width / 2) {

                bubbles.remove(bubble);
            } else if (bubble.getX() + bubble.getRadius() > width / 2 && bubble.getX() + bubble.getRadius() + bubble.getSpeedX() <= width / 2) {

                bubbles.remove(bubble);
            } else {
                int i = bubbles.indexOf(bubble);
//                if (bubble.getX() + bubble.getSpeedX() <= bubble.getRadius()) {
//                    bubble.setX(bubble.getRadius());
//                } else if (bubble.getX() + bubble.getSpeedX() >= width - bubble.getRadius()) {
//                    bubble.setX(width - bubble.getRadius());
//                } else {
//                }
                bubble.setX(bubble.getX() + bubble.getSpeedX());
                bubble.setY(bubble.getY() + bubble.getSpeedY());
                bubbles.set(i, bubble);
//				canvas.drawCircle(bubble.getX(), bubble.getY(), bubble.getRadius(), paint);
                try {
                    canvas.drawBitmap(bubble.getBitmap(), bubble.getX(), bubble.getY(), paint);
                } catch (Exception e) {
                    bubbles.remove(bubble);
                }
            }
        }
        if (!isDestory) {
            //刷新屏幕
            invalidate();
        }
    }

    //内部VO，不需要太多注释吧？
    private class Bubble {
        //气泡半径
        private float radius;
        //上升速度
        private float speedY;
        //平移速度
        private float speedX;
        //气泡x坐标
        private float x;
        // 气泡y坐标
        private float y;

        private float scale;

        private Bitmap bitmap = null;

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public float getRadius() {
            return radius;
        }

        public void setRadius(float radius) {
            this.radius = radius;
        }

        public void setScale(float scale) {
            this.scale = scale;
        }

        public float getScale() {
            return scale;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public float getSpeedY() {
            return speedY;
        }

        public void setSpeedY(float speedY) {
            this.speedY = speedY;
        }

        public float getSpeedX() {
            return speedX;
        }

        public void setSpeedX(float speedX) {
            this.speedX = speedX;
        }

    }
}