package com.easy.junk.easyactivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.easy.clean.easyutils.MyUtils;
import com.easy.clean.easyutils.PreData;
import com.android.client.AndroidSdk;
import com.easy.junk.R;
import com.easy.junk.easytools.EasyConstant;
import com.easy.junk.easytools.SetAdUtil;
import com.easy.junk.easytools.PhonesManager;
import com.easy.junk.easytools.ShortCutUtils;

import org.json.JSONObject;

/**
 * Created by on 2017/3/8.
 */

public class LoadingActivity extends BaseActivity {
    Handler myHandler;
    TextView loading_text_1, loading_text_2;
    private AnimatorSet animatorSet;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading);
        loading_text_1 = (TextView) findViewById(R.id.loading_text_1);
        loading_text_2 = (TextView) findViewById(R.id.loading_text_2);
        ShortCutUtils.addShortcut(this);
        myHandler = new Handler();
        if (PreData.getDB(this, EasyConstant.ROOT_TRAK, true)) {
            SetAdUtil.track("是否获取root权限", PhonesManager.isRoot() == true ? "是" : "否", "", 1);
            PreData.putDB(this, EasyConstant.ROOT_TRAK, false);
            PreData.putDB(this, EasyConstant.KEY_CLEAN_TIME, System.currentTimeMillis());
        }
        myHandler.removeCallbacks(runnable1);
        myHandler.postDelayed(runnable1, 2000);
        if (PreData.getDB(this, EasyConstant.FULL_START, 0) == 1) {
            AndroidSdk.loadFullAd("loading_full", null);
        }
        loading_text_1.setVisibility(View.INVISIBLE);
        loading_text_2.setAlpha(0);
        animatorSet = new AnimatorSet();

        myHandler.postDelayed(r, 500);
    }

    Runnable r = new Runnable() {
        @Override
        public void run() {
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(loading_text_1, View.TRANSLATION_Y, getResources().getDimensionPixelOffset(R.dimen.d20), 0);
            objectAnimator.setDuration(1000);
            ObjectAnimator objectAnimator_1 = ObjectAnimator.ofFloat(loading_text_2, View.TRANSLATION_Y, getResources().getDimensionPixelOffset(R.dimen.d20), 0);
            ObjectAnimator objectAnimator_2 = ObjectAnimator.ofFloat(loading_text_2, View.ALPHA, 0, 1);
            objectAnimator_1.setDuration(1000);
            objectAnimator_2.setDuration(1000);
            animatorSet.play(objectAnimator);
            animatorSet.play(objectAnimator_1).with(objectAnimator_2).after(500);
            animatorSet.start();
            loading_text_1.setVisibility(View.VISIBLE);
        }
    };

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
                    PreData.putDB(LoadingActivity.this, EasyConstant.POWERACTIVITY, jsonObject.getInt("poweractivity"));
                }
                if (jsonObject.has("fileactivity")) {
                    PreData.putDB(LoadingActivity.this, EasyConstant.FILEACTIVITY, jsonObject.getInt("fileactivity"));
                }
                if (jsonObject.has("notifiactivity")) {
                    PreData.putDB(LoadingActivity.this, EasyConstant.NOTIFIACTIVITY, jsonObject.getInt("notifiactivity"));
                }

                if (jsonObject.has("full_main")) {
                    PreData.putDB(LoadingActivity.this, EasyConstant.FULL_MAIN, jsonObject.getInt("full_main"));
                }
                if (jsonObject.has("full_start")) {
                    PreData.putDB(LoadingActivity.this, EasyConstant.FULL_START, jsonObject.getInt("full_start"));
                }
                if (jsonObject.has("full_exit")) {
                    PreData.putDB(LoadingActivity.this, EasyConstant.FULL_EXIT, jsonObject.getInt("full_exit"));
                }
                if (jsonObject.has("skip_time")) {
                    PreData.putDB(LoadingActivity.this, EasyConstant.SKIP_TIME, jsonObject.getInt("skip_time"));
                    Log.e("timeada", jsonObject.getInt("skip_time") + "==");
                }
                if (jsonObject.has("full_manager")) {
                    PreData.putDB(LoadingActivity.this, EasyConstant.FULL_MANAGER, jsonObject.getInt("full_manager"));
                }
                if (jsonObject.has("full_message")) {
                    PreData.putDB(LoadingActivity.this, EasyConstant.FULL_MESSAGE, jsonObject.getInt("full_message"));
                }
                if (jsonObject.has("full_success")) {
                    PreData.putDB(LoadingActivity.this, EasyConstant.FULL_SUCCESS, jsonObject.getInt("full_success"));
                }
                if (jsonObject.has("full_setting")) {
                    PreData.putDB(LoadingActivity.this, EasyConstant.FULL_SETTING, jsonObject.getInt("full_setting"));
                }
                if (jsonObject.has("full_unload")) {
                    PreData.putDB(LoadingActivity.this, EasyConstant.FULL_UNLOAD, jsonObject.getInt("full_unload"));
                }
                if (jsonObject.has("full_float")) {
                    PreData.putDB(LoadingActivity.this, EasyConstant.FULL_FLOAT, jsonObject.getInt("full_float"));
                }
                if (jsonObject.has("full_cool")) {
                    PreData.putDB(LoadingActivity.this, EasyConstant.FULL_COOL, jsonObject.getInt("full_cool"));
                }
                if (jsonObject.has("full_shortcut")) {
                    PreData.putDB(LoadingActivity.this, EasyConstant.FULL_SHORTCUT, jsonObject.getInt("full_shortcut"));
                }
                if (jsonObject.has("full_file")) {
                    PreData.putDB(LoadingActivity.this, EasyConstant.FULL_FILE, jsonObject.getInt("full_file"));
                }
                if (jsonObject.has("full_file_1")) {
                    PreData.putDB(LoadingActivity.this, EasyConstant.FULL_FILE_1, jsonObject.getInt("full_file_1"));
                }
                if (jsonObject.has("full_file_2")) {
                    PreData.putDB(LoadingActivity.this, EasyConstant.FULL_FILE_2, jsonObject.getInt("full_file_2"));
                }
                if (jsonObject.has("full_similar_photo")) {
                    PreData.putDB(LoadingActivity.this, EasyConstant.PICTURE, jsonObject.getInt("full_similar_photo"));
                }
                if (jsonObject.has("full_recyclebin")) {
                    PreData.putDB(LoadingActivity.this, EasyConstant.RECYCLEBIN, jsonObject.getInt("full_recyclebin"));
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
    protected void onResume() {
        super.onResume();
        AndroidSdk.onResumeWithoutTransition(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (myHandler != null) {
            myHandler.removeCallbacksAndMessages(null);
            myHandler.removeCallbacks(runnable1);
            myHandler.removeCallbacks(r);
        }
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        super.onDestroy();
    }

}
