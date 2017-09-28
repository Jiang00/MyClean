package com.supers.clean.junk.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.clean.core.CleanManager;
import com.android.clean.db.CleanDBHelper;
import com.android.clean.gboost.GameBooster;
import com.android.clean.util.PreData;
import com.android.clean.util.Util;
import com.android.clean.util.LoadManager;
import com.fraumobi.call.Utils.BadgerCount;
import com.rd.PageIndicatorView;
import com.supers.clean.junk.R;
import com.supers.clean.junk.adapter.AddGameAdapter;
import com.supers.clean.junk.customeview.LaunchpadAdapter;
import com.supers.clean.junk.customeview.PagerView;
import com.android.clean.entity.JunkInfo;
import com.supers.clean.junk.util.AdUtil;
import com.android.clean.util.MemoryManager;
import com.android.clean.util.Constant;
import com.supers.clean.junk.util.ShortCutUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivy on 2017/5/5.
 */

public class GBoostActivity extends BaseActivity {
    private static final int ADD_DATA = 0;

    FrameLayout title_left;
    TextView title_name;
    ImageView add_right;
    ImageButton clear;
    EditText search_edit_text;
    PagerView gboost_recyc;
    PageIndicatorView pageindicatorview;
    TextView gboost_ram_size;
    TextView gboost_network_size;
    TextView gboost_cpu_szie;
    ImageView gboost_power_check;
    Button gboost_clean_button;
    LinearLayout ll_add_game;
    FrameLayout add_left;
    ListView list_game;
    TextView add_short;


    private LaunchpadAdapter adapter;
    private ArrayList<String> list;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ADD_DATA:

                    break;
            }

            super.handleMessage(msg);
        }
    };
    private AddGameAdapter whiteListAdapter;
    private MyApplication cleanApplication;

    private long total;
    private long idle;
    private long lastTotalRxBytes;
    private long lastTimeStamp;
    private boolean search;
    private List<JunkInfo> gboost_add, listEdit;
    private int screenWidth;
    private int width;
    private LinearLayout ll_ad;

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        add_right = (ImageView) findViewById(R.id.add_right);
        clear = (ImageButton) findViewById(R.id.clear);
        search_edit_text = (EditText) findViewById(R.id.search_edit_text);

        gboost_recyc = (PagerView) findViewById(R.id.gboost_recyc);
        pageindicatorview = (PageIndicatorView) findViewById(R.id.pageindicatorview);
        gboost_ram_size = (TextView) findViewById(R.id.gboost_ram_size);
        gboost_network_size = (TextView) findViewById(R.id.gboost_network_size);
        gboost_cpu_szie = (TextView) findViewById(R.id.gboost_cpu_szie);
        gboost_power_check = (ImageView) findViewById(R.id.gboost_power_check);
        gboost_clean_button = (Button) findViewById(R.id.gboost_clean_button);
        ll_add_game = (LinearLayout) findViewById(R.id.ll_add_game);
        add_left = (FrameLayout) findViewById(R.id.add_left);
        list_game = (ListView) findViewById(R.id.list_game);
        add_short = (TextView) findViewById(R.id.add_short);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_gboost);
        PreData.putDB(this, Constant.HONG_GBOOST, false);
        BadgerCount.setCount(this);
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        mHandler = new Handler();
        cleanApplication = (MyApplication) getApplication();

        title_left.setOnClickListener(clickListener);
        add_left.setOnClickListener(clickListener);
        title_name.setText(R.string.gboost_0);

        long size = MemoryManager.getPhoneFreeRamMemory(this);
        gboost_ram_size.setText(Util.convertStorage(size, true));

        if (Util.isAccessibilitySettingsOn(this)) {
            gboost_power_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            gboost_power_check.setImageResource(R.mipmap.side_check_normal);
        }
        gboost_power_check.setOnClickListener(clickListener);
        gboost_clean_button.setOnClickListener(clickListener);
        add_right.setOnClickListener(clickListener);
        clear.setOnClickListener(clickListener);
        add_short.setOnClickListener(clickListener);
        gboost_add = new ArrayList<>();
        listEdit = new ArrayList<>();
        initList();
        addAd();
    }

    private void addAd() {
        View native_xiao = AdUtil.getNativeAdView("", R.layout.native_ad_3);
        if (ll_ad != null && native_xiao != null) {
            ll_ad.addView(native_xiao);
            ll_ad.setVisibility(View.VISIBLE);
        }
    }

    private void initList() {
        list = new ArrayList<>();
        addData();

    }

    private void addData() {
        if (PreData.getDB(GBoostActivity.this, Constant.GBOOST_LUN, true)) {
            PreData.putDB(GBoostActivity.this, Constant.GBOOST_LUN, false);
            list.addAll(GameBooster.getInstalledGameList(GBoostActivity.this));
            shortGame(false);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> gboost_names = CleanDBHelper.getInstance(GBoostActivity.this).getWhiteList(CleanDBHelper.TableType.GameBoost);
                for (String pkg : gboost_names) {
                    if (LoadManager.getInstance(GBoostActivity.this).isPkgInstalled(pkg)) {
                        list.add(pkg);
                    }
                }
                Message msg = mHandler.obtainMessage();
                msg.what = ADD_DATA;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        list.add(0, getString(R.string.gboost_7));
                        Log.e("jfy", list.size() + "==");
                        adapter = new LaunchpadAdapter(GBoostActivity.this, list);
                        gboost_recyc.setAdapter(adapter);
                        gboost_recyc.refreshView();
                        if (list.size() <= 12) {
                            pageindicatorview.setCount(0);
                        } else if (list.size() <= 24) {
                            pageindicatorview.setCount(2);
                        } else if (list.size() <= 36) {
                            pageindicatorview.setCount(3);
                        } else if (list.size() <= 48) {
                            pageindicatorview.setCount(3);
                        } else {
                            pageindicatorview.setCount(5);
                        }
                        gboost_recyc.setOnPageChangedListener(new PagerView.OnPageChangedListener() {
                            @Override
                            public void onPageChange(int oldPage, int newPage) {
                                pageindicatorview.setSelection(newPage);
                            }
                        });
                        gboost_recyc.setOnItemClickListener(new PagerView.OnItemClickListener() {
                            @Override
                            public void onClick(Object o, int pos) {
                                if (pos == 0) {
                                    AdUtil.track("游戏加速页面", "点击添加游戏", "", 1);
                                    ll_add_game.setVisibility(View.VISIBLE);
                                    whiteListAdapter = new AddGameAdapter(GBoostActivity.this, list);
                                    list_game.setAdapter(whiteListAdapter);
                                    initData();
                                } else {
                                    try {
                                        AdUtil.track("游戏加速页面", "点击启动游戏", list.get(pos), 1);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("from", "GBoost");
                                        bundle.putString("packageName", list.get(pos));
                                        jumpToActivity(PowerActivity.class, bundle);
                                    } catch (Exception e) {
                                    }
                                }
                            }
                        });

                    }
                });
            }
        }).start();

    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add_left:
                    ll_add_game.setVisibility(View.GONE);
                    gboost_recyc.refreshView();
                    if (list.size() <= 12) {
                        pageindicatorview.setCount(0);
                    } else if (list.size() <= 24) {
                        pageindicatorview.setCount(2);
                    } else if (list.size() <= 36) {
                        pageindicatorview.setCount(3);
                    } else if (list.size() <= 48) {
                        pageindicatorview.setCount(3);
                    } else {
                        pageindicatorview.setCount(5);
                    }
//                    gboost_recyc.scrollBy(2000, 0);
                    shortGame(false);
                    break;
                case R.id.title_left:
                    onBackPressed();
                    break;
                case R.id.add_right:
                    toggleEditAnimation();
                    break;
                case R.id.clear:
                    toggleEditAnimation();
                    break;

                case R.id.gboost_power_check:
                    AdUtil.track("游戏加速页面", "开启辅助功能", "", 1);
                    try {
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        startActivityForResult(intent, 100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent transintent = new Intent(GBoostActivity.this, PermissionActivity.class);
                            startActivity(transintent);
                        }
                    }, 1500);
                    break;
                case R.id.gboost_clean_button:
                    AdUtil.track("游戏加速页面", "点击一键加速", "", 1);
                    Bundle bundle = new Bundle();
                    bundle.putString("from", "GBoost");
                    jumpToActivity(PowerActivity.class, bundle, 1);
                    break;
                case R.id.add_short:
                    shortGame(true);
                    break;
            }
        }
    };

    private void toggleEditAnimation() {
        final View searchView = findViewById(R.id.goost_container);
        View normalView = findViewById(R.id.normal_bar);

        final View visibleView, invisibleView;
        if (searchView.getVisibility() == View.GONE) {
            visibleView = normalView;
            invisibleView = searchView;
        } else {
            visibleView = searchView;
            invisibleView = normalView;
//            showSoftKeyboard(AbsActivity.this, null, false);
        }

        final ObjectAnimator invis2vis = ObjectAnimator.ofFloat(invisibleView, "rotationY", -90, 0);
        invis2vis.setDuration(500);
        invis2vis.setInterpolator(new LinearInterpolator());
        ObjectAnimator vis2invis = ObjectAnimator.ofFloat(visibleView, "rotationY", 0, 90);
        vis2invis.setDuration(500);
        vis2invis.setInterpolator(new LinearInterpolator());

        vis2invis.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                visibleView.setVisibility(View.GONE);
                invisibleView.setVisibility(View.VISIBLE);

                if (search) {
                    initData();
                    search = false;
                } else {
                    search_edit_text.setText("");
                    search_edit_text.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void afterTextChanged(Editable s) {
                            upData(s.toString());
                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }
                    });
                    search = true;
                }

                invis2vis.start();
            }
        });
        vis2invis.start();
    }

    private long getCup() {
        long usage = 0;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("/proc/stat")), 1000);
            String load = reader.readLine();
            reader.close();

            String[] toks = load.split(" ");

            long currTotal = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4]);
            long currIdle = Long.parseLong(toks[5]);

            usage = (long) ((currTotal - total) * 100.0f / (currTotal - total + currIdle - idle));
            total = currTotal;
            idle = currIdle;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return usage;
    }

    private long getNetWork() {
        long nowTotalRxBytes = getTotalRxBytes(); // 获取当前数据总量
        long nowTimeStamp = System.currentTimeMillis(); // 当前时间
        long speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp == lastTimeStamp ? nowTimeStamp : nowTimeStamp
                - lastTimeStamp));// 毫秒转换
//        gboost_network_size.setText(Util.convertStorageWifi(speed));
        lastTimeStamp = nowTimeStamp;
        lastTotalRxBytes = nowTotalRxBytes;
        return speed;
    }

    private long getTotalRxBytes() {
        // 得到整个手机的流量值
        return TrafficStats.getUidRxBytes(getApplicationInfo().uid) == TrafficStats.UNSUPPORTED ? 0
                : (TrafficStats.getTotalRxBytes());//
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final long sizeall = MemoryManager.getPhoneTotalRamMemory();
                while (!onPause) {
                    final int cup = (int) getCup();
                    final int speed = (int) getNetWork();
                    final long size = MemoryManager.getPhoneFreeRamMemory(GBoostActivity.this);
                    final int memo = (int) (size * 100 / sizeall);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (cup <= 40) {
                                gboost_cpu_szie.setTextColor(ContextCompat.getColor(GBoostActivity.this, R.color.gboost_1));
                            } else if (cup <= 80) {
                                gboost_cpu_szie.setTextColor(ContextCompat.getColor(GBoostActivity.this, R.color.gboost_2));
                            } else {
                                gboost_cpu_szie.setTextColor(ContextCompat.getColor(GBoostActivity.this, R.color.gboost_3));
                            }
                            if (memo <= 20) {
                                gboost_ram_size.setTextColor(ContextCompat.getColor(GBoostActivity.this, R.color.gboost_3));
                            } else if (memo <= 60) {
                                gboost_ram_size.setTextColor(ContextCompat.getColor(GBoostActivity.this, R.color.gboost_2));
                            } else {
                                gboost_ram_size.setTextColor(ContextCompat.getColor(GBoostActivity.this, R.color.gboost_1));
                            }
                            gboost_cpu_szie.setText(cup + "%");
                            gboost_network_size.setText(Util.convertStorageWifi(speed));
                            gboost_ram_size.setText(Util.convertStorage(size, true));
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        if (ll_add_game.getVisibility() == View.VISIBLE) {
            ll_add_game.setVisibility(View.GONE);
            gboost_recyc.refreshView();
            if (list.size() <= 12) {
                pageindicatorview.setCount(0);
            } else if (list.size() <= 24) {
                pageindicatorview.setCount(2);
            } else if (list.size() <= 36) {
                pageindicatorview.setCount(3);
            } else if (list.size() <= 48) {
                pageindicatorview.setCount(3);
            } else {
                pageindicatorview.setCount(5);
            }
            shortGame(false);
        } else {
            finish();
        }
    }

    private void shortGame(boolean isChuangjian) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                search_edit_text.setText("");
            }
        });

        Intent shortcutIntent = new Intent();
        shortcutIntent.setAction(Intent.ACTION_VIEW);
        shortcutIntent.setComponent(new ComponentName(getPackageName(),
                GameShortCutActivity.class.getCanonicalName()));
        String title = GBoostActivity.this.getString(R.string.gboost_0);
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
        if (true) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.short_7);
            ShortCutUtils.addShortcut(GBoostActivity.this, shortcutIntent, title, false, bitmap);
            return;
        }
        if (list.size() > 1 && (!PreData.getDB(GBoostActivity.this, Constant.GBOOST_SI, false) || isChuangjian)) {
            View shortcut_view = View.inflate(GBoostActivity.this, R.layout.layout_gboost_short, null);
            if (list.size() > 1) {
                ImageView iv_1 = (ImageView) shortcut_view.findViewById(R.id.iv_1);
                iv_1.setImageDrawable(LoadManager.getInstance(GBoostActivity.this).getAppIcon(list.get(1)));
            }
            if (list.size() > 2) {
                ImageView iv_2 = (ImageView) shortcut_view.findViewById(R.id.iv_2);
                iv_2.setImageDrawable(LoadManager.getInstance(GBoostActivity.this).getAppIcon(list.get(2)));
            }
            if (list.size() > 3) {
                ImageView iv_3 = (ImageView) shortcut_view.findViewById(R.id.iv_3);
                iv_3.setImageDrawable(LoadManager.getInstance(GBoostActivity.this).getAppIcon(list.get(3)));
            }
            if (list.size() > 4) {
                ImageView iv_4 = (ImageView) shortcut_view.findViewById(R.id.iv_4);
                iv_4.setImageDrawable(LoadManager.getInstance(GBoostActivity.this).getAppIcon(list.get(4)));
                PreData.putDB(GBoostActivity.this, Constant.GBOOST_SI, true);
            }

            Bitmap bitmap = Util.getViewBitmap(shortcut_view);
            if (bitmap != null) {
//                Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
//                shortcutIntent.setClass(GBoostActivity.this, GBoostActivity.class);
//                shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                ShortCutUtils.removeShortcut(GBoostActivity.this, shortcutIntent, title);
                ShortCutUtils.addShortcut(GBoostActivity.this, shortcutIntent, title, false, bitmap);
            }
        }
    }

    private void initData() {
        ArrayList<String> gboost_names = CleanDBHelper.getInstance(this).getWhiteList(CleanDBHelper.TableType.GameBoost);
        gboost_add.clear();
        for (JunkInfo info : CleanManager.getInstance(this).getAppList()) {
            boolean isA = false;
            for (String packagename : gboost_names) {
                if (packagename.equals(info.pkg)) {
                    isA = true;
                }
            }
            if (!isA) {
                gboost_add.add(info);
            }
        }
        whiteListAdapter.upList(gboost_add);
        whiteListAdapter.notifyDataSetChanged();
    }

    private void upData(String string) {
        listEdit.clear();
        for (JunkInfo info : gboost_add) {
            if (info.label.contains(string)) {
                listEdit.add(info);
            }
        }
        whiteListAdapter.upList(listEdit);
        whiteListAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 || requestCode == 1) {
            if (Util.isAccessibilitySettingsOn(this)) {
                gboost_power_check.setImageResource(R.mipmap.side_check_passed);
            } else {
                gboost_power_check.setImageResource(R.mipmap.side_check_normal);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
