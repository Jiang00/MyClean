package com.fast.clean.junk.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fast.clean.mutil.PreData;
import com.fast.clean.mutil.Util;
import com.android.client.AndroidSdk;
import com.fast.clean.junk.R;
import com.fast.clean.junk.util.AdUtil;
import com.fast.clean.junk.util.Constant;
import com.fast.clean.junk.util.PhoneManager;
import com.fast.clean.junk.util.ShortCutUtils;

import org.json.JSONObject;

/**
 * Created by on 2017/3/8.
 */

public class LoadingActivity extends BaseActivity {
    Handler myHandler;
    TextView tv_tiaoguo;
    LinearLayout loading_1;
    ImageView loading_2;
    TextView load_text_3;
    private AnimatorSet animatorSet;

    @Override
    protected void findId() {
        super.findId();
        tv_tiaoguo = (TextView) findViewById(R.id.tv_tiaoguo);
        loading_1 = (LinearLayout) findViewById(R.id.loading_1);
        loading_2 = (ImageView) findViewById(R.id.loading_2);
        load_text_3 = (TextView) findViewById(R.id.load_text_3);
        // ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading);
        ShortCutUtils.addShortcut(this);
        myHandler = new Handler();
        tv_tiaoguo.setVisibility(View.INVISIBLE);
        if (PreData.getDB(this, Constant.ROOT_TRAK, true)) {
            AdUtil.track("是否获取root权限", PhoneManager.isRoot() == true ? "是" : "否", "", 1);
            PreData.putDB(this, Constant.ROOT_TRAK, false);
            PreData.putDB(this, Constant.KEY_CLEAN_TIME, System.currentTimeMillis());
        }
        animatorSet = new AnimatorSet();
        ObjectAnimator animator_1 = ObjectAnimator.ofFloat(loading_2, View.TRANSLATION_X, -getResources().getDimensionPixelSize(R.dimen.d20), 0);
        ObjectAnimator animator_2 = ObjectAnimator.ofFloat(loading_1, View.TRANSLATION_X, getResources().getDimensionPixelSize(R.dimen.d333), 0);
        ObjectAnimator animator_3 = ObjectAnimator.ofFloat(load_text_3, View.TRANSLATION_Y, getResources().getDimensionPixelSize(R.dimen.d20), 0);
        animatorSet.setDuration(2000);
        animatorSet.play(animator_1).with(animator_2).with(animator_3);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                jumpTo(MainActivity.class);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationStart(Animator animation) {

            }
        });
        myHandler.post(runnable1);
        if (PreData.getDB(this, Constant.FULL_START, 0) == 1) {
            AndroidSdk.loadFullAd("loading_full", null);
        }
    }

    Runnable runnable1 = new Runnable() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public void run() {
            try {
                JSONObject jsonObject = new JSONObject(AndroidSdk.getExtraData());
                if (jsonObject.has("full_main")) {
                    PreData.putDB(LoadingActivity.this, Constant.FULL_MAIN, jsonObject.getInt("full_main"));
                }
                if (jsonObject.has("full_start")) {
                    PreData.putDB(LoadingActivity.this, Constant.FULL_START, jsonObject.getInt("full_start"));
                }
                if (jsonObject.has("gift_switch")) {
                    PreData.putDB(LoadingActivity.this, Constant.FULL_LIBAO, jsonObject.getInt("gift_switch"));
                }
                if (jsonObject.has("full_exit")) {
                    PreData.putDB(LoadingActivity.this, Constant.FULL_EXIT, jsonObject.getInt("full_exit"));
                }
                if (jsonObject.has("show_exit_native")) {
                    PreData.putDB(LoadingActivity.this, Constant.NATIVE_EXIT, jsonObject.getInt("show_exit_native"));
                }
                if (jsonObject.has("skip_time")) {
                    PreData.putDB(LoadingActivity.this, Constant.SKIP_TIME, jsonObject.getInt("skip_time"));
                    Log.e("timeada", jsonObject.getInt("skip_time") + "==");
                }
                if (jsonObject.has("full_manager")) {
                    PreData.putDB(LoadingActivity.this, Constant.FULL_MANAGER, jsonObject.getInt("full_manager"));
                }
                if (jsonObject.has("full_message")) {
                    PreData.putDB(LoadingActivity.this, Constant.FULL_MESSAGE, jsonObject.getInt("full_message"));
                }
                if (jsonObject.has("full_success")) {
                    PreData.putDB(LoadingActivity.this, Constant.FULL_SUCCESS, jsonObject.getInt("full_success"));
                }
                if (jsonObject.has(Constant.FULL_SUCCESS_MAX_COUNT)) {
                    PreData.putDB(LoadingActivity.this, Constant.FULL_SUCCESS_MAX_COUNT, jsonObject.getInt(Constant.FULL_SUCCESS_MAX_COUNT));
                }
                if (jsonObject.has(Constant.AD_LOADING_TIME)) {
                    PreData.putDB(LoadingActivity.this, Constant.AD_LOADING_TIME, jsonObject.getInt(Constant.AD_LOADING_TIME));
                }
                if (jsonObject.has("clean_result_native")) {
                    PreData.putDB(LoadingActivity.this, Constant.NATIVE_SUCCESS, jsonObject.getInt("clean_result_native"));
                }
                if (jsonObject.has("full_setting")) {
                    PreData.putDB(LoadingActivity.this, Constant.FULL_SETTING, jsonObject.getInt("full_setting"));
                }
                if (jsonObject.has("full_unload")) {
                    PreData.putDB(LoadingActivity.this, Constant.FULL_UNLOAD, jsonObject.getInt("full_unload"));
                }
                if (jsonObject.has("full_float")) {
                    PreData.putDB(LoadingActivity.this, Constant.FULL_FLOAT, jsonObject.getInt("full_float"));
                }
                if (jsonObject.has("full_cool")) {
                    PreData.putDB(LoadingActivity.this, Constant.FULL_COOL, jsonObject.getInt("full_cool"));
                }
                if (jsonObject.has("full_shortcut")) {
                    PreData.putDB(LoadingActivity.this, Constant.FULL_SHORTCUT, jsonObject.getInt("full_shortcut"));
                }
                if (jsonObject.has("full_file")) {
                    PreData.putDB(LoadingActivity.this, Constant.FULL_FILE, jsonObject.getInt("full_file"));
                }
                if (jsonObject.has("full_file_1")) {
                    PreData.putDB(LoadingActivity.this, Constant.FULL_FILE_1, jsonObject.getInt("full_file_1"));
                }
                if (jsonObject.has("full_file_2")) {
                    PreData.putDB(LoadingActivity.this, Constant.FULL_FILE_2, jsonObject.getInt("full_file_2"));
                }
                if (jsonObject.has("full_similar_photo")) {
                    PreData.putDB(LoadingActivity.this, Constant.PICTURE, jsonObject.getInt("full_similar_photo"));
                }
                if (jsonObject.has("full_recyclebin")) {
                    PreData.putDB(LoadingActivity.this, Constant.RECYCLEBIN, jsonObject.getInt("full_recyclebin"));
                }
                if (jsonObject.has("system_cache")) {
                    PreData.putDB(LoadingActivity.this, com.fast.clean.mutil.Constant.SYSTEM_CACHE, jsonObject.getInt("system_cache"));
                }
                if (jsonObject.has("apk_file")) {
                    PreData.putDB(LoadingActivity.this, com.fast.clean.mutil.Constant.APK_FILE, jsonObject.getInt("apk_file"));
                }
                if (jsonObject.has("log_file")) {
                    PreData.putDB(LoadingActivity.this, com.fast.clean.mutil.Constant.LOG_FILE, jsonObject.getInt("log_file"));
                }
                if (jsonObject.has("unload_file")) {
                    PreData.putDB(LoadingActivity.this, com.fast.clean.mutil.Constant.UNLOAD_FILE, jsonObject.getInt("unload_file"));
                }
                if (jsonObject.has("user_cache")) {
                    PreData.putDB(LoadingActivity.this, com.fast.clean.mutil.Constant.USER_CACHE, jsonObject.getInt("user_cache"));
                }
                if (jsonObject.has("ram_kill")) {
                    PreData.putDB(LoadingActivity.this, com.fast.clean.mutil.Constant.RAM_KILL, jsonObject.getInt("ram_kill"));
                }
            } catch (Exception e) {

            }
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int a = Util.dp2px(360);
            Log.e("jfy", "px=" + a + "" + "=" + metrics.density + "=" + metrics.widthPixels);

        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        AndroidSdk.onResumeWithoutTransition(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        myHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
