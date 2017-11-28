package com.easy.junk.easyactivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.easy.clean.core.CleanManager;
import com.easy.clean.easydb.CleanDBHelper;
import com.easy.clean.easygoodgame.GameBooster;
import com.easy.clean.easyutils.LoadManager;
import com.easy.clean.easyutils.MemoryManager;
import com.easy.clean.easyutils.MyUtils;
import com.easy.clean.easyutils.PreData;
import com.easy.clean.entity.JunkInfo;
import com.easy.junk.R;
import com.easy.junk.easycustomadapter.JiaGoodGameAdapter;
import com.easy.junk.easycustomview.EasyGridViewAdAdapter;
import com.easy.junk.easycustomview.EasyLaunchpadAdapter;
import com.easy.junk.easycustomview.WidgetContainer;
import com.easy.junk.easytools.EasyConstant;
import com.easy.junk.easytools.SetAdUtil;
import com.easy.junk.easytools.ShortCutUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2017/5/5.
 */

public class EasyGoodGameActivity extends BaseActivity {
    private static final int ADD_DATA = 0;
    private EasyLaunchpadAdapter adapter;
    private EasyGridViewAdAdapter gridViewAdAdapter;
    private long lastTotalRxBytes;
    private List<JunkInfo> gboost_add, listEdit;
    private long lastTimeStamp;
    LinearLayout ll_add_game;
    TextView gboost_clean_button;
    private ArrayList<String> list;
    ImageButton clear;
    ImageView add_right;
    private long total;
    private long idle;
    private static boolean flag = false;
    private JiaGoodGameAdapter whiteListAdapter;
    private boolean search;
    private int screenWidth;
    FrameLayout add_left;
    ImageView gboost_power_check;
    ListView list_game;
    EditText search_edit_text;
    ImageView gboost_short_iv;
    FrameLayout title_left;
    TextView title_name;
    TextView gboost_short_add;
    private int width;
    TextView gboost_item_textview;
    GridView gboost_gridview;

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
    private AlertDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_gboost1);
        registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        mHandler = new Handler();

        title_left.setOnClickListener(clickListener);

        add_left.setOnClickListener(clickListener);
        gboost_clean_button.setOnClickListener(clickListener);
        title_name.setText(R.string.gboost_0);

        long size = MemoryManager.getPhoneFreeRamMemory(this);
        if (MyUtils.isAccessibilitySettingsOn(this)) {
            gboost_power_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            gboost_power_check.setImageResource(R.mipmap.side_check_normal);
        }
        gboost_power_check.setOnClickListener(clickListener);
        add_right.setOnClickListener(clickListener);
        clear.setOnClickListener(clickListener);
        gboost_short_add.setOnClickListener(clickListener);
        gboost_add = new ArrayList<>();
        listEdit = new ArrayList<>();
        initList();
    }

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        add_right = (ImageView) findViewById(R.id.add_right);
        clear = (ImageButton) findViewById(R.id.clear);
        search_edit_text = (EditText) findViewById(R.id.search_edit_text);
        gboost_gridview = (GridView) findViewById(R.id.gboost_gridview);
        gboost_power_check = (ImageView) findViewById(R.id.gboost_power_check);
        ll_add_game = (LinearLayout) findViewById(R.id.ll_add_game);
        add_left = (FrameLayout) findViewById(R.id.add_left);
        list_game = (ListView) findViewById(R.id.list_game);
        gboost_item_textview = (TextView) findViewById(R.id.gboost_item_textview);
        gboost_clean_button = (TextView) findViewById(R.id.gboost_clean_button);
        gboost_short_add = (TextView) findViewById(R.id.gboost_short_add);
        gboost_short_iv = (ImageView) findViewById(R.id.gboost_short_iv);
    }

    private void initIcon() {
        View shortcut_view = View.inflate(EasyGoodGameActivity.this, R.layout.layout_gboost_short, null);
        if (list.size() > 1) {
            ImageView iv_1 = (ImageView) shortcut_view.findViewById(R.id.iv_1);
            iv_1.setImageDrawable(LoadManager.getInstance(EasyGoodGameActivity.this).getAppIcon(list.get(1)));
        }
        if (list.size() > 2) {
            ImageView iv_2 = (ImageView) shortcut_view.findViewById(R.id.iv_2);
            iv_2.setImageDrawable(LoadManager.getInstance(EasyGoodGameActivity.this).getAppIcon(list.get(2)));
        }
        if (list.size() > 3) {
            ImageView iv_3 = (ImageView) shortcut_view.findViewById(R.id.iv_3);
            iv_3.setImageDrawable(LoadManager.getInstance(EasyGoodGameActivity.this).getAppIcon(list.get(3)));
        }
        if (list.size() > 4) {
            ImageView iv_4 = (ImageView) shortcut_view.findViewById(R.id.iv_4);
            iv_4.setImageDrawable(LoadManager.getInstance(EasyGoodGameActivity.this).getAppIcon(list.get(4)));
        }
        Bitmap bitmap = MyUtils.getViewBitmap(shortcut_view);
        gboost_short_iv.setImageBitmap(bitmap);
    }

    private void initList() {
        list = new ArrayList<>();
        addData();
    }

    private void addData() {
        if (PreData.getDB(EasyGoodGameActivity.this, EasyConstant.GBOOST_LUN, true)) {
            PreData.putDB(EasyGoodGameActivity.this, EasyConstant.GBOOST_LUN, false);
            shortGame(false);
        }
        list.addAll(GameBooster.getInstalledGameList(EasyGoodGameActivity.this));
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> gboost_names = CleanDBHelper.getInstance(EasyGoodGameActivity.this).getWhiteList(CleanDBHelper.TableType.GameBoost);
                for (String pkg : gboost_names) {
                    if (LoadManager.getInstance(EasyGoodGameActivity.this).isPkgInstalled(pkg)) {
                        list.add(pkg);
                    }
                }
                Message msg = mHandler.obtainMessage();
                msg.what = ADD_DATA;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        list.add(0, getString(R.string.gboost_7));
//                        adapter = new EasyLaunchpadAdapter(EasyGoodGameActivity.this, list);
                        gridViewAdAdapter = new EasyGridViewAdAdapter(EasyGoodGameActivity.this, list);
                        gboost_gridview.setAdapter(gridViewAdAdapter);
                        gboost_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if (position == 0) {
                                    SetAdUtil.track("游戏加速页面", "点击添加游戏", "", 1);
                                    ll_add_game.setVisibility(View.VISIBLE);
                                    Log.e("gboost_clean_button", "=======1=====" + gboost_clean_button.getVisibility());
                                    gboost_clean_button.setVisibility(View.GONE);
                                    Log.e("gboost_clean_button", "=======2=====" + gboost_clean_button.getVisibility());
                                    whiteListAdapter = new JiaGoodGameAdapter(EasyGoodGameActivity.this, list);
                                    list_game.setAdapter(whiteListAdapter);
                                    initData();
                                } else {
                                    try {
                                        SetAdUtil.track("游戏加速页面", "点击启动游戏", list.get(position), 1);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("from", "GBoost");
                                        bundle.putString("packageName", list.get(position));
                                        jumpToActivity(EasyDeepingActivity.class, bundle);
                                    } catch (Exception e) {
                                    }
                                }
                            }
                        });
                        initIcon();
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
                    gboost_clean_button.setVisibility(View.VISIBLE);
//                    initList();
                    gridViewAdAdapter.notifyDataSetChanged();
                    shortGame(false);
                    break;
                case R.id.gboost_short_add:
                    SetAdUtil.track("游戏加速页面", "点击添加快捷方式", "", 1);
                    shortGame(true);
                    break;
                case R.id.gboost_clean_button:
                    SetAdUtil.track("游戏加速页面", "点击一键加速", "", 1);
                    Bundle bundle = new Bundle();
                    bundle.putString("from", "GBoost");
                    jumpToActivity(EasyDeepingActivity.class, bundle, 1);
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
                    SetAdUtil.track("游戏加速页面", "开启辅助功能", "", 1);
                    permissIntent();
                    break;
            }
        }
    };

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (ll_add_game.getVisibility() == View.VISIBLE) {
                    ll_add_game.setVisibility(View.GONE);
                    gboost_clean_button.setVisibility(View.VISIBLE);
//                    initList();
                    gridViewAdAdapter.notifyDataSetChanged();
                    shortGame(false);
                } else {
                    return super.onKeyDown(keyCode, event);
                }
        }
        return false;
    }

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

    private long getTotalRxBytes() {
        // 得到整个手机的流量值
        return TrafficStats.getUidRxBytes(getApplicationInfo().uid) == TrafficStats.UNSUPPORTED ? 0
                : (TrafficStats.getTotalRxBytes());//
    }

    private long getNetWork() {
        long nowTotalRxBytes = getTotalRxBytes(); // 获取当前数据总量
        long nowTimeStamp = System.currentTimeMillis(); // 当前时间
        long speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp == lastTimeStamp ? nowTimeStamp : nowTimeStamp
                - lastTimeStamp));// 毫秒转换
        lastTimeStamp = nowTimeStamp;
        lastTotalRxBytes = nowTotalRxBytes;
        return speed;
    }

    //游戏加速快捷键
    private void shortGame(boolean isChuangjian) {
        search_edit_text.setText("");
        Log.e("short", "chuangjian1 ");
        Intent shortcutIntent = new Intent();
        shortcutIntent.setAction(Intent.ACTION_VIEW);
        shortcutIntent.setComponent(new ComponentName(getPackageName(),
                EasyGoodGameActivity.class.getCanonicalName()));
        String title = EasyGoodGameActivity.this.getString(R.string.gboost_0);
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
        if (true) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.gboost_short1);
            ShortCutUtils.addShortcut(EasyGoodGameActivity.this, shortcutIntent, title, false, bitmap);
            gboost_short_iv.setImageBitmap(bitmap);
            return;
        }
        Log.e("short", "chuangjian1===" + list.size());
        if (list.size() > 0 && (!PreData.getDB(EasyGoodGameActivity.this, EasyConstant.GBOOST_SI, false) || isChuangjian)) {
            View shortcut_view = View.inflate(EasyGoodGameActivity.this, R.layout.layout_gboost_short, null);
            if (list.size() > 1) {
                ImageView iv_1 = (ImageView) shortcut_view.findViewById(R.id.iv_1);
                iv_1.setImageDrawable(LoadManager.getInstance(EasyGoodGameActivity.this).getAppIcon(list.get(1)));
            }
            if (list.size() > 2) {
                ImageView iv_2 = (ImageView) shortcut_view.findViewById(R.id.iv_2);
                iv_2.setImageDrawable(LoadManager.getInstance(EasyGoodGameActivity.this).getAppIcon(list.get(2)));
            }
            if (list.size() > 3) {
                ImageView iv_3 = (ImageView) shortcut_view.findViewById(R.id.iv_3);
                iv_3.setImageDrawable(LoadManager.getInstance(EasyGoodGameActivity.this).getAppIcon(list.get(3)));
            }
            if (list.size() > 4) {
                ImageView iv_4 = (ImageView) shortcut_view.findViewById(R.id.iv_4);
                iv_4.setImageDrawable(LoadManager.getInstance(EasyGoodGameActivity.this).getAppIcon(list.get(4)));
                PreData.putDB(EasyGoodGameActivity.this, EasyConstant.GBOOST_SI, true);
            }

            Bitmap bitmap = MyUtils.getViewBitmap(shortcut_view);
            if (bitmap != null) {
                Log.e("short", "chuangjian ");
                ShortCutUtils.removeShortcut(EasyGoodGameActivity.this, shortcutIntent, title);
                ShortCutUtils.addShortcut(EasyGoodGameActivity.this, shortcutIntent, title, false, bitmap);
                gboost_short_iv.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (gridViewAdAdapter != null) {
            gridViewAdAdapter.notifyDataSetChanged();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                final long sizeall = MemoryManager.getPhoneTotalRamMemory();
                while (!onPause) {
                    final int cup = (int) getCup();
                    final int speed = (int) getNetWork();
                    final long size = MemoryManager.getPhoneFreeRamMemory(EasyGoodGameActivity.this);
                    final int memo = (int) (size * 100 / sizeall);
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
            gridViewAdAdapter.notifyDataSetChanged();
            shortGame(false);
        } else {
            finish();
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
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                if (widgetContainer != null) {
                    widgetContainer.removeFromWindow();
                }
            }
        }
    };

    public void permissIntent() {
        try {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivityForResult(intent, 100);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mHandler.postDelayed(runnable_acc, 1500);
    }

    private WidgetContainer widgetContainer;
    Runnable runnable_acc = new Runnable() {
        @Override
        public void run() {
            View view = LayoutInflater.from(EasyGoodGameActivity.this).inflate(R.layout.layout_power_promiss, null);
            widgetContainer = new WidgetContainer(EasyGoodGameActivity.this.getApplicationContext(), Gravity.NO_GRAVITY, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT, true);
            widgetContainer.addView(view);
            widgetContainer.addToWindow();
            widgetContainer.setWidgetListener(new WidgetContainer.IWidgetListener() {
                @Override
                public boolean onBackPressed() {
                    return false;
                }

                @Override
                public boolean onMenuPressed() {
                    return false;
                }

                @Override
                public void onClick() {
                    widgetContainer.removeFromWindow();
                }
            });
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 || requestCode == 1) {
            if (MyUtils.isAccessibilitySettingsOn(this)) {
                gboost_power_check.setImageResource(R.mipmap.side_check_passed);
            } else {
                gboost_power_check.setImageResource(R.mipmap.side_check_normal);
            }
        }
        if (requestCode == 100) {
            if (mHandler != null) {
                mHandler.removeCallbacks(runnable_acc);
            }
            if (MyUtils.isAccessibilitySettingsOn(EasyGoodGameActivity.this)) {
            } else {
                showDialogPermiss();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showDialogPermiss() {
        View view = View.inflate(this, R.layout.dialog_power, null);
        TextView exit_queren = (TextView) view.findViewById(R.id.bt_queren);
        TextView exit_quxiao = (TextView) view.findViewById(R.id.bt_quxiao);
        ImageView iv_cha = (ImageView) view.findViewById(R.id.iv_cha);
        exit_queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissIntent();
                dialog.dismiss();

            }
        });
        iv_cha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        exit_quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog = new AlertDialog.Builder(this, R.style.exit_dialog).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(view);
        dialog.show();
    }
}
