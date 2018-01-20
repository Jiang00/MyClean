package com.eifmobi.clean.junk.myActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eifmobi.clean.junk.util.DecelerateAccelerateInterpolator;
import com.eifmobi.clean.util.PreData;
import com.eifmobi.clean.util.Util;
import com.android.client.AndroidSdk;
import com.eifmobi.clean.junk.R;
import com.eifmobi.clean.junk.util.AdUtil;
import com.eifmobi.clean.junk.util.Constant;
import com.eifmobi.clean.junk.util.PhoneManager;
import com.eifmobi.clean.junk.util.ShortCutUtils;

import org.json.JSONObject;

/**
 * Created by on 2017/3/8.
 */

public class LoadingActivity extends BaseActivity {
    Handler myHandler;

    LinearLayout loading_text_1;
    TextView loading_text_2;
    private AnimatorSet animatorSet;

    @Override
    protected void findId() {
        super.findId();
        loading_text_1 = (LinearLayout) findViewById(R.id.loading_text_1);
        loading_text_2 = (TextView) findViewById(R.id.loading_text_2);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSdk.onCreate(this);
        setContentView(R.layout.layout_loading);
        ShortCutUtils.addShortcut(this);
        ShortCutUtils.addShortcutJiang(this);
        myHandler = new Handler();
        if (PreData.getDB(this, Constant.ROOT_TRAK, true)) {
            AdUtil.track("是否获取root权限", PhoneManager.isRoot() == true ? "是" : "否", "", 1);
            PreData.putDB(this, Constant.ROOT_TRAK, false);
            PreData.putDB(this, Constant.KEY_CLEAN_TIME, System.currentTimeMillis());
        }
        init();
        myHandler.removeCallbacks(runnable1);
        myHandler.postDelayed(runnable1, 4000);
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animatorSet = new AnimatorSet();
                ObjectAnimator animator = ObjectAnimator.ofFloat(loading_text_1, View.TRANSLATION_X, loading_text_1.getWidth(), 0);
                animator.setDuration(2000);
                animator.setInterpolator(new BounceInterpolator());
                animatorSet.play(animator);
                animatorSet.start();
                loading_text_1.setVisibility(View.VISIBLE);
//                Animation animation = AnimationUtils.loadAnimation(LoadingActivity.this, R.anim.translate_loading);
//                loading_text_1.startAnimation(animation);
//                loading_text_1.setVisibility(View.VISIBLE);
//                animation.setAnimationListener(new Animation.AnimationListener() {
//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//                        Animation animation1 = AnimationUtils.loadAnimation(LoadingActivity.this, R.anim.translate_loading);
//                        loading_text_2.startAnimation(animation1);
//                        loading_text_2.setVisibility(View.VISIBLE);
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationStart(Animation animation) {
//
//                    }
//                });
            }
        }, 1000);

//        if (PreData.getDB(this, Constant.FULL_START, 0) == 1) {
//            AndroidSdk.loadFullAd("loading_full", null);
//        }

    }

    private void init() {
        try {
            JSONObject jsonObject = new JSONObject(AndroidSdk.getExtraData());
            if (jsonObject.has("full_main")) {
                PreData.putDB(LoadingActivity.this, Constant.FULL_MAIN, jsonObject.getInt("full_main"));
            }
            if (jsonObject.has("full_start")) {
                PreData.putDB(LoadingActivity.this, Constant.FULL_START, jsonObject.getInt("full_start"));
            }
            if (jsonObject.has("full_exit")) {
                PreData.putDB(LoadingActivity.this, Constant.FULL_EXIT, jsonObject.getInt("full_exit"));
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
            if (jsonObject.has("clean_native_size")) {
                PreData.putDB(LoadingActivity.this, Constant.SUCCESS_NATIVE_SIZE, jsonObject.getString("clean_native_size"));
            }
            if (jsonObject.has("float_native_size")) {
                PreData.putDB(LoadingActivity.this, Constant.FLOAT_NATIVE_SIZE, jsonObject.getString("float_native_size"));
            }
            if (jsonObject.has("shortcut_native_size")) {
                PreData.putDB(LoadingActivity.this, Constant.SHORT_NATIVE_SIZE, jsonObject.getString("shortcut_native_size"));
            }
            if (jsonObject.has("show_deepclean_native")) {
                PreData.putDB(LoadingActivity.this, Constant.FULL_DEEP_NATIVE, jsonObject.getInt("show_deepclean_native"));
            }
            if (jsonObject.has("show_exit_native")) {
                PreData.putDB(LoadingActivity.this, Constant.FULL_EDIT_NATIVE, jsonObject.getInt("show_exit_native"));
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
            if (jsonObject.has("notifi_kaiguan")) {
                PreData.putDB(LoadingActivity.this, Constant.NOTIFI_KAIGUAN, jsonObject.getInt("notifi_kaiguan"));
            }
            if (jsonObject.has("deep_kaiguan")) {
                PreData.putDB(LoadingActivity.this, Constant.DEEP_KAIGUAN, jsonObject.getInt("deep_kaiguan"));
            }
            if (jsonObject.has("file_kaiguan")) {
                PreData.putDB(LoadingActivity.this, Constant.FILE_KAIGUAN, jsonObject.getInt("file_kaiguan"));
            }
            if (jsonObject.has("gboost_kaiguan")) {
                PreData.putDB(LoadingActivity.this, Constant.GBOOST_KAIGUAN, jsonObject.getInt("gboost_kaiguan"));
            }
            if (jsonObject.has("picture_kaiguan")) {
                PreData.putDB(LoadingActivity.this, Constant.PICTURE_KAIGUAN, jsonObject.getInt("picture_kaiguan"));
            }
        } catch (Exception e) {

        }
    }

    Runnable runnable1 = new Runnable() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public void run() {

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int a = Util.dp2px(360);
            Log.e("jfy", "px=" + a + "" + "=" + metrics.density + "=" + metrics.widthPixels);
            jumpTo(MainActivity.class);
            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 1000);

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