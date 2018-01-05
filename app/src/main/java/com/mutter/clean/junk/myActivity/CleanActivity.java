package com.mutter.clean.junk.myActivity;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mutter.clean.junk.service.NotificationService;
import com.mutter.clean.junk.util.BadgerCount;
import com.mutter.clean.junk.view.ShowPercentView;
import com.mutter.clean.junk.view.ShowPercentViewjunk;
import com.mutter.clean.util.MemoryManager;
import com.mutter.clean.util.PreData;
import com.mutter.clean.util.Util;
import com.android.client.AndroidSdk;
import com.mutter.clean.junk.R;
import com.mutter.clean.junk.util.AdUtil;
import com.mutter.clean.junk.util.Constant;
import com.mutter.clean.junk.view.JunkView;
import com.mutter.clean.junk.myAdapter.JunkAdapter;
import com.mutter.clean.entity.JunkInfo;
import com.mutter.clean.junk.myview.ListViewForScrollView;
import com.mutter.clean.junk.myview.MyScrollView;
import com.mutter.clean.junk.presenter.JunkPresenter;
import com.sample.lottie.LottieAnimationView;

import java.util.List;

/**
 * Created by on 2017/3/2.
 */

public class CleanActivity extends BaseActivity implements JunkView {

    FrameLayout title_left;
    ShowPercentViewjunk ram_baifen;
    TextView title_name;
    TextView ram_zhanyong, ram_all, ram_parent;
    LottieAnimationView lottie_power;

    private JunkPresenter junkPresenter;
    public Handler myHandler;

    @Override
    protected void findId() {
        super.findId();
        title_left = $(R.id.title_left);
        title_name = $(R.id.title_name);
        ram_baifen = $(R.id.ram_baifen);
        ram_zhanyong = $(R.id.ram_zhanyong);
        ram_all = $(R.id.ram_all);
        ram_parent = $(R.id.ram_parent);
        lottie_power = $(R.id.lottie_power);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_junk);
        BadgerCount.setCount(this);
        AndroidSdk.loadFullAd(AdUtil.FULL_DEFAULT, null);
        myHandler = new Handler();
        junkPresenter = new JunkPresenter(this, this);
        junkPresenter.init();
    }

    @Override
    public void loadFullAd() {

    }

    @Override
    public void initData(long allSize) {
        title_name.setText(R.string.main_junk_name);
        if (allSize <= 0) {
            Bundle bundle = new Bundle();
            bundle.putString("from", "junkClean");
            junkPresenter.jumpToActivity(SuccessActivity.class, bundle, 1);
            return;
        } else {
            PreData.putDB(CleanActivity.this, Constant.KEY_CLEAN_TIME, System.currentTimeMillis());
            junkPresenter.bleachFile();
        }
    }


    @Override
    public void onClick() {
        title_left.setOnClickListener(onClickListener);
    }

    @Override
    public void cleanAnimation(final long cleanSize) {
        long sd_all = MemoryManager.getPhoneAllSize();
        long sd_kongxian = MemoryManager.getPhoneAllFreeSize();
        long sd_shiyong = sd_all - sd_kongxian;
        int sd_me = (int) (sd_shiyong * 100 / sd_all);
        ram_zhanyong.setText(Util.convertStorage(sd_shiyong, true));
        ram_all.setText("/" + Util.convertStorage(sd_all, true));
        ram_parent.setText("" + sd_me);
        ram_baifen.start(sd_me);
        ram_baifen.setListener(new ShowPercentViewjunk.PercentViewListener() {
            @Override
            public void setPerc(int percent) {

            }

            @Override
            public void setSucc() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lottie_power.playAnimation();
                        lottie_power.setVisibility(View.VISIBLE);
                        lottie_power.addAnimatorListener(animatorListener);
                    }
                });
            }
        });

    }

    Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            final Bundle bundle = new Bundle();
            bundle.putString("from", "junkClean");
            junkPresenter.jumpToActivity(SuccessActivity.class, bundle, 1);
        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }

        @Override
        public void onAnimationStart(Animator animation) {

        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_left:
                    AdUtil.track("垃圾页面", "点击返回", "", 1);
                    onBackPressed();
                    break;
            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myHandler != null) {
            myHandler.removeCallbacksAndMessages(null);
        }
        if (ram_baifen != null) {
            ram_baifen.setListener(null);
        }
        if (lottie_power != null) {
            lottie_power.removeAnimatorListener(animatorListener);
            lottie_power.cancelAnimation();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        AndroidSdk.onResume(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            setResult(Constant.JUNK_RESUIL);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(Constant.JUNK_RESUIL);
        finish();
    }
}
