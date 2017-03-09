package com.supers.clean.junk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.supers.clean.junk.R;
import com.supers.clean.junk.modle.CommonUtil;
import com.supers.clean.junk.modle.MemoryManager;
import com.supers.clean.junk.modle.entity.JunkInfo;

import java.io.File;

/**
 * Created by Ivy on 2017/2/6.
 */

public class UnloadActivity extends BaseActivity {
    ImageView iv_icon;
    TextView tv_size;
    Button bt_quxiao, bt_queren;
    ImageView iv_cha;
    long size;
    String path;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_unload);
        MyApplication cleanApplication = (MyApplication) getApplication();
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        tv_size = (TextView) findViewById(R.id.tv_size);
        bt_queren = (Button) findViewById(R.id.bt_queren);
        bt_quxiao = (Button) findViewById(R.id.bt_quxiao);
        iv_cha = (ImageView) findViewById(R.id.iv_cha);
        boolean a = false;
        String packageName = getIntent().getStringExtra("packageName");
        AndroidSdk.track("卸载残余页面", "展示", "", 1);
        for (JunkInfo info : cleanApplication.getAppCache()) {
            if (info.packageName.equals(packageName)) {
                a = true;
                iv_icon.setRotation(-10);
                iv_icon.setImageDrawable(info.icon);
                size = info.size;
                path = info.path;
                tv_size.setText(CommonUtil.getFileSize2(size));
                bt_queren.setOnClickListener(BtClickListener);
                bt_quxiao.setOnClickListener(BtClickListener);
                iv_cha.setOnClickListener(BtClickListener);
            }
        }
        if (!a) {
            for (JunkInfo info : cleanApplication.getListMng()) {
                if (info.packageName.equals(packageName)) {
                    a = true;
                    iv_icon.setRotation(-10);
                    iv_icon.setImageDrawable(info.icon);
                    size = (long) ((Math.random() * 1024 * 100) + 1024 * 10);
                    path = MemoryManager.getPhoneInSDCardPath() + "/Android/data/" + packageName;
                    tv_size.setText(CommonUtil.getFileSize2(size));
                    bt_queren.setOnClickListener(BtClickListener);
                    bt_quxiao.setOnClickListener(BtClickListener);
                    iv_cha.setOnClickListener(BtClickListener);
                }
            }
        }
        if (!a) {
            finish();
        }

    }

    View.OnClickListener BtClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_queren:
                    AndroidSdk.track("卸载残余页面", "点击清理", "", 1);
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
                    AndroidSdk.track("卸载残余页面", "点击取消", "", 1);
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
