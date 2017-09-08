package com.supers.clean.junk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.clean.core.CleanManager;
import com.android.clean.util.LoadManager;
import com.android.clean.util.PreData;
import com.android.client.AndroidSdk;
import com.supers.clean.junk.R;
import com.supers.clean.junk.util.AdUtil;
import com.android.clean.util.Util;
import com.android.clean.util.Constant;
import com.android.clean.util.MemoryManager;
import com.android.clean.entity.JunkInfo;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by on 2017/2/6.
 */

public class UnloadActivity extends BaseActivity {
    ImageView iv_icon;
    TextView tv_size;
    Button bt_quxiao, bt_queren;
    ImageView iv_cha;
    LinearLayout ll_ad;
    long size;
    String path;

    private View nativeView;
    private String TAG_UNLOAD = "eos_unload";
    private ArrayList<JunkInfo> mngList;
    Handler myHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_unload);
        myHandler = new Handler();
        MyApplication cleanApplication = (MyApplication) getApplication();
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        tv_size = (TextView) findViewById(R.id.tv_size);
        bt_queren = (Button) findViewById(R.id.bt_queren);
        bt_quxiao = (Button) findViewById(R.id.bt_quxiao);
        iv_cha = (ImageView) findViewById(R.id.iv_cha);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
        boolean a = false;
        String packageName = getIntent().getStringExtra("packageName");
        if (TextUtils.equals(getPackageName(), packageName)) {
            finish();
            return;
        }
        AdUtil.track("卸载残余页面", "展示", "", 1);
        for (JunkInfo info : CleanManager.getInstance(this).getAppCaches()) {
            if (info.pkg.equals(packageName)) {
                a = true;
                iv_icon.setRotation(-10);

                iv_icon.setImageDrawable(LoadManager.getInstance(this).getAppIcon(info.pkg));
                size = info.size;
                path = info.path;
                tv_size.setText(Util.convertStorage(size, false));
                bt_queren.setOnClickListener(BtClickListener);
                bt_quxiao.setOnClickListener(BtClickListener);
                iv_cha.setOnClickListener(BtClickListener);
            }
        }
        if (!a) {
            mngList = CleanManager.getInstance(this).getAppList();
            if (mngList == null || mngList.size() == 0) {
                return;
            }
            for (JunkInfo info : mngList) {
                if (info.pkg.equals(packageName)) {
                    a = true;
                    iv_icon.setRotation(-10);

                    iv_icon.setImageDrawable(LoadManager.getInstance(this).getAppIcon(info.pkg));
                    size = (long) ((Math.random() * 1024 * 100) + 1024 * 10);
                    path = MemoryManager.getPhoneInSDCardPath() + "/Android/data/" + packageName;
                    tv_size.setText(Util.convertStorage(size, false));
                    bt_queren.setOnClickListener(BtClickListener);
                    bt_quxiao.setOnClickListener(BtClickListener);
                    iv_cha.setOnClickListener(BtClickListener);
                }
            }
        }
        if (!a) {
            finish();
            return;
        }
        addAd();
    }

    private void addAd() {
        if (PreData.getDB(this, Constant.FULL_UNLOAD, 0) == 1) {
            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AndroidSdk.showFullAd(AdUtil.DEFAULT);
                }
            }, 1000);

        } else {
            nativeView = AdUtil.getNativeAdView(TAG_UNLOAD, R.layout.native_ad_4);
            if (ll_ad != null && nativeView != null) {
                ViewGroup.LayoutParams layout_ad = ll_ad.getLayoutParams();
                if (nativeView.getHeight() == Util.dp2px(250)) {
                    layout_ad.height = Util.dp2px(250);
                }
                ll_ad.setLayoutParams(layout_ad);
                ll_ad.addView(nativeView);
            }
        }
    }

    View.OnClickListener BtClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_queren:
                    AdUtil.track("卸载残余页面", "点击清理", "", 1);
                    bt_queren.setOnClickListener(null);
                    Bundle bundle = new Bundle();
                    bundle.putLong("size", size);
                    Intent intent = new Intent(UnloadActivity.this, SuccessActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1);
                    deleteFiles(path);
                    finish();
                    break;
                case R.id.bt_quxiao:
                case R.id.iv_cha:
                    AdUtil.track("卸载残余页面", "点击取消", "", 1);
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        finish();
    }

    public void deleteFiles(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        deleteFiles(files[i].getAbsolutePath());
                    }
                }
                if (true) {
                    if (!file.isDirectory()) {
                        file.delete();
                    } else {
                        if (file.listFiles().length == 0) {
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
