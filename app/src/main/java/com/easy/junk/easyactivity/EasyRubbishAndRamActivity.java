package com.easy.junk.easyactivity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.easy.clean.entity.JunkInfo;
import com.easy.clean.easyutils.MyUtils;
import com.easy.clean.easyutils.PreData;
import com.easy.junk.R;
import com.easy.junk.easyinterfaceview.LogHeRamView;
import com.easy.junk.easycustomadapter.CustomJunkRamAdapter;
import com.easy.junk.easycustomview.ListViewForScrollView;
import com.easy.junk.easycustomview.EasyScrollView;
import com.easy.junk.easypresenter.EasyLogRamPresenter;
import com.easy.junk.easytools.EasyConstant;
import com.easy.junk.easytools.SetAdUtil;

import java.util.List;

/**
 * Created by on 2017/3/2.
 */

public class EasyRubbishAndRamActivity extends BaseActivity implements LogHeRamView {
    FrameLayout title_left;
    TextView junk_unit;
    TextView junk_fangxin;
    LinearLayout junk_title_backg;
    TextView junk_size_all;
    private CustomJunkRamAdapter adapterSystem, adapterApk, adapterUnload, adapterLog, adapterUser, adapterRam, adapterClear;
    private EasyLogRamPresenter junkPresenter;
    TextView junk_system_size, junk_apk_size, junk_unload_size, junk_log_size, junk_user_size, junk_ram_size;
    LinearLayout junk_button_system, junk_button_apk, junk_button_unload, junk_button_log, junk_button_user, junk_button_ram;
    ListViewForScrollView junk_system_list, junk_apk_list, junk_unload_list, junk_log_list, junk_user_list, junk_ram_list;
    TextView title_name;
    private boolean color2 = true;
    public Handler myHandler;
    private boolean color1 = true;
    private boolean color3 = true;
    EasyScrollView junk_scroll;
    ListView junk_list_all;
    ImageView junk_system_jiantou, junk_apk_jiaotou, junk_unload_jiantou, junk_log_jiantou, junk_user_jiantou, junk_ram_jiantou;
    TextView junk_button_clean;
    ImageView junk_system_check, junk_apk_check, junk_unload_check, junk_log_check, junk_user_check, junk_ram_check;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_junk_ram);
        AndroidSdk.loadFullAd(AndroidSdk.FULL_TAG_PAUSE);
        myHandler = new Handler();
        junkPresenter = new EasyLogRamPresenter(this, this);
        junkPresenter.init();
    }

    @Override
    public void onClick() {
        title_left.setOnClickListener(onClickListener);
        junk_button_system.setOnClickListener(onClickListener);
        junk_button_apk.setOnClickListener(onClickListener);
        junk_button_unload.setOnClickListener(onClickListener);
        junk_button_log.setOnClickListener(onClickListener);
        junk_button_user.setOnClickListener(onClickListener);
        junk_button_ram.setOnClickListener(onClickListener);
        junk_button_clean.setOnClickListener(onClickListener);

    }

    @Override
    public void initData(long allSize) {
        title_name.setText(R.string.all_junk_title);
        if (allSize <= 0) {
            Bundle bundle = new Bundle();
            bundle.putString("from", "allJunk");
            junkPresenter.jumpToActivity(EasySucceedActivity.class, bundle, 1);
            return;
        }
        adapterSystem = new CustomJunkRamAdapter(this, junkPresenter);
        adapterApk = new CustomJunkRamAdapter(this, junkPresenter);
        adapterUnload = new CustomJunkRamAdapter(this, junkPresenter);
        adapterLog = new CustomJunkRamAdapter(this, junkPresenter);
        adapterUser = new CustomJunkRamAdapter(this, junkPresenter);
        adapterRam = new CustomJunkRamAdapter(this, junkPresenter);
        adapterClear = new CustomJunkRamAdapter(this, junkPresenter);
        junk_system_list.setAdapter(adapterSystem);
        junk_apk_list.setAdapter(adapterApk);
        junk_unload_list.setAdapter(adapterUnload);
        junk_log_list.setAdapter(adapterLog);
        junk_user_list.setAdapter(adapterUser);
        junk_ram_list.setAdapter(adapterRam);
        junkPresenter.setUnit(allSize, junk_unit);
        junkPresenter.addAdapterData();
    }

    @Override
    public void setColor(final long size) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int time = 100;
                for (long i = 0; i <= size; i += (size / 15)) {
                    final long finalI = i;
                    time -= 5;
                    if (time < 30) {
                        time = 30;
                    }
                    if (onDestroyed) {
                        break;
                    }
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            junk_size_all.setText(MyUtils.convertStorage(finalI, false));
                            junkPresenter.setUnit(size, junk_unit);
                        }
                    });
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Animation animation = AnimationUtils.loadAnimation(EasyRubbishAndRamActivity.this, R.anim.translate_notifi);
                        junk_button_clean.startAnimation(animation);
                        junk_button_clean.setVisibility(View.VISIBLE);
                    }
                });
            }
        }).start();
//        junk_size_all.setText(MyUtils.getFileSize2(size));

        if (size > 1024 * 1024 * 100 && size <= 1024 * 1024 * 200) {
            if (color1) {
                color1 = false;
                ValueAnimator colorAnim = ObjectAnimator.ofInt(junk_title_backg, "backgroundColor", getResources().getColor(R.color.A1), getResources().getColor(R.color.A21));
                colorAnim.setDuration(2000);
                colorAnim.setRepeatCount(0);
                colorAnim.setEvaluator(new ArgbEvaluator());
                colorAnim.start();
                junk_button_clean.setBackgroundColor(getResources().getColor(R.color.A21));
            }
        } else if (size > 1024 * 1024 * 200) {
            if (color2) {
                color2 = false;
                ValueAnimator colorAnim = ObjectAnimator.ofInt(junk_title_backg, "backgroundColor", getResources().getColor(R.color.A21), getResources().getColor(R.color.A2));
                colorAnim.setDuration(2000);
                colorAnim.setRepeatCount(0);
                colorAnim.setEvaluator(new ArgbEvaluator());
                colorAnim.start();
                junk_button_clean.setBackgroundColor(getResources().getColor(R.color.A2));
            }
        } else {
            if (color3) {
                color3 = false;
                ValueAnimator colorAnim = ObjectAnimator.ofInt(junk_title_backg, "backgroundColor", getResources().getColor(R.color.A1), getResources().getColor(R.color.A1));
                colorAnim.setDuration(2000);
                colorAnim.setRepeatCount(0);
                colorAnim.setEvaluator(new ArgbEvaluator());
                colorAnim.start();
                junk_button_clean.setBackgroundColor(getResources().getColor(R.color.A1));
            }
        }
    }

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        junk_title_backg = (LinearLayout) findViewById(R.id.junk_title_backg);
        junk_size_all = (TextView) findViewById(R.id.junk_size_all);
        junk_unit = (TextView) findViewById(R.id.junk_unit);
        junk_fangxin = (TextView) findViewById(R.id.junk_fangxin);
        junk_button_system = (LinearLayout) findViewById(R.id.junk_button_system);
        junk_button_apk = (LinearLayout) findViewById(R.id.junk_button_apk);
        junk_button_unload = (LinearLayout) findViewById(R.id.junk_button_unload);
        junk_button_log = (LinearLayout) findViewById(R.id.junk_button_log);
        junk_button_user = (LinearLayout) findViewById(R.id.junk_button_user);
        junk_button_ram = (LinearLayout) findViewById(R.id.junk_button_ram);
        junk_system_size = (TextView) findViewById(R.id.junk_system_size);
        junk_apk_size = (TextView) findViewById(R.id.junk_apk_size);
        junk_unload_size = (TextView) findViewById(R.id.junk_unload_size);
        junk_log_size = (TextView) findViewById(R.id.junk_log_size);
        junk_user_size = (TextView) findViewById(R.id.junk_user_size);
        junk_ram_size = (TextView) findViewById(R.id.junk_ram_size);
        junk_system_jiantou = (ImageView) findViewById(R.id.junk_system_jiantou);
        junk_apk_jiaotou = (ImageView) findViewById(R.id.junk_apk_jiaotou);
        junk_unload_jiantou = (ImageView) findViewById(R.id.junk_unload_jiantou);
        junk_log_jiantou = (ImageView) findViewById(R.id.junk_log_jiantou);
        junk_user_jiantou = (ImageView) findViewById(R.id.junk_user_jiantou);
        junk_ram_jiantou = (ImageView) findViewById(R.id.junk_ram_jiantou);
        junk_system_list = (ListViewForScrollView) findViewById(R.id.junk_system_list);
        junk_apk_list = (ListViewForScrollView) findViewById(R.id.junk_apk_list);
        junk_unload_list = (ListViewForScrollView) findViewById(R.id.junk_unload_list);
        junk_log_list = (ListViewForScrollView) findViewById(R.id.junk_log_list);
        junk_user_list = (ListViewForScrollView) findViewById(R.id.junk_user_list);
        junk_ram_list = (ListViewForScrollView) findViewById(R.id.junk_ram_list);
        junk_button_clean = (TextView) findViewById(R.id.junk_button_clean);
        junk_scroll = (EasyScrollView) findViewById(R.id.junk_scroll);
        junk_list_all = (ListView) findViewById(R.id.junk_list_all);
        junk_system_check = $(R.id.junk_system_check);
        junk_apk_check = $(R.id.junk_apk_check);
        junk_unload_check = $(R.id.junk_unload_check);
        junk_log_check = $(R.id.junk_log_check);
        junk_user_check = $(R.id.junk_user_check);
        junk_ram_check = $(R.id.junk_ram_check);
    }

    @Override
    public void loadFullAd() {
    }

    @Override
    public void addApkdata(final long size, List<JunkInfo> list) {
        adapterApk.addDataList(list);
        junk_apk_list.setVisibility(View.GONE);
//        junk_apk_size.setText(MyUtils.getFileSizeKongge(size));
//        junkPresenter.setUnit(size, junk_apk_unit);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int time = 100;
                for (long i = 0; i <= size; i += (size / 15)) {
                    final long finalI = i;
                    time -= 5;
                    if (time < 30) {
                        time = 30;
                    }
                    if (onDestroyed) {
                        break;
                    }
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            junk_apk_size.setText(MyUtils.convertStorage(finalI, true));
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void addSystemdata(final long size, List<JunkInfo> list) {
        for (JunkInfo info : list) {
            if (info.size > 0) {
                info.isChecked = true;
                adapterSystem.addData(info);
            }
        }
        junk_system_list.setVisibility(View.GONE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                int time = 100;
                for (long i = 0; i <= size; i += (size / 15)) {
                    final long finalI = i;
                    time -= 5;
                    if (time < 30) {
                        time = 30;
                    }
                    if (onDestroyed) {
                        break;
                    }
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            junk_system_size.setText(MyUtils.convertStorage(finalI, true));
                        }
                    });
                }
            }
        }).start();
    }


    @Override
    public void addUserdata(final long size, List<JunkInfo> list) {
        adapterUser.addDataList(list);
        junk_user_list.setVisibility(View.GONE);
//        junk_user_size.setText(MyUtils.getFileSizeKongge(size));
//        junkPresenter.setUnit(size, junk_user_unit);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int time = 100;
                for (long i = 0; i <= size; i += (size / 15)) {
                    final long finalI = i;
                    time -= 5;
                    if (time < 30) {
                        time = 30;
                    }
                    if (onDestroyed) {
                        break;
                    }
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            junk_user_size.setText(MyUtils.convertStorage(finalI, true));
                        }
                    });
                }
            }
        }).start();

    }

    @Override
    public void addLogdata(final long size, List<JunkInfo> list) {
        adapterLog.addDataList(list);
        junk_log_list.setVisibility(View.GONE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int time = 100;
                for (long i = 0; i <= size; i += (size / 15)) {
                    final long finalI = i;
                    time -= 5;
                    if (time < 30) {
                        time = 30;
                    }
                    if (onDestroyed) {
                        break;
                    }
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            junk_log_size.setText(MyUtils.convertStorage(finalI, true));
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void addRamdata(final long size, List<JunkInfo> list) {
        adapterRam.addDataList(list);
        junk_ram_list.setVisibility(View.GONE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int time = 100;
                for (long i = 0; i <= size; i += (size / 15)) {
                    final long finalI = i;
                    time -= 5;
                    if (time < 30) {
                        time = 30;
                    }
                    if (onDestroyed) {
                        break;
                    }
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            junk_ram_size.setText(MyUtils.convertStorage(finalI, true));
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void setUnit(long size, TextView textView) {
        textView.setText(MyUtils.convertStorageDanwei(size));
    }

    @Override
    public void addUnloaddata(final long size, List<JunkInfo> list) {
        adapterUnload.addDataList(list);
        junk_unload_list.setVisibility(View.GONE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int time = 100;
                for (long i = 0; i <= size; i += (size / 15)) {
                    final long finalI = i;
                    time -= 5;
                    if (time < 30) {
                        time = 30;
                    }
                    if (onDestroyed) {
                        break;
                    }
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            junk_unload_size.setText(MyUtils.convertStorage(finalI, true));
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void setCleanDAta(boolean isFirst, final long cleanSize) {
        if (isFirst) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int time = 100;
                    for (long i = 0; i <= cleanSize; i += (cleanSize / 15)) {
                        final long finalI = i;
                        time -= 5;
                        if (time < 30) {
                            time = 30;
                        }
                        if (onDestroyed) {
                            break;
                        }
                        try {
                            Thread.sleep(time);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (finalI != 0) {
                                    junk_button_clean.setText(getResources().getText(R.string.junk_button) + "(" + MyUtils.convertStorage(finalI, true) + ")");
                                }
                            }
                        });
                    }
                }
            }).start();
        } else {
            if (cleanSize != 0) {
                junk_button_clean.setText(getResources().getText(R.string.junk_button) + "(" + MyUtils.convertStorage(cleanSize, true) + ")");
            }
        }

    }

    int systemCount, systemCount1, apkCount, apkCount1, unloadCount, unloadCount1, logCount, logCount1, userCount, userCount1, ramCount, ramCount1;
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_left:
                    SetAdUtil.track("所有垃圾页面", "点击返回", "", 1);
                    onBackPressed();
                    break;
                case R.id.junk_button_system:
                    if (adapterSystem.getCount() == 0) {
                        break;
                    }
                    if (junk_system_list.getVisibility() == View.VISIBLE) {
                        SetAdUtil.track("所有垃圾页面", "点击收起系统缓存", "", 1);
                        if (systemCount == adapterSystem.getCount()) {
                            junk_system_check.setImageResource(R.mipmap.ram_passed);
                        } else if (systemCount == 0) {
                            junk_system_check.setImageResource(R.mipmap.ram_normal);
                        } else {
                            junk_system_check.setImageResource(R.mipmap.ram_check);
                        }
                        junk_system_jiantou.setImageResource(R.mipmap.junk_x);
                        junk_system_list.setVisibility(View.GONE);
                    } else {
                        SetAdUtil.track("所有垃圾页面", "点击打开系统缓存", "", 1);
                        systemCount1 = 0;
                        if (adapterSystem != null) {//回调函数
                            adapterSystem.setOnlistener(new CustomJunkRamAdapter.AllListener() {

                                @Override
                                public void onChecked(boolean check, int position, boolean oncli) {
                                    if (oncli) {
                                        systemCount = position;
                                    } else {
                                        systemCount = systemCount1;
                                    }
                                    if (systemCount < systemCount1 - 1) {
                                        systemCount = systemCount1 - 1;
                                    } else if (systemCount > systemCount1) {
                                        systemCount = systemCount1;
                                    }
                                    if (check) {
                                        systemCount++;
                                    } else if (!check) {
                                        if (!oncli) {
                                            systemCount--;
                                        }
                                    }
                                    systemCount1 = systemCount;
                                    if (systemCount == adapterSystem.getCount()) {
                                        junk_system_check.setImageResource(R.mipmap.ram_passed);
                                    } else if (systemCount == 0) {
                                        junk_system_check.setImageResource(R.mipmap.ram_normal);
                                    } else {
                                        junk_system_check.setImageResource(R.mipmap.ram_check);
                                    }
                                }
                            });
                        }
                        if (systemCount == adapterSystem.getCount()) {
                            junk_system_check.setImageResource(R.mipmap.ram_passed);
                        } else if (systemCount == 0) {
                            junk_system_check.setImageResource(R.mipmap.ram_normal);
                        } else {
                            junk_system_check.setImageResource(R.mipmap.ram_check);
                        }
                        junk_system_jiantou.setImageResource(R.mipmap.junk_up);
                        junk_system_list.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.junk_button_apk:
                    if (adapterApk.getCount() == 0) {
                        break;
                    }
                    if (junk_apk_list.getVisibility() == View.VISIBLE) {
                        SetAdUtil.track("所有垃圾页面", "点击收起apk文件", "", 1);
                        if (apkCount == adapterApk.getCount()) {
                            junk_apk_check.setImageResource(R.mipmap.ram_passed);
                        } else if (apkCount == 0) {
                            junk_apk_check.setImageResource(R.mipmap.ram_normal);
                        } else {
                            junk_apk_check.setImageResource(R.mipmap.ram_check);
                        }
                        junk_apk_jiaotou.setImageResource(R.mipmap.junk_x);
                        junk_apk_list.setVisibility(View.GONE);
                    } else {
                        apkCount1 = 0;
                        if (adapterApk != null) {//回调函数
                            adapterApk.setOnlistener(new CustomJunkRamAdapter.AllListener() {

                                @Override
                                public void onChecked(boolean check, int position, boolean oncli) {
                                    if (oncli) {
                                        apkCount = position;
                                    } else {
                                        apkCount = apkCount1;
                                    }
                                    if (apkCount < apkCount1 - 1) {
                                        apkCount = apkCount1 - 1;
                                    } else if (apkCount > apkCount1) {
                                        apkCount = apkCount1;
                                    }
                                    if (check) {
                                        apkCount++;
                                    } else if (!check) {
                                        if (!oncli) {
                                            apkCount--;
                                        }
                                    }
                                    apkCount1 = apkCount;
                                    if (apkCount == adapterApk.getCount()) {
                                        junk_apk_check.setImageResource(R.mipmap.ram_passed);
                                    } else if (apkCount == 0) {
                                        junk_apk_check.setImageResource(R.mipmap.ram_normal);
                                    } else {
                                        junk_apk_check.setImageResource(R.mipmap.ram_check);
                                    }
                                }
                            });
                        }
                        SetAdUtil.track("垃圾页面", "点击打开apk文件", "", 1);
                        junk_apk_jiaotou.setImageResource(R.mipmap.junk_up);
                        junk_apk_list.setVisibility(View.VISIBLE);
                        if (apkCount == adapterApk.getCount()) {
                            junk_apk_check.setImageResource(R.mipmap.ram_passed);
                        } else if (apkCount == 0) {
                            junk_apk_check.setImageResource(R.mipmap.ram_normal);
                        } else {
                            junk_apk_check.setImageResource(R.mipmap.ram_check);
                        }
                    }
                    break;
                case R.id.junk_button_unload:
                    if (adapterUnload.getCount() == 0) {
                        break;
                    }
                    if (junk_unload_list.getVisibility() == View.VISIBLE) {
                        SetAdUtil.track("所有垃圾页面", "点击收起unload文件", "", 1);
                        if (unloadCount == adapterUnload.getCount()) {
                            junk_unload_check.setImageResource(R.mipmap.ram_passed);
                        } else if (unloadCount == 0) {
                            junk_unload_check.setImageResource(R.mipmap.ram_normal);
                        } else {
                            junk_unload_check.setImageResource(R.mipmap.ram_check);
                        }
                        junk_unload_jiantou.setImageResource(R.mipmap.junk_x);
                        junk_unload_list.setVisibility(View.GONE);
                    } else {
                        unloadCount1 = 0;
                        if (adapterUnload != null) {//回调函数
                            adapterUnload.setOnlistener(new CustomJunkRamAdapter.AllListener() {

                                @Override
                                public void onChecked(boolean check, int position, boolean oncli) {
                                    if (oncli) {
                                        unloadCount = position;
                                    } else {
                                        unloadCount = unloadCount1;
                                    }
                                    if (unloadCount < unloadCount1 - 1) {
                                        unloadCount = unloadCount1 - 1;
                                    } else if (unloadCount > unloadCount1) {
                                        unloadCount = unloadCount1;
                                    }
                                    if (check) {
                                        unloadCount++;
                                    } else if (!check) {
                                        if (!oncli) {
                                            unloadCount--;
                                        }
                                    }
                                    unloadCount1 = unloadCount;
                                    if (unloadCount == adapterUnload.getCount()) {
                                        junk_unload_check.setImageResource(R.mipmap.ram_passed);
                                    } else if (unloadCount == 0) {
                                        junk_unload_check.setImageResource(R.mipmap.ram_normal);
                                    } else {
                                        junk_unload_check.setImageResource(R.mipmap.ram_check);
                                    }
                                }
                            });
                        }
                        SetAdUtil.track("垃圾页面", "点击打开apk文件", "", 1);
                        junk_unload_jiantou.setImageResource(R.mipmap.junk_up);
                        junk_unload_list.setVisibility(View.VISIBLE);
                        if (unloadCount == adapterUnload.getCount()) {
                            junk_unload_check.setImageResource(R.mipmap.ram_passed);
                        } else if (unloadCount == 0) {
                            junk_unload_check.setImageResource(R.mipmap.ram_normal);
                        } else {
                            junk_unload_check.setImageResource(R.mipmap.ram_check);
                        }
                    }
                    break;
                case R.id.junk_button_log:
                    if (adapterLog.getCount() == 0) {
                        break;
                    }
                    if (junk_log_list.getVisibility() == View.VISIBLE) {
                        SetAdUtil.track("所有垃圾页面", "点击收起log文件", "", 1);
                        if (logCount == adapterLog.getCount()) {
                            junk_log_check.setImageResource(R.mipmap.ram_passed);
                        } else if (logCount == 0) {
                            junk_log_check.setImageResource(R.mipmap.ram_normal);
                        } else {
                            junk_log_check.setImageResource(R.mipmap.ram_check);
                        }
                        junk_log_jiantou.setImageResource(R.mipmap.junk_x);
                        junk_log_list.setVisibility(View.GONE);
                    } else {
                        logCount1 = 0;
                        if (adapterLog != null) {//回调函数
                            adapterLog.setOnlistener(new CustomJunkRamAdapter.AllListener() {

                                @Override
                                public void onChecked(boolean check, int position, boolean oncli) {
                                    if (oncli) {
                                        logCount = position;
                                    } else {
                                        logCount = logCount1;
                                    }
                                    if (logCount < logCount1 - 1) {
                                        logCount = logCount1 - 1;
                                    } else if (logCount > logCount1) {
                                        logCount = logCount1;
                                    }
                                    if (check) {
                                        logCount++;
                                    } else if (!check) {
                                        if (!oncli) {
                                            logCount--;
                                        }
                                    }
                                    logCount1 = logCount;
                                    if (logCount == adapterLog.getCount()) {
                                        junk_log_check.setImageResource(R.mipmap.ram_passed);
                                    } else if (logCount == 0) {
                                        junk_log_check.setImageResource(R.mipmap.ram_normal);
                                    } else {
                                        junk_log_check.setImageResource(R.mipmap.ram_check);
                                    }
                                }
                            });
                        }
                        SetAdUtil.track("垃圾页面", "点击打开log文件", "", 1);
                        junk_log_jiantou.setImageResource(R.mipmap.junk_up);
                        junk_log_list.setVisibility(View.VISIBLE);
                        if (logCount == adapterLog.getCount()) {
                            junk_log_check.setImageResource(R.mipmap.ram_passed);
                        } else if (logCount == 0) {
                            junk_log_check.setImageResource(R.mipmap.ram_normal);
                        } else {
                            junk_log_check.setImageResource(R.mipmap.ram_check);
                        }
                    }
                    break;
                case R.id.junk_button_user:
                    if (adapterUser.getCount() == 0) {
                        break;
                    }
                    if (junk_user_list.getVisibility() == View.VISIBLE) {
                        SetAdUtil.track("所有垃圾页面", "点击收起user文件", "", 1);
                        if (userCount == adapterUser.getCount()) {
                            junk_user_check.setImageResource(R.mipmap.ram_passed);
                        } else if (userCount == 0) {
                            junk_user_check.setImageResource(R.mipmap.ram_normal);
                        } else {
                            junk_user_check.setImageResource(R.mipmap.ram_check);
                        }
                        junk_user_jiantou.setImageResource(R.mipmap.junk_x);
                        junk_user_list.setVisibility(View.GONE);
                    } else {
                        userCount1 = 0;
                        if (adapterUser != null) {//回调函数
                            adapterUser.setOnlistener(new CustomJunkRamAdapter.AllListener() {

                                @Override
                                public void onChecked(boolean check, int position, boolean oncli) {
                                    if (oncli) {
                                        userCount = position;
                                    } else {
                                        userCount = userCount1;
                                    }
                                    if (userCount < userCount1 - 1) {
                                        userCount = userCount1 - 1;
                                    } else if (userCount > userCount1) {
                                        userCount = userCount1;
                                    }
                                    if (check) {
                                        userCount++;
                                    } else if (!check) {

                                        if (!oncli) {
                                            userCount--;
                                        }
                                    }
                                    userCount1 = userCount;
                                    if (userCount == adapterUser.getCount()) {
                                        junk_user_check.setImageResource(R.mipmap.ram_passed);
                                    } else if (userCount == 0) {
                                        junk_user_check.setImageResource(R.mipmap.ram_normal);
                                    } else {
                                        junk_user_check.setImageResource(R.mipmap.ram_check);
                                    }
                                }
                            });
                        }
                        SetAdUtil.track("垃圾页面", "点击打开user文件", "", 1);
                        junk_user_jiantou.setImageResource(R.mipmap.junk_up);
                        junk_user_list.setVisibility(View.VISIBLE);
                        if (userCount == adapterUser.getCount()) {
                            junk_user_check.setImageResource(R.mipmap.ram_passed);
                        } else if (userCount == 0) {
                            junk_user_check.setImageResource(R.mipmap.ram_normal);
                        } else {
                            junk_user_check.setImageResource(R.mipmap.ram_check);
                        }
                    }

                    break;
                case R.id.junk_button_ram:
                    if (adapterRam.getCount() == 0) {
                        break;
                    }

                    if (junk_ram_list.getVisibility() == View.VISIBLE) {
                        SetAdUtil.track("所有垃圾页面", "点击收缩ram", "", 1);
                        if (ramCount == adapterRam.getCount()) {
                            junk_ram_check.setImageResource(R.mipmap.ram_passed);
                        } else if (ramCount == 0) {
                            junk_ram_check.setImageResource(R.mipmap.ram_normal);
                        } else {
                            junk_ram_check.setImageResource(R.mipmap.ram_check);
                        }
                        junk_ram_jiantou.setImageResource(R.mipmap.junk_x);
                        junk_ram_list.setVisibility(View.GONE);
                    } else {
                        ramCount1 = 0;
                        if (adapterRam != null) {//回调函数
                            adapterRam.setOnlistener(new CustomJunkRamAdapter.AllListener() {

                                @Override
                                public void onChecked(boolean check, int position, boolean oncli) {
                                    if (oncli) {
                                        ramCount = position;
                                    } else {
                                        ramCount = ramCount1;
                                    }
                                    if (ramCount < ramCount1 - 1) {
                                        ramCount = ramCount1 - 1;
                                    } else if (ramCount > ramCount1) {
                                        ramCount = ramCount1;
                                    }
                                    if (check) {
                                        ramCount++;
                                    } else if (!check) {
                                        if (!oncli) {
                                            ramCount--;
                                        }
                                    }
                                    ramCount1 = ramCount;
                                    if (ramCount == adapterRam.getCount()) {
                                        junk_ram_check.setImageResource(R.mipmap.ram_passed);
                                    } else if (ramCount == 0) {
                                        junk_ram_check.setImageResource(R.mipmap.ram_normal);
                                    } else {
                                        junk_ram_check.setImageResource(R.mipmap.ram_check);
                                    }
                                }
                            });
                        }
                        SetAdUtil.track("所有垃圾页面", "点击展开ram", "", 1);
                        junk_ram_jiantou.setImageResource(R.mipmap.junk_up);
                        junk_ram_list.setVisibility(View.VISIBLE);
                        if (userCount == adapterUser.getCount()) {
                            junk_ram_check.setImageResource(R.mipmap.ram_passed);
                        } else if (userCount == 0) {
                            junk_ram_check.setImageResource(R.mipmap.ram_normal);
                        } else {
                            junk_ram_check.setImageResource(R.mipmap.ram_check);
                        }
                    }
                    break;
                case R.id.junk_button_clean:
                    PreData.putDB(EasyRubbishAndRamActivity.this, EasyConstant.KEY_CLEAN_TIME, System.currentTimeMillis());
                    SetAdUtil.track("所有垃圾页面", "点击清理", "", 1);
                    junk_button_clean.setOnClickListener(null);
                    showToast((String) getText(R.string.toast_ing));
                    if (junk_system_list.getVisibility() == View.GONE && junk_apk_list.getVisibility() == View.GONE && junk_unload_list.getVisibility() == View.GONE &&
                            junk_log_list.getVisibility() == View.GONE && junk_user_list.getVisibility() == View.GONE && junk_ram_list.getVisibility() == View.GONE) {
                        junkPresenter.bleachFile(false, adapterSystem.getData(), adapterApk.getData(), adapterUnload.getData(), adapterLog.getData(), adapterUser.getData(), adapterRam.getData());
                    } else {
                        junkPresenter.bleachFile(true, adapterSystem.getData(), adapterApk.getData(), adapterUnload.getData(), adapterLog.getData(), adapterUser.getData(), adapterRam.getData());
                    }
                    break;
            }
        }
    };

    public void startCleanAnimation(final View view) {
        Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(300);
        animation.setFillAfter(true);
        view.startAnimation(animation);
    }

    @Override
    public void cleanAnimation(boolean isZhankai, final List<JunkInfo> cleanList, final long cleanSize) {
        final Bundle bundle = new Bundle();
        bundle.putLong("size", cleanSize);
        bundle.putString("from", "allJunk");
        if (isZhankai) {
            adapterClear.addDataList(cleanList);
            junk_list_all.setAdapter(adapterClear);
            junk_scroll.setVisibility(View.GONE);
            junk_list_all.setVisibility(View.VISIBLE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int a = adapterClear.getCount();
                    int time = 100;
                    for (int i = 0; i < a; i++) {
                        if (i > 10) {
                            time = 30;
                        }
                        if (onDestroyed) {
                            break;
                        }
                        try {
                            Thread.sleep(time--);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (adapterClear.getData().size() != 0) {
                                    adapterClear.removeData(adapterClear.getData(0));
                                    adapterClear.notifyDataSetChanged();
                                }
                            }
                        });
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            junkPresenter.jumpToActivity(EasySucceedActivity.class, bundle, 1);
                        }
                    });
                }
            }).start();

        } else {
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    startCleanAnimation(junk_button_system);
                }
            });
            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startCleanAnimation(junk_button_apk);
                }
            }, 100);
            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startCleanAnimation(junk_button_unload);
                }
            }, 200);
            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startCleanAnimation(junk_button_log);
                }
            }, 300);
            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startCleanAnimation(junk_button_user);
                }
            }, 400);
            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startCleanAnimation(junk_button_ram);
                }
            }, 500);
            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    junkPresenter.jumpToActivity(EasySucceedActivity.class, bundle, 1);
                }
            }, 800);

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        AndroidSdk.onResumeWithoutTransition(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            setResult(1);
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        if ("notifi".equals(getIntent().getStringExtra("from"))) {
            if (TextUtils.equals("twoday", getIntent().getStringExtra("from2"))) {
                SetAdUtil.track("通知栏", "两天唤醒", "点击", 1);
            } else {
                SetAdUtil.track("通知栏", "垃圾通知", "点击", 1);
            }
            jumpTo(MainActivity.class);
        }
        finish();
    }
}
