package com.mutter.clean.junk.myview.conpentview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.android.client.AdListener;
import com.android.client.AndroidSdk;
import com.frigate.layout.FrigateFrameLayout;
import com.mutter.clean.junk.R;

/**
 * Created by ${} on 2018/1/30.
 */

public class AutoLoadFllView extends FrigateFrameLayout {

    Handler handler;

    public AutoLoadFllView(Context context) {
        this(context, null);
    }

    public AutoLoadFllView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoLoadFllView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        handler = new Handler();
        LayoutInflater.from(mContext).inflate(R.layout.auto_loadfull, this, true);
        initView();
    }

    private void initView() {
        load_1 = (ImageView) findViewById(R.id.load_1);
        load_2 = (ImageView) findViewById(R.id.load_2);
        load_2_2 = (ImageView) findViewById(R.id.load_2_2);
        load_3 = (ImageView) findViewById(R.id.load_3);
        load_4 = (ImageView) findViewById(R.id.load_4);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    private ObjectAnimator load_rotate;
    ImageView load_2, load_2_2, load_3, load_1, load_4;

   public void startLoad() {
        AndroidSdk.loadFullAd("loading_full", new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                if (load_rotate != null) {
                    load_rotate.removeAllListeners();
                    load_rotate.cancel();
                }
                setVisibility(View.GONE);
            }
        });
        load_2.setVisibility(View.VISIBLE);
        load_1.setVisibility(View.VISIBLE);
        load_3.setVisibility(View.VISIBLE);
        load_2_2.setVisibility(View.VISIBLE);
        load_4.setVisibility(View.GONE);
        load_rotate = ObjectAnimator.ofFloat(load_1, View.ROTATION, 0, 3600);
        load_rotate.setDuration(4000);
        load_rotate.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            int count = 0;

            public void onAnimationUpdate(ValueAnimator animation) {
                count++;
                if (count % 15 == 0) {
                    if (load_2.getVisibility() == View.VISIBLE) {
                        load_2.setVisibility(View.GONE);
                    } else {
                        load_2.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        load_rotate.start();
        setVisibility(View.VISIBLE);
        handler.postDelayed(runnable_load, 4500);
    }

    Runnable runnable_load = new Runnable() {
        @Override
        public void run() {
            AndroidSdk.showFullAd("loading_full");
            setVisibility(View.GONE);
        }
    };

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == GONE) {
            if (load_rotate != null) {
                load_rotate.removeAllListeners();
                load_rotate.cancel();
            }
            handler.removeCallbacks(runnable_load);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (load_rotate != null) {
            load_rotate.removeAllListeners();
            load_rotate.cancel();
        }
        handler.removeCallbacks(runnable_load);
    }
}
