package com.bruder.clean.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
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
        myHandler.postDelayed(runnable1, 2000);
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
