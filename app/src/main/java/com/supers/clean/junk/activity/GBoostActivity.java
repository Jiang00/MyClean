package com.supers.clean.junk.activity;

import android.graphics.Bitmap;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.supers.clean.junk.R;
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


    RecyclerView gboost_recyc;
    TextView gboost_ram_size;
    TextView gboost_network_size;
    TextView gboost_cpu_szie;


    private HorizontalPageLayoutManager pageLayoutManager;
    private PagingScrollHelper pagingScrollHelper;
    private MyAdapter adapter;
    private ArrayList<JunkInfo> list;
    private Handler mHandler;
    private long lastTotalRxBytes;
    private long lastTimeStamp;

    @Override
    protected void findId() {
        super.findId();
        gboost_recyc = (RecyclerView) findViewById(R.id.gboost_recyc);
        gboost_ram_size = (TextView) findViewById(R.id.gboost_ram_size);
        gboost_network_size = (TextView) findViewById(R.id.gboost_network_size);
        gboost_cpu_szie = (TextView) findViewById(R.id.gboost_cpu_szie);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_gboost);
        mHandler = new Handler();

        Sampler.getInstance().init(getApplicationContext(), 1000);
        Sampler.getInstance().start();
        Sampler.getInstance().setOnCpuUpdateListener(new Sampler.OnCpuUpdateListener() {
            @Override
            public void cupUpdate(final double cpu) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        gboost_cpu_szie.setText(cpu + "%");
                    }
                });
            }
        });

        mHandler.removeCallbacks(runnableW);
        mHandler.post(runnableW);

        long size = MemoryManager.getPhoneFreeRamMemory(this);
        gboost_ram_size.setText(CommonUtil.convertStorage(size) + CommonUtil.convertStorageDanwei(size));

        initList();

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

        }
    };

    private Runnable runnableW = new Runnable() {
        public void run() {
            long nowTotalRxBytes = getTotalRxBytes(); // 获取当前数据总量
            long nowTimeStamp = System.currentTimeMillis(); // 当前时间
            long speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp == lastTimeStamp ? nowTimeStamp : nowTimeStamp
                    - lastTimeStamp));// 毫秒转换
            gboost_network_size.setText(CommonUtil.convertStorageWifi(speed));
            lastTimeStamp = nowTimeStamp;
            lastTotalRxBytes = nowTotalRxBytes;
            mHandler.postDelayed(this, 2000);
        }
    };

    private long getTotalRxBytes() {
        // 得到整个手机的流量值
        return TrafficStats.getUidRxBytes(getApplicationInfo().uid) == TrafficStats.UNSUPPORTED ? 0
                : (TrafficStats.getTotalRxBytes());//
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(runnableW);
        super.onDestroy();

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
