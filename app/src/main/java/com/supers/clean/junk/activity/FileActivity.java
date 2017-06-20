package com.supers.clean.junk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.clean.filemanager.FileCategoryHelper;
import com.android.clean.filemanager.Util;
import com.android.clean.util.PreData;
import com.android.client.AndroidSdk;
import com.supers.clean.junk.R;
import com.supers.clean.junk.customeview.FileLineView;
import com.supers.clean.junk.util.AdUtil;
import com.supers.clean.junk.util.Constant;

/**
 * Created by Ivy on 2017/4/20.
 */

public class FileActivity extends BaseActivity {
    FrameLayout title_left;
    TextView title_name;
    TextView file_sd_kong;
    FileLineView file_piechart;
    TextView file_apk_size, file_zip_size, file_txt_size, file_music_size, file_video_size, file_other_size;
    TextView file_apk_num, file_zip_num, file_txt_num, file_music_num, file_video_num, file_other_num;
    LinearLayout file_apk_button, file_zip_button, file_txt_button, file_music_button, file_video_button, file_other_button;
    LinearLayout file_apk_null, file_zip_null, file_txt_null, file_music_null, file_video_null, file_other_null;
    LinearLayout file_apk, file_zip, file_txt, file_music, file_video, file_other;
    LinearLayout ll_ad;
    private View nativeView;

    private String TAG_FILE = "eos_file";
    private FileCategoryHelper fileHelper;
    private FileCategoryHelper.CategoryInfo apkInfo, zipInfo, docInfo, musicInfo, videoInfo, otherInfo;
    private com.android.clean.filemanager.Util.SDCardInfo sdCardInfo;
    private Handler mHandler;

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        file_sd_kong = (TextView) findViewById(R.id.file_sd_kong);
        file_piechart = (FileLineView) findViewById(R.id.file_piechart);
        file_apk_size = (TextView) findViewById(R.id.file_apk_size);
        file_zip_size = (TextView) findViewById(R.id.file_zip_size);
        file_txt_size = (TextView) findViewById(R.id.file_txt_size);
        file_music_size = (TextView) findViewById(R.id.file_music_size);
        file_video_size = (TextView) findViewById(R.id.file_video_size);
        file_other_size = (TextView) findViewById(R.id.file_other_size);
        file_apk_num = (TextView) findViewById(R.id.file_apk_num);
        file_zip_num = (TextView) findViewById(R.id.file_zip_num);
        file_txt_num = (TextView) findViewById(R.id.file_txt_num);
        file_music_num = (TextView) findViewById(R.id.file_music_num);
        file_video_num = (TextView) findViewById(R.id.file_video_num);
        file_other_num = (TextView) findViewById(R.id.file_other_num);
        file_apk_button = (LinearLayout) findViewById(R.id.file_apk_button);
        file_zip_button = (LinearLayout) findViewById(R.id.file_zip_button);
        file_txt_button = (LinearLayout) findViewById(R.id.file_txt_button);
        file_music_button = (LinearLayout) findViewById(R.id.file_music_button);
        file_video_button = (LinearLayout) findViewById(R.id.file_video_button);
        file_other_button = (LinearLayout) findViewById(R.id.file_other_button);
        file_apk_null = (LinearLayout) findViewById(R.id.file_apk_null);
        file_zip_null = (LinearLayout) findViewById(R.id.file_zip_null);
        file_txt_null = (LinearLayout) findViewById(R.id.file_txt_null);
        file_music_null = (LinearLayout) findViewById(R.id.file_music_null);
        file_video_null = (LinearLayout) findViewById(R.id.file_video_null);
        file_other_null = (LinearLayout) findViewById(R.id.file_other_null);
        file_apk = (LinearLayout) findViewById(R.id.file_apk);
        file_zip = (LinearLayout) findViewById(R.id.file_zip);
        file_txt = (LinearLayout) findViewById(R.id.file_txt);
        file_music = (LinearLayout) findViewById(R.id.file_music);
        file_video = (LinearLayout) findViewById(R.id.file_video);
        file_other = (LinearLayout) findViewById(R.id.file_other);

        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
    }

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
    protected void onResume() {
        super.onResume();


    }

    private void initData() {
        sdCardInfo = Util.getSDCardInfo();
        if (sdCardInfo != null) {
//            file_sd_all.setText(Util.convertStorage(sdCardInfo.total));
//            file_sd_shengyu.setText(Util.convertStorage(sdCardInfo.free));
            file_sd_kong.setText(sdCardInfo.free * 100 / sdCardInfo.total + "%");
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
                        otherInfo = fileHelper.getCategoryInfo(FileCategoryHelper.FileCategory.Other);
                        file_apk_size.setText(Util.convertStorage(apkInfo.size));
                        file_zip_size.setText(Util.convertStorage(zipInfo.size));
                        file_txt_size.setText(Util.convertStorage(docInfo.size));
                        file_music_size.setText(Util.convertStorage(musicInfo.size));
                        file_video_size.setText(Util.convertStorage(videoInfo.size));
                        file_other_size.setText(Util.convertStorage(otherInfo.size));
                        if (apkInfo.count == 0) {
                            file_apk.setVisibility(View.GONE);
                            file_apk_null.setVisibility(View.VISIBLE);
                        } else {
                            file_apk.setVisibility(View.VISIBLE);
                            file_apk_null.setVisibility(View.GONE);
                            file_apk_num.setText(apkInfo.count + getString(R.string.file_apk));
                        }
                        if (zipInfo.count == 0) {
                            file_zip.setVisibility(View.GONE);
                            file_zip_null.setVisibility(View.VISIBLE);
                        } else {
                            file_zip.setVisibility(View.VISIBLE);
                            file_zip_null.setVisibility(View.GONE);
                            file_zip_num.setText(zipInfo.count + getString(R.string.file_zip));
                        }
                        if (docInfo.count == 0) {
                            file_txt.setVisibility(View.GONE);
                            file_txt_null.setVisibility(View.VISIBLE);
                        } else {
                            file_txt.setVisibility(View.VISIBLE);
                            file_txt_null.setVisibility(View.GONE);
                            file_txt_num.setText(docInfo.count + getString(R.string.file_txt));
                        }
                        if (musicInfo.count == 0) {
                            file_music.setVisibility(View.GONE);
                            file_music_null.setVisibility(View.VISIBLE);
                        } else {
                            file_music.setVisibility(View.VISIBLE);
                            file_music_null.setVisibility(View.GONE);
                            file_music_num.setText(musicInfo.count + getString(R.string.file_music));
                        }
                        if (videoInfo.count == 0) {
                            file_video.setVisibility(View.GONE);
                            file_video_null.setVisibility(View.VISIBLE);
                        } else {
                            file_video.setVisibility(View.VISIBLE);
                            file_video_null.setVisibility(View.GONE);
                            file_video_num.setText(videoInfo.count + getString(R.string.file_video));
                        }
                        if (otherInfo.count == 0) {
                            file_other.setVisibility(View.GONE);
                            file_other_null.setVisibility(View.VISIBLE);
                        } else {
                            file_other.setVisibility(View.VISIBLE);
                            file_other_null.setVisibility(View.GONE);
                            file_other_num.setText(otherInfo.count + getString(R.string.file_other));
                        }
                        file_piechart.setProgress(mHandler, apkInfo.size, zipInfo.size, docInfo.size, musicInfo.size, videoInfo.size, otherInfo.size);
                        setListener();
                    }
                });
            }
        }).start();
    }

    private void initAd() {

        if (PreData.getDB(this, Constant.FULL_FILE, 0) == 1) {
            AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
        } else {
            nativeView = AdUtil.getNativeAdView(TAG_FILE, R.layout.native_ad_4);
            if (ll_ad != null && nativeView != null) {
                ll_ad.addView(nativeView);
                ll_ad.setVisibility(View.VISIBLE);
            } else {
                ll_ad.setVisibility(View.GONE);
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
        file_other_button.setOnClickListener(clickListener);
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

                    AdUtil.track("文件管理页面", "点击进入安装包页面", "", 1);
                    bundle.putString("name", "apk");
                    bundle.putInt("nameId", R.string.file_apk);
                    if (apkInfo.count == 0) {
                        bundle.putInt("count", 0);
                    }
                    jumpToActivity(FileListActivity.class, bundle, 1);
                    break;
                case R.id.file_zip_button:
                    if (zipInfo.count == 0) {
                        bundle.putInt("count", 0);
                    }
                    AdUtil.track("文件管理页面", "点击进入压缩包页面", "", 1);
                    bundle.putString("name", "zip");
                    bundle.putInt("nameId", R.string.file_zip);
                    jumpToActivity(FileListActivity.class, bundle, 1);
                    break;
                case R.id.file_txt_button:
                    if (docInfo.count == 0) {
                        bundle.putInt("count", 0);
                    }
                    AdUtil.track("文件管理页面", "点击进入文档页面", "", 1);
                    jumpToActivity(FileDocActivity.class, 1);
                    break;
                case R.id.file_music_button:
                    if (musicInfo.count == 0) {
                        bundle.putInt("count", 0);
                    }
                    AdUtil.track("文件管理页面", "点击进入音乐页面", "", 1);
                    bundle.putString("name", "music");
                    bundle.putInt("nameId", R.string.file_music);
                    jumpToActivity(FileListActivity.class, bundle, 1);
                    break;
                case R.id.file_video_button:
                    if (videoInfo.count == 0) {
                        bundle.putInt("count", 0);
                    }
                    AdUtil.track("文件管理页面", "点击进入视频页面", "", 1);
                    bundle.putString("name", "video");
                    bundle.putInt("nameId", R.string.file_video);
                    jumpToActivity(FileListActivity.class, bundle, 1);
                    break;
                case R.id.file_other_button:
                    AdUtil.track("文件管理页面", "点击进入其他页面", "", 1);
                    if (otherInfo.count == 0) {
                        bundle.putInt("count", 0);
                    }
                    bundle.putString("name", "other");
                    bundle.putInt("nameId", R.string.file_other);
                    jumpToActivity(FileListActivity.class, bundle, 1);
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

}
