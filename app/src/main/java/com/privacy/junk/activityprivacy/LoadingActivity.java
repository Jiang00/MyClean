package com.privacy.junk.activityprivacy;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.privacy.clean.utilsprivacy.MyUtils;
import com.privacy.clean.utilsprivacy.PreData;
import com.privacy.junk.R;
import com.privacy.junk.toolsprivacy.MyConstantPrivacy;
import com.privacy.junk.toolsprivacy.PhonesManager;
import com.privacy.junk.toolsprivacy.PrivacyShortCutUtils;
import com.privacy.junk.toolsprivacy.SetAdUtilPrivacy;

import org.json.JSONObject;

/**
 * Created by on 2017/3/8.
 */

public class LoadingActivity extends BaseActivity {
    Handler myHandler;
    TextView loading_tv1, loading_tv2;
    AnimatorSet animSet, animSet2;
    ObjectAnimator animator1;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading);
        PrivacyShortCutUtils.addShortcut(this);
        loading_tv1 = (TextView) findViewById(R.id.loading_tv1);
        loading_tv2 = (TextView) findViewById(R.id.loading_tv2);
        myHandler = new Handler();
        if (PreData.getDB(this, MyConstantPrivacy.ROOT_TRAK, true)) {
            SetAdUtilPrivacy.track("是否获取root权限", PhonesManager.isRoot() == true ? "是" : "否", "", 1);
            PreData.putDB(this, MyConstantPrivacy.ROOT_TRAK, false);
            PreData.putDB(this, MyConstantPrivacy.KEY_CLEAN_TIME, System.currentTimeMillis());
        }
        ObjectAnimator animator = ObjectAnimator.ofFloat(loading_tv1, "alpha", 0f, 1f);
        animator1 = ObjectAnimator.ofFloat(loading_tv2, "alpha", 0f, 1f);

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(loading_tv1, "translationY", loading_tv1.getTranslationY(),
                loading_tv1.getTranslationY() - getResources().getDimensionPixelSize(R.dimen.d4));

        animSet = new AnimatorSet();
        animSet.setDuration(1000);
        animSet.play(animator).with(animator2);
        animSet.start();
        animSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ObjectAnimator animator3 = ObjectAnimator.ofFloat(loading_tv2, "translationY", loading_tv2.getTranslationY(),
                        loading_tv2.getTranslationY() - getResources().getDimensionPixelSize(R.dimen.d4));

                animSet2 = new AnimatorSet();
                animSet2.setDuration(1000);
                animSet2.play(animator1).with(animator3);
                animSet2.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        myHandler.removeCallbacks(runnable1);
        myHandler.postDelayed(runnable1, 2500);
        if (PreData.getDB(this, MyConstantPrivacy.FULL_START, 0) == 1) {
            AndroidSdk.loadFullAd("loading_full", null);
        }
    }

    @Override
    protected void findId() {
        super.findId();
    }

    Runnable runnable1 = new Runnable() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public void run() {
            try {
                JSONObject jsonObject = new JSONObject(AndroidSdk.getExtraData());
                if (jsonObject.has("poweractivity")) {
                    PreData.putDB(LoadingActivity.this, MyConstantPrivacy.POWERACTIVITY, jsonObject.getInt("poweractivity"));
                }
                if (jsonObject.has("fileactivity")) {
                    PreData.putDB(LoadingActivity.this, MyConstantPrivacy.FILEACTIVITY, jsonObject.getInt("fileactivity"));
                }
                if (jsonObject.has("notifiactivity")) {
                    PreData.putDB(LoadingActivity.this, MyConstantPrivacy.NOTIFIACTIVITY, jsonObject.getInt("notifiactivity"));
                }
                if (jsonObject.has("goodgame")) {
                    PreData.putDB(LoadingActivity.this, MyConstantPrivacy.GOODGAME, jsonObject.getInt("goodgame"));
                }
                if (jsonObject.has("picturex")) {
                    PreData.putDB(LoadingActivity.this, MyConstantPrivacy.PICTUREX, jsonObject.getInt("picturex"));
                }

                if (jsonObject.has("full_main")) {
                    PreData.putDB(LoadingActivity.this, MyConstantPrivacy.FULL_MAIN, jsonObject.getInt("full_main"));
                }
                if (jsonObject.has("full_start")) {
                    PreData.putDB(LoadingActivity.this, MyConstantPrivacy.FULL_START, jsonObject.getInt("full_start"));
                }
                if (jsonObject.has("full_exit")) {
                    PreData.putDB(LoadingActivity.this, MyConstantPrivacy.FULL_EXIT, jsonObject.getInt("full_exit"));
                }
                if (jsonObject.has("show_exit_native")) {
                    PreData.putDB(LoadingActivity.this, MyConstantPrivacy.FULL_EXIT_NATIVE, jsonObject.getInt("show_exit_native"));
                }
                if (jsonObject.has("clean_result_native")) {
                    PreData.putDB(LoadingActivity.this, MyConstantPrivacy.FULL_RESUTL, jsonObject.getInt("clean_result_native"));
                }
                if (jsonObject.has("skip_time")) {
                    PreData.putDB(LoadingActivity.this, MyConstantPrivacy.SKIP_TIME, jsonObject.getInt("skip_time"));
                    Log.e("timeada", jsonObject.getInt("skip_time") + "==");
                }
                if (jsonObject.has("full_manager")) {
                    PreData.putDB(LoadingActivity.this, MyConstantPrivacy.FULL_MANAGER, jsonObject.getInt("full_manager"));
                }
                if (jsonObject.has("full_message")) {
                    PreData.putDB(LoadingActivity.this, MyConstantPrivacy.FULL_MESSAGE, jsonObject.getInt("full_message"));
                }
                if (jsonObject.has("full_success")) {
                    PreData.putDB(LoadingActivity.this, MyConstantPrivacy.FULL_SUCCESS, jsonObject.getInt("full_success"));
                }
                if (jsonObject.has("full_setting")) {
                    PreData.putDB(LoadingActivity.this, MyConstantPrivacy.FULL_SETTING, jsonObject.getInt("full_setting"));
                }
                if (jsonObject.has("full_unload")) {
                    PreData.putDB(LoadingActivity.this, MyConstantPrivacy.FULL_UNLOAD, jsonObject.getInt("full_unload"));
                }
                if (jsonObject.has("full_float")) {
                    PreData.putDB(LoadingActivity.this, MyConstantPrivacy.FULL_FLOAT, jsonObject.getInt("full_float"));
                }
                if (jsonObject.has("full_cool")) {
                    PreData.putDB(LoadingActivity.this, MyConstantPrivacy.FULL_COOL, jsonObject.getInt("full_cool"));
                }
                if (jsonObject.has("full_shortcut")) {
                    PreData.putDB(LoadingActivity.this, MyConstantPrivacy.FULL_SHORTCUT, jsonObject.getInt("full_shortcut"));
                }
                if (jsonObject.has("full_file")) {
                    PreData.putDB(LoadingActivity.this, MyConstantPrivacy.FULL_FILE, jsonObject.getInt("full_file"));
                }
                if (jsonObject.has("full_file_1")) {
                    PreData.putDB(LoadingActivity.this, MyConstantPrivacy.FULL_FILE_1, jsonObject.getInt("full_file_1"));
                }
                if (jsonObject.has("full_file_2")) {
                    PreData.putDB(LoadingActivity.this, MyConstantPrivacy.FULL_FILE_2, jsonObject.getInt("full_file_2"));
                }
                if (jsonObject.has("full_similar_photo")) {
                    PreData.putDB(LoadingActivity.this, MyConstantPrivacy.PICTURE, jsonObject.getInt("full_similar_photo"));
                }
                if (jsonObject.has("full_recyclebin")) {
                    PreData.putDB(LoadingActivity.this, MyConstantPrivacy.RECYCLEBIN, jsonObject.getInt("full_recyclebin"));
                }
            } catch (Exception e) {

            }
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int a = MyUtils.dp2px(360);
            Log.e("jfy", "px=" + a + "" + "=" + metrics.density + "=" + metrics.widthPixels);
            jumpTo(MainActivity.class);
            finish();
        }
    };


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        myHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AndroidSdk.onResumeWithoutTransition(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (animSet != null) {
            animSet.pause();
        }
    }
}
