package com.bruder.clean.activity;

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
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.bruder.clean.junk.R;
import com.bruder.clean.util.Constant;
import com.bruder.clean.util.PhoneManager;
import com.bruder.clean.util.ShortCutUtils;
import com.bruder.clean.util.UtilAd;
import com.cleaner.util.DataPre;
import com.cleaner.util.Util;

import org.json.JSONObject;

/**
 * Created by on 2017/3/8.
 */

public class LoadingActivity extends BaseActivity {
    TextView tv_tiaoguo;
    Handler myHandler;
    TextView loading_appname;
    ImageView loading_iv1, loading_iv2, loading_iv3, loading_iv4, loading_iv5, loading_iv6, loading_iv7,
            loading_iv8, loading_iv9, loading_iv10, loading_iv11, loading_iv12, loading_iv13;
    AnimatorSet animSet, animSet2, animSet3, animSet4;
    ObjectAnimator animator1, animator2, animator3, animator4, animator5, animator6, animator7, animator8,
            animator9, animator10, animator11, animator12, animator13;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading);
        //添加当前应用的桌面快捷方式
        ShortCutUtils.addShortcut(this);
        myHandler = new Handler();
        tv_tiaoguo.setVisibility(View.INVISIBLE);
        if (DataPre.getDB(this, Constant.ROOT_TRAK, true)) {
            UtilAd.track("是否获取root权限", PhoneManager.isRoot() == true ? "是" : "否", "", 1);
            DataPre.putDB(this, Constant.ROOT_TRAK, false);
            DataPre.putDB(this, Constant.KEY_CLEAN_TIME, System.currentTimeMillis());
        }
        myHandler.removeCallbacks(runnable1);
        myHandler.postDelayed(runnable1, 4000);
        loading_appname = (TextView) findViewById(R.id.loading_appname);
        loading_iv1 = (ImageView) findViewById(R.id.loading_iv1);
        loading_iv2 = (ImageView) findViewById(R.id.loading_iv2);
        loading_iv3 = (ImageView) findViewById(R.id.loading_iv3);
        loading_iv4 = (ImageView) findViewById(R.id.loading_iv4);
        loading_iv5 = (ImageView) findViewById(R.id.loading_iv5);
        loading_iv6 = (ImageView) findViewById(R.id.loading_iv6);
        loading_iv7 = (ImageView) findViewById(R.id.loading_iv7);
        loading_iv8 = (ImageView) findViewById(R.id.loading_iv8);
        loading_iv9 = (ImageView) findViewById(R.id.loading_iv9);
        loading_iv10 = (ImageView) findViewById(R.id.loading_iv10);
        loading_iv11 = (ImageView) findViewById(R.id.loading_iv11);
        loading_iv12 = (ImageView) findViewById(R.id.loading_iv12);
        loading_iv13 = (ImageView) findViewById(R.id.loading_iv13);

        animator1 = ObjectAnimator.ofFloat(loading_iv1, "alpha", 0f, 0.3f);
        animator2 = ObjectAnimator.ofFloat(loading_iv2, "alpha", 0f, 0.3f);
        animator5 = ObjectAnimator.ofFloat(loading_iv5, "alpha", 0f, 0.6f);
        animator4 = ObjectAnimator.ofFloat(loading_iv4, "alpha", 0f, 0.4f);
        ObjectAnimator animatory1 = ObjectAnimator.ofFloat(loading_iv1, "translationY", loading_iv1.getTranslationY(),
                loading_iv1.getTranslationY() - getResources().getDimensionPixelSize(R.dimen.d8));
        ObjectAnimator animatory2 = ObjectAnimator.ofFloat(loading_iv2, "translationY", loading_iv2.getTranslationY(),
                loading_iv2.getTranslationY() - getResources().getDimensionPixelSize(R.dimen.d8));
        ObjectAnimator animatory3 = ObjectAnimator.ofFloat(loading_iv5, "translationY", loading_iv5.getTranslationY(),
                loading_iv5.getTranslationY() - getResources().getDimensionPixelSize(R.dimen.d8));
        ObjectAnimator animatory4 = ObjectAnimator.ofFloat(loading_iv4, "translationY", loading_iv4.getTranslationY(),
                loading_iv4.getTranslationY() - getResources().getDimensionPixelSize(R.dimen.d8));
        animSet2 = new AnimatorSet();
        animSet2.setDuration(1000);
        animSet2.play(animator1).with(animator2).with(animator5).with(animator4).with(animatory1).with(animatory2).with(animatory3).with(animatory4);
        animSet2.start();

        animSet2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animator3 = ObjectAnimator.ofFloat(loading_iv3, "alpha", 0f, 0.5f);
                animator6 = ObjectAnimator.ofFloat(loading_iv6, "alpha", 0f, 1f);
                animator7 = ObjectAnimator.ofFloat(loading_iv7, "alpha", 0f, 0.7f);
                animator8 = ObjectAnimator.ofFloat(loading_iv8, "alpha", 0f, 0.3f);
                animSet3 = new AnimatorSet();
                animSet3.setDuration(1000);
                ObjectAnimator animatory1 = ObjectAnimator.ofFloat(loading_iv3, "translationY", loading_iv3.getTranslationY(),
                        loading_iv3.getTranslationY() - getResources().getDimensionPixelSize(R.dimen.d8));
                ObjectAnimator animatory2 = ObjectAnimator.ofFloat(loading_iv6, "translationY", loading_iv6.getTranslationY(),
                        loading_iv6.getTranslationY() - getResources().getDimensionPixelSize(R.dimen.d8));
                ObjectAnimator animatory3 = ObjectAnimator.ofFloat(loading_iv7, "translationY", loading_iv7.getTranslationY(),
                        loading_iv7.getTranslationY() - getResources().getDimensionPixelSize(R.dimen.d8));
                ObjectAnimator animatory4 = ObjectAnimator.ofFloat(loading_iv8, "translationY", loading_iv8.getTranslationY(),
                        loading_iv8.getTranslationY() - getResources().getDimensionPixelSize(R.dimen.d8));
                animSet3.play(animator3).with(animator6).with(animator7).with(animator8).with(animatory1).with(animatory2).with(animatory3).with(animatory4);
                animSet3.start();
                animSet3.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animator9 = ObjectAnimator.ofFloat(loading_iv9, "alpha", 0f, 0.3f);
                        animator10 = ObjectAnimator.ofFloat(loading_iv10, "alpha", 0f, 0.3f);
                        animator11 = ObjectAnimator.ofFloat(loading_iv11, "alpha", 0f, 0.3f);
                        animator12 = ObjectAnimator.ofFloat(loading_iv12, "alpha", 0f, 1f);
                        ObjectAnimator animatory1 = ObjectAnimator.ofFloat(loading_iv9, "translationY", loading_iv9.getTranslationY(),
                                loading_iv9.getTranslationY() - getResources().getDimensionPixelSize(R.dimen.d8));
                        ObjectAnimator animatory2 = ObjectAnimator.ofFloat(loading_iv10, "translationY", loading_iv10.getTranslationY(),
                                loading_iv10.getTranslationY() - getResources().getDimensionPixelSize(R.dimen.d8));
                        ObjectAnimator animatory3 = ObjectAnimator.ofFloat(loading_iv11, "translationY", loading_iv11.getTranslationY(),
                                loading_iv11.getTranslationY() - getResources().getDimensionPixelSize(R.dimen.d8));
                        ObjectAnimator animatory4 = ObjectAnimator.ofFloat(loading_iv12, "translationY", loading_iv12.getTranslationY(),
                                loading_iv12.getTranslationY() - getResources().getDimensionPixelSize(R.dimen.d8));
                        animSet4 = new AnimatorSet();
                        animSet4.setDuration(1000);
                        animSet4.play(animator9).with(animator10).with(animator11).with(animator12).with(animatory1).with(animatory2).with(animatory3).with(animatory4);
                        animSet4.start();

                        animSetAddListener(animSet4);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void animSetAddListener(AnimatorSet animSet5) {
        animSet5.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animator13 = ObjectAnimator.ofFloat(loading_iv13, "alpha", 0f, 1f);

                ObjectAnimator animator14 = ObjectAnimator.ofFloat(loading_appname, "alpha", 0f, 1f);

                ObjectAnimator animator15 = ObjectAnimator.ofFloat(loading_appname, "translationY", loading_appname.getTranslationY(),
                        loading_appname.getTranslationY() - getResources().getDimensionPixelSize(R.dimen.d8));

                animSet = new AnimatorSet();
                animSet.setDuration(1000);
                animSet.play(animator14).with(animator15).with(animator13);
                animSet.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    protected void findId() {
        super.findId();
        tv_tiaoguo = (TextView) findViewById(R.id.tv_tiaoguo);
    }


    @Override
    protected void onResume() {
        super.onResume();
        AndroidSdk.onResumeWithoutTransition(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    Runnable runnable1 = new Runnable() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public void run() {
            try {
                //解析json数据,AndroidSdk.getExtraData()获得default.json文件的所有数据字符串
                JSONObject jsonObject = new JSONObject(AndroidSdk.getExtraData());
                if (jsonObject.has("full_main")) {
                    DataPre.putDB(LoadingActivity.this, Constant.FULL_MAIN, jsonObject.getInt("full_main"));
                }
                if (jsonObject.has("full_start")) {
                    DataPre.putDB(LoadingActivity.this, Constant.FULL_START, jsonObject.getInt("full_start"));
                }
                if (jsonObject.has("full_exit")) {
                    DataPre.putDB(LoadingActivity.this, Constant.FULL_EXIT, jsonObject.getInt("full_exit"));
                }
                if (jsonObject.has("skip_time")) {
                    DataPre.putDB(LoadingActivity.this, Constant.SKIP_TIME, jsonObject.getInt("skip_time"));
                    Log.e("timeada", jsonObject.getInt("skip_time") + "==");
                }
                if (jsonObject.has("full_manager")) {
                    DataPre.putDB(LoadingActivity.this, Constant.FULL_MANAGER, jsonObject.getInt("full_manager"));
                }
                if (jsonObject.has("full_message")) {
                    DataPre.putDB(LoadingActivity.this, Constant.FULL_MESSAGE, jsonObject.getInt("full_message"));
                }
                if (jsonObject.has("full_success")) {
                    DataPre.putDB(LoadingActivity.this, Constant.FULL_SUCCESS, jsonObject.getInt("full_success"));
                }
                if (jsonObject.has("full_setting")) {
                    DataPre.putDB(LoadingActivity.this, Constant.FULL_SETTING, jsonObject.getInt("full_setting"));
                }
                if (jsonObject.has("full_unload")) {
                    DataPre.putDB(LoadingActivity.this, Constant.FULL_UNLOAD, jsonObject.getInt("full_unload"));
                }
                if (jsonObject.has("full_float")) {
                    DataPre.putDB(LoadingActivity.this, Constant.FULL_FLOAT, jsonObject.getInt("full_float"));
                }
                if (jsonObject.has("full_cool")) {
                    DataPre.putDB(LoadingActivity.this, Constant.FULL_COOL, jsonObject.getInt("full_cool"));
                }
                if (jsonObject.has("full_shortcut")) {
                    DataPre.putDB(LoadingActivity.this, Constant.FULL_SHORTCUT, jsonObject.getInt("full_shortcut"));
                }
                if (jsonObject.has("full_file")) {
                    DataPre.putDB(LoadingActivity.this, Constant.FULL_FILE, jsonObject.getInt("full_file"));
                }
                if (jsonObject.has("full_file_1")) {
                    DataPre.putDB(LoadingActivity.this, Constant.FULL_FILE_1, jsonObject.getInt("full_file_1"));
                }
                if (jsonObject.has("full_file_2")) {
                    DataPre.putDB(LoadingActivity.this, Constant.FULL_FILE_2, jsonObject.getInt("full_file_2"));
                }
                if (jsonObject.has("full_similar_photo")) {
                    DataPre.putDB(LoadingActivity.this, Constant.PICTURE, jsonObject.getInt("full_similar_photo"));
                }
                if (jsonObject.has("full_recyclebin")) {
                    DataPre.putDB(LoadingActivity.this, Constant.RECYCLEBIN, jsonObject.getInt("full_recyclebin"));
                }
                if (jsonObject.has("poweracativity")) {
                    DataPre.putDB(LoadingActivity.this, Constant.POWERACATIVITY, jsonObject.getInt("poweracativity"));
                }
                if (jsonObject.has("fileactivity")) {
                    DataPre.putDB(LoadingActivity.this, Constant.FILEACTIVITY, jsonObject.getInt("fileactivity"));
                }
                if (jsonObject.has("notifiactivity")) {
                    DataPre.putDB(LoadingActivity.this, Constant.NOTIFIACTIVITY, jsonObject.getInt("notifiactivity"));
                }
            } catch (Exception e) {

            }
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int a = Util.dp2px(360);
            Log.e("jfy", "px=" + a + "" + "=" + metrics.density + "=" + metrics.widthPixels);
            // 在jumpTo中实现界面跳转
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
}
