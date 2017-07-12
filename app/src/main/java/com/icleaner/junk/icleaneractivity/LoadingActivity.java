package com.icleaner.junk.icleaneractivity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;

import com.icleaner.clean.utils.MyUtils;
import com.icleaner.clean.utils.PreData;
import com.android.client.AndroidSdk;
import com.icleaner.junk.R;
import com.icleaner.junk.mytools.MyConstant;
import com.icleaner.junk.mytools.SetAdUtil;
import com.icleaner.junk.mytools.PhonesManager;
import com.icleaner.junk.mytools.ShortCutUtils;

import org.json.JSONObject;

/**
 * Created by on 2017/3/8.
 */

public class LoadingActivity extends BaseActivity {
    Handler myHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading);
        ShortCutUtils.addShortcut(this);
        myHandler = new Handler();
        if (PreData.getDB(this, MyConstant.ROOT_TRAK, true)) {
            SetAdUtil.track("是否获取root权限", PhonesManager.isRoot() == true ? "是" : "否", "", 1);
            PreData.putDB(this, MyConstant.ROOT_TRAK, false);
            PreData.putDB(this, MyConstant.KEY_CLEAN_TIME, System.currentTimeMillis());
        }
        myHandler.removeCallbacks(runnable1);
        myHandler.postDelayed(runnable1, 2000);
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
                    PreData.putDB(LoadingActivity.this, MyConstant.POWERACTIVITY, jsonObject.getInt("poweractivity"));
                }

                if (jsonObject.has("fileactivity")) {
                    PreData.putDB(LoadingActivity.this, MyConstant.FILEACTIVITY, jsonObject.getInt("fileactivity"));
                }

                if (jsonObject.has("notifiactivity")) {
                    PreData.putDB(LoadingActivity.this, MyConstant.NOTIFIACTIVITY, jsonObject.getInt("notifiactivity"));
                }

                if (jsonObject.has("goodgame")) {
                    PreData.putDB(LoadingActivity.this, MyConstant.GOODGAME, jsonObject.getInt("goodgame"));
                }

                if (jsonObject.has("picturex")) {
                    PreData.putDB(LoadingActivity.this, MyConstant.PICTUREX, jsonObject.getInt("picturex"));
                }

                if (jsonObject.has("full_main")) {
                    PreData.putDB(LoadingActivity.this, MyConstant.FULL_MAIN, jsonObject.getInt("full_main"));
                }
                if (jsonObject.has("full_start")) {
                    PreData.putDB(LoadingActivity.this, MyConstant.FULL_START, jsonObject.getInt("full_start"));
                }
                if (jsonObject.has("full_exit")) {
                    PreData.putDB(LoadingActivity.this, MyConstant.FULL_EXIT, jsonObject.getInt("full_exit"));
                }
                if (jsonObject.has("skip_time")) {
                    PreData.putDB(LoadingActivity.this, MyConstant.SKIP_TIME, jsonObject.getInt("skip_time"));
                    Log.e("timeada", jsonObject.getInt("skip_time") + "==");
                }
                if (jsonObject.has("full_manager")) {
                    PreData.putDB(LoadingActivity.this, MyConstant.FULL_MANAGER, jsonObject.getInt("full_manager"));
                }
                if (jsonObject.has("full_message")) {
                    PreData.putDB(LoadingActivity.this, MyConstant.FULL_MESSAGE, jsonObject.getInt("full_message"));
                }
                if (jsonObject.has("full_success")) {
                    PreData.putDB(LoadingActivity.this, MyConstant.FULL_SUCCESS, jsonObject.getInt("full_success"));
                }
                if (jsonObject.has("full_setting")) {
                    PreData.putDB(LoadingActivity.this, MyConstant.FULL_SETTING, jsonObject.getInt("full_setting"));
                }
                if (jsonObject.has("full_unload")) {
                    PreData.putDB(LoadingActivity.this, MyConstant.FULL_UNLOAD, jsonObject.getInt("full_unload"));
                }
                if (jsonObject.has("full_float")) {
                    PreData.putDB(LoadingActivity.this, MyConstant.FULL_FLOAT, jsonObject.getInt("full_float"));
                }
                if (jsonObject.has("full_cool")) {
                    PreData.putDB(LoadingActivity.this, MyConstant.FULL_COOL, jsonObject.getInt("full_cool"));
                }
                if (jsonObject.has("full_shortcut")) {
                    PreData.putDB(LoadingActivity.this, MyConstant.FULL_SHORTCUT, jsonObject.getInt("full_shortcut"));
                }
                if (jsonObject.has("full_file")) {
                    PreData.putDB(LoadingActivity.this, MyConstant.FULL_FILE, jsonObject.getInt("full_file"));
                }
                if (jsonObject.has("full_file_1")) {
                    PreData.putDB(LoadingActivity.this, MyConstant.FULL_FILE_1, jsonObject.getInt("full_file_1"));
                }
                if (jsonObject.has("full_file_2")) {
                    PreData.putDB(LoadingActivity.this, MyConstant.FULL_FILE_2, jsonObject.getInt("full_file_2"));
                }
                if (jsonObject.has("full_similar_photo")) {
                    PreData.putDB(LoadingActivity.this, MyConstant.PICTURE, jsonObject.getInt("full_similar_photo"));
                }
                if (jsonObject.has("full_recyclebin")) {
                    PreData.putDB(LoadingActivity.this, MyConstant.RECYCLEBIN, jsonObject.getInt("full_recyclebin"));
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
        myHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

}
