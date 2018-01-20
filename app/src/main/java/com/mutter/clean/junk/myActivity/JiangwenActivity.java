package com.mutter.clean.junk.myActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.mutter.clean.junk.R;
import com.mutter.clean.junk.myview.CoolingView;
import com.mutter.clean.junk.service.NotificationService;
import com.mutter.clean.junk.util.AdUtil;
import com.mutter.clean.junk.util.BadgerCount;
import com.mutter.clean.junk.util.Constant;
import com.mutter.clean.junk.myview.FlakeView;
import com.mutter.clean.util.PreData;

import java.util.Random;


/**
 * Created by on 2017/3/2.
 */

public class JiangwenActivity extends BaseActivity {
    private static final int FLAKE_NUM = 5;
    LinearLayout cooling_text;
    CoolingView cooling_view;
    TextView cooling_wendu;
    FrameLayout title_left;
    TextView title_name;
    LinearLayout cooling_piao;

    private FlakeView flakeView;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (flakeView != null) {
                flakeView.addFlakes(FLAKE_NUM);
            }
        }
    };
    private Random random;
    private int time;
    int count;

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        cooling_piao = (LinearLayout) findViewById(R.id.cooling_piao);
        cooling_text = (LinearLayout) findViewById(R.id.cooling_text);
        cooling_wendu = (TextView) findViewById(R.id.cooling_wendu);
        cooling_view = (CoolingView) findViewById(R.id.cooling_view);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cooling);
        PreData.putDB(this, Constant.HONG_COOLING, false);
        AndroidSdk.loadFullAd(AdUtil.FULL_DEFAULT, null);
        BadgerCount.setCount(this);
        title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        title_name.setText(R.string.main_cooling_name);

        startCoolingAni();

        cooling_text.setVisibility(View.VISIBLE);
        final int wendu = getIntent().getIntExtra("wendu", 40);
        cooling_view.startProgress2(30);
        cooling_view.setCustomRoundListener(new CoolingView.CustomRoundListener() {
            @Override
            public void progressSuccess() {
                handler.sendEmptyMessage(10);
            }
        });
        cooling_wendu.setText(wendu + "℃");
        new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i <= time; i++) {
                    if (onPause) {
                        return;
                    }
                    final int finalI = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            cooling_wendu.setText((wendu - finalI) + "℃");
                        }
                    });
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 10) {
                count++;
                startAc();
            } else if (msg.what == 11) {
                count++;
                startAc();
                hideSnow();
            }
        }
    };


    private void startAc() {
        if (count < 2) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("wendu", time);
        bundle.putString("from", "cooling");
        bundle.putString("name", getString(R.string.cooling_succ));
        jumpToActivity(SuccessActivity.class, bundle, 1);
    }

    private void hideSnow() {

        if (flakeView != null) {
            flakeView.subtractFlakes(FLAKE_NUM);
            flakeView.pause();
            flakeView = null;
        }
        if (cooling_piao != null) {
            cooling_piao.removeAllViews();
            cooling_piao.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        hideSnow();
    }

    @Override
    public void onBackPressed() {
        if ("notifi".equals(getIntent().getStringExtra("from"))) {
            jumpTo(MainActivity.class);
        } else {
            setResult(Constant.COOLING_RESUIL, new Intent().putExtra("wendu", time));
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        if (handler != null && runnable != null) {
            handler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }

    private void startCoolingAni() {
        random = new Random();
        time = random.nextInt(5) + 1;
        handler.sendEmptyMessageDelayed(11, 3000);

    }

    @Override
    protected void onResume() {
        super.onResume();
        flakeView = new FlakeView(this);
        cooling_piao.addView(flakeView);
        handler.post(runnable);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            onBackPressed();
        }
    }
}
