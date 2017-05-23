package com.supers.clean.junk.customeview;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.mingle.circletreveal.CircleRevealEnable;
import com.mingle.widget.animation.CRAnimation;

/**
 * Created by Ivy on 2017/5/22.
 */

public class CirLinearLayout extends LinearLayout implements CircleRevealEnable {

    public CirLinearLayout(Context context) {
        super(context);
    }

    public CirLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CirLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public CRAnimation circularReveal(int centerX, int centerY, float startRadius, float endRadius) {
        return null;
    }

    @Override
    public void superDraw(Canvas canvas) {

    }
}
