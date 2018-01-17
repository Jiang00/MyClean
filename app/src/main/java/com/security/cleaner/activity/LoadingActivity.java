package com.security.cleaner.activity;

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
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.security.cleaner.utils.AdUtil;
import com.security.cleaner.utils.Constant;
import com.security.cleaner.utils.PhoneManager;
import com.security.cleaner.utils.ShortCutUtils;
import com.security.mcleaner.mutil.PreData;
import com.security.mcleaner.mutil.Util;
import com.android.client.AndroidSdk;
import com.security.cleaner.R;

import org.json.JSONObject;

/**
 * Created by on 2017/3/8.
 */

public class LoadingActivity extends BaseActivity {
    Handler myHandler;
    ImageView icon;
    TextView text;
    private AnimatorSet animatorSet;

    @Override
    protected void findId() {
        super.findId();
        icon = (ImageView) findViewById(R.id.icon);
        text = (TextView) findViewById(R.id.text);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading);
        ShortCutUtils.addShortcut(this);
        myHandler = new Handler();
        if (PreData.getDB(this, Constant.ROOT_TRAK, true)) {
            AdUtil.track("是否获取root权限", PhoneManager.isRoot() == true ? "是" : "否", "", 1);
            PreData.putDB(this, Constant.ROOT_TRAK, false);
            PreData.putDB(this, Constant.KEY_CLEAN_TIME, System.currentTimeMillis());
        }

        initBackgData();
        myHandler.removeCallbacks(runnable1);
        myHandler.postDelayed(runnable1, 500);
        if (PreData.getDB(this, Constant.FULL_START, 0) == 1) {
            AndroidSdk.loadFullAd("loading_full", null);
        }
    }

    private void initAnimation() {
        animatorSet = new AnimatorSet();
        ObjectAnimator objectanimator = ObjectAnimator.ofFloat(icon, View.TRANSLATION_Y, getResources().getDimension(R.dimen.d121), 0);
        objectanimator.setDuration(1300);
        ObjectAnimator objectanimator_2 = ObjectAnimator.ofFloat(text, View.TRANSLATION_Y, getResources().getDimension(R.dimen.d121), 0);
        objectanimator_2.setDuration(1500);
        animatorSet.play(objectanimator);
        animatorSet.play(objectanimator_2);
        animatorSet.start();
        icon.setVisibility(View.VISIBLE);
        text.setVisibility(View.VISIBLE);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
                int a = Util.dp2px(360);
                Log.e("jfy", "px=" + a + "" + "=" + metrics.density + "=" + metrics.widthPixels);
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
    }

    private void initBackgData() {
        try {
            JSONObject jsonObject = new JSONObject(AndroidSdk.getExtraData());
            if (jsonObject.has("full_start")) {
                PreData.putDB(LoadingActivity.this, Constant.FULL_START, jsonObject.getInt("full_start"));
            }
            if (jsonObject.has("full_exit")) {
                PreData.putDB(LoadingActivity.this, Constant.FULL_EXIT, jsonObject.getInt("full_exit"));
            }
            if (jsonObject.has("show_exit_native")) {
                PreData.putDB(LoadingActivity.this, Constant.FULL_EXIT_NATIVE, jsonObject.getInt("show_exit_native"));
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
            if (jsonObject.has("clean_result_native")) {
                PreData.putDB(LoadingActivity.this, Constant.FULL_SUCCESS_NATIVE, jsonObject.getInt("clean_result_native"));
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
            if (jsonObject.has("clean_native_size")) {
                PreData.putDB(LoadingActivity.this, Constant.SUCCESS_NATIVVE_SIZE, jsonObject.getString("clean_native_size"));
            }
            if (jsonObject.has("float_native_size")) {
                PreData.putDB(LoadingActivity.this, Constant.FLOAT_NATIVVE_SIZE, jsonObject.getString("float_native_size"));
            }
            if (jsonObject.has("shortcut_native_size")) {
                PreData.putDB(LoadingActivity.this, Constant.SHORTCUT_NATIVVE_SIZE, jsonObject.getString("shortcut_native_size"));
            }
            if (jsonObject.has("full_recyclebin")) {
                PreData.putDB(LoadingActivity.this, Constant.RECYCLEBIN, jsonObject.getInt("full_recyclebin"));
            }
            if (jsonObject.has("notifi_kaiguan")) {
                PreData.putDB(LoadingActivity.this, Constant.NOTIFI_KAIGUAN, jsonObject.getInt("notifi_kaiguan"));
            }
            if (jsonObject.has("file_kaiguan")) {
                PreData.putDB(LoadingActivity.this, Constant.FILE_KAIGUAN, jsonObject.getInt("file_kaiguan"));
            }
            if (jsonObject.has("photo_kaiguan")) {
                PreData.putDB(LoadingActivity.this, Constant.PHOTO_KAIGUAN, jsonObject.getInt("photo_kaiguan"));
            }
        } catch (Exception e) {

        }
    }

    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            initAnimation();
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
        if (animatorSet != null && animatorSet.isRunning()) {
            animatorSet.cancel();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
