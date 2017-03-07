package com.supers.clean.junk.myView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by Ivy on 2017/2/28.
 */

public class MainScrollView extends ScrollView implements Pullable {


    private boolean shutTouch;

    public MainScrollView(Context context) {
        super(context);
    }

    public MainScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MainScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // ture：禁止控件本身的滑动.
        if (shutTouch)
            return true;
        else
            return super.onTouchEvent(ev);

    }


    public void setShutTouch(boolean shutTouch) {
        this.shutTouch = shutTouch;
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
