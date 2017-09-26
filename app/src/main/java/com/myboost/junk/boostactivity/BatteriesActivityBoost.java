package com.myboost.junk.boostactivity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.myboost.clean.utilsprivacy.PreData;
import com.myboost.junk.R;
import com.myboost.junk.boosttools.BoostMyConstant;
import com.myboost.junk.boosttools.SetAdUtilPrivacy;
import com.myboost.junk.customviewboost.Cooling2View;
import com.myboost.junk.customviewboost.CoolongView;

import java.util.Random;


/**
 * Created by on 2017/3/2.
 */

public class BatteriesActivityBoost extends BaseActivity {
    LinearLayout cooling_text;
    CoolongView cooling_xue;
    Cooling2View cooling_2;
    TextView cooling_wendu;
    FrameLayout title_left;
    private Random random;
    TextView title_name;
    private Animation rotate_zheng;
    Handler handler;
    LinearLayout cooling_fl;
    private static final int FLAKE_NUM = 5;
    private int time;
    private Animation suo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cooling);
        AndroidSdk.loadFullAd(SetAdUtilPrivacy.DEFAULT_FULL,null);
        handler = new Handler();
        title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        title_name.setText(R.string.main_cooling_name);

        rotate_zheng = AnimationUtils.loadAnimation(this, R.anim.rotate_cooling);
        suo = AnimationUtils.loadAnimation(this, R.anim.suo);
        cooling_2.reStart();
//        startCoolingAni();

//        cooling_text.setVisibility(View.VISIBLE);
        final int wendu = getIntent().getIntExtra("wendu", 40);
        cooling_wendu.setText(wendu + "℃");
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                for (int i = 0; i <= time; i++) {
//                    if (onPause) {
//                        return;
//                    }
//                    final int finalI = i;
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            cooling_wendu.setText((wendu - finalI) + "℃");
//                        }
//                    });
//                    try {
//                        Thread.sleep(500);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
        handler.postDelayed(runnable, 3500);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            startCoolingAni();
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        cooling_xue.pause();
    }

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        cooling_text = (LinearLayout) findViewById(R.id.cooling_text);
        cooling_xue = (CoolongView) findViewById(R.id.cooling_xue);
        cooling_2 = (Cooling2View) findViewById(R.id.cooling_2);
        cooling_wendu = (TextView) findViewById(R.id.cooling_wendu);
        cooling_fl = (LinearLayout) findViewById(R.id.cooling_fl);
    }

    private void startCoolingAni() {
        random = new Random();
        time = random.nextInt(5) + 1;
        if (PreData.getDB(BatteriesActivityBoost.this, BoostMyConstant.FULL_COOL, 0) == 1) {
            AndroidSdk.showFullAd(SetAdUtilPrivacy.DEFAULT_FULL);
        }
        Bundle bundle = new Bundle();
        bundle.putInt("wendu", time);
        bundle.putString("from", "cooling");
        jumpToActivity(SucceedActivityBoost.class, bundle, 1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cooling_xue.reStart();
    }

    @Override
    public void onBackPressed() {
        if ("notifi".equals(getIntent().getStringExtra("from"))) {
            jumpTo(MainActivity.class);
        } else {
            setResult(BoostMyConstant.COOLING_RESUIL, new Intent().putExtra("wendu", time));
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            onBackPressed();
        }
    }


    @Override
    protected void onDestroy() {
        cooling_xue.destroy();
        cooling_2.pause();
        super.onDestroy();
    }
}
