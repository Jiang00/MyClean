package com.myboost.junk.activityprivacy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.myboost.clean.filemanager.PhoneFileCategoryHelper;
import com.myboost.clean.filemanager.Util;
import com.myboost.clean.utilsprivacy.PreData;
import com.myboost.junk.R;
import com.myboost.junk.interfaceviewprivacy.MainViewPrivacy;
import com.myboost.junk.presenterprivacy.PrivacyPresenterMain;
import com.myboost.junk.toolsprivacy.MyConstantPrivacy;
import com.myboost.junk.toolsprivacy.SetAdUtilPrivacy;

/**
 * Created by on 2017/4/20.
 */

public class BoostFileManagerActivity extends BaseActivity implements MainViewPrivacy {
    private String TAG_FILE = "cprivacy_file";
    LinearLayout file_apk_button, file_zip_button, file_txt_button, file_music_button, file_video_button, file_other_button;
    FrameLayout title_left;
    private PhoneFileCategoryHelper fileHelper;
    TextView title_name;
    private PhoneFileCategoryHelper.CategoryInfo apkInfo, zipInfo, docInfo, musicInfo, videoInfo, otherInfo, pictureInfo, logInfo;
    private Handler mHandler;
    TextView file_used;
    public String size = null;
    private View nativeView;
    ProgressBar delete_progress;
    private PrivacyPresenterMain mainPresenter;
    TextView file_apk_size, file_zip_size, file_txt_size, file_music_size, file_video_size, file_qita_size;
    LinearLayout ll_ad;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_file);
        mainPresenter = new PrivacyPresenterMain(this, this);
        mainPresenter.init();
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
        file_used = (TextView) findViewById(R.id.file_used);
        file_zip_size = (TextView) findViewById(R.id.file_zip_size);
        file_apk_size = (TextView) findViewById(R.id.file_apk_size);
        delete_progress = (ProgressBar) findViewById(R.id.delete_progress);
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

    @Override
    protected void onResume() {
        super.onResume();
        String[] arr = size.split("/");

//        file_total.setText(arr[1] + "B ");
        Double.valueOf(arr[1].substring(0, arr[1].length() - 1));
        double d = Double.valueOf(arr[0].substring(0, arr[0].length() - 1)) / Double.valueOf(arr[1].substring(0, arr[1].length() - 1)) * 100;

        file_used.setText(new java.text.DecimalFormat("#.00").format(Double.valueOf(arr[1].substring(0, arr[1].length() - 1)) - Double.valueOf(arr[0].substring(0, arr[0].length() - 1)))
                + arr[1].substring(arr[1].length() - 1, arr[1].length()) + "B");
        delete_progress.setProgress((int) d);
        delete_progress.setMax(100);
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

        if (PreData.getDB(this, MyConstantPrivacy.FULL_FILE, 0) == 1) {
            AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
        } else {
            nativeView = SetAdUtilPrivacy.getNativeAdView(TAG_FILE, R.layout.native_ad_4);
            if (ll_ad != null && nativeView != null) {
                ll_ad.addView(nativeView);
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
                    SetAdUtilPrivacy.track("文件管理页面", "点击进入安装包页面", "", 1);
                    bundle.putString("name", "apk");
                    bundle.putInt("nameId", R.string.file_apk);
                    if (apkInfo.count == 0) {
                        bundle.putInt("count", 0);
                    }
                    jumpToActivity(FileActivityBoost.class, bundle, 1);
                    break;
                case R.id.file_zip_button:
                    if (zipInfo.count == 0) {
                        bundle.putInt("count", 0);
                    }
                    SetAdUtilPrivacy.track("文件管理页面", "点击进入压缩包页面", "", 1);
                    bundle.putString("name", "zip");
                    bundle.putInt("nameId", R.string.file_zip);
                    jumpToActivity(FileActivityBoost.class, bundle, 1);
                    break;
                case R.id.file_txt_button:
                    if (docInfo.count == 0) {
                        bundle.putInt("count", 0);
                    }
                    SetAdUtilPrivacy.track("文件管理页面", "点击进入文档页面", "", 1);
                    jumpToActivity(BoostDocActivity.class, 1);
                    break;
                case R.id.file_music_button:
                    if (musicInfo.count == 0) {
                        bundle.putInt("count", 0);
                    }
                    SetAdUtilPrivacy.track("文件管理页面", "点击进入音乐页面", "", 1);
                    bundle.putString("name", "music");
                    bundle.putInt("nameId", R.string.file_music);
                    jumpToActivity(FileActivityBoost.class, bundle, 1);
                    break;
                case R.id.file_video_button:
                    if (videoInfo.count == 0) {
                        bundle.putInt("count", 0);
                    }
                    SetAdUtilPrivacy.track("文件管理页面", "点击进入视频页面", "", 1);
                    bundle.putString("name", "video");
                    bundle.putInt("nameId", R.string.file_video);
                    jumpToActivity(FileActivityBoost.class, bundle, 1);
                    break;
                case R.id.file_other_button:
                    SetAdUtilPrivacy.track("文件管理页面", "点击进入其他页面", "", 1);
                    if (otherInfo.count == 0) {
                        bundle.putInt("count", 0);
                    }
                    bundle.putString("name", "other");
                    bundle.putInt("nameId", R.string.file_pther);
                    jumpToActivity(FileActivityBoost.class, bundle, 1);
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

    @Override
    public void loadFullAd() {

    }

    @Override
    public void onClick() {

    }

    @Override
    public void initCpu(int temp) {

    }

    @Override
    public void initSd(final int perent, final String size, final long sd_kongxian) {
        this.size = size;
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Log.e("file", "====" + perent + "==size=" + size + "===" + sd_kongxian);
            }
        });
    }

    @Override
    public void initRam(int percent, String size) {

    }

    @Override
    public void initQiu(int fenshu, boolean isReStart) {

    }

    @Override
    public void setRotateGone() {

    }

    @Override
    public void closeDrawer() {

    }

    @Override
    public void initSideData() {

    }

    @Override
    public void openDrawer() {

    }
}
