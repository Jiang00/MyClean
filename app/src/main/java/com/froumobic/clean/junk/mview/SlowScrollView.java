package com.froumobic.clean.junk.mview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.froumobic.clean.junk.R;

/**
 * Created by on 2017/1/12.
 */

public class SlowScrollView extends ScrollView {

    private Scroller mScroller;
    Context context;

    public SlowScrollView(Context context) {
        super(context);
        this.context = context;
        mScroller = new Scroller(context);
    }

    public SlowScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mScroller = new Scroller(context);
    }

    public SlowScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        mScroller = new Scroller(context);
    }

    //调用此方法滚动到目标位置  duration滚动时间
    public void smoothScrollToSlow(int duration) {
        int dx = 0 - getScrollX();//mScroller.getFinalX();  普通view使用这种方法
        int dy = getBottom() - getScrollY() + context.getResources().getDimensionPixelSize(R.dimen.d500);  //mScroller.getFinalY();
        smoothScrollBySlow(dx, dy, duration);
    }


    //调用此方法设置滚动的相对偏移
    public void smoothScrollBySlow(int dx, int dy, int duration) {

        //设置mScroller的滚动偏移量
        mScroller.startScroll(getScrollX(), getScrollY(), 0, dy, duration);//scrollView使用的方法（因为可以触摸拖动）
//        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy, duration);  //普通view使用的方法
        invalidate();//这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
    }

    @Override
    public void computeScroll() {
        //先判断mScroller滚动是否完成
        if (mScroller.computeScrollOffset() && !isTouch) {
            //这里调用View的scrollTo()完成实际的滚动
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
//            smoothScrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            //必须调用该方法，否则不一定能看到滚动效果
            postInvalidate();
        }
        super.computeScroll();
    }

    /**
     * 滑动事件，这是控制手指滑动的惯性速度
     */
    @Override
    public void fling(int velocityY) {
        super.fling(velocityY / 4);
    }

    public boolean isTouch;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        isTouch = true;
        return super.onTouchEvent(ev);
    }
}