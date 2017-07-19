package com.mingle.easywidget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.mingle.easycircletreveal.CircleRevealEnable;
import com.mingle.easylistener.SingleClickListener;
import com.mingle.easyskin.SkinEnable;
import com.mingle.easycircletreveal.CircleRevealHelper;
import com.mingle.easyskin.SkinStyle;
import com.mingle.easyskin.hepler.SkinHelper;
import com.mingle.easywidget.animation.CRAnimation;


/**
 * Created by zzz40500 on 15/8/26.
 */
public class Button extends AppCompatButton implements CircleRevealEnable,SkinEnable {

    private CircleRevealHelper  mCircleRevealHelper ;
    private  SkinHelper mSkinHelper;

    public Button(Context context) {
        super(context);
        init(null);

    }

    public Button(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {

        mSkinHelper=SkinHelper.create(this);
        mSkinHelper.init(this, attrs);
        mSkinHelper.setCurrentTheme();
        mCircleRevealHelper=new CircleRevealHelper(this);
    }

    public Button(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }


    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(new SingleClickListener(l));
    }

    @Override
    public void draw(Canvas canvas) {
        mCircleRevealHelper.draw(canvas);
    }


    @Override
    public void superDraw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    public CRAnimation circularReveal(int centerX, int centerY, float startRadius, float endRadius) {
       return mCircleRevealHelper.circularReveal(centerX,centerY,startRadius,endRadius);
    }

    @Override
    public void setSkinStyle(SkinStyle skinStyle) {
        mSkinHelper.setSkinStyle(skinStyle);

    }
}
