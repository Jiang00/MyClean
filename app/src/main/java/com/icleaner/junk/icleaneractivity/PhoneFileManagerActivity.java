package com.icleaner.junk.icleaneractivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.icleaner.clean.filemanager.PhoneFileCategoryHelper;
import com.icleaner.clean.filemanager.Util;
import com.icleaner.clean.utils.PreData;
import com.android.client.AndroidSdk;
import com.icleaner.junk.R;
import com.icleaner.junk.mytools.SetAdUtil;
import com.icleaner.junk.mytools.MyConstant;

/**
 * Created by on 2017/4/20.
 */

public class PhoneFileManagerActivity extends BaseActivity {
    LinearLayout file_apk_button, file_zip_button, file_txt_button, file_music_button, file_video_button, file_other_button;
    TextView file_apk_num, file_zip_num, file_txt_num, file_music_num, file_video_num, file_qita_num;
    TextView file_apk_size, file_zip_size, file_txt_size, file_music_size, file_video_size, file_qita_size;
    LinearLayout ll_ad;
    FrameLayout title_left;
    private String TAG_FILE = "icleaner_file";
    private PhoneFileCategoryHelper fileHelper;
    TextView title_name;
    private PhoneFileCategoryHelper.CategoryInfo apkInfo, zipInfo, docInfo, musicInfo, videoInfo, otherInfo, pictureInfo, logInfo;
    private Handler mHandler;
    private View nativeView;


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                fileHelper.refreshCategoryInfo();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        apkInfo = fileHelper.getCategoryInfo(PhoneFileCategoryHelper.FileCategory.Apk);
                        zipInfo = fileHelper.getCategoryInfo(PhoneFileCategoryHelper.FileCategory.Zip);
                        docInfo = fileHelper.getCategoryInfo(PhoneFileCategoryHelper.FileCategory.Doc);
                        musicInfo = fileHelper.getCategoryInfo(PhoneFileCategoryHelper.FileCategory.Music);
                        videoInfo = fileHelper.getCategoryInfo(PhoneFileCategoryHelper.FileCategory.Video);
                        pictureInfo = fileHelper.getCategoryInfo(PhoneFileCategoryHelper.FileCategory.Picture);
                        logInfo = fileHelper.getCategoryInfo(PhoneFileCategoryHelper.FileCategory.Log);
                        otherInfo = fileHelper.getCategoryInfo(PhoneFileCategoryHelper.FileCategory.Other);
                        file_apk_num.setText(getString(R.string.file_num, apkInfo.count));
//                        file_zip_num.setText(getString(R.string.file_num, zipInfo.count));
                        file_txt_num.setText(getString(R.string.file_num, docInfo.count));
                        file_music_num.setText(getString(R.string.file_num, musicInfo.count));
                        file_video_num.setText(getString(R.string.file_num, videoInfo.count));
                        file_qita_num.setText(getString(R.string.file_num, otherInfo.count));
                        file_zip_size.setText(Util.convertStorage(zipInfo.size));
                        file_apk_size.setText(Util.convertStorage(apkInfo.size));
                        file_txt_size.setText(Util.convertStorage(docInfo.size));
                        file_music_size.setText(Util.convertStorage(musicInfo.size));
                        file_video_size.setText(Util.convertStorage(videoInfo.size));
                        file_qita_size.setText(Util.convertStorage(otherInfo.size));
                        setListener();
                    }
                });
            }
        }).start();
    }

    private void initAd() {

        if (PreData.getDB(this, MyConstant.FULL_FILE, 0) == 1) {
            AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
        } else {
            nativeView = SetAdUtil.getNativeAdView(TAG_FILE, R.layout.native_ad_4);
            if (ll_ad != null && nativeView != null) {
                ll_ad.addView(nativeView);
            }
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_file);
        title_name.setText(R.string.side_file);
        fileHelper = new PhoneFileCategoryHelper(this);
        mHandler = new Handler();
        initAd();
        initData();
    }

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
//        file_zip_num = (TextView) findViewById(R.id.file_zip_num);
        file_apk_num = (TextView) findViewById(R.id.file_apk_num);
        file_txt_num = (TextView) findViewById(R.id.file_txt_num);
        file_music_num = (TextView) findViewById(R.id.file_music_num);
        file_video_num = (TextView) findViewById(R.id.file_video_num);
        file_qita_num = (TextView) findViewById(R.id.file_qita_num);
        file_zip_size = (TextView) findViewById(R.id.file_zip_size);
        file_apk_size = (TextView) findViewById(R.id.file_apk_size);
        file_txt_size = (TextView) findViewById(R.id.file_txt_size);
        file_music_size = (TextView) findViewById(R.id.file_music_size);
        file_video_size = (TextView) findViewById(R.id.file_video_size);
        file_qita_size = (TextView) findViewById(R.id.file_qita_size);
        file_apk_button = (LinearLayout) findViewById(R.id.file_apk_button);
        file_zip_button = (LinearLayout) findViewById(R.id.file_zip_button);
        file_txt_button = (LinearLayout) findViewById(R.id.file_txt_button);
        file_music_button = (LinearLayout) findViewById(R.id.file_music_button);
        file_video_button = (LinearLayout) findViewById(R.id.file_video_button);
        file_other_button = (LinearLayout) findViewById(R.id.file_other_button);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            switch (v.getId()) {
                case R.id.title_left:
                    onBackPressed();
                    break;
                case R.id.file_apk_button:
                    SetAdUtil.track("文件管理页面", "点击进入安装包页面", "", 1);
                    bundle.putString("name", "apk");
                    bundle.putInt("nameId", R.string.file_apk);
                    if (apkInfo.count == 0) {
                        bundle.putInt("count", 0);
                    }
                    jumpToActivity(PhoneFileActivity.class, bundle, 1);
                    break;
                case R.id.file_zip_button:
                    if (zipInfo.count == 0) {
                        bundle.putInt("count", 0);
                    }
                    SetAdUtil.track("文件管理页面", "点击进入压缩包页面", "", 1);
                    bundle.putString("name", "zip");
                    bundle.putInt("nameId", R.string.file_zip);
                    jumpToActivity(PhoneFileActivity.class, bundle, 1);
                    break;
                case R.id.file_txt_button:
                    if (docInfo.count == 0) {
                        bundle.putInt("count", 0);
                    }
                    SetAdUtil.track("文件管理页面", "点击进入文档页面", "", 1);
                    jumpToActivity(PhoneDocActivity.class, 1);
                    break;
                case R.id.file_music_button:
                    if (musicInfo.count == 0) {
                        bundle.putInt("count", 0);
                    }
                    SetAdUtil.track("文件管理页面", "点击进入音乐页面", "", 1);
                    bundle.putString("name", "music");
                    bundle.putInt("nameId", R.string.file_music);
                    jumpToActivity(PhoneFileActivity.class, bundle, 1);
                    break;
                case R.id.file_video_button:
                    if (videoInfo.count == 0) {
                        bundle.putInt("count", 0);
                    }
                    SetAdUtil.track("文件管理页面", "点击进入视频页面", "", 1);
                    bundle.putString("name", "video");
                    bundle.putInt("nameId", R.string.file_video);
                    jumpToActivity(PhoneFileActivity.class, bundle, 1);
                    break;
                case R.id.file_other_button:
                    SetAdUtil.track("文件管理页面", "点击进入其他页面", "", 1);
                    if (otherInfo.count == 0) {
                        bundle.putInt("count", 0);
                    }
                    bundle.putString("name", "other");
                    bundle.putInt("nameId", R.string.file_pther);
                    jumpToActivity(PhoneFileActivity.class, bundle, 1);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            initData();
        }
    }

    private void setListener() {
        title_left.setOnClickListener(clickListener);
        file_apk_button.setOnClickListener(clickListener);
        file_zip_button.setOnClickListener(clickListener);
        file_txt_button.setOnClickListener(clickListener);
        file_music_button.setOnClickListener(clickListener);
        file_video_button.setOnClickListener(clickListener);
        file_other_button.setOnClickListener(clickListener);
    }

}
