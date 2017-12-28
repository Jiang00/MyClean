package com.vector.cleaner.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vector.cleaner.utils.AdUtil;
import com.vector.cleaner.utils.Constant;
import com.vector.mcleaner.file.FileCategoryHelper;
import com.vector.mcleaner.mutil.MemoryManager;
import com.vector.mcleaner.mutil.PreData;
import com.android.client.AndroidSdk;
import com.vector.cleaner.R;
import com.vector.mcleaner.mutil.Util;

/**
 * Created by  on 2017/4/20.
 */

public class FilesActivity extends BaseActivity {
    LinearLayout ll_ad;

    FrameLayout title_left;
    LinearLayout file_apk_button, file_zip_button, file_txt_button, file_music_button, file_video_button;
    TextView title_name;
    private View nativeView;
    TextView file_apk_num, file_zip_num, file_txt_num, file_music_num, file_video_num;
    TextView file_apk_num_d, file_zip_num_d, file_txt_num_d, file_music_num_d, file_video_num_d;
    TextView file_apk_num_check, file_zip_num_check, file_txt_num_check, file_music_num_check, file_video_num_check;

    TextView file_sd_keyong;
    TextView file_video_size, file_apk_size, file_audio_size, file_zip_size, file_dec_size;

    private String TAG_FILE = "vector_file";
    private FileCategoryHelper fileHelper;
    private FileCategoryHelper.CategoryInfo apkInfo, zipInfo, docInfo, musicInfo, videoInfo;
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
        file_apk_num_d = (TextView) findViewById(R.id.file_apk_num_d);
        file_zip_num_d = (TextView) findViewById(R.id.file_zip_num_d);
        file_txt_num_d = (TextView) findViewById(R.id.file_txt_num_d);
        file_music_num_d = (TextView) findViewById(R.id.file_music_num_d);
        file_video_num_d = (TextView) findViewById(R.id.file_video_num_d);
        file_apk_num_check = (TextView) findViewById(R.id.file_apk_num_check);
        file_zip_num_check = (TextView) findViewById(R.id.file_zip_num_check);
        file_txt_num_check = (TextView) findViewById(R.id.file_txt_num_check);
        file_music_num_check = (TextView) findViewById(R.id.file_music_num_check);
        file_video_num_check = (TextView) findViewById(R.id.file_video_num_check);
        file_apk_button = (LinearLayout) findViewById(R.id.file_apk_button);
        file_zip_button = (LinearLayout) findViewById(R.id.file_zip_button);
        file_txt_button = (LinearLayout) findViewById(R.id.file_txt_button);
        file_music_button = (LinearLayout) findViewById(R.id.file_music_button);
        file_video_button = (LinearLayout) findViewById(R.id.file_video_button);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
        file_sd_keyong = (TextView) findViewById(R.id.file_sd_keyong);
        file_video_size = (TextView) findViewById(R.id.file_video_size);
        file_apk_size = (TextView) findViewById(R.id.file_apk_size);
        file_audio_size = (TextView) findViewById(R.id.file_audio_size);
        file_zip_size = (TextView) findViewById(R.id.file_zip_size);
        file_dec_size = (TextView) findViewById(R.id.file_dec_size);
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
        long sd_kongxian = MemoryManager.getPhoneAllFreeSize();
        file_sd_keyong.setText(Util.convertStorage(sd_kongxian, true) + " " + getString(R.string.main_msg_sd));

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

                        setFileNum(file_apk_num, file_apk_num_d, file_apk_num_check, apkInfo.count);
                        setFileNum(file_zip_num, file_zip_num_d, file_zip_num_check, zipInfo.count);
                        setFileNum(file_txt_num, file_txt_num_d, file_txt_num_check, docInfo.count);
                        setFileNum(file_music_num, file_music_num_d, file_music_num_check, musicInfo.count);
                        setFileNum(file_video_num, file_video_num_d, file_video_num_check, videoInfo.count);

                        file_video_size.setText(Util.convertStorage(videoInfo.size, true));
                        file_apk_size.setText(Util.convertStorage(apkInfo.size, true));
                        file_audio_size.setText(Util.convertStorage(musicInfo.size, true));
                        file_zip_size.setText(Util.convertStorage(zipInfo.size, true));
                        file_dec_size.setText(Util.convertStorage(docInfo.size, true));
                        setListener();
                    }
                });
            }
        }).start();
    }

    private void setFileNum(TextView view1, TextView view2, TextView view3, long num) {
        view1.setText(num + " " + getString(R.string.file_num));
        if (num > 0) {
            view1.setTextColor(ContextCompat.getColor(FilesActivity.this, R.color.A2));
            view2.setText(R.string.file_main);
            view2.setTextColor(ContextCompat.getColor(FilesActivity.this, R.color.Z1));
            view3.setBackgroundResource(R.drawable.shape_a2_round);
        } else {
            view1.setTextColor(ContextCompat.getColor(FilesActivity.this, R.color.Z1));
            view2.setText(R.string.file_null);
            view2.setTextColor(ContextCompat.getColor(FilesActivity.this, R.color.Z2));
            view3.setBackgroundResource(R.drawable.shape_b3_round);
        }
    }

    private void initAd() {
        if (PreData.getDB(this, Constant.FULL_FILE, 0) == 1) {
            AndroidSdk.showFullAd(AdUtil.DEFAULT);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            initData();
        }
    }

}
