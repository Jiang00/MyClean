package com.mutter.clean.junk.myActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.LruCache;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mutter.clean.imageclean.ImageHelper;
import com.mutter.clean.imageclean.ImageInfo;
import com.mutter.clean.util.PreData;
import com.android.client.AndroidSdk;
import com.mutter.clean.junk.R;
import com.mutter.clean.imageclean.RecyclerDbHelper;
import com.mutter.clean.util.Util;
import com.mutter.clean.junk.util.AdUtil;
import com.mutter.clean.junk.util.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 */

public class PhotoHuiActivity extends BaseActivity {

    RecyclerView picture_hui_recyc;
    LinearLayout hui_never;
    LinearLayout ll_picture;
    Button picture_delete;
    FrameLayout title_left;
    TextView title_name;
    ImageView title_right;

    private ImageHelper imageHelper;
    private HuiAdapter adapter;
    GridLayoutManager gridLayoutManager;
    private AlertDialog dialog;
    private boolean isRestore;
    private Handler mHandler;
    private String TAG_RECYCLE = "recyclebin";
    private View nativeView;
    private LinearLayout ll_ad;
    FrameLayout ad_fl;

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_right = (ImageView) findViewById(R.id.title_right);
        title_name = (TextView) findViewById(R.id.title_name);
        picture_hui_recyc = (RecyclerView) findViewById(R.id.picture_hui_recyc);
        hui_never = (LinearLayout) findViewById(R.id.hui_never);
        ll_picture = (LinearLayout) findViewById(R.id.ll_picture);
        picture_delete = (Button) findViewById(R.id.picture_delete);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
        ad_fl = (FrameLayout) findViewById(R.id.ad_fl);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_picture_hui);
        title_name.setText(R.string.picture_10);
        imageHelper = new ImageHelper();
        mHandler = new Handler();
        title_right.setVisibility(View.VISIBLE);
        title_right.setImageResource(R.mipmap.picture_huanyuan);
        ArrayList<ImageInfo> imageInfos = RecyclerDbHelper.getInstance(this).getRecyclerImageList();
        if (imageInfos.size() == 0) {
            hui_never.setVisibility(View.VISIBLE);
            ll_picture.setVisibility(View.INVISIBLE);
            title_right.setVisibility(View.GONE);
            picture_delete.setVisibility(View.GONE);
        } else {
            title_right.setVisibility(View.VISIBLE);
            gridLayoutManager = new GridLayoutManager(this, 3);
            picture_hui_recyc.setLayoutManager(gridLayoutManager);
            picture_hui_recyc.setAdapter(adapter = new HuiAdapter(imageInfos));
            picture_hui_recyc.setItemAnimator(new DefaultItemAnimator());
        }
        addListener();

        loadAd();
    }


    private void addListener() {
        title_left.setOnClickListener(clickListener);
        title_right.setOnClickListener(clickListener);
        picture_delete.setOnClickListener(clickListener);

    }

    private void loadAd() {
        if (PreData.getDB(this, Constant.RECYCLEBIN, 0) == 1) {
            AndroidSdk.showFullAd(AdUtil.FULL_DEFAULT);
        } else {
            addAd();
        }
    }

    private void addAd() {
        nativeView = AdUtil.getNativeAdView(TAG_RECYCLE, R.layout.native_ad_3);
        if (ll_ad != null && nativeView != null) {
            ll_ad.addView(nativeView);
            ad_fl.setVisibility(View.VISIBLE);
        }
    }

    boolean isfalse = true;
    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.title_left:
                    onBackPressed();
                    break;
                case R.id.title_check:
                    if (isfalse) {
                        isfalse = false;
                        adapter.allPassed(false);
                    } else {
                        isfalse = true;
                        adapter.allPassed(true);
                    }
                    break;
                case R.id.title_right:
                    if (adapter.checkNum() > 0) {
                        dialog(true, adapter.checkNum());
                    }
                    break;
                case R.id.picture_delete:
                    if (adapter.checkNum() > 0) {
                        dialog(false, adapter.checkNum());
                    }
                    break;
            }
        }
    };

    private void notifiActivity() {
        if (adapter.getItemCount() == 0) {
            hui_never.setVisibility(View.VISIBLE);
            ll_picture.setVisibility(View.INVISIBLE);
            title_right.setVisibility(View.INVISIBLE);
        } else {
            title_right.setVisibility(View.VISIBLE);
            hui_never.setVisibility(View.INVISIBLE);
            ll_picture.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if (isRestore) {
            setResult(5);
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    private void dialog(final boolean isrestore, final int count) {
        View view = View.inflate(this, R.layout.dialog_picture, null);
        final TextView pro = (TextView) view.findViewById(R.id.count);
        final TextView message = (TextView) view.findViewById(R.id.message);
        final TextView title = (TextView) view.findViewById(R.id.title);
        final ProgressBar delete_progress = (ProgressBar) view.findViewById(R.id.delete_progress);
        final LinearLayout progress_lin = (LinearLayout) view.findViewById(R.id.progress_lin);
        final TextView ok = (TextView) view.findViewById(R.id.ok);
        final TextView cancle = (TextView) view.findViewById(R.id.cancle);
        pro.setVisibility(View.GONE);
        if (isrestore) {
            message.setText(getString(R.string.picture_15, count));
            title.setText(getString(R.string.picture_16));
        } else {
            message.setText(getString(R.string.picture_12, count));
        }
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ok.setOnClickListener(null);
                cancle.setOnClickListener(null);
                pro.setText(count + "/" + "0");
                pro.setVisibility(View.VISIBLE);
                progress_lin.setVisibility(View.VISIBLE);
                message.setVisibility(View.GONE);
                if (isrestore) {
                    adapter.restore(new MyCallBack() {
                        int num = 0;

                        @Override
                        public void progress() {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    delete_progress.setProgress(++num * 100 / count);
                                    pro.setText(count + "/" + num);
                                }
                            });
                        }

                        @Override
                        public void success() {
                            dialog.dismiss();
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                    notifiActivity();
                                }
                            });

                        }
                    });
                } else {
                    adapter.delete(new MyCallBack() {
                        int num = 0;

                        @Override
                        public void progress() {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    delete_progress.setProgress(++num * 100 / count);
                                    pro.setText(count + "/" + num);
                                }
                            });
                        }

                        @Override
                        public void success() {
                            dialog.dismiss();
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                    notifiActivity();
                                }
                            });
                        }
                    });
                }
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog = new AlertDialog.Builder(this).create();
        dialog.setView(view);
        dialog.show();
    }

    MyCallBack callBack;

    public interface MyCallBack {
        void progress();

        void success();
    }

    class HuiAdapter extends RecyclerView.Adapter<HuiAdapter.HomeViewHolder> {
        ArrayList<ImageInfo> list;
        LruCache lruCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / 1024) / 4) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // 返回用户定义的item的大小，默认返回1代表item的数量.重写此方法来衡量每张图片的大小。
                return bitmap.getByteCount() / 1024;
            }
        };


        public HuiAdapter(ArrayList<ImageInfo> list) {
            this.list = list;
            for (ImageInfo info : list) {
                info.isNormal = false;
            }
        }

        public void restore(MyCallBack callback) {
            callBack = callback;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    ArrayList<ImageInfo> rostore = new ArrayList<>();
                    for (ImageInfo info : list) {
                        if (!info.isNormal) {
                            RecyclerDbHelper.getInstance(PhotoHuiActivity.this).restoreImageFromRecycler(info);
                            rostore.add(info);
                            isRestore = true;
                            if (callBack != null) {
                                callBack.progress();
                            }
                        }
                    }
                    list.removeAll(rostore);
                    if (callBack != null) {
                        callBack.success();
                    }
                }
            }).start();


        }

        public int checkNum() {
            int count = 0;
            for (ImageInfo info : list) {
                if (!info.isNormal) {
                    count++;
                }
            }
            return count;
        }

        public void delete(MyCallBack callback) {
            callBack = callback;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ArrayList<ImageInfo> delete = new ArrayList<>();
                    for (ImageInfo info : list) {
                        if (!info.isNormal) {
                            RecyclerDbHelper.getInstance(PhotoHuiActivity.this).deleteImageFromRecyler(info);
                            delete.add(info);
                            if (callBack != null) {
                                callBack.progress();
                            }
                        }
                    }
                    list.removeAll(delete);
                    if (callBack != null) {
                        callBack.success();
                    }
                }
            }).start();

        }

        public void allPassed(boolean check) {
            for (ImageInfo info : list) {
                info.isNormal = check;
            }
            notifyDataSetChanged();
        }


        public HuiAdapter.HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            HuiAdapter.HomeViewHolder holder = new HuiAdapter.HomeViewHolder(LayoutInflater.from(
                    PhotoHuiActivity.this).inflate(R.layout.layout_hui_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final HuiAdapter.HomeViewHolder holder, final int position) {
            final ImageInfo info = list.get(position);
            if (info.isNormal) {
                holder.picture_check.setImageResource(R.mipmap.ram_normal);
            } else {
                holder.picture_check.setImageResource(R.mipmap.ram_passed);
            }

            Bitmap cachebitmap = getBitmapFromCache(info.backFilePath);
            if (cachebitmap != null) {
                holder.picture_icon.setImageBitmap(cachebitmap);
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Bitmap bitma = imageHelper.pathWithScaledBitmap(PhotoHuiActivity.this, info.backFilePath, getResources().getDimensionPixelOffset(R.dimen.d112),
                                getResources().getDimensionPixelOffset(R.dimen.d112));
                        if (bitma == null) {
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    addBitmapToCache(info.backFilePath, bitma);
                                    holder.picture_icon.setImageBitmap(bitma);
                                    holder.picture_icon.setTag(info.backFilePath);
                                }
                            });
                        }
                    }
                }).start();
            }

            long time = System.currentTimeMillis();
            long last = time;
            String regEx = "\\d{4}-\\d{1,2}-\\d{1,2}";
            Pattern pat = Pattern.compile(regEx);
            Matcher matcher = pat.matcher(info.backFilePath);
            if (matcher.find()) {
                Calendar calendar = Calendar.getInstance();
                try {
                    calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(matcher.group()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                last = calendar.getTimeInMillis();
            }

            holder.picture_time.setText((Util.millTransFate(RecyclerDbHelper.RECYCLER_AUTO_DELETE_INTERVAL - (time - last)) + 1) + " " + getText(R.string.picture_11));
            holder.picture_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    info.isNormal = !info.isNormal;
                    if (info.isNormal) {
                        holder.picture_check.setImageResource(R.mipmap.ram_normal);
                    } else {
                        holder.picture_check.setImageResource(R.mipmap.ram_passed);
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
            ImageView picture_icon;
            ImageView picture_check;
            TextView picture_time;

            public HomeViewHolder(View view) {
                super(view);
                picture_icon = (ImageView) view.findViewById(R.id.picture_icon);
                picture_check = (ImageView) view.findViewById(R.id.picture_check);
                picture_time = (TextView) view.findViewById(R.id.picture_time);
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
