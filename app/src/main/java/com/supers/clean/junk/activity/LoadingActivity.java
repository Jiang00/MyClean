package com.supers.clean.junk.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.eos.eshop.ShopMaster;
import com.supers.clean.junk.R;
import com.supers.clean.junk.util.CommonUtil;
import com.supers.clean.junk.util.Constant;
import com.supers.clean.junk.util.PhoneManager;
import com.supers.clean.junk.util.PreData;

/**
 * Created by on 2017/3/8.
 */

public class LoadingActivity extends BaseActivity {
    Handler myHandler;
    TextView tv_tiaoguo;

    @Override
    protected void findId() {
        super.findId();
        tv_tiaoguo = (TextView) findViewById(R.id.tv_tiaoguo);
        // ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        AndroidSdk.Builder builder = new AndroidSdk.Builder();
        AndroidSdk.onCreate(this, builder);
        ShopMaster.onCreate(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading);
//        ShortCutUtils.addShortcut(this);
        myHandler = new Handler();
        tv_tiaoguo.setVisibility(View.INVISIBLE);
        if (PreData.getDB(this, Constant.ROOT_TRAK, true)) {
            CommonUtil.track("是否获取root权限", PhoneManager.isRoot() == true ? "是" : "否", "", 1);
            CommonUtil.track("是否安装applock", CommonUtil.isPkgInstalled("com.eosmobi.applock", getPackageManager()) == true ? "是" : "否", "", 1);
            PreData.putDB(this, Constant.ROOT_TRAK, false);
            PreData.putDB(this, Constant.KEY_CLEAN_TIME, System.currentTimeMillis());
        }
        myHandler.removeCallbacks(runnable1);
        myHandler.postDelayed(runnable1, 2000);
    }

    Runnable runnable1 = new Runnable() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public void run() {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int a = CommonUtil.dp2px(360);
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
