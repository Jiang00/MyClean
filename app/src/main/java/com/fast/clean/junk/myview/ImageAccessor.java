package com.fast.clean.junk.myview;

import android.widget.ImageView;

import com.twee.module.tweenengine.TweenAccessor;


public class ImageAccessor implements TweenAccessor<ImageView> {
    public static final int BOUNCE_EFFECT = 1;

    @Override
    public void setValues(ImageView target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case BOUNCE_EFFECT:
                target.setX(newValues[0]);
                target.setY(newValues[1]);
                target.setScaleX(newValues[2]);
                target.setScaleY(newValues[2]);
                target.setAlpha(newValues[3]);
//			target.setX(newValues[0]);
//			target.setY(newValues[1]);
            default:
                assert false;
        }
    }

    @Override
    public int getValues(ImageView target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case BOUNCE_EFFECT:
                returnValues[0] = target.getX();
                returnValues[1] = target.getY();
                returnValues[2] = target.getScaleX();
                returnValues[2] = target.getScaleY();
                returnValues[3] = target.getAlpha();

                return 4;
            default:
                assert false;
                return -1;
        }
    }


}
