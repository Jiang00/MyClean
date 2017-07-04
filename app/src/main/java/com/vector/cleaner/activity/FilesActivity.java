package com.vector.cleaner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vector.cleaner.utils.AdUtil;
import com.vector.cleaner.utils.Constant;
import com.vector.mcleaner.file.FileCategoryHelper;
import com.vector.mcleaner.mutil.PreData;
import com.android.client.AndroidSdk;
import com.vector.cleaner.R;

/**
 * Created by  on 2017/4/20.
 */

public class FilesActivity extends BaseActivity {
    LinearLayout ll_ad;

    FrameLayout title_left;
    LinearLayout file_apk_button, file_zip_button, file_txt_button, file_music_button, file_video_button, file_other_button;
    TextView title_name;
    private View nativeView;
    TextView file_apk_num, file_zip_num, file_txt_num, file_music_num, file_video_num, file_qita_num;
    TextView file_apk_num_d, file_zip_num_d, file_txt_num_d, file_music_num_d, file_video_num_d, file_qita_num_d;

    private String TAG_FILE = "vector_file";
    private FileCategoryHelper fileHelper;
    private FileCategoryHelper.CategoryInfo apkInfo, zipInfo, docInfo, musicInfo, videoInfo, otherInfo;
    private Handler mHandler;

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
        file_qita_num = (TextView) findViewById(R.id.file_qita_num);
        file_apk_num_d = (TextView) findViewById(R.id.file_apk_num_d);
        file_zip_num_d = (TextView) findViewById(R.id.file_zip_num_d);
        file_txt_num_d = (TextView) findViewById(R.id.file_txt_num_d);
        file_music_num_d = (TextView) findViewById(R.id.file_music_num_d);
        file_video_num_d = (TextView) findViewById(R.id.file_video_num_d);
        file_qita_num_d = (TextView) findViewById(R.id.file_qita_num_d);
        file_apk_button = (LinearLayout) findViewById(R.id.file_apk_button);
        file_zip_button = (LinearLayout) findViewById(R.id.file_zip_button);
        file_txt_button = (LinearLayout) findViewById(R.id.file_txt_button);
        file_music_button = (LinearLayout) findViewById(R.id.file_music_button);
        file_video_button = (LinearLayout) findViewById(R.id.file_video_button);
        file_other_button = (LinearLayout) findViewById(R.id.file_other_button);
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


    private void initData() {
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
                        file_apk_num.setText(apkInfo.count + "");
                        file_zip_num.setText(zipInfo.count + "");
                        file_txt_num.setText(docInfo.count + "");
                        file_music_num.setText(musicInfo.count + "");
                        file_video_num.setText(videoInfo.count + "");
                        file_qita_num.setText(otherInfo.count + "");
                        file_apk_num_d.setVisibility(View.VISIBLE);
                        file_zip_num_d.setVisibility(View.VISIBLE);
                        file_txt_num_d.setVisibility(View.VISIBLE);
                        file_music_num_d.setVisibility(View.VISIBLE);
                        file_video_num_d.setVisibility(View.VISIBLE);
                        file_qita_num_d.setVisibility(View.VISIBLE);
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
            }
        }

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
                    bundle.putInt("nameId", R.string.file_pther);
                    jumpToActivity(FileListActivity.class, bundle, 1);
                    break;
            }
        }
    };

    private void setListener() {
        title_left.setOnClickListener(clickListener);
        file_apk_button.setOnClickListener(clickListener);
        file_zip_button.setOnClickListener(clickListener);
        file_txt_button.setOnClickListener(clickListener);
        file_music_button.setOnClickListener(clickListener);
        file_video_button.setOnClickListener(clickListener);
        file_other_button.setOnClickListener(clickListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            initData();
        }
    }

}
