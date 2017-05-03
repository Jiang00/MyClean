package com.supers.clean.junk.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.supers.clean.junk.R;
import com.supers.clean.junk.customeview.MyGridLayoutManager;
import com.supers.clean.junk.db.RecyclerDbHelper;
import com.supers.clean.junk.similarimage.ImageHelper;
import com.supers.clean.junk.similarimage.ImageInfo;
import com.supers.clean.junk.util.CommonUtil;

import java.util.ArrayList;

/**
 * Created by Ivy on 2017/4/28.
 */

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {
    ArrayList<ArrayList<ImageInfo>> list;
    int bastPosition;
    Context context;
    private ImageHelper imageHelper;
    private MyGridLayoutManager gridLayoutManager;
    private RecycleViewCallBack deleteCallBack;
    private ItemCallBack itemCallBack;

    public interface RecycleViewCallBack {
        void deleteItemCallback(long filesize);

        void deleteSuccCallback(ArrayList<ArrayList<ImageInfo>> list);
    }

    public interface ItemCallBack {
        void clickItem(ArrayList<ImageInfo> list);
    }

    public void setitemClickListener(ItemCallBack itemCallBack) {
        this.itemCallBack = itemCallBack;
    }

    public RecycleViewAdapter(Context context) {
        list = new ArrayList<>();
        this.context = context.getApplicationContext();

        imageHelper = new ImageHelper();
    }

    public void upList(ArrayList<ArrayList<ImageInfo>> list) {
        this.list = list;
    }

    public void addData(ArrayList<ImageInfo> list) {
        this.list.add(list);
    }

    public void addData(ArrayList<ImageInfo> list, int location) {
        this.list.add(location, list);
    }

    public ArrayList<Bitmap> checkDate() {
        ArrayList<Bitmap> check = new ArrayList<>();
        for (ArrayList<ImageInfo> info : list) {
            for (ImageInfo i : info) {
                if (!i.isNormal) {
                    check.add((Bitmap) lruCache.get(i.name));
                }
            }
        }
        return check;
    }

    public void upData(final RecycleViewCallBack callBack) {
        deleteCallBack = callBack;
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<ArrayList<ImageInfo>> deleteList = new ArrayList<>();
                for (ArrayList<ImageInfo> info : list) {
                    ArrayList<ImageInfo> delete = new ArrayList<>();
                    for (ImageInfo i : info) {
                        if (!i.isNormal) {
                            delete.add(i);
                            RecyclerDbHelper.getInstance(context).putImageToRecycler(i);
                            deleteCallBack.deleteItemCallback(i.fileSize);
                        }
                    }
                    info.removeAll(delete);
                    if (info.size() <= 1) {
                        deleteList.add(info);
                    }
                }
                list.removeAll(deleteList);
                deleteCallBack.deleteSuccCallback(list);
            }
        }).start();
    }

    public void deleteItem() {
        ArrayList<ArrayList<ImageInfo>> deleteList = new ArrayList<>();
        for (ArrayList<ImageInfo> info : list) {
            if (info.size() <= 1) {
                deleteList.add(info);
            }
        }
        list.removeAll(deleteList);
        notifyDataSetChanged();
    }

    @Override
    public RecycleViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.layout_recycle, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Log.e("picture", "onBindViewHolder");
        final ArrayList<ImageInfo> info = list.get(position);
        addItemView(holder, info);
    }

    private void addItemView(MyViewHolder holder, ArrayList<ImageInfo> list) {
        holder.recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        gridLayoutManager = new MyGridLayoutManager(context, 3);
        holder.recyclerView.setLayoutManager(gridLayoutManager);
        holder.recyclerView.setAdapter(new HomeAdapter(list));
        holder.recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void reChangesData(int position) {
        notifyItemRangeChanged(position, this.list.size() - position); //mList是数据
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;

        public MyViewHolder(View view) {
            super(view);
            recyclerView = (RecyclerView) view.findViewById(R.id.recyc);
        }

    }

    LruCache lruCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / 1024) / 4) {
        @Override
        protected int sizeOf(String key, Bitmap bitmap) {
            // 返回用户定义的item的大小，默认返回1代表item的数量.重写此方法来衡量每张图片的大小。
            return bitmap.getByteCount() / 1024;
        }
    };

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {
        ArrayList<ImageInfo> list;
        int bastPosition;


        public HomeAdapter(ArrayList<ImageInfo> list) {
            this.list = list;
            bastPosition = imageHelper.getBestImageIndex(list);
        }

        @Override
        public HomeAdapter.HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            HomeAdapter.HomeViewHolder holder = new HomeViewHolder(LayoutInflater.from(
                    context).inflate(R.layout.layout_picture_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final HomeAdapter.HomeViewHolder holder, final int position) {
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
//            HomeAdapter.LoadImage imageLoad = new HomeAdapter.LoadImage(holder);
//            imageLoad.execute(info.path, info.name);
            Bitmap cachebitmap = getBitmapFromCache(info.name);
            if (cachebitmap != null) {
                holder.picture_icon.setImageBitmap(cachebitmap);
            } else {
                Bitmap bitma = imageHelper.pathWithScaledBitmap(context, info.path, CommonUtil.dp2px(112), CommonUtil.dp2px(112));
                if (bitma == null) {
                } else {
                    addBitmapToCache(info.name, bitma);
                    holder.picture_icon.setImageBitmap(bitma);
                }
            }
            holder.picture_icon.setTag(info.name);
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
            holder.picture_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemCallBack.clickItem(list);
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

        class HomeViewHolder extends RecyclerView.ViewHolder {
            ImageView picture_icon;
            ImageView picture_check;
            ImageView picture_best;

            public HomeViewHolder(View view) {
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
/**
 *
 class LoadImage extends AsyncTask<String, Integer, Bitmap> {
 String key = null;
 String path = null;
 HomeAdapter.HomeViewHolder myViewHolder = null;

 public LoadImage(HomeAdapter.HomeViewHolder myViewHolder) {
 this.myViewHolder = myViewHolder;
 }

 @Override protected Bitmap doInBackground(String... strings) {
 path = strings[0];
 key = strings[1];
 Bitmap cachebitmap = getBitmapFromCache(key);
 //先从缓存中取，如果缓存不为空，则返回图片
 if (cachebitmap != null) {
 Log.e(key, "存在于内存中,直接返回");
 return cachebitmap;
 } else {
 Bitmap bitma = imageHelper.pathWithScaledBitmap(context, path, CommonUtil.dp2px(112), CommonUtil.dp2px(112));
 if (bitma == null) {
 return null;
 } else {
 Log.e(key, "重新加入到内存缓存中");
 addBitmapToCache(key, bitma);
 return bitma;
 }
 }
 }

 @Override protected void onPostExecute(Bitmap bitmap) {
 ImageView image = myViewHolder.picture_icon;
 if (image.getTag().equals(key)) {
 image.setVisibility(View.VISIBLE);
 image.setImageBitmap(bitmap);
 }
 super.onPostExecute(bitmap);

 }

 }
 */
    }
}
