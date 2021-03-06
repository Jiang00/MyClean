package com.supers.clean.junk.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.clean.db.CleanDBHelper;
import com.android.clean.filemanager.FileCategoryHelper;
import com.android.clean.util.PreData;
import com.android.client.AndroidSdk;
import com.supers.clean.junk.R;
import com.supers.clean.junk.customeview.FileRoundView;
import com.android.clean.util.Util;
import com.supers.clean.junk.util.AdUtil;
import com.android.clean.util.Constant;
import com.supers.clean.junk.util.BadgerCount;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

/**
 * Created by Ivy on 2017/4/20.
 */

public class FileActivity extends BaseActivity {
    FrameLayout title_left;
    TextView title_name;

    FileRoundView file_piechart;
    TextView file_sd_shengyu, file_sd_all;
    TextView file_apk_size, file_zip_size, file_txt_size, file_music_size, file_video_size, file_other_size;
    TextView file_apk_num, file_zip_num, file_txt_num, file_music_num, file_video_num, file_qita_num, file_recent_num, file_download_num, file_all_num;
    LinearLayout file_apk_button, file_zip_button, file_txt_button, file_music_button, file_video_button, file_other_button, file_recent_button, file_download_button, file_sd_button;
    LinearLayout ll_ad;
    FrameLayout ad_fl;
    private View nativeView;

    private String TAG_FILE = "eos_file";
    private FileCategoryHelper fileHelper;
    private FileCategoryHelper.CategoryInfo apkInfo, zipInfo, docInfo, musicInfo, videoInfo, otherInfo;
    private com.android.clean.filemanager.Util.SDCardInfo sdCardInfo;
    private Handler mHandler;
    private ArrayList<String> whiteList;

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        file_piechart = (FileRoundView) findViewById(R.id.file_piechart);
        file_sd_shengyu = (TextView) findViewById(R.id.file_sd_shengyu);
        file_sd_all = (TextView) findViewById(R.id.file_sd_all);
        file_apk_size = (TextView) findViewById(R.id.file_apk_size);
        file_zip_size = (TextView) findViewById(R.id.file_zip_size);
        file_txt_size = (TextView) findViewById(R.id.file_txt_size);
        file_music_size = (TextView) findViewById(R.id.file_music_size);
        file_video_size = (TextView) findViewById(R.id.file_video_size);
        file_other_size = (TextView) findViewById(R.id.file_qita_size);
        file_apk_num = (TextView) findViewById(R.id.file_apk_num);
        file_zip_num = (TextView) findViewById(R.id.file_zip_num);
        file_txt_num = (TextView) findViewById(R.id.file_txt_num);
        file_music_num = (TextView) findViewById(R.id.file_music_num);
        file_video_num = (TextView) findViewById(R.id.file_video_num);
        file_qita_num = (TextView) findViewById(R.id.file_qita_num);
        file_recent_num = (TextView) findViewById(R.id.file_recent_num);
        file_download_num = (TextView) findViewById(R.id.file_download_num);
        file_all_num = (TextView) findViewById(R.id.file_all_num);
        file_apk_button = (LinearLayout) findViewById(R.id.file_apk_button);
        file_zip_button = (LinearLayout) findViewById(R.id.file_zip_button);
        file_txt_button = (LinearLayout) findViewById(R.id.file_txt_button);
        file_music_button = (LinearLayout) findViewById(R.id.file_music_button);
        file_video_button = (LinearLayout) findViewById(R.id.file_video_button);
        file_other_button = (LinearLayout) findViewById(R.id.file_other_button);
        file_recent_button = (LinearLayout) findViewById(R.id.file_recent_button);
        file_sd_button = (LinearLayout) findViewById(R.id.file_sd_button);
        file_download_button = (LinearLayout) findViewById(R.id.file_download_button);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
        ad_fl = (FrameLayout) findViewById(R.id.ad_fl);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_file);
        title_name.setText(R.string.side_file);
        whiteList = new ArrayList<>();
        whiteList = CleanDBHelper.getInstance(this).getFileList(CleanDBHelper.TableType.RacentFile);
        if (whiteList.size() > 10) {
            int count = 0;
            for (int i = 0; i < whiteList.size() - 10; i++) {
                count = ++count;
                Log.e("list", "==" + count);
                CleanDBHelper.getInstance(this).deleteItem(CleanDBHelper.TableType.RacentFile, whiteList.get(i));
            }
        }

        fileHelper = new FileCategoryHelper(this);
        mHandler = new Handler();
        initAd();
        initData();


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    private void initData() {
        whiteList = CleanDBHelper.getInstance(this).getFileList(CleanDBHelper.TableType.RacentFile);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File[] files = new File(path).listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return !pathname.isHidden();
            }
        });
        File[] files_2 = new File(path + "/Download").listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return !pathname.isHidden();
            }
        });
        if (null != files) {
            file_all_num.setText(getString(R.string.file_num, files.length));
        }
        if (null != files_2) {
            file_download_num.setText(getString(R.string.file_num, files_2.length));
        }
        sdCardInfo = com.android.clean.filemanager.Util.getSDCardInfo();
        if (sdCardInfo != null) {
            file_sd_all.setText(com.android.clean.filemanager.Util.convertStorage(sdCardInfo.total));
            file_sd_shengyu.setText(com.android.clean.filemanager.Util.convertStorage(sdCardInfo.free));
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
                        file_apk_size.setText(com.android.clean.filemanager.Util.convertStorage(apkInfo.size));
                        file_zip_size.setText(com.android.clean.filemanager.Util.convertStorage(zipInfo.size));
                        file_txt_size.setText(com.android.clean.filemanager.Util.convertStorage(docInfo.size));
                        file_music_size.setText(com.android.clean.filemanager.Util.convertStorage(musicInfo.size));
                        file_video_size.setText(com.android.clean.filemanager.Util.convertStorage(videoInfo.size));
                        file_other_size.setText(com.android.clean.filemanager.Util.convertStorage(otherInfo.size));
                        file_apk_num.setText(getString(R.string.file_num, apkInfo.count));
                        file_zip_num.setText(getString(R.string.file_num, zipInfo.count));
                        file_txt_num.setText(getString(R.string.file_num, docInfo.count));
                        file_music_num.setText(getString(R.string.file_num, musicInfo.count));
                        file_video_num.setText(getString(R.string.file_num, videoInfo.count));
                        file_qita_num.setText(getString(R.string.file_num, otherInfo.count));
                        file_recent_num.setText(getString(R.string.file_num, whiteList.size()));
                        file_piechart.setProgress(mHandler, apkInfo.size, zipInfo.size, docInfo.size, musicInfo.size, videoInfo.size, otherInfo.size);
                        setListener();
                    }
                });
            }
        }).start();
    }

    private void initAd() {
            mHandler.postDelayed(runnable, 2500);

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            nativeView = AdUtil.getNativeAdView(FileActivity.this, TAG_FILE, R.layout.native_ad_4);
            if (ll_ad != null && nativeView != null) {
                ll_ad.addView(nativeView);
                ll_ad.setVisibility(View.VISIBLE);
                int w = View.MeasureSpec.makeMeasureSpec(0,
                        View.MeasureSpec.UNSPECIFIED);
                int h = View.MeasureSpec.makeMeasureSpec(0,
                        View.MeasureSpec.UNSPECIFIED);
                ll_ad.measure(w, h);
                int height = ll_ad.getMeasuredHeight();
                ValueAnimator animator = ValueAnimator.ofInt(0, height);
                animator.setDuration(500);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int h = (int) animation.getAnimatedValue();
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ad_fl.getLayoutParams();
                        layoutParams.height = h;
                        ad_fl.setLayoutParams(layoutParams);
                    }
                });
                animator.start();
            }
        }
    };

    private void setListener() {
        title_left.setOnClickListener(clickListener);
        file_apk_button.setOnClickListener(clickListener);
        file_zip_button.setOnClickListener(clickListener);
        file_txt_button.setOnClickListener(clickListener);
        file_music_button.setOnClickListener(clickListener);
        file_recent_button.setOnClickListener(clickListener);
        file_download_button.setOnClickListener(clickListener);
        file_sd_button.setOnClickListener(clickListener);
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
                    bundle.putInt("nameId", R.string.file_pther);
                    jumpToActivity(FileListActivity.class, bundle, 1);
                    break;
                case R.id.file_recent_button:
                    AdUtil.track("文件管理页面", "点击进入最近页面", "", 1);
                    if (whiteList.size() == 0) {
                        bundle.putInt("count", 0);
                    }
                    bundle.putString("name", "racent");
                    bundle.putInt("nameId", R.string.file_racent);
                    jumpToActivity(FileListActivity.class, bundle, 1);
                    break;
                case R.id.file_sd_button:
                    AdUtil.track("文件管理页面", "点击进入全部页面", "", 1);
                    bundle.putInt("nameId", R.string.file_sd);
                    bundle.putString("path", Environment.getExternalStorageDirectory().getAbsolutePath());
                    bundle.putString("name", "sdcard");
                    jumpToActivity(SDCardActivity.class, bundle, 1);
                    break;
                case R.id.file_download_button:
                    AdUtil.track("文件管理页面", "点击进入下载页面", "", 1);
                    bundle.putInt("nameId", R.string.file_sd);
                    bundle.putString("path", Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download");
                    bundle.putString("name", "sdcard");
                    jumpToActivity(SDCardActivity.class, bundle, 1);
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacks(runnable);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            initData();
        }
    }

    private void setPieChat() {
//            List<PieEntry> entries = new ArrayList<>();
//    //        entries.add(new PieEntry(apkInfo.size));
//    //        entries.add(new PieEntry(zipInfo.size));
//    //        entries.add(new PieEntry(docInfo.size));
//    //        entries.add(new PieEntry(musicInfo.size));
//    //        entries.add(new PieEntry(videoInfo.size));
//    //        entries.add(new PieEntry(otherInfo.size));
//    //        if (sdCardInfo != null) {
//    //            entries.add(new PieEntry(sdCardInfo.free));
//    //        } else {
//    //            entries.add(new PieEntry(MemoryManager.getPhoneAllFreeSize()));
//    //        }
//            entries.add(new PieEntry(1, "apk"));
//            entries.add(new PieEntry(1.1f, "zip"));
//            entries.add(new PieEntry(1.1f, "doc"));
//            entries.add(new PieEntry(1.1f, "music"));
//            entries.add(new PieEntry(100, "video"));
//            entries.add(new PieEntry(500, "other"));
//    //        entries.add(new PieEntry(10000,"free"));
//
//            PieDataSet set = new PieDataSet(entries, "");
//            set.setSelectionShift(0); // 选中态多出的长度
//            set.setSliceSpace(0f); //设置个饼状图之间的距离
//            set.setDrawValues(false);//百分比是否画
//            set.setColors(new int[]{ContextCompat.getColor(this, R.color.file_apk), ContextCompat.getColor(this, R.color.file_zip), ContextCompat.getColor(this, R.color.file_txt),
//                    ContextCompat.getColor(this, R.color.file_music), ContextCompat.getColor(this, R.color.file_video),
//                    ContextCompat.getColor(this, R.color.file_qita), ContextCompat.getColor(this, R.color.file_shengyu)});
//            PieData data = new PieData(set);
//            file_piechart.setData(data);
//            file_piechart.getDescription().setEnabled(false);//设置标题说明是否显示
//            Legend mLegend = file_piechart.getLegend(); //设置比例块
//            mLegend.setEnabled(false);//比例块是否显示
//            file_piechart.setRotationAngle(70); // 初始旋转角度
//    //        file_piechart.setDrawHoleEnabled(false);//实心
//            file_piechart.setHoleRadius(Util.dp2px(30));  //内空心洞半径
//            file_piechart.setTransparentCircleRadius(0); // 半透明圈
//    //        file_piechart.setHoleRadius(0)  //内空心洞半径为零==实心圆
//            file_piechart.setRotationEnabled(false); // 手动旋转开关
//    //        file_piechart.animateX(1000);  //设置动画
//            file_piechart.setDrawEntryLabels(false);//设置lable是否画
//            file_piechart.setHardwareAccelerationEnabled(true);
//            file_piechart.invalidate();
    }
}
