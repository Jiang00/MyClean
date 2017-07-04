package com.vector.cleaner.activity;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.vector.cleaner.utils.AdUtil;
import com.vector.cleaner.utils.Constant;
import com.vector.cleaner.utils.PhoneManager;
import com.vector.cleaner.utils.ShortCutUtils;
import com.vector.mcleaner.mutil.PreData;
import com.vector.mcleaner.mutil.Util;
import com.android.client.AndroidSdk;
import com.vector.cleaner.R;

import org.json.JSONObject;

/**
 * Created by on 2017/3/8.
 */

public class LoadingActivity extends BaseActivity {
    private ObjectAnimator rotationY;
    Handler myHandler;
    ImageView icon;

    @Override
    protected void findId() {
        super.findId();
        icon = (ImageView) findViewById(R.id.icon);
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
        rotationY = ObjectAnimator.ofFloat(icon, "rotationY", 0, 90, 0, -90, 0);
        rotationY.setDuration(1800);
        rotationY.setInterpolator(new LinearInterpolator());
        rotationY.start();
        myHandler.removeCallbacks(runnable1);
        myHandler.postDelayed(runnable1, 2000);
    }

    Runnable runnable1 = new Runnable() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public void run() {
            rotationY.cancel();
            icon.clearAnimation();
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
                if (jsonObject.has("file_kaiguan")) {
                    PreData.putDB(LoadingActivity.this, Constant.FILE_KAIGUAN, jsonObject.getInt("file_kaiguan"));
                }
                if (jsonObject.has("photo_kaiguan")) {
                    PreData.putDB(LoadingActivity.this, Constant.PHOTO_KAIGUAN, jsonObject.getInt("photo_kaiguan"));
                }
            } catch (Exception e) {

            }
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
