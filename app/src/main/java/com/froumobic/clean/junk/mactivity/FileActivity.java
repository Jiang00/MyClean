package com.froumobic.clean.junk.mactivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.clean.filemanager.FileCategoryHelper;
import com.android.clean.util.PreData;
import com.android.clean.util.Util;
import com.android.client.AndroidSdk;
import com.android.ui.demo.UiManager;
import com.android.ui.demo.cross.Builder;
import com.android.ui.demo.cross.CrossView;
import com.froumobic.clean.junk.R;
import com.froumobic.clean.junk.util.AdUtil;
import com.froumobic.clean.junk.util.Constant;

/**
 * Created by froumobi on 2017/4/20.
 */

public class FileActivity extends MBaseActivity {
    FrameLayout title_left;
    TextView title_name;

    TextView file_apk_num, file_zip_num, file_txt_num, file_music_num, file_video_num, file_qita_num;
    TextView apk_chakan, zip_chakan, doc_chakan, music_chakan, video_chakan, other_chakan;
    LinearLayout apk_null, zip_null, doc_null, music_null, video_null, other_null;
    LinearLayout file_apk_button, file_zip_button, file_txt_button, file_music_button, file_video_button, file_other_button;
    LinearLayout ll_ad;
    private View nativeView;

    private String TAG_FILE = "file";
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
        apk_chakan = (TextView) findViewById(R.id.apk_chakan);
        zip_chakan = (TextView) findViewById(R.id.zip_chakan);
        doc_chakan = (TextView) findViewById(R.id.doc_chakan);
        music_chakan = (TextView) findViewById(R.id.music_chakan);
        video_chakan = (TextView) findViewById(R.id.video_chakan);
        other_chakan = (TextView) findViewById(R.id.other_chakan);
        apk_null = (LinearLayout) findViewById(R.id.apk_null);
        zip_null = (LinearLayout) findViewById(R.id.zip_null);
        doc_null = (LinearLayout) findViewById(R.id.doc_null);
        music_null = (LinearLayout) findViewById(R.id.music_null);
        video_null = (LinearLayout) findViewById(R.id.video_null);
        other_null = (LinearLayout) findViewById(R.id.other_null);
        apk_null = (LinearLayout) findViewById(R.id.apk_null);
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
                        //size__
                        if (apkInfo.size == 0) {
                            apk_null.setVisibility(View.VISIBLE);
                            file_apk_num.setVisibility(View.GONE);
                            apk_chakan.setVisibility(View.GONE);
                        } else {
                            file_apk_num.setVisibility(View.VISIBLE);
                            apk_chakan.setVisibility(View.VISIBLE);
                            apk_null.setVisibility(View.GONE);
                            file_apk_num.setText(getString(R.string.file_xuyao, Util.convertStorage(apkInfo.size, true)));
                        }
                        if (zipInfo.size == 0) {
                            zip_null.setVisibility(View.VISIBLE);
                            file_zip_num.setVisibility(View.GONE);
                            zip_chakan.setVisibility(View.GONE);
                        } else {
                            file_zip_num.setVisibility(View.VISIBLE);
                            zip_chakan.setVisibility(View.VISIBLE);
                            zip_null.setVisibility(View.GONE);
                            file_zip_num.setText(getString(R.string.file_xuyao, Util.convertStorage(zipInfo.size, true)));
                        }
                        if (docInfo.size == 0) {
                            doc_null.setVisibility(View.VISIBLE);
                            file_txt_num.setVisibility(View.GONE);
                            doc_chakan.setVisibility(View.GONE);
                        } else {
                            file_txt_num.setVisibility(View.VISIBLE);
                            doc_chakan.setVisibility(View.VISIBLE);
                            doc_null.setVisibility(View.GONE);
                            file_txt_num.setText(getString(R.string.file_xuyao, Util.convertStorage(docInfo.size, true)));
                        }
                        if (musicInfo.size == 0) {
                            music_null.setVisibility(View.VISIBLE);
                            file_music_num.setVisibility(View.GONE);
                            music_chakan.setVisibility(View.GONE);
                        } else {
                            music_chakan.setVisibility(View.VISIBLE);
                            music_null.setVisibility(View.GONE);
                            file_music_num.setVisibility(View.VISIBLE);
                            file_music_num.setText(getString(R.string.file_xuyao, Util.convertStorage(musicInfo.size, true)));
                        }
                        if (videoInfo.size == 0) {
                            video_null.setVisibility(View.VISIBLE);
                            file_video_num.setVisibility(View.GONE);
                            video_chakan.setVisibility(View.GONE);
                        } else {
                            video_chakan.setVisibility(View.VISIBLE);
                            file_video_num.setVisibility(View.VISIBLE);
                            video_null.setVisibility(View.GONE);
                            file_video_num.setText(getString(R.string.file_xuyao, Util.convertStorage(videoInfo.size, true)));
                        }
                        if (otherInfo.size == 0) {
                            other_null.setVisibility(View.VISIBLE);
                            other_chakan.setVisibility(View.GONE);
                            file_qita_num.setVisibility(View.GONE);
                        } else {
                            other_chakan.setVisibility(View.VISIBLE);
                            file_qita_num.setVisibility(View.VISIBLE);
                            other_null.setVisibility(View.GONE);
                            file_qita_num.setText(getString(R.string.file_xuyao, Util.convertStorage(otherInfo.size, true)));
                        }

                        setListener();
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initAd();
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

    private void initAd() {

        if (PreData.getDB(this, Constant.FULL_FILE, 0) == 1) {
            AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
        } else {
            int a = (int) (1 + Math.random() * (2)); //从1到10的int型随数
            if (a == 1) {
                nativeView = AdUtil.getNativeAdView(TAG_FILE, R.layout.native_ad_4);
                if (ll_ad != null && nativeView != null) {
                    ll_ad.removeAllViews();
                    ll_ad.addView(nativeView);
                }
            } else {
                try {
                    UiManager.getCrossView(this, new Builder("cross")
                                    .setServiceData(AndroidSdk.getExtraData())
                                    .setType(Builder.Type.TYPE_HORIZONTAL_99)
                                    .setIsShouldShowDownLoadBtn(true).setAdTagImageId(R.mipmap.ad)
                                    .setRootViewBackgroundColor(ContextCompat.getColor(FileActivity.this, R.color.white_100))
                                    .setActionBtnBackground(R.drawable.select_text_ad)
                                    .setActionTextColor(getResources().getColor(R.color.white_100))
                                    .setTitleTextColor(getResources().getColor(R.color.B2))
                                    .setSubTitleTextColor(getResources().getColor(R.color.B3))
                                    .setTrackTag("广告位_文件管理")
                            , new CrossView.OnDataFinishListener() {
                                @Override
                                public void onFinish(CrossView crossView) {
                                    ll_ad.removeAllViews();
                                    ll_ad.addView(crossView);
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                    ll_ad.setVisibility(View.GONE);
                }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            initData();
        }
    }
}
