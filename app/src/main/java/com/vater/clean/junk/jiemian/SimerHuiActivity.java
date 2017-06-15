package com.vater.clean.junk.jiemian;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.LruCache;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vater.clean.image.ImageHelper;
import com.vater.clean.image.ImageInfo;
import com.vater.clean.util.PreData;
import com.android.client.AndroidSdk;
import com.vater.clean.junk.R;
import com.vater.clean.image.RecyclerDbHelper;
import com.vater.clean.util.Util;
import com.vater.clean.junk.gongju.AdUtil;
import com.vater.clean.junk.gongju.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by on 2017/4/28.
 */

public class SimerHuiActivity extends BaseActivity {

    TextView title_name;
    LinearLayout ll_picture;
    TextView picture_restore;
    TextView hui_never;
    FrameLayout title_left;
    TextView title_check;
    RecyclerView picture_hui_recyc;
    TextView picture_delete;

    private String TAG_RECYCLE = "recyclebin";
    private View nativeView;
    private LinearLayout ll_ad;
    GridLayoutManager gridLayoutManager;
    private ImageHelper imageHelper;
    private HuiAdapter adapter;
    private AlertDialog dialog;
    private boolean isRestore;
    private Handler mHandler;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_picture_hui);
        title_name.setText(R.string.picture_10);
        imageHelper = new ImageHelper();
        mHandler = new Handler();
        ArrayList<ImageInfo> imageInfos = RecyclerDbHelper.getInstance(this).getRecyclerImageList();
        if (imageInfos.size() == 0) {
            hui_never.setVisibility(View.VISIBLE);
            ll_picture.setVisibility(View.INVISIBLE);
            title_check.setVisibility(View.INVISIBLE);
        } else {
            title_check.setVisibility(View.VISIBLE);
            gridLayoutManager = new GridLayoutManager(this, 3);
            picture_hui_recyc.setLayoutManager(gridLayoutManager);
            picture_hui_recyc.setAdapter(adapter = new HuiAdapter(imageInfos));
            picture_hui_recyc.setItemAnimator(new DefaultItemAnimator());
        }
        addListener();
        loadAd();
    }

    private void loadAd() {
        if (PreData.getDB(this, Constant.RECYCLEBIN, 0) == 1) {
            AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
        } else {
            addAd();
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
                case R.id.picture_restore:
//                    dialog(adapter.checkNum());
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
            title_check.setVisibility(View.INVISIBLE);
        } else {
            title_check.setVisibility(View.VISIBLE);
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
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        title_check = (TextView) findViewById(R.id.title_check);
        picture_hui_recyc = (RecyclerView) findViewById(R.id.picture_hui_recyc);
        hui_never = (TextView) findViewById(R.id.hui_never);
        ll_picture = (LinearLayout) findViewById(R.id.ll_picture);
        picture_restore = (TextView) findViewById(R.id.picture_restore);
        picture_delete = (TextView) findViewById(R.id.picture_delete);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
    }

    private void addAd() {
        nativeView = AdUtil.getNativeAdView(TAG_RECYCLE, R.layout.native_ad_3);
        if (ll_ad != null && nativeView != null) {
            ViewGroup.LayoutParams layout_ad = ll_ad.getLayoutParams();
            ll_ad.setLayoutParams(layout_ad);
            ll_ad.addView(nativeView);
        }
    }

    private void addListener() {
        title_left.setOnClickListener(clickListener);
        title_check.setOnClickListener(clickListener);
        picture_restore.setOnClickListener(clickListener);
        picture_delete.setOnClickListener(clickListener);

    }

    private void dialog(final boolean isrestore, final int count) {
        View view = View.inflate(this, R.layout.dialog_picture, null);
        final TextView pro = (TextView) view.findViewById(R.id.count);
        TextView message = (TextView) view.findViewById(R.id.message);
        final ProgressBar delete_progress = (ProgressBar) view.findViewById(R.id.delete_progress);
        final TextView ok = (TextView) view.findViewById(R.id.ok);
        final TextView cancle = (TextView) view.findViewById(R.id.cancle);
        pro.setVisibility(View.GONE);
        if (isrestore) {
            message.setText(getString(R.string.picture_15, count));
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
                delete_progress.setVisibility(View.VISIBLE);
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
        dialog = new AlertDialog.Builder(this, R.style.add_dialog).create();
        dialog.show();
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = dm.widthPixels; //设置宽度
        lp.height = dm.heightPixels; //设置高度
        int uiOptions =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        //布局位于状态栏下方
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        //隐藏导航栏
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        if (Build.VERSION.SDK_INT >= 19) {
            uiOptions |= 0x00001000;
        } else {
            uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        }
        dialog.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setContentView(view);
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
                info.isNormal = true;
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
                            RecyclerDbHelper.getInstance(SimerHuiActivity.this).restoreImageFromRecycler(info);
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
                            RecyclerDbHelper.getInstance(SimerHuiActivity.this).deleteImageFromRecyler(info);
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
                    SimerHuiActivity.this).inflate(R.layout.layout_hui_item, parent,
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
                        final Bitmap bitma = imageHelper.pathWithScaledBitmap(SimerHuiActivity.this, info.backFilePath, Util.dp2px(112), Util.dp2px(112));
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
