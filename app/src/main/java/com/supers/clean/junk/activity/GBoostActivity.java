package com.supers.clean.junk.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.eos.manager.AppLockPermissionActivity;
import com.supers.clean.junk.R;
import com.supers.clean.junk.adapter.AddGameAdapter;
import com.supers.clean.junk.adapter.WhiteListAdapter;
import com.supers.clean.junk.db.RecyclerDbHelper;
import com.supers.clean.junk.entity.JunkInfo;
import com.supers.clean.junk.gboost.HorizontalPageLayoutManager;
import com.supers.clean.junk.gboost.PagingScrollHelper;
import com.supers.clean.junk.gboost.Sampler;
import com.supers.clean.junk.similarimage.ImageInfo;
import com.supers.clean.junk.util.CommonUtil;
import com.supers.clean.junk.util.MemoryManager;
import com.supers.clean.junk.util.PhoneManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ivy on 2017/5/5.
 */

public class GBoostActivity extends BaseActivity {

    FrameLayout title_left;
    TextView title_name;
    RecyclerView gboost_recyc;
    TextView gboost_ram_size;
    TextView gboost_network_size;
    TextView gboost_cpu_szie;
    ImageView gboost_power_check;
    Button gboost_clean_button;
    LinearLayout ll_add_game;
    FrameLayout add_left;
    ListView list_game;


    private HorizontalPageLayoutManager pageLayoutManager;
    private PagingScrollHelper pagingScrollHelper;
    private MyAdapter adapter;
    private ArrayList<JunkInfo> list;
    private Handler mHandler;
    private AddGameAdapter whiteListAdapter;
    private MyApplication cleanApplication;

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        gboost_recyc = (RecyclerView) findViewById(R.id.gboost_recyc);
        gboost_ram_size = (TextView) findViewById(R.id.gboost_ram_size);
        gboost_network_size = (TextView) findViewById(R.id.gboost_network_size);
        gboost_cpu_szie = (TextView) findViewById(R.id.gboost_cpu_szie);
        gboost_power_check = (ImageView) findViewById(R.id.gboost_power_check);
        gboost_clean_button = (Button) findViewById(R.id.gboost_clean_button);
        ll_add_game = (LinearLayout) findViewById(R.id.ll_add_game);
        add_left = (FrameLayout) findViewById(R.id.add_left);
        list_game = (ListView) findViewById(R.id.list_game);
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

        Sampler.getInstance().init(getApplicationContext(), 1000);
        Sampler.getInstance().start();
        Sampler.getInstance().setOnCpuUpdateListener(new Sampler.OnCpuUpdateListener() {
            @Override
            public void cupUpdate(final int cpu, final long speed) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        gboost_cpu_szie.setText(cpu + "%");
                        gboost_network_size.setText(CommonUtil.convertStorageWifi(speed));
                    }
                });
            }
        });


        long size = MemoryManager.getPhoneFreeRamMemory(this);
        gboost_ram_size.setText(CommonUtil.convertStorage(size) + CommonUtil.convertStorageDanwei(size));

        if (CommonUtil.isAccessibilitySettingsOn(this)) {
            gboost_power_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            gboost_power_check.setImageResource(R.mipmap.side_check_normal);
        }
        gboost_power_check.setOnClickListener(clickListener);
        gboost_clean_button.setOnClickListener(clickListener);

        initList();

        whiteListAdapter = new AddGameAdapter(this, list);

    }

    private void initList() {
        pageLayoutManager = new HorizontalPageLayoutManager(3, 4);
        gboost_recyc.setLayoutManager(pageLayoutManager);
        pagingScrollHelper = new PagingScrollHelper();
        list = new ArrayList<>();
        addData();
        list.add(0, new JunkInfo(ContextCompat.getDrawable(this, R.mipmap.gboost_add), "000", "a"));
        adapter = new MyAdapter(list);
        gboost_recyc.setAdapter(adapter);
        pagingScrollHelper.setUpRecycleView(gboost_recyc);
        pagingScrollHelper.setOnPageChangeListener(new PagingScrollHelper.onPageChangeListener() {
            @Override
            public void onPageChange(int index) {
                Log.e("gboost", "第" + index + "页");
            }
        });
    }

    private void addData() {
        int index = 0;
        list.add(new JunkInfo(ContextCompat.getDrawable(this, R.mipmap.icon), ++index + "", "a"));
        list.add(new JunkInfo(ContextCompat.getDrawable(this, R.mipmap.icon), ++index + "", "a"));
        list.add(new JunkInfo(ContextCompat.getDrawable(this, R.mipmap.icon), ++index + "", "a"));
        list.add(new JunkInfo(ContextCompat.getDrawable(this, R.mipmap.icon), ++index + "", "a"));
        list.add(new JunkInfo(ContextCompat.getDrawable(this, R.mipmap.icon), ++index + "", "a"));
        list.add(new JunkInfo(ContextCompat.getDrawable(this, R.mipmap.icon), ++index + "", "a"));
        list.add(new JunkInfo(ContextCompat.getDrawable(this, R.mipmap.icon), ++index + "", "a"));
        list.add(new JunkInfo(ContextCompat.getDrawable(this, R.mipmap.icon), ++index + "", "a"));
        list.add(new JunkInfo(ContextCompat.getDrawable(this, R.mipmap.icon), ++index + "", "a"));
        list.add(new JunkInfo(ContextCompat.getDrawable(this, R.mipmap.icon), ++index + "", "a"));
        list.add(new JunkInfo(ContextCompat.getDrawable(this, R.mipmap.icon), ++index + "", "a"));
        list.add(new JunkInfo(ContextCompat.getDrawable(this, R.mipmap.icon), ++index + "", "a"));
        list.add(new JunkInfo(ContextCompat.getDrawable(this, R.mipmap.icon), ++index + "", "a"));
        list.add(new JunkInfo(ContextCompat.getDrawable(this, R.mipmap.icon), ++index + "", "a"));
        list.add(new JunkInfo(ContextCompat.getDrawable(this, R.mipmap.icon), ++index + "", "a"));
        list.add(new JunkInfo(ContextCompat.getDrawable(this, R.mipmap.icon), ++index + "", "a"));
        list.add(new JunkInfo(ContextCompat.getDrawable(this, R.mipmap.icon), ++index + "", "a"));
        list.add(new JunkInfo(ContextCompat.getDrawable(this, R.mipmap.icon), ++index + "", "a"));
        list.add(new JunkInfo(ContextCompat.getDrawable(this, R.mipmap.icon), ++index + "", "a"));
        list.add(new JunkInfo(ContextCompat.getDrawable(this, R.mipmap.icon), ++index + "", "a"));
        list.add(new JunkInfo(ContextCompat.getDrawable(this, R.mipmap.icon), ++index + "", "a"));
        list.add(new JunkInfo(ContextCompat.getDrawable(this, R.mipmap.icon), ++index + "", "a"));
        list.add(new JunkInfo(ContextCompat.getDrawable(this, R.mipmap.icon), ++index + "", "a"));
        list.add(new JunkInfo(ContextCompat.getDrawable(this, R.mipmap.icon), ++index + "", "a"));
        list.add(new JunkInfo(ContextCompat.getDrawable(this, R.mipmap.icon), ++index + "", "a"));
        list.add(new JunkInfo(ContextCompat.getDrawable(this, R.mipmap.icon), ++index + "", "a"));
        list.add(new JunkInfo(ContextCompat.getDrawable(this, R.mipmap.icon), ++index + "", "a"));
        list.add(new JunkInfo(ContextCompat.getDrawable(this, R.mipmap.icon), ++index + "", "a"));
        list.add(new JunkInfo(ContextCompat.getDrawable(this, R.mipmap.icon), ++index + "", "a"));
        list.add(new JunkInfo(ContextCompat.getDrawable(this, R.mipmap.icon), ++index + "", "a"));
        list.add(new JunkInfo(ContextCompat.getDrawable(this, R.mipmap.icon), ++index + "", "a"));
        list.add(new JunkInfo(ContextCompat.getDrawable(this, R.mipmap.icon), ++index + "", "a"));
        list.add(new JunkInfo(ContextCompat.getDrawable(this, R.mipmap.icon), ++index + "", "a"));
        list.add(new JunkInfo(ContextCompat.getDrawable(this, R.mipmap.icon), ++index + "", "a"));
        list.add(new JunkInfo(ContextCompat.getDrawable(this, R.mipmap.icon), ++index + "", "a"));
        list.add(new JunkInfo(ContextCompat.getDrawable(this, R.mipmap.icon), ++index + "", "a"));
        list.add(new JunkInfo(ContextCompat.getDrawable(this, R.mipmap.icon), ++index + "", "a"));
        list.add(new JunkInfo(ContextCompat.getDrawable(this, R.mipmap.icon), ++index + "", "a"));
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add_left:
                    ll_add_game.setVisibility(View.GONE);
                    gboost_recyc.scrollBy(2000, 0);
                    break;
                case R.id.title_left:
                    onBackPressed();
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
                    jumpToActivity(PowerActivity.class, bundle);
                    break;
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
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
            holder.gboost_item_name.setText(info.name);
            holder.gboost_item_c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position == 0) {
                        gboost_recyc.scrollBy(2000, 0);
                        ll_add_game.setVisibility(View.VISIBLE);
                        list_game.setAdapter(whiteListAdapter);
                        whiteListAdapter.upList(cleanApplication.getListMng());
                        whiteListAdapter.notifyDataSetChanged();
                        showToast("点击添加");
                    } else {
                        showToast(position + "");
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
