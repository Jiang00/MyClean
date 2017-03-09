package com.supers.clean.junk.myView;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.supers.clean.junk.R;

/**
 * Created by Ivy on 2017/2/28.
 */

public class MainScrollView extends ScrollView implements Pullable {

    private ValueAnimator valueAnimator;

    private MarginLayoutParams headerParams;//头部的参数
    private int headerHeight;               //头部的原始高度
    private View headerView;                //头布局

    private boolean isHeadVisible = true;          //头部是否可见

    private int cercleHeight;

    private int touchSlop;

    FrameLayout main_scale_all;

    private boolean isActionDown = false;   //第一次接收的事件是否是Down事件
    private float lastEventX;               //Move事件最后一次发生时的X坐标
    private float lastEventY;               //Move事件最后一次发生时的Y坐标
    private float downX;                    //Down事件的X坐标
    private float downY;                    //Down事件的Y坐标

    ValueAnimator.AnimatorUpdateListener updateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            cercleHeight = (int) animation.getAnimatedValue();
            Log.e("rqy", "--height=" + cercleHeight);
            headerParams.height = cercleHeight;
            headerView.setLayoutParams(headerParams);
            main_scale_all.setScaleY((float) cercleHeight / headerHeight);
            main_scale_all.setScaleX((float) cercleHeight / headerHeight);
            scrollTo(0, headerHeight - cercleHeight);
        }
    };

    public MainScrollView(Context context) {
        super(context);
    }

    public MainScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        Log.e("rqy", "touchSlop=" + touchSlop);
    }

    public MainScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        headerView = findViewById(R.id.main_all_cercle);
        main_scale_all = (FrameLayout) findViewById(R.id.main_scale_all);
        headerParams = (MarginLayoutParams) headerView.getLayoutParams();

        cercleHeight = headerHeight = headerParams.height;
        Log.e("rqy", "headerHeight=" + headerHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("rqy", "scrollViewTouchListener,v=" + "onTouchEvent");
        float currentX = event.getX();
        float currentY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = lastEventX = currentX;
                downY = lastEventY = currentY;
                isActionDown = true;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("rqy", "getScrollY=" + getScrollY() + "scrollViewTouchListener,ACTION_MOVE--isScroll=");
                if (!isActionDown) {
                    downX = lastEventX = currentX;
                    downY = lastEventY = currentY;
                    isActionDown = true;
                }
                float shiftX = Math.abs(currentX - downX);
                float shiftY = Math.abs(currentY - downY);
                float dx = currentX - lastEventX;
                float dy = currentY - lastEventY;
                lastEventY = currentY;

                Log.e("rqy,", "shiftX=" + shiftX + "--shiftY=" + shiftY + "--dx=" + dx + "--dy=" + dy + "--getScrollY=" + getScrollY());
                //rqy move
                // main_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                if (shiftY > shiftX && shiftY > touchSlop && getScrollY() > 0) {
                    cercleHeight += dy;
                    if (cercleHeight >= headerHeight) {
                        cercleHeight = headerHeight;
                        isHeadVisible = false;
                    } else if (cercleHeight <= 0) {
                        cercleHeight = 0;
                        isHeadVisible = false;
                    }

                    main_scale_all.setScaleY((float) cercleHeight / headerHeight);
                    main_scale_all.setScaleX((float) cercleHeight / headerHeight);
                    headerParams.height = cercleHeight;
                    headerView.setLayoutParams(headerParams);
                    Log.e("rqy1", "cercleHeight=" + cercleHeight + "--headerHeight=" + headerHeight);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isActionDown = false;
                Log.e("rqy", "scrollViewTouchListener,ACTION_UP---getScrollY=" + getScrollY());
                boolean isHeadVisible = !(headerParams.height == 0);
                if (!isHeadVisible || headerParams.height == headerHeight) {
                    break;
                }
                if (headerParams.height < (headerHeight / 2)) {
                    Log.e("rqy1", "cercleHeight=" + cercleHeight + "--headerHeight=" + headerHeight);
                    valueAnimator = ValueAnimator.ofInt(headerParams.height, 0);
                    startValueAnimator();
                } else if (headerParams.height < headerHeight) {
                    Log.e("rqy1", "cercleHeight1=" + cercleHeight + "--headerHeight1=" + headerHeight);
                    valueAnimator = ValueAnimator.ofInt(headerParams.height, headerHeight);
                    startValueAnimator();
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    //启动回弹动画
    private void startValueAnimator() {
        if (valueAnimator != null) {
            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            valueAnimator.setDuration(200);
            valueAnimator.addUpdateListener(updateListener);
            valueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationCancel(Animator animation) {
                    //main_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    // main_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }

                @Override
                public void onAnimationStart(Animator animation) {

                }
            });
            valueAnimator.start();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    public boolean canPullDown() {
//        if (getScrollY() == 0)
//            return true;
//        else
        return false;
    }

    public boolean canPullUp() {
        if (getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight()))
            return true;
        else
            return false;
    }

}
