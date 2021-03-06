package com.supers.clean.junk.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.clean.util.LoadManager;
import com.android.clean.util.PreData;
import com.android.client.AndroidSdk;
import com.android.client.PaymentSystemListener;
import com.eos.eshop.ShopMaster;
import com.supers.clean.junk.R;
import com.android.clean.util.Util;
import com.supers.clean.junk.util.AdUtil;
import com.supers.clean.junk.util.PhoneManager;
import com.android.clean.util.Constant;
import com.supers.clean.junk.util.ShortCutUtils;

/**
 * Created by on 2017/3/8.
 */

public class LoadingActivity extends BaseActivity {
    Handler myHandler;
    LinearLayout loading_text;
    TextView load_name, load_text;
    ImageView load_icon;
    long time;

    @Override
    protected void findId() {
        super.findId();
        loading_text = (LinearLayout) findViewById(R.id.loading_text);
        load_name = (TextView) findViewById(R.id.load_name);
        load_text = (TextView) findViewById(R.id.load_text);
        load_icon = (ImageView) findViewById(R.id.load_icon);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        time = System.currentTimeMillis();
        ShopMaster.onCreate(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading);
        PreData.putDB(LoadingActivity.this, Constant.BILL_YOUXIAO, false);

        ShortCutUtils.addShortcut(this);
        myHandler = new Handler();
        if (PreData.getDB(this, Constant.ROOT_TRAK, true)) {
            AdUtil.track("是否获取root权限", PhoneManager.isRoot() == true ? "是" : "否", "", 1);
            AdUtil.track("是否安装applock", (LoadManager.getInstance(this).isPkgInstalled("com.eosmobi.applock")) == true ? "是" : "否", "", 1)
            ;
            PreData.putDB(this, Constant.ROOT_TRAK, false);
            PreData.putDB(this, Constant.KEY_CLEAN_TIME, System.currentTimeMillis());
        }
        if (PreData.getDB(this, Constant.FULL_START, 0) == 1 && !PreData.getDB(this, Constant.BILL_YOUXIAO, true)) {
            AndroidSdk.loadFullAd("loading_full", null);
        }
        myHandler.removeCallbacks(runnable1);
        myHandler.postDelayed(runnable1, 2500);
//        myHandler.removeCallbacks(runnableAni);
//        myHandler.postDelayed(runnableAni, 500);
//        animation1 = AnimationUtils.loadAnimation(LoadingActivity.this, R.anim.load_icon);
//        Animation animation2 = AnimationUtils.loadAnimation(LoadingActivity.this, R.anim.load_name);
//        Animation animation3 = AnimationUtils.loadAnimation(LoadingActivity.this, R.anim.load_text);
//        load_icon.startAnimation(animation1);
//        load_name.startAnimation(animation2);
//        load_text.startAnimation(animation3);
//        loading_text.setVisibility(View.VISIBLE);
//        animation1.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationEnd(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//        });
    }

    private Animation animation1;
    Runnable runnableAni = new Runnable() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public void run() {

        }
    };
    Runnable runnable1 = new Runnable() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public void run() {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int a = Util.dp2px(360);
            Log.e("jfy", "px=" + a + "" + "=" + metrics.density + "=" + metrics.widthPixels);
            jumpTo(MainActivity.class);
            finish();

        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        Log.e("loading_time", "loaingonResume" + (System.currentTimeMillis() - time));
        AndroidSdk.onResumeWithoutTransition(this);
        Log.e("loading_time", "loaingonResume结束" + (System.currentTimeMillis() - time));
    }

    @Override
    protected void onStop() {
        super.onStop();
        load_icon.clearAnimation();
        load_name.clearAnimation();
        load_text.clearAnimation();

    }

    @Override
    protected void onDestroy() {

        if (animation1 != null) {
            animation1.setAnimationListener(null);
            animation1.cancel();
        }
        if (myHandler != null) {
            myHandler.removeCallbacks(runnable1);
            myHandler.removeCallbacks(runnableAni);
            myHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
