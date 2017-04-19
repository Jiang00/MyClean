package com.supers.clean.junk.activity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.supers.clean.junk.R;
import com.supers.clean.junk.view.JunkView;
import com.supers.clean.junk.adapter.JunkAdapter;
import com.supers.clean.junk.util.CommonUtil;
import com.supers.clean.junk.entity.JunkInfo;
import com.supers.clean.junk.customeview.ListViewForScrollView;
import com.supers.clean.junk.customeview.MyScrollView;
import com.supers.clean.junk.presenter.JunkPresenter;

import java.util.List;

/**
 * Created by on 2017/3/2.
 */

public class JunkActivity extends BaseActivity implements JunkView {

    FrameLayout title_left;
    TextView title_name;
    LinearLayout junk_title_backg;
    TextView junk_size_all;
    TextView junk_unit;
    TextView junk_fangxin;
    LinearLayout junk_button_system, junk_button_apk, junk_button_unload, junk_button_log, junk_button_user;
    TextView junk_system_size, junk_apk_size, junk_unload_size, junk_log_size, junk_user_size;
    TextView junk_system_unit, junk_apk_unit, junk_unload_unit, junk_log_unit, junk_user_unit;
    ImageView junk_system_jiantou, junk_apk_jiaotou, junk_unload_jiantou, junk_log_jiantou, junk_user_jiantou;
    ListViewForScrollView junk_system_list, junk_apk_list, junk_unload_list, junk_log_list, junk_user_list;
    Button junk_button_clean;
    MyScrollView junk_scroll;
    ListView junk_list_all;

    private JunkPresenter junkPresenter;
    private JunkAdapter adapterSystem, adapterApk, adapterUnload, adapterLog, adapterUser, adapterClear;
    private boolean color1 = true;
    private boolean color2 = true;
    private Handler myHandler;

    @Override
    protected void findId() {
        super.findId();
        title_left = $(R.id.title_left);
        title_name = $(R.id.title_name);
        junk_title_backg = $(R.id.junk_title_backg);
        junk_size_all = $(R.id.junk_size_all);
        junk_unit = $(R.id.junk_unit);
        junk_fangxin = $(R.id.junk_fangxin);
        junk_button_system = $(R.id.junk_button_system);
        junk_button_apk = $(R.id.junk_button_apk);
        junk_button_unload = $(R.id.junk_button_unload);
        junk_button_log = $(R.id.junk_button_log);
        junk_button_user = $(R.id.junk_button_user);
        junk_system_size = $(R.id.junk_system_size);
        junk_apk_size = $(R.id.junk_apk_size);
        junk_unload_size = $(R.id.junk_unload_size);
        junk_log_size = $(R.id.junk_log_size);
        junk_user_size = $(R.id.junk_user_size);
        junk_system_unit = $(R.id.junk_system_unit);
        junk_apk_unit = $(R.id.junk_apk_unit);
        junk_unload_unit = $(R.id.junk_unload_unit);
        junk_log_unit = $(R.id.junk_log_unit);
        junk_user_unit = $(R.id.junk_user_unit);
        junk_system_jiantou = $(R.id.junk_system_jiantou);
        junk_apk_jiaotou = $(R.id.junk_apk_jiaotou);
        junk_unload_jiantou = $(R.id.junk_unload_jiantou);
        junk_log_jiantou = $(R.id.junk_log_jiantou);
        junk_user_jiantou = $(R.id.junk_user_jiantou);
        junk_system_list = $(R.id.junk_system_list);
        junk_apk_list = $(R.id.junk_apk_list);
        junk_unload_list = $(R.id.junk_unload_list);
        junk_log_list = $(R.id.junk_log_list);
        junk_user_list = $(R.id.junk_user_list);
        junk_button_clean = $(R.id.junk_button_clean);
        junk_scroll = $(R.id.junk_scroll);
        junk_list_all = $(R.id.junk_list_all);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_junk);
        myHandler = new Handler();
        junkPresenter = new JunkPresenter(this, this);
        junkPresenter.init();
    }

    @Override
    public void loadFullAd() {

    }

    @Override
    public void onClick() {
        title_left.setOnClickListener(onClickListener);
        junk_button_system.setOnClickListener(onClickListener);
        junk_button_apk.setOnClickListener(onClickListener);
        junk_button_unload.setOnClickListener(onClickListener);
        junk_button_log.setOnClickListener(onClickListener);
        junk_button_user.setOnClickListener(onClickListener);
        junk_button_clean.setOnClickListener(onClickListener);
    }

    @Override
    public void initData(long allSize) {
        title_name.setText(R.string.main_junk_name);
        if (allSize <= 0) {
            junkPresenter.jumpToActivity(SuccessActivity.class, 1);
            return;
        }
        adapterSystem = new JunkAdapter(this, junkPresenter);
        adapterApk = new JunkAdapter(this, junkPresenter);
        adapterUnload = new JunkAdapter(this, junkPresenter);
        adapterLog = new JunkAdapter(this, junkPresenter);
        adapterUser = new JunkAdapter(this, junkPresenter);
        adapterClear = new JunkAdapter(this, junkPresenter);
        junk_system_list.setAdapter(adapterSystem);
        junk_apk_list.setAdapter(adapterApk);
        junk_unload_list.setAdapter(adapterUnload);
        junk_log_list.setAdapter(adapterLog);
        junk_user_list.setAdapter(adapterUser);
        junkPresenter.addAdapterData();
    }

    @Override
    public void setColor(final long allSize) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                int time = 100;
                for (long i = 0; i <= allSize; i += (allSize / 15)) {
                    if (onDestroyed) {
                        break;
                    }
                    final long finalI = i;
                    time -= 5;
                    if (time < 30) {
                        time = 30;
                    }
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            junk_size_all.setText(CommonUtil.getFileSize2(finalI));
                            junkPresenter.setUnit(allSize, junk_unit);
                        }
                    });
                }
            }
        }).start();

//        junk_size_all.setText(CommonUtil.getFileSize2(allSize));

        if (allSize > 1024 * 1024 * 100 && allSize <= 1024 * 1024 * 200) {
            if (color1) {
                color1 = false;
                ValueAnimator colorAnim = ObjectAnimator.ofInt(junk_title_backg, "backgroundColor", getResources().getColor(R.color.app_color_first), getResources().getColor(R.color.app_color_second));
                colorAnim.setDuration(2000);
                colorAnim.setRepeatCount(0);
                colorAnim.setEvaluator(new ArgbEvaluator());
                colorAnim.start();
            }
        } else if (allSize > 1024 * 1024 * 200) {
            if (color2) {
                color2 = false;
                ValueAnimator colorAnim = ObjectAnimator.ofInt(junk_title_backg, "backgroundColor", getResources().getColor(R.color.app_color_second), getResources().getColor(R.color.app_color_third));
                colorAnim.setDuration(2000);
                colorAnim.setRepeatCount(0);
                colorAnim.setEvaluator(new ArgbEvaluator());
                colorAnim.start();
            }
        }
    }

    @Override
    public void addSystemdata(final long size, List<JunkInfo> list) {
        adapterSystem.addDataList(list);
        junk_system_list.setVisibility(View.GONE);
//        junk_system_size.setText(CommonUtil.getFileSizeKongge(size));
//        junkPresenter.setUnit(size, junk_system_unit);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int time = 100;

                for (long i = 0; i <= size; i += (size / 15)) {
                    if (onDestroyed) {
                        break;
                    }
                    final long finalI = i;
                    time -= 5;
                    if (time < 30) {
                        time = 30;
                    }
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            junk_system_size.setText(CommonUtil.getFileSizeKongge(finalI));
                            junkPresenter.setUnit(size, junk_system_unit);
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void addApkdata(final long size, List<JunkInfo> list) {
        adapterApk.addDataList(list);
        junk_apk_list.setVisibility(View.GONE);
//        junk_apk_size.setText(CommonUtil.getFileSizeKongge(size));
//        junkPresenter.setUnit(size, junk_apk_unit);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int time = 100;
                for (long i = 0; i <= size; i += (size / 15)) {
                    if (onDestroyed) {
                        break;
                    }
                    final long finalI = i;
                    time -= 5;
                    if (time < 30) {
                        time = 30;
                    }
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            junk_apk_size.setText(CommonUtil.getFileSizeKongge(finalI));
                            junkPresenter.setUnit(size, junk_apk_unit);
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void addUnloaddata(final long size, List<JunkInfo> list) {
        adapterUnload.addDataList(list);
        junk_unload_list.setVisibility(View.GONE);
//        junk_unload_size.setText(CommonUtil.getFileSizeKongge(size));
//        junkPresenter.setUnit(size, junk_unload_unit);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int time = 100;
                for (long i = 0; i <= size; i += (size / 15)) {
                    if (onDestroyed) {
                        break;
                    }
                    final long finalI = i;
                    time -= 5;
                    if (time < 30) {
                        time = 30;
                    }
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            junk_unload_size.setText(CommonUtil.getFileSizeKongge(finalI));
                            junkPresenter.setUnit(size, junk_unload_unit);
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
//        junk_log_size.setText(CommonUtil.getFileSizeKongge(size));
//        junkPresenter.setUnit(size, junk_log_unit);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int time = 100;
                for (long i = 0; i <= size; i += (size / 15)) {
                    if (onDestroyed) {
                        break;
                    }
                    final long finalI = i;
                    time -= 5;
                    if (time < 30) {
                        time = 30;
                    }
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            junk_log_size.setText(CommonUtil.getFileSizeKongge(finalI));
                            junkPresenter.setUnit(size, junk_log_unit);
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
//        junk_user_size.setText(CommonUtil.getFileSizeKongge(size));
//        junkPresenter.setUnit(size, junk_user_unit);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int time = 100;
                for (long i = 0; i <= size; i += (size / 15)) {
                    if (onDestroyed) {
                        break;
                    }
                    final long finalI = i;
                    time -= 5;
                    if (time < 30) {
                        time = 30;
                    }
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            junk_user_size.setText(CommonUtil.getFileSizeKongge(finalI));
                            junkPresenter.setUnit(size, junk_user_unit);
                        }
                    });
                }
            }
        }).start();

    }

    @Override
    public void setUnit(long size, TextView textView) {
        if (size < 1024) {
            textView.setText("B");
        } else if (size < 1048576) {
            textView.setText("KB");
        } else if (size < 1073741824) {
            textView.setText("MB");
        } else {
            textView.setText("GB");
        }
    }

    @Override
    public void setCleanDAta(boolean isFirst, final long size) {
        if (isFirst) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int time = 100;
                    for (long i = 0; i <= size; i += (size / 15)) {
                        if (onDestroyed) {
                            break;
                        }
                        final long finalI = i;
                        time -= 5;
                        if (time < 30) {
                            time = 30;
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
                                    junk_button_clean.setText(getResources().getText(R.string.junk_button) + "(" + CommonUtil.getFileSize4(finalI) + ")");
                                }
                            }
                        });
                    }
                }
            }).start();
        } else {
            if (size != 0) {
                junk_button_clean.setText(getResources().getText(R.string.junk_button) + "(" + CommonUtil.getFileSize4(size) + ")");
            }
        }

    }

    @Override
    public void cleanAnimation(boolean isZhankai, List<JunkInfo> cleanList, long cleanSize) {
        final Bundle bundle = new Bundle();
        bundle.putLong("size", cleanSize);
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
                        if (onDestroyed) {
                            break;
                        }
                        if (i > 10) {
                            time = 30;
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
                            junkPresenter.jumpToActivity(SuccessActivity.class, bundle, 1);
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
                    junkPresenter.jumpToActivity(SuccessActivity.class, bundle, 1);
                }
            }, 700);

        }

    }

    public void startCleanAnimation(final View view) {
        Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(300);
        animation.setFillAfter(true);
        view.startAnimation(animation);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_left:
                    CommonUtil.track("垃圾页面", "点击返回", "", 1);
                    onBackPressed();
                    break;
                case R.id.junk_button_system:
                    if (adapterSystem.getCount() == 0) {
                        break;
                    }
                    if (junk_system_list.getVisibility() == View.VISIBLE) {
                        CommonUtil.track("垃圾页面", "点击收起系统缓存", "", 1);
                        junk_system_list.setVisibility(View.GONE);
                    } else {
                        CommonUtil.track("垃圾页面", "点击打开系统缓存", "", 1);
                        junk_system_list.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.junk_button_apk:
                    if (adapterApk.getCount() == 0) {
                        break;
                    }
                    if (junk_apk_list.getVisibility() == View.VISIBLE) {
                        CommonUtil.track("垃圾页面", "点击收起apk文件", "", 1);
                        junk_apk_list.setVisibility(View.GONE);
                    } else {
                        CommonUtil.track("垃圾页面", "点击打开apk文件", "", 1);
                        junk_apk_list.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.junk_button_unload:
                    if (adapterUnload.getCount() == 0) {
                        break;
                    }
                    if (junk_unload_list.getVisibility() == View.VISIBLE) {
                        CommonUtil.track("垃圾页面", "点击收起unload文件", "", 1);
                        junk_unload_list.setVisibility(View.GONE);
                    } else {
                        CommonUtil.track("垃圾页面", "点击打开apk文件", "", 1);
                        junk_unload_list.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.junk_button_log:
                    if (adapterLog.getCount() == 0) {
                        break;
                    }
                    if (junk_log_list.getVisibility() == View.VISIBLE) {
                        CommonUtil.track("垃圾页面", "点击收起log文件", "", 1);
                        junk_log_list.setVisibility(View.GONE);
                    } else {
                        CommonUtil.track("垃圾页面", "点击打开log文件", "", 1);
                        junk_log_list.setVisibility(View.VISIBLE);
                    }

                    break;
                case R.id.junk_button_user:
                    if (adapterUser.getCount() == 0) {
                        break;
                    }
                    if (junk_user_list.getVisibility() == View.VISIBLE) {
                        CommonUtil.track("垃圾页面", "点击收起user文件", "", 1);
                        junk_user_list.setVisibility(View.GONE);
                    } else {
                        CommonUtil.track("垃圾页面", "点击打开user文件", "", 1);
                        junk_user_list.setVisibility(View.VISIBLE);
                    }

                    break;
                case R.id.junk_button_clean:
                    junk_button_clean.setOnClickListener(null);
                    showToast((String) getText(R.string.toast_ing));
                    Log.e("aaa", "===junk点击");
                    CommonUtil.track("垃圾页面", "点击清理", "", 1);
                    if (junk_system_list.getVisibility() == View.GONE && junk_apk_list.getVisibility() == View.GONE && junk_unload_list.getVisibility() == View.GONE &&
                            junk_log_list.getVisibility() == View.GONE && junk_user_list.getVisibility() == View.GONE) {
                        junkPresenter.bleachFile(false, adapterSystem.getData(), adapterApk.getData(), adapterUnload.getData(), adapterLog.getData(), adapterUser.getData());
                    } else {
                        junkPresenter.bleachFile(true, adapterSystem.getData(), adapterApk.getData(), adapterUnload.getData(), adapterLog.getData(), adapterUser.getData());
                    }

                    break;


            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myHandler != null) {
            myHandler.removeCallbacksAndMessages(null);
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
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
