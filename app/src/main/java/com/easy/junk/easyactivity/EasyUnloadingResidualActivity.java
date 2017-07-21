package com.easy.junk.easyactivity;

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

import com.android.client.AndroidSdk;
import com.easy.clean.core.CleanManager;
import com.easy.clean.easyutils.MemoryManager;
import com.easy.clean.easyutils.MyUtils;
import com.easy.clean.easyutils.PreData;
import com.easy.clean.entity.JunkInfo;
import com.easy.junk.R;
import com.easy.junk.easytools.EasyConstant;
import com.easy.junk.easytools.SetAdUtil;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by on 2017/2/6.
 */

public class EasyUnloadingResidualActivity extends BaseActivity {
    private String TAG_UNLOAD = "cleanmobi_unload";
    TextView tv_size;
    Button bt_quxiao, bt_queren;
    private View nativeView;
    Handler myHandler;
    long size;
    String path;
    ImageView iv_cha;
    LinearLayout ll_ad;
    private ArrayList<JunkInfo> mngList;

    View.OnClickListener BtClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_queren:
                    SetAdUtil.track("卸载残余页面", "点击清理", "", 1);
                    bt_queren.setOnClickListener(null);
                    Bundle bundle = new Bundle();
                    bundle.putLong("size", size);
                    bundle.putString("from", "unloading");
                    Intent intent = new Intent(EasyUnloadingResidualActivity.this, EasySucceedActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1);
                    deleteFiles(path);
                    finish();
                    break;
                case R.id.bt_quxiao:
                case R.id.iv_cha:
                    SetAdUtil.track("卸载残余页面", "点击取消", "", 1);
                    finish();
                    break;
            }
        }
    };

    private void addAd() {
        if (PreData.getDB(this, EasyConstant.FULL_UNLOAD, 0) == 1) {
            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
                }
            }, 1000);

        } else {
            nativeView = SetAdUtil.getNativeAdView(TAG_UNLOAD, R.layout.native_ad_4);
            if (ll_ad != null && nativeView != null) {
                ViewGroup.LayoutParams layout_ad = ll_ad.getLayoutParams();
                if (nativeView.getHeight() == MyUtils.dp2px(250)) {
                    layout_ad.height = MyUtils.dp2px(250);
                }
                ll_ad.setLayoutParams(layout_ad);
                ll_ad.addView(nativeView);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_unload);
        myHandler = new Handler();
        MyApplication cleanApplication = (MyApplication) getApplication();
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
        SetAdUtil.track("卸载残余页面", "展示", "", 1);
        for (JunkInfo info : CleanManager.getInstance(this).getAppCaches()) {
            if (info.pkg.equals(packageName)) {
                a = true;
                size = info.size;
                path = info.path;
                tv_size.setText(MyUtils.convertStorage(size, false));
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
                    size = (long) ((Math.random() * 1024 * 100) + 1024 * 10);
                    path = MemoryManager.getPhoneInSDCardPath() + "/Android/data/" + packageName;
                    tv_size.setText(MyUtils.convertStorage(size, false));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        finish();
    }
}
