package com.bruder.clean.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.bruder.clean.junk.R;
import com.bruder.clean.util.Constant;
import com.bruder.clean.util.UtilAd;
import com.cleaner.filemanager.FileCategoryHelper;
import com.cleaner.filemanager.Util;
import com.cleaner.util.DataPre;

/**
 */

public class FilesActivity extends BaseActivity {
    FrameLayout title_left;
    TextView title_name;
    private FileCategoryHelper.CategoryInfo apkInfo, zipInfo, docInfo, musicInfo, videoInfo, pictureInfo, otherInfo;
    private Util.SDCardInfo sdCardInfo;
    private Handler mHandler;
    TextView file_apk_num, file_zip_num, file_txt_num, file_music_num, file_video_num, file_picture_num, file_qita_num;
    LinearLayout file_apk_button, file_zip_button, file_txt_button, file_music_button, file_video_button, file_picture_button, file_other_button;
    LinearLayout ll_ad;
    private String TAG_FILE = "bruder_file";//
    private FileCategoryHelper fileHelper;
    private View nativeView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_file);
        title_name.setText(R.string.side_file);
        fileHelper = new FileCategoryHelper(this);
        mHandler = new Handler();
        initAd();
        initData();
    }

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        file_apk_num = (TextView) findViewById(R.id.file_apk_num);
        file_zip_num = (TextView) findViewById(R.id.file_zip_num);
        file_txt_num = (TextView) findViewById(R.id.file_txt_num);
        file_music_num = (TextView) findViewById(R.id.file_music_num);
        file_video_num = (TextView) findViewById(R.id.file_video_num);
        file_picture_num = (TextView) findViewById(R.id.file_picture_num);
        file_qita_num = (TextView) findViewById(R.id.file_qita_num);
        file_apk_button = (LinearLayout) findViewById(R.id.file_apk_button);
        file_zip_button = (LinearLayout) findViewById(R.id.file_zip_button);
        file_txt_button = (LinearLayout) findViewById(R.id.file_txt_button);
        file_music_button = (LinearLayout) findViewById(R.id.file_music_button);
        file_video_button = (LinearLayout) findViewById(R.id.file_video_button);
        file_picture_button = (LinearLayout) findViewById(R.id.file_picture_button);
        file_other_button = (LinearLayout) findViewById(R.id.file_other_button);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
    }

    //ad
    private void initAd() {
        if (DataPre.getDB(this, Constant.FULL_FILE, 0) == 1) {
            AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
        } else {
            nativeView = UtilAd.getNativeAdView(TAG_FILE, R.layout.native_ad_4);
            if (ll_ad != null && nativeView != null) {
                ll_ad.addView(nativeView);
            }
        }
    }

    private void setListener() {
        title_left.setOnClickListener(clickListener);
        file_apk_button.setOnClickListener(clickListener);
        file_zip_button.setOnClickListener(clickListener);
        file_txt_button.setOnClickListener(clickListener);
        file_music_button.setOnClickListener(clickListener);
        file_video_button.setOnClickListener(clickListener);
        file_picture_button.setOnClickListener(clickListener);
        file_other_button.setOnClickListener(clickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initData() {
        sdCardInfo = Util.getSDCardInfo();
        if (sdCardInfo != null) {
//            file_sd_all.setText(Util.convertStorage(sdCardInfo.total));
//            file_sd_shengyu.setText(Util.convertStorage(sdCardInfo.free));
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                fileHelper.refreshCategoryInfo();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        apkInfo = fileHelper.getCategoryInfo(FileCategoryHelper.FileCategory.Apk);
                        zipInfo = fileHelper.getCategoryInfo(FileCategoryHelper.FileCategory.Zip);
                        docInfo = fileHelper.getCategoryInfo(FileCategoryHelper.FileCategory.Doc);
                        musicInfo = fileHelper.getCategoryInfo(FileCategoryHelper.FileCategory.Music);
                        videoInfo = fileHelper.getCategoryInfo(FileCategoryHelper.FileCategory.Video);
                        pictureInfo = fileHelper.getCategoryInfo(FileCategoryHelper.FileCategory.Picture);
                        otherInfo = fileHelper.getCategoryInfo(FileCategoryHelper.FileCategory.Other);
//                        file_apk_size.setText(Util.convertStorage(apkInfo.size));
//                        file_zip_size.setText(Util.convertStorage(zipInfo.size));
//                        file_txt_size.setText(Util.convertStorage(docInfo.size));
//                        file_music_size.setText(Util.convertStorage(musicInfo.size));
//                        file_video_size.setText(Util.convertStorage(videoInfo.size));
//                        file_other_size.setText(Util.convertStorage(otherInfo.size));
                        file_apk_num.setText(getString(R.string.file_num, apkInfo.count));
                        file_zip_num.setText(getString(R.string.file_num, zipInfo.count));
                        file_txt_num.setText(getString(R.string.file_num, docInfo.count));
                        file_music_num.setText(getString(R.string.file_num, musicInfo.count));
                        file_video_num.setText(getString(R.string.file_num, videoInfo.count));
                        file_picture_num.setText(getString(R.string.file_num, pictureInfo.count));
                        file_qita_num.setText(getString(R.string.file_num, otherInfo.count));
//                        file_piechart.setProgress(mHandler, apkInfo.size, zipInfo.size, docInfo.size, musicInfo.size, videoInfo.size, otherInfo.size);
                        setListener();
                    }
                });
            }
        }).start();
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
                    UtilAd.track("文件管理页面", "点击进入安装包页面", "", 1);
                    bundle.putString("name", "apk");
                    bundle.putInt("nameId", R.string.file_apk);
                    if (apkInfo.count == 0) {
                        bundle.putInt("count", 0);
                    }
                    jumpToActivity(FilesListActivity.class, bundle, 1);
                    break;
                case R.id.file_zip_button:
                    if (zipInfo.count == 0) {
                        bundle.putInt("count", 0);
                    }
                    UtilAd.track("文件管理页面", "点击进入压缩包页面", "", 1);
                    bundle.putString("name", "zip");
                    bundle.putInt("nameId", R.string.file_zip);
                    jumpToActivity(FilesListActivity.class, bundle, 1);
                    break;
                case R.id.file_txt_button:
                    if (docInfo.count == 0) {
                        bundle.putInt("count", 0);
                    }
                    UtilAd.track("文件管理页面", "点击进入文档页面", "", 1);
                    jumpToActivity(FilesDocActivity.class, 1);
                    break;
                case R.id.file_music_button:
                    if (musicInfo.count == 0) {
                        bundle.putInt("count", 0);
                    }
                    UtilAd.track("文件管理页面", "点击进入音乐页面", "", 1);
                    bundle.putString("name", "music");
                    bundle.putInt("nameId", R.string.file_music);
                    jumpToActivity(FilesListActivity.class, bundle, 1);
                    break;
                case R.id.file_video_button:
                    if (videoInfo.count == 0) {
                        bundle.putInt("count", 0);
                    }
                    UtilAd.track("文件管理页面", "点击进入视频页面", "", 1);
                    bundle.putString("name", "video");
                    bundle.putInt("nameId", R.string.file_video);
                    jumpToActivity(FilesListActivity.class, bundle, 1);
                    break;
                case R.id.file_picture_button:
                    if (pictureInfo.count == 0) {
                        bundle.putInt("count", 0);
                    }
                    UtilAd.track("文件管理页面", "点击进入图片页面", "", 1);
                    bundle.putString("name", "picture");
                    bundle.putInt("nameId", R.string.file_picture);
                    Log.e("ppp", "dadasdas");
                    jumpToActivity(FilesListActivity.class, bundle, 1);
                    break;
                case R.id.file_other_button:
                    UtilAd.track("文件管理页面", "点击进入其他页面", "", 1);
                    if (otherInfo.count == 0) {
                        bundle.putInt("count", 0);
                    }
                    bundle.putString("name", "other");
                    bundle.putInt("nameId", R.string.file_pther);
                    jumpToActivity(FilesListActivity.class, bundle, 1);
                    break;
            }
        }
    };

    private void setPieChat() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            initData();
        }
    }
}
