package com.supers.clean.junk.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.LruCache;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.disklrucache.DiskLruCache;
import com.supers.clean.junk.R;
import com.supers.clean.junk.customeview.LineProgressView;
import com.supers.clean.junk.customeview.MyGridLayoutManager;
import com.supers.clean.junk.similarimage.ImageHelper;
import com.supers.clean.junk.similarimage.ImageInfo;
import com.supers.clean.junk.util.CommonUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Ivy on 2017/4/27.
 */

public class PictureActivity extends BaseActivity {
    private static final int PICTHRE_PATH = 0;
    private static final int PICTHRE_SUCC = 1;
    private static final int ADAPTER_NOTIFI = 2;

    FrameLayout title_left;
    TextView title_name;
    ImageView title_right;
    LinearLayout picture_item;
    TextView picture_path;
    TextView picture_danwei;
    TextView picture_size;
    Button picture_button;
    TextView picture_scan;
    ProgressBar picture_progressbar;
    ScrollView picture_scroll;
    LineProgressView picture_line;

    private MyGridLayoutManager gridLayoutManager;
    private ImageHelper imageHelper;


    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        title_right = (ImageView) findViewById(R.id.title_right);
        picture_item = (LinearLayout) findViewById(R.id.picture_item);
        picture_path = (TextView) findViewById(R.id.picture_path);
        picture_size = (TextView) findViewById(R.id.picture_size);
        picture_danwei = (TextView) findViewById(R.id.picture_danwei);
        picture_button = (Button) findViewById(R.id.picture_button);
        picture_scan = (TextView) findViewById(R.id.picture_scan);
        picture_progressbar = (ProgressBar) findViewById(R.id.picture_progressbar);
        picture_scroll = (ScrollView) findViewById(R.id.picture_scroll);
        picture_line = (LineProgressView) findViewById(R.id.picture_line);


    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PICTHRE_PATH:
                    picture_path.setText((String) msg.obj);
                    picture_line.setProgress(msg.arg1 * 100 / msg.arg2);
                    break;
                case PICTHRE_SUCC:
                    picture_button.setVisibility(View.VISIBLE);
                    picture_line.setVisibility(View.GONE);
                    picture_path.setVisibility(View.GONE);
                    picture_scan.setText(R.string.picture_jianyi);
                    break;
                case ADAPTER_NOTIFI:
                    HomeAdapter adapter = (HomeAdapter) msg.obj;
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }

            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_picture);
        title_name.setText(R.string.side_picture);
        imageHelper = new ImageHelper();
        new Thread(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        }).start();
        picture_scroll.setEnabled(false);
        clickListen();

    }

    private void clickListen() {
        title_left.setOnClickListener(clickListener);
        title_right.setOnClickListener(clickListener);
        picture_button.setOnClickListener(clickListener);
    }

    private void initData() {
        final int dp4 = (int) getResources().getDimension(R.dimen.d4);
        final int dp6 = (int) getResources().getDimension(R.dimen.d6);
        final int dp8 = (int) getResources().getDimension(R.dimen.d8);

        imageHelper.querySimilarImage(this, new ImageHelper.OnQuerySimilarPicCallBack() {
            @Override
            public void startAsync(ArrayList<ImageInfo> localImageList) {

            }

            @Override
            public void endAsync(ArrayList<ImageInfo> localImageList, ArrayList<ArrayList<ImageInfo>> localImages) {
                Message msg = mHandler.obtainMessage();//同 new Message();
                msg.what = PICTHRE_SUCC;
                mHandler.sendMessage(msg);
            }

            @Override
            public void startAsyncPic(int i, ArrayList<ImageInfo> localImageList) {
                Message msg = mHandler.obtainMessage();//同 new Message();
                msg.what = PICTHRE_PATH;
                msg.arg1 = i;
                msg.arg2 = localImageList.size();
                msg.obj = localImageList.get(i).path;
                mHandler.sendMessage(msg);
            }

            @Override
            public void endAsyncPic(int i, ArrayList<ImageInfo> localImageList) {

            }

            @Override
            public void haveQuerySimilarPic(int i, ArrayList<ImageInfo> similarImage, ArrayList<ArrayList<ImageInfo>> totalSimilarImage, final long totalSize) {
                final ArrayList<ImageInfo> list_item = totalSimilarImage.get(totalSimilarImage.size() - 1);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        picture_progressbar.setVisibility(View.GONE);
                        picture_size.setText(CommonUtil.convertStorage(totalSize));
                        picture_danwei.setText(CommonUtil.convertStorageDanwei(totalSize));
                        addItemView(list_item, dp4, dp6, dp8);
                    }
                });
            }

        });

    }

    private void addItemView(ArrayList<ImageInfo> list, int dp4, int dp6, int dp8) {
        RecyclerView recycleView = new RecyclerView(this);
        recycleView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recycleView.setPadding(dp6, dp4, dp6, dp4);
        recycleView.setBackgroundColor(ContextCompat.getColor(this, R.color.white_100));
        gridLayoutManager = new MyGridLayoutManager(this, 3);
        recycleView.setLayoutManager(gridLayoutManager);
        recycleView.setAdapter(new HomeAdapter(list));
        recycleView.setItemAnimator(new DefaultItemAnimator());
        picture_item.addView(recycleView, 0);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) recycleView.getLayoutParams();
        layoutParams.bottomMargin = dp8;
        recycleView.setLayoutParams(layoutParams);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_left:
                    onBackPressed();
                    break;
                case R.id.title_right:

                    break;
                case R.id.picture_button:

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int size = picture_item.getChildCount();
                            if (size == 0) {
                                return;
                            }
                            Log.e("picture", System.currentTimeMillis() + "");
                            for (int i = 0; i < size; i++) {
                                RecyclerView recye = (RecyclerView) picture_item.getChildAt(i);
                                final HomeAdapter adapter = (HomeAdapter) recye.getAdapter();
                                adapter.cleanData();
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            Log.e("picture", System.currentTimeMillis() + "");
                        }
                    }).start();

                    break;
            }

        }
    };

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
        ArrayList<ImageInfo> list;
        int bastPosition;
        LruCache lruCache;

        public HomeAdapter(ArrayList<ImageInfo> list) {
            this.list = list;
            bastPosition = imageHelper.getBestImageIndex(list);
            this.list.get(bastPosition).isNormal = true;
            lruCache = new LruCache<String, Bitmap>(500) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    // 返回用户定义的item的大小，默认返回1代表item的数量.重写此方法来衡量每张图片的大小。
                    return bitmap.getByteCount() / 1024;
                }
            };
        }

        public void cleanData() {
            ArrayList<ImageInfo> listdata = new ArrayList<>();
            for (ImageInfo info : list) {
                if (!info.isNormal) {
                    listdata.add(info);
                }
            }
            list.removeAll(listdata);
            bastPosition = imageHelper.getBestImageIndex(list);
            Message msg = new Message();
            msg.obj = this;
            msg.what = ADAPTER_NOTIFI;
            mHandler.sendMessage(msg);
//            mHandler.post(new Runnable() {
//                @Override
//                public void run() {
////                    notifyItemRangeChanged(0, list.size()); //mList是数据
//                    notifyDataSetChanged();
//                }
//            });
        }

        public void upList(ArrayList<ImageInfo> list) {
            this.list = list;
        }

        @Override
        public PictureActivity.HomeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            PictureActivity.HomeAdapter.MyViewHolder holder = new PictureActivity.HomeAdapter.MyViewHolder(LayoutInflater.from(
                    PictureActivity.this).inflate(R.layout.layout_picture_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final PictureActivity.HomeAdapter.MyViewHolder holder, final int position) {
            Log.e("picture", "onBindViewHolder");
            final ImageInfo info = list.get(position);
            if (bastPosition == position) {
                holder.picture_best.setVisibility(View.VISIBLE);
            } else {
                holder.picture_best.setVisibility(View.INVISIBLE);
            }
            if (info.isNormal) {
                holder.picture_check.setImageResource(R.mipmap.picture_normal);
            } else {
                holder.picture_check.setImageResource(R.mipmap.picture_passed);
            }
//            Bitmap bitmap = imageHelper.pathWithScaledBitmap(PictureActivity.this, info.path, CommonUtil.dp2px(112), CommonUtil.dp2px(112));
            LoadImage imageLoad = new LoadImage(holder);
            imageLoad.execute(info.path);
            holder.picture_icon.setTag(info.path);
//            holder.picture_icon.setImageBitmap(bitmap);
            holder.picture_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    info.isNormal = !info.isNormal;
                    if (info.isNormal) {
                        holder.picture_check.setImageResource(R.mipmap.picture_normal);
                    } else {
                        holder.picture_check.setImageResource(R.mipmap.picture_passed);
                    }
                }
            });
        }

        public void reChangesData(int position) {
            bastPosition = imageHelper.getBestImageIndex(list);
            notifyItemRangeChanged(position, this.list.size() - position); //mList是数据

        }

        @Override
        public int getItemCount() {
            return this.list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView picture_icon;
            ImageView picture_check;
            ImageView picture_best;

            public MyViewHolder(View view) {
                super(view);
                picture_icon = (ImageView) view.findViewById(R.id.picture_icon);
                picture_check = (ImageView) view.findViewById(R.id.picture_check);
                picture_best = (ImageView) view.findViewById(R.id.picture_best);
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

        class LoadImage extends AsyncTask<String, Integer, Bitmap> {
            String url = null;
            MyViewHolder myViewHolder = null;

            public LoadImage(MyViewHolder myViewHolder) {
                this.myViewHolder = myViewHolder;
            }

            @Override
            protected Bitmap doInBackground(String... strings) {
                url = strings[0];
                Bitmap cachebitmap = getBitmapFromCache(url);
                //先从缓存中取，如果缓存不为空，则返回图片
                if (cachebitmap != null) {
                    Log.e(url, "存在于内存中,直接返回");
                    return cachebitmap;
                } else {
                    Bitmap bitma = imageHelper.pathWithScaledBitmap(PictureActivity.this, url, CommonUtil.dp2px(112), CommonUtil.dp2px(112));
                    if (bitma == null) {
                        return null;
                    } else {
                        Log.e(url, "重新加入到内存缓存中");
                        addBitmapToCache(url, bitma);
                        return bitma;
                    }
                }
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                ImageView image = myViewHolder.picture_icon;
                if (image.getTag().equals(url)) {
                    image.setVisibility(View.VISIBLE);
                    image.setImageBitmap(bitmap);
                }
                super.onPostExecute(bitmap);

            }
        }

    }
}
