package com.mingle.privacycircletreveal;

import android.graphics.Canvas;

import com.mingle.widgetprivacy.animation.CRAnimation;

/**
 * Created by zzz40500 on 15/8/27.
 */
public interface CircleRevealEnable {



    CRAnimation circularReveal(int centerX, int centerY, float startRadius, float endRadius);

    void superDraw(Canvas canvas);


}
