package com.mutter.clean.junk.myview.conpentview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.frigate.layout.FrigateFrameLayout;
import com.frigate.utils.AutoUtils;
import com.mutter.clean.junk.R;
import com.mutter.clean.junk.myview.RoundSd;

/**
 * Created by ${} on 2018/1/23.
 */

public class AutoRotateXView extends FrigateFrameLayout {
    Context mContext;
    ImageView main_circle, lot_family;
    FrameLayout main_ad;
    Handler handler;

    public AutoRotateXView(Context context) {
        this(context, null);
    }

    public AutoRotateXView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoRotateXView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        handler = new Handler();
        LayoutInflater.from(context).inflate(R.layout.auto_rotate_x, this, true);
        intView();
    }


    private void intView() {
        main_circle = (ImageView) findViewById(R.id.main_circle);
        main_ad = (FrameLayout) findViewById(R.id.main_ad);
        lot_family = (ImageView) findViewById(R.id.lot_family);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (handler != null)
            handler.post(runnable);
        RotateAnimation animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(1000);//设置动画持续时间
        animation.setRepeatCount(-1);//设置重复次数
        animation.setInterpolator(new LinearInterpolator());
        animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        lot_family.startAnimation(animation);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            toggleEditAnimation(R.id.main_ad, R.id.main_circle);
            if (handler != null)
                handler.postDelayed(this, 4000);
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (handler != null)
            handler.removeCallbacks(runnable);
        lot_family.clearAnimation();
    }

    public void toggleEditAnimation(int resId1, int resId2) {
        final View searchView = findViewById(resId1);
        View normalView = findViewById(resId2);

        final View visibleView, invisibleView;
        if (searchView.getVisibility() == View.GONE) {
            visibleView = normalView;
            invisibleView = searchView;
        } else {
            visibleView = searchView;
            invisibleView = normalView;
        }
        final ObjectAnimator invis2vis = ObjectAnimator.ofFloat(invisibleView, "rotationY", -90, 0);
        invis2vis.setDuration(300);
        invis2vis.setInterpolator(new LinearInterpolator());
        ObjectAnimator vis2invis = ObjectAnimator.ofFloat(visibleView, "rotationY", 0, 90);
        vis2invis.setDuration(300);
        vis2invis.setInterpolator(new LinearInterpolator());

        vis2invis.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                visibleView.setVisibility(View.GONE);
                invisibleView.setVisibility(View.VISIBLE);
                invis2vis.start();
            }
        });
        vis2invis.start();
    }

}
