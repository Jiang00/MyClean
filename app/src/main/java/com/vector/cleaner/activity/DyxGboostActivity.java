package com.vector.cleaner.activity;

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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.vector.cleaner.myview.WidgetContainer;
import com.vector.cleaner.utils.ShortCutUtils;
import com.vector.mcleaner.manager.CleanManager;
import com.vector.mcleaner.db.CleanDBHelper;
import com.vector.mcleaner.gameBoost.GameBooster;
import com.vector.mcleaner.mutil.PreData;
import com.vector.mcleaner.mutil.Util;
import com.vector.mcleaner.mutil.LoadManager;
import com.vector.cleaner.R;
import com.vector.cleaner.madapter.GameAddAdapter;
import com.vector.mcleaner.entity.JunkInfo;
import com.vector.cleaner.utils.AdUtil;
import com.vector.cleaner.utils.Constant;
import com.vector.mcleaner.mutil.MemoryManager;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2017/5/5.
 */

public class DyxGboostActivity extends BaseActivity {
    private static final int ADD_DATA = 0;

    TextView gboost_power_check;
    Button gboost_clean_button;
    LinearLayout ll_add_game;
    FrameLayout title_left;
    EditText search_edit_text;
    TextView title_name;
    ImageView add_right;
    ImageButton clear;
    RecyclerView gboost_recyc;
    FrameLayout add_left;
    ListView list_game;


    private HuiAdapter adapter;
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
    private GameAddAdapter whiteListAdapter;

    private long total;
    private long idle;
    private long lastTotalRxBytes;
    private long lastTimeStamp;
    private boolean search;
    private List<JunkInfo> gboost_add, listEdit;
    private int screenWidth;
    private int width;
    private AlertDialog dialog;

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        add_right = (ImageView) findViewById(R.id.add_right);
        clear = (ImageButton) findViewById(R.id.clear);
        search_edit_text = (EditText) findViewById(R.id.search_edit_text);

        gboost_recyc = (RecyclerView) findViewById(R.id.gboost_recyc);
        gboost_power_check = (TextView) findViewById(R.id.gboost_power_check);
        gboost_clean_button = (Button) findViewById(R.id.gboost_clean_button);
        ll_add_game = (LinearLayout) findViewById(R.id.ll_add_game);
        add_left = (FrameLayout) findViewById(R.id.add_left);
        list_game = (ListView) findViewById(R.id.list_game);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_gboost);
        registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        mHandler = new Handler();

        title_left.setOnClickListener(clickListener);
        add_left.setOnClickListener(clickListener);
        title_name.setText(R.string.gboost_0);

        long size = MemoryManager.getPhoneFreeRamMemory(this);

        if (Util.isAccessibilitySettingsOn(this)) {
            gboost_power_check.setText(R.string.gboost_kai_2);
        } else {
            gboost_power_check.setText(R.string.gboost_kai);
        }
        gboost_power_check.setOnClickListener(clickListener);
        gboost_clean_button.setOnClickListener(clickListener);
        add_right.setOnClickListener(clickListener);
        clear.setOnClickListener(clickListener);
        gboost_add = new ArrayList<>();
        listEdit = new ArrayList<>();
        initList();
    }


    private void addData() {
        if (PreData.getDB(DyxGboostActivity.this, Constant.GBOOST_LUN, true)) {
            PreData.putDB(DyxGboostActivity.this, Constant.GBOOST_LUN, false);
            list.addAll(GameBooster.getInstalledGameList(DyxGboostActivity.this));
            shortGame(false);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> gboost_names = CleanDBHelper.getInstance(DyxGboostActivity.this).getWhiteList(CleanDBHelper.TableType.GameBoost);
                for (String pkg : gboost_names) {
                    if (LoadManager.getInstance(DyxGboostActivity.this).isPkgInstalled(pkg)) {
                        list.add(pkg);
                    }
                }
                list.add(0, getString(R.string.gboost_7));
                Message msg = mHandler.obtainMessage();
                msg.what = ADD_DATA;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("jfy", list.size() + "==");
                        adapter = new HuiAdapter();
                        gboost_recyc.setAdapter(adapter);
                        gboost_recyc.setLayoutManager(new GridLayoutManager(DyxGboostActivity.this, 4));

                    }
                });
            }
        }).

                start();

    }

    private void initList() {
        list = new ArrayList<>();
        addData();

    }


    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add_left:
                    ll_add_game.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
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
                    permissIntent();
                    break;
                case R.id.gboost_clean_button:
                    AdUtil.track("游戏加速页面", "点击一键加速", "", 1);
                    Bundle bundle = new Bundle();
                    bundle.putString("from", "GBoost");
                    jumpToActivity(DeepActivity.class, bundle, 1);
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


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (ll_add_game.getVisibility() == View.VISIBLE) {
            ll_add_game.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
            shortGame(false);
        } else {
            finish();
        }
    }

    private void shortGame(boolean isChuangjian) {
        search_edit_text.setText("");
        Log.e("short", "chuangjian1 ");
        Intent shortcutIntent = new Intent();
        shortcutIntent.setAction(Intent.ACTION_VIEW);
        shortcutIntent.setComponent(new ComponentName(getPackageName(),
                DyxGboostActivity.class.getCanonicalName()));
        String title = DyxGboostActivity.this.getString(R.string.gboost_0);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.short_7);
            ShortCutUtils.addShortcut(DyxGboostActivity.this, shortcutIntent, title, false, bitmap);
            return;
        }
        if (list.size() > 1 && (!PreData.getDB(DyxGboostActivity.this, Constant.GBOOST_SI, false) || isChuangjian)) {
            View shortcut_view = View.inflate(DyxGboostActivity.this, R.layout.layout_gboost_short, null);
            if (list.size() > 1) {
                ImageView iv_1 = (ImageView) shortcut_view.findViewById(R.id.iv_1);
                iv_1.setImageDrawable(LoadManager.getInstance(DyxGboostActivity.this).getAppIcon(list.get(1)));
            }
            if (list.size() > 2) {
                ImageView iv_2 = (ImageView) shortcut_view.findViewById(R.id.iv_2);
                iv_2.setImageDrawable(LoadManager.getInstance(DyxGboostActivity.this).getAppIcon(list.get(2)));
            }
            if (list.size() > 3) {
                ImageView iv_3 = (ImageView) shortcut_view.findViewById(R.id.iv_3);
                iv_3.setImageDrawable(LoadManager.getInstance(DyxGboostActivity.this).getAppIcon(list.get(3)));
            }
            if (list.size() > 4) {
                ImageView iv_4 = (ImageView) shortcut_view.findViewById(R.id.iv_4);
                iv_4.setImageDrawable(LoadManager.getInstance(DyxGboostActivity.this).getAppIcon(list.get(4)));
                PreData.putDB(DyxGboostActivity.this, Constant.GBOOST_SI, true);
            }

            Bitmap bitmap = Util.getViewBitmap(shortcut_view);
            if (bitmap != null) {
                Log.e("short", "chuangjian ");
                ShortCutUtils.removeShortcut(DyxGboostActivity.this, shortcutIntent, title);
                ShortCutUtils.addShortcut(DyxGboostActivity.this, shortcutIntent, title, false, bitmap);
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
            View view = LayoutInflater.from(DyxGboostActivity.this).inflate(R.layout.layout_power_promiss, null);
            widgetContainer = new WidgetContainer(DyxGboostActivity.this.getApplicationContext(), Gravity.NO_GRAVITY, WindowManager.LayoutParams.MATCH_PARENT,
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
            if (Util.isAccessibilitySettingsOn(this)) {
                gboost_power_check.setText(R.string.gboost_kai_2);
            } else {
                gboost_power_check.setText(R.string.gboost_kai);
            }
        }
        if (requestCode == 100) {
            if (mHandler != null) {
                mHandler.removeCallbacks(runnable_acc);
            }
            if (Util.isAccessibilitySettingsOn(DyxGboostActivity.this)) {
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

    class HuiAdapter extends RecyclerView.Adapter<HuiAdapter.HomeViewHolder> {

        public HuiAdapter() {
        }


        public HuiAdapter.HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            HuiAdapter.HomeViewHolder holder = new HuiAdapter.HomeViewHolder(LayoutInflater.from(
                    DyxGboostActivity.this).inflate(R.layout.layout_gboost_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final DyxGboostActivity.HuiAdapter.HomeViewHolder holder, final int position) {
            final String info = list.get(position);
            if (position == 0) {
                holder.gboost_item_icon.setImageResource(R.mipmap.game_add);
                holder.gboost_item_name.setText(info);
            } else {
                holder.gboost_item_icon.setImageDrawable(LoadManager.getInstance(DyxGboostActivity.this).getAppIcon(info));
                holder.gboost_item_name.setText(LoadManager.getInstance(DyxGboostActivity.this).getAppLabel(info));
            }
            holder.gboost_item_c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position == 0) {
                        AdUtil.track("游戏加速页面", "点击添加游戏", "", 1);
                        ll_add_game.setVisibility(View.VISIBLE);
                        whiteListAdapter = new GameAddAdapter(DyxGboostActivity.this, list);
                        list_game.setAdapter(whiteListAdapter);
                        initData();
                    } else {
                        try {
                            AdUtil.track("游戏加速页面", "点击启动游戏", list.get(position), 1);
                            Bundle bundle = new Bundle();
                            bundle.putString("from", "GBoost");
                            bundle.putString("packageName", list.get(position));
                            jumpToActivity(DeepActivity.class, bundle);
                        } catch (Exception e) {
                        }
                    }
                }
            });

        }

        public void reChangesData(int position) {
            notifyItemRangeChanged(position, list.size() - position); //mList是数据

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class HomeViewHolder extends RecyclerView.ViewHolder {
            ImageView gboost_item_icon;
            TextView gboost_item_name;
            FrameLayout gboost_item_c;

            public HomeViewHolder(View view) {
                super(view);
                gboost_item_c = (FrameLayout) view.findViewById(R.id.gboost_item_c);
                gboost_item_icon = (ImageView) view.findViewById(R.id.gboost_item_icon);
                gboost_item_name = (TextView) view.findViewById(R.id.gboost_item_name);
            }
        }

    }


}
