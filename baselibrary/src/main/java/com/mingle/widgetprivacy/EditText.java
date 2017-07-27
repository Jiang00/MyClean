package com.mingle.widgetprivacy;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.mingle.privacycircletreveal.CircleRevealEnable;
import com.mingle.privacycircletreveal.CircleRevealHelper;
import com.mingle.listenerprivacy.SingleClickListener;
import com.mingle.privacyskin.SkinEnable;
import com.mingle.privacyskin.SkinStyle;
import com.mingle.privacyskin.hepler.SkinHelper;
import com.mingle.widgetprivacy.animation.CRAnimation;

/**
 * Created by zzz40500 on 15/8/26.
 */
public class EditText extends AppCompatEditText implements CircleRevealEnable,SkinEnable {

    private CircleRevealHelper mCircleRevealHelper ;
    private SkinHelper mSkinHelper;

    public EditText(Context context) {
        super(context);
        init(null);

    }

    public EditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {

        mSkinHelper=SkinHelper.create(this);
        mSkinHelper.init(this, attrs);
        mSkinHelper.setCurrentTheme();
        mCircleRevealHelper=new CircleRevealHelper(this);
    }

    public EditText(Context context, AttributeSet attrs, int defStyleAttr) {
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
