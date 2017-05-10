package com.supers.clean.junk.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.eos.manager.AppLockPermissionActivity;
import com.supers.clean.junk.R;
import com.supers.clean.junk.adapter.AddGameAdapter;
import com.supers.clean.junk.adapter.WhiteListAdapter;
import com.supers.clean.junk.customeview.PageIndicatorView;
import com.supers.clean.junk.db.RecyclerDbHelper;
import com.supers.clean.junk.entity.JunkInfo;
import com.supers.clean.junk.gboost.GameBooster;
import com.supers.clean.junk.gboost.HorizontalPageLayoutManager;
import com.supers.clean.junk.gboost.JackSnapHelper;
import com.supers.clean.junk.gboost.PagingItemDecoration;
import com.supers.clean.junk.gboost.PagingScrollHelper;
import com.supers.clean.junk.gboost.Sampler;
import com.supers.clean.junk.similarimage.ImageInfo;
import com.supers.clean.junk.util.CommonUtil;
import com.supers.clean.junk.util.Constant;
import com.supers.clean.junk.util.MemoryManager;
import com.supers.clean.junk.util.PhoneManager;
import com.supers.clean.junk.util.PreData;
import com.supers.clean.junk.util.ShortCutUtils;
import com.supers.clean.junk.util.ShortcutSuperUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    RecyclerView gboost_recyc;
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


    private HorizontalPageLayoutManager pageLayoutManager;
    private PagingScrollHelper pagingScrollHelper;
    private PagingItemDecoration itemDecoration;
    private MyAdapter adapter;
    private ArrayList<JunkInfo> list;
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

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        add_right = (ImageView) findViewById(R.id.add_right);
        clear = (ImageButton) findViewById(R.id.clear);
        search_edit_text = (EditText) findViewById(R.id.search_edit_text);

        gboost_recyc = (RecyclerView) findViewById(R.id.gboost_recyc);
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
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_gboost);
        mHandler = new Handler();
        cleanApplication = (MyApplication) getApplication();

        title_left.setOnClickListener(clickListener);
        add_left.setOnClickListener(clickListener);
        title_name.setText(R.string.gboost_0);

        long size = MemoryManager.getPhoneFreeRamMemory(this);
        gboost_ram_size.setText(CommonUtil.convertStorage(size) + CommonUtil.convertStorageDanwei(size));

        if (CommonUtil.isAccessibilitySettingsOn(this)) {
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
    }

    private void initList() {
        pageLayoutManager = new HorizontalPageLayoutManager(3, 4);
        itemDecoration = new PagingItemDecoration(this, pageLayoutManager);
        gboost_recyc.setLayoutManager(pageLayoutManager);
        gboost_recyc.addItemDecoration(itemDecoration);
        pagingScrollHelper = new PagingScrollHelper();
        list = new ArrayList<>();
        addData();

    }

    private void addData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                list.addAll(GameBooster.getInstalledGameList(GBoostActivity.this));
                Message msg = mHandler.obtainMessage();
                msg.what = ADD_DATA;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        list.add(0, new JunkInfo(ContextCompat.getDrawable(GBoostActivity.this, R.mipmap.gboost_add), getString(R.string.gboost_7), "a"));
                        adapter = new MyAdapter(list);
                        gboost_recyc.setAdapter(adapter);
                        pagingScrollHelper.setUpRecycleView(gboost_recyc);
                        pagingScrollHelper.updateLayoutManger();
                        pagingScrollHelper.setOnPageChangeListener(new PagingScrollHelper.onPageChangeListener() {
                            @Override
                            public void onPageChange(int index) {

                                pageindicatorview.setSelectedPage(index);
                            }
                        });
                        adapter.notifyDataSetChanged();
                        if (list.size() <= 12) {
                            pageindicatorview.initIndicator(1);
                            pageindicatorview.setVisibility(View.INVISIBLE);
                        } else if (list.size() <= 24) {
                            pageindicatorview.initIndicator(2);
                            pageindicatorview.setVisibility(View.VISIBLE);
                        } else if (list.size() <= 36) {
                            pageindicatorview.initIndicator(3);
                            pageindicatorview.setVisibility(View.VISIBLE);
                        } else {
                            pageindicatorview.initIndicator(4);
                        }

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
                    adapter.notifyDataSetChanged();
                    gboost_recyc.scrollBy(2000, 0);
                    shortGame(false);
                    if (list.size() <= 12) {
                        pageindicatorview.initIndicator(1);
                        pageindicatorview.setVisibility(View.INVISIBLE);
                    } else if (list.size() <= 24) {
                        pageindicatorview.initIndicator(2);
                        pageindicatorview.setVisibility(View.VISIBLE);
                        pageindicatorview.setSelectedPage(1);
                    } else if (list.size() <= 36) {
                        pageindicatorview.initIndicator(3);
                        pageindicatorview.setVisibility(View.VISIBLE);
                        pageindicatorview.setSelectedPage(2);
                    } else {
                        pageindicatorview.initIndicator(4);
                        pageindicatorview.setSelectedPage(3);
                    }
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
                    try {
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        startActivityForResult(intent, 100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent transintent = new Intent(GBoostActivity.this, AppLockPermissionActivity.class);
                            startActivity(transintent);
                        }
                    }, 1500);
                    break;
                case R.id.gboost_clean_button:
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
//        gboost_network_size.setText(CommonUtil.convertStorageWifi(speed));
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
                            gboost_network_size.setText(CommonUtil.convertStorageWifi(speed));
                            gboost_ram_size.setText(CommonUtil.convertStorage(size) + CommonUtil.convertStorageDanwei(size));
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
            adapter.notifyDataSetChanged();
            ll_add_game.setVisibility(View.GONE);
            shortGame(false);
        } else {
            finish();
        }
    }

    private void shortGame(boolean isChuangjian) {
//        pagingScrollHelper.updateLayoutManger();
        search_edit_text.setText("");
        if ((list.size() != 1 && !PreData.getDB(GBoostActivity.this, Constant.GBOOST_SI, false)) || isChuangjian) {
            View shortcut_view = View.inflate(GBoostActivity.this, R.layout.layout_gboost_short, null);
            if (list.size() > 1) {
                ImageView iv_1 = (ImageView) shortcut_view.findViewById(R.id.iv_1);
                iv_1.setImageDrawable(list.get(1).icon);
            }
            if (list.size() > 2) {
                ImageView iv_2 = (ImageView) shortcut_view.findViewById(R.id.iv_2);
                iv_2.setImageDrawable(list.get(2).icon);
            }
            if (list.size() > 3) {
                ImageView iv_3 = (ImageView) shortcut_view.findViewById(R.id.iv_3);
                iv_3.setImageDrawable(list.get(3).icon);
            }
            if (list.size() > 4) {
                ImageView iv_4 = (ImageView) shortcut_view.findViewById(R.id.iv_4);
                iv_4.setImageDrawable(list.get(4).icon);
                PreData.putDB(GBoostActivity.this, Constant.GBOOST_SI, true);
            }

            Bitmap bitmap = CommonUtil.getViewBitmap(shortcut_view);
            if (bitmap != null) {
                Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
                shortcutIntent.setClass(GBoostActivity.this, GBoostActivity.class);
                shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                String title = GBoostActivity.this.getString(R.string.gboost_0);
                ShortCutUtils.removeShortcut(GBoostActivity.this, shortcutIntent, title);
                ShortCutUtils.addShortcut(GBoostActivity.this, shortcutIntent, title, false, bitmap);
            }
        }
    }

    private void initData() {
        ArrayList<String> gboost_names = PreData.getNameList(this, Constant.GBOOST_LIST);
        gboost_add.clear();
        for (JunkInfo info : ((MyApplication) getApplication()).getListMng()) {
            boolean isA = false;
            for (String packagename : gboost_names) {
                if (packagename.equals(info.packageName)) {
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
            if (CommonUtil.isAccessibilitySettingsOn(this)) {
                gboost_power_check.setImageResource(R.mipmap.side_check_passed);
            } else {
                gboost_power_check.setImageResource(R.mipmap.side_check_normal);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.HomeViewHolder> {
        ArrayList<JunkInfo> list;
        LruCache lruCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / 1024) / 4) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // 返回用户定义的item的大小，默认返回1代表item的数量.重写此方法来衡量每张图片的大小。
                return bitmap.getByteCount() / 1024;
            }
        };


        public MyAdapter(ArrayList<JunkInfo> list) {
            this.list = list;
        }

        public MyAdapter.HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyAdapter.HomeViewHolder holder = new MyAdapter.HomeViewHolder(LayoutInflater.from(
                    GBoostActivity.this).inflate(R.layout.layout_gboost_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyAdapter.HomeViewHolder holder, final int position) {
            final JunkInfo info = list.get(position);
            holder.gboost_item_icon.setImageDrawable(info.icon);
            holder.gboost_item_name.setText(info.label);
            holder.gboost_item_c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position == 0) {
                        if (list.size() <= 12) {
                            pageindicatorview.initIndicator(1);
                            pageindicatorview.setVisibility(View.INVISIBLE);
                        } else if (list.size() <= 24) {
                            pageindicatorview.initIndicator(2);
                            pageindicatorview.setVisibility(View.VISIBLE);
                            pageindicatorview.setSelectedPage(1);
                        } else if (list.size() <= 36) {
                            pageindicatorview.initIndicator(3);
                            pageindicatorview.setVisibility(View.VISIBLE);
                            pageindicatorview.setSelectedPage(2);
                        } else {
                            pageindicatorview.initIndicator(4);
                            pageindicatorview.setSelectedPage(3);
                        }
                        ll_add_game.setVisibility(View.VISIBLE);
                        whiteListAdapter = new AddGameAdapter(GBoostActivity.this, list);
                        list_game.setAdapter(whiteListAdapter);
                        initData();
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString("from", "GBoost");
                        bundle.putString("packageName", info.packageName);
                        jumpToActivity(PowerActivity.class, bundle);
//                        CommonUtil.doStartApplicationWithPackageName(GBoostActivity.this, info.packageName);
                    }
                }
            });
        }

        public void reChangesData(int position) {
            notifyItemRangeChanged(position, this.list.size() - position); //mList是数据

        }

        @Override
        public int getItemCount() {
            return this.list.size();
        }

        class HomeViewHolder extends RecyclerView.ViewHolder {
            ImageView gboost_item_icon;
            TextView gboost_item_name;
            FrameLayout gboost_item_c;

            public HomeViewHolder(View view) {
                super(view);
                gboost_item_icon = (ImageView) view.findViewById(R.id.gboost_item_icon);
                gboost_item_name = (TextView) view.findViewById(R.id.gboost_item_name);
                gboost_item_c = (FrameLayout) view.findViewById(R.id.gboost_item_c);
            }
        }

        /**
         * @param key    传入图片的key值，一般用图片url代替
         * @param bitmap 要缓存的图片对象
         */
        public void addBitmapToCache(String key, Bitmap bitmap) {
            if (getBitmapFromCache(key) == null) {
                if (bitmap == null) {
                    return;
                } else {
                    lruCache.put(key, bitmap);
                }
            }
        }

        /**
         * @param key 要取出的bitmap的key值
         * @return 返回取出的bitmap
         */
        public Bitmap getBitmapFromCache(String key) {

            return (Bitmap) lruCache.get(key);
        }
    }
}
