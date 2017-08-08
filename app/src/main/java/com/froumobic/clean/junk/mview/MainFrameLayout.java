package com.froumobic.clean.junk.mview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * The type Main  layout.
 */
public class MainFrameLayout extends FrameLayout {
    /**
     * Instantiates a new Main frame layout.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public MainFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
