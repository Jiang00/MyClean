package com.mutter.clean.junk.myActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.support.v7.widget.GridLayoutManager;
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

import com.mutter.clean.core.CleanManager;
import com.mutter.clean.db.CleanDBHelper;
import com.mutter.clean.entity.JunkInfo;
import com.mutter.clean.gboost.GameBooster;
import com.mutter.clean.junk.util.BadgerCount;
import com.mutter.clean.util.LoadManager;
import com.mutter.clean.util.MemoryManager;
import com.mutter.clean.util.PreData;
import com.mutter.clean.util.Util;
import com.mutter.clean.junk.R;
import com.mutter.clean.junk.myAdapter.AddGameAdapter;
import com.mutter.clean.junk.util.AdUtil;
import com.mutter.clean.junk.util.Constant;
import com.mutter.clean.junk.util.ShortCutUtils;

import java.util.ArrayList;
import java.util.List;

/**
 */

public class GameActivity extends BaseActivity {
    private static final int ADD_DATA = 0;

    ImageView add_right;
    ImageButton clear;
    EditText search_edit_text;
    RecyclerView gboost_recyc;
    FrameLayout title_left;
    FrameLayout add_left;
    ListView list_game;
    private PackageManager pm;
    TextView gboost_short_add;
    LinearLayout ll_ad, ll_ad_a;
    ImageView gboost_short_iv;
    TextView title_name;
    Button gboost_clean_button;
    LinearLayout ll_add_game;
    private GbAdapter adapter;
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

    private boolean search;
    private List<JunkInfo> gboost_add, listEdit;
    private int screenWidth;
    private int width;
    private View nativeView, nativeView_2;

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        add_right = (ImageView) findViewById(R.id.add_right);
        clear = (ImageButton) findViewById(R.id.clear);
        search_edit_text = (EditText) findViewById(R.id.search_edit_text);

        gboost_recyc = (RecyclerView) findViewById(R.id.gboost_recyc);
        gboost_clean_button = (Button) findViewById(R.id.gboost_clean_button);
        ll_add_game = (LinearLayout) findViewById(R.id.ll_add_game);
        add_left = (FrameLayout) findViewById(R.id.add_left);
        list_game = (ListView) findViewById(R.id.list_game);
        gboost_short_iv = (ImageView) findViewById(R.id.gboost_short_iv);
        gboost_short_add = (TextView) findViewById(R.id.gboost_short_add);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
        ll_ad_a = (LinearLayout) findViewById(R.id.ll_ad_a);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_gboost);
        PreData.putDB(this, Constant.HONG_GBOOST, false);
        BadgerCount.setCount(this);
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        mHandler = new Handler();
        pm = getPackageManager();
        title_left.setOnClickListener(clickListener);
        add_left.setOnClickListener(clickListener);
        title_name.setText(R.string.gboost_0);

        long size = MemoryManager.getPhoneFreeRamMemory(this);

        gboost_clean_button.setOnClickListener(clickListener);
        add_right.setOnClickListener(clickListener);
        clear.setOnClickListener(clickListener);
        gboost_short_add.setOnClickListener(clickListener);
        gboost_add = new ArrayList<>();
        listEdit = new ArrayList<>();
        initList();
        initAd();
    }

    private void initAd() {
        nativeView = AdUtil.getNativeAdView("", R.layout.native_ad_3);
        if (ll_ad != null && nativeView != null) {
            ll_ad.addView(nativeView);
            ll_ad.setVisibility(View.VISIBLE);
        }
        nativeView_2 = AdUtil.getNativeAdView("", R.layout.native_ad_3);
        if (ll_ad_a != null && nativeView_2 != null) {
            ll_ad_a.addView(nativeView_2);
            ll_ad_a.setVisibility(View.VISIBLE);
        }
    }

    private void initIcon() {
        View shortcut_view = View.inflate(GameActivity.this, R.layout.layout_gboost_short, null);
        if (list.size() > 1) {
            ImageView iv_1 = (ImageView) shortcut_view.findViewById(R.id.iv_1);
            iv_1.setImageDrawable(LoadManager.getInstance(GameActivity.this).getAppIcon(list.get(1)));
        }
        if (list.size() > 2) {
            ImageView iv_2 = (ImageView) shortcut_view.findViewById(R.id.iv_2);
            iv_2.setImageDrawable(LoadManager.getInstance(GameActivity.this).getAppIcon(list.get(2)));
        }
        if (list.size() > 3) {
            ImageView iv_3 = (ImageView) shortcut_view.findViewById(R.id.iv_3);
            iv_3.setImageDrawable(LoadManager.getInstance(GameActivity.this).getAppIcon(list.get(3)));
        }
        if (list.size() > 4) {
            ImageView iv_4 = (ImageView) shortcut_view.findViewById(R.id.iv_4);
            iv_4.setImageDrawable(LoadManager.getInstance(GameActivity.this).getAppIcon(list.get(4)));
        }
        Bitmap bitmap = getViewBitmap(shortcut_view, this);
        gboost_short_iv.setImageBitmap(bitmap);
    }

    //布局转bitmap
    public static Bitmap getViewBitmap(View view, Context context) {
        if (null == view) {
            throw new IllegalArgumentException("parameter can't be null.");
        }
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, context.getResources().getDimensionPixelOffset(R.dimen.d61),
                context.getResources().getDimensionPixelOffset(R.dimen.d61));
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
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

                case R.id.gboost_clean_button:
                    AdUtil.track("游戏加速页面", "点击一键加速", "", 1);
                    Bundle bundle = new Bundle();
                    bundle.putString("from", "GBoost");
                    jumpToActivity(PowerActivity.class, bundle, 1);
                    break;
                case R.id.gboost_short_add:
                    AdUtil.track("游戏加速页面", "点击添加快捷方式", "", 1);
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

    private void addData() {
        if (PreData.getDB(GameActivity.this, Constant.GBOOST_LUN, true)) {
            PreData.putDB(GameActivity.this, Constant.GBOOST_LUN, false);
            list.addAll(GameBooster.getInstalledGameList(GameActivity.this));
            shortGame(false);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> gboost_names = CleanDBHelper.getInstance(GameActivity.this).getWhiteList(CleanDBHelper.TableType.GameBoost);
                for (String pkg : gboost_names) {
                    if (LoadManager.getInstance(GameActivity.this).isPkgInstalled(pkg)) {
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
                        adapter = new GbAdapter(list);
                        gboost_recyc.setLayoutManager(new GridLayoutManager(GameActivity.this, 3));
                        gboost_recyc.setAdapter(adapter);
                        initIcon();
                    }
                });
            }
        }).start();

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
                GameActivity.class.getCanonicalName()));
        String title = GameActivity.this.getString(R.string.gboost_0);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.short_7);
            ShortCutUtils.addShortcut(GameActivity.this, shortcutIntent, title, false, bitmap);
            gboost_short_iv.setImageBitmap(bitmap);
            return;
        }
        if (list.size() > 1 && (!PreData.getDB(GameActivity.this, Constant.GBOOST_SI, false) || isChuangjian)) {
            View shortcut_view = View.inflate(GameActivity.this, R.layout.layout_gboost_short, null);
            if (list.size() > 1) {
                ImageView iv_1 = (ImageView) shortcut_view.findViewById(R.id.iv_1);
                iv_1.setImageDrawable(LoadManager.getInstance(GameActivity.this).getAppIcon(list.get(1)));
            }
            if (list.size() > 2) {
                ImageView iv_2 = (ImageView) shortcut_view.findViewById(R.id.iv_2);
                iv_2.setImageDrawable(LoadManager.getInstance(GameActivity.this).getAppIcon(list.get(2)));
            }
            if (list.size() > 3) {
                ImageView iv_3 = (ImageView) shortcut_view.findViewById(R.id.iv_3);
                iv_3.setImageDrawable(LoadManager.getInstance(GameActivity.this).getAppIcon(list.get(3)));
            }
            if (list.size() > 4) {
                ImageView iv_4 = (ImageView) shortcut_view.findViewById(R.id.iv_4);
                iv_4.setImageDrawable(LoadManager.getInstance(GameActivity.this).getAppIcon(list.get(4)));
                PreData.putDB(GameActivity.this, Constant.GBOOST_SI, true);
            }

            Bitmap bitmap = getViewBitmap(shortcut_view, this);
            if (bitmap != null) {
                ShortCutUtils.removeShortcut(GameActivity.this, shortcutIntent, title);
                ShortCutUtils.addShortcut(GameActivity.this, shortcutIntent, title, false, bitmap);
                gboost_short_iv.setImageBitmap(bitmap);
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
        super.onActivityResult(requestCode, resultCode, data);
    }


    class GbAdapter extends RecyclerView.Adapter<GbAdapter.HomeViewHolder> {
        ArrayList<String> list;
        LruCache lruCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / 1024) / 4) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // 返回用户定义的item的大小，默认返回1代表item的数量.重写此方法来衡量每张图片的大小。
                return bitmap.getByteCount() / 1024;
            }
        };


        public GbAdapter(ArrayList<String> list) {
            this.list = list;
        }


        public GbAdapter.HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            GbAdapter.HomeViewHolder holder = new GbAdapter.HomeViewHolder(LayoutInflater.from(
                    GameActivity.this).inflate(R.layout.layout_gboost_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final GbAdapter.HomeViewHolder holder, final int position) {
            if (position == 0) {
                holder.gboost_item_icon.setImageResource(R.mipmap.gboost_add);
                holder.gboost_item_name.setText(list.get(position));
            } else {
                try {
                    ApplicationInfo info = pm.getApplicationInfo(list.get(position), 0);
                    holder.gboost_item_icon.setImageDrawable(info.loadIcon(pm));
                    holder.gboost_item_name.setText(info.loadLabel(pm));
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
            holder.gboost_item_c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position == 0) {
                        AdUtil.track("游戏加速页面", "点击添加游戏", "", 1);
                        ll_add_game.setVisibility(View.VISIBLE);
                        whiteListAdapter = new AddGameAdapter(GameActivity.this, list);
                        list_game.setAdapter(whiteListAdapter);
                        initData();
                    } else {
                        try {
                            AdUtil.track("游戏加速页面", "点击启动游戏", list.get(position), 1);
                            Bundle bundle = new Bundle();
                            bundle.putString("from", "GBoost");
                            bundle.putString("packageName", list.get(position));
                            jumpToActivity(PowerActivity.class, bundle);
                        } catch (Exception e) {
                        }
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