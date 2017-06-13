package com.fast.clean.junk.adapter;

import android.graphics.Bitmap;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.util.LruCache;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fast.clean.xsimilarimage.ImageHelper;
import com.fast.clean.xsimilarimage.ImageInfo;
import com.fast.clean.junk.R;
import com.fast.clean.junk.ui.PictureActivity;
import com.fast.clean.junk.myview.MyGridLayoutManager;
import com.fast.clean.xsimilarimage.RecyclerDbHelper;

import java.util.ArrayList;

/**
 * Created by on 2017/4/28.
 */

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {
    public ArrayList<ArrayList<ImageInfo>> groupList;
    private PictureActivity pictureActivity;
    private ImageHelper imageHelper;
    private MyGridLayoutManager gridLayoutManager;
    private RecycleViewCallBack deleteCallBack;
    private Handler handler;


    public interface RecycleViewCallBack {
        void deleteItemCallback(long fileSize);

        void deleteSuccessCallback(ArrayList<ArrayList<ImageInfo>> list);
    }

    public RecycleViewAdapter(PictureActivity pictureActivity, ArrayList<ArrayList<ImageInfo>> totalSimilarImage, ImageHelper imageHelper, Handler handler) {
        this.handler = handler;
        this.pictureActivity = pictureActivity;
        this.imageHelper = imageHelper;
        groupList = totalSimilarImage;
    }


    public void addData(ArrayList<ImageInfo> list, int location) {
        groupList.add(location, list);
    }

    public void upData(ArrayList<ArrayList<ImageInfo>> list) {
        groupList = list;
    }

    public ArrayList<Bitmap> checkDate() {
        ArrayList<Bitmap> check = new ArrayList<>();
        for (ArrayList<ImageInfo> info : groupList) {
            for (ImageInfo i : info) {
                if (!i.isNormal) {
                    check.add((Bitmap) lruCache.get(i.name));
                }
            }
        }
        return check;
    }

    public void delete(final RecycleViewCallBack callBack) {
        deleteCallBack = callBack;
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<ArrayList<ImageInfo>> deleteList = new ArrayList<>();
                for (ArrayList<ImageInfo> info : groupList) {
                    ArrayList<ImageInfo> delete = new ArrayList<>();
                    for (ImageInfo i : info) {
                        if (!i.isNormal) {
                            delete.add(i);
                            RecyclerDbHelper.getInstance(pictureActivity).putImageToRecycler(i);
                            deleteCallBack.deleteItemCallback(i.fileSize);
                        }
                    }
                    info.removeAll(delete);
                    if (info.size() <= 1) {
                        deleteList.add(info);
                    }
                }
                groupList.removeAll(deleteList);
                deleteCallBack.deleteSuccessCallback(groupList);

            }
        }).start();
    }

    public void deleteItem() {
        ArrayList<ArrayList<ImageInfo>> deleteList = new ArrayList<>();
        for (ArrayList<ImageInfo> info : groupList) {
            if (info.size() <= 1) {
                deleteList.add(info);
            }
        }
        groupList.removeAll(deleteList);
        notifyDataSetChanged();
    }

    @Override
    public RecycleViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                pictureActivity).inflate(R.layout.layout_recycle, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ArrayList<ImageInfo> info = groupList.get(position);
        addItemView(holder, info);
    }

    private void addItemView(MyViewHolder holder, ArrayList<ImageInfo> list) {
        holder.recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        gridLayoutManager = new MyGridLayoutManager(pictureActivity, 3);
        holder.recyclerView.setLayoutManager(gridLayoutManager);
        holder.recyclerView.setAdapter(new HomeAdapter(list));
        holder.recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void reChangesData(int position) {
        notifyItemRangeChanged(position, this.groupList.size() - position); //mList是数据
    }

    @Override
    public int getItemCount() {
        return this.groupList.size();
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
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).isNormal) {
                    bastPosition = i;
                    return;
                }
            }
        }

        @Override
        public HomeAdapter.HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            HomeAdapter.HomeViewHolder holder = new HomeViewHolder(LayoutInflater.from(
                    pictureActivity).inflate(R.layout.layout_picture_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final HomeAdapter.HomeViewHolder holder, final int position) {
            final ImageInfo info = list.get(position);
            if (bastPosition == position) {
                holder.picture_best.setVisibility(View.VISIBLE);
            } else {
                holder.picture_best.setVisibility(View.INVISIBLE);
            }
            if (info.isNormal) {
                holder.picture_check.setImageResource(R.mipmap.ram_normal);
            } else {
                holder.picture_check.setImageResource(R.mipmap.ram_passed);
            }
//            HomeAdapter.LoadImage imageLoad = new HomeAdapter.LoadImage(holder);
//            imageLoad.execute(info.path, info.name);

            Bitmap cachebitmap = getBitmapFromCache(info.name);
            if (cachebitmap != null) {
                holder.picture_icon.setImageBitmap(cachebitmap);
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Bitmap bitmap = imageHelper.getImageThumbnail(pictureActivity, info.originId, MediaStore.Images.Thumbnails.MINI_KIND);
                        // final Bitmap bitmap = imageHelper.pathWithScaledBitmap(pictureActivity, info.path, FileUtil.dp2px(112), FileUtil.dp2px(112));
                        if (bitmap != null) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    addBitmapToCache(info.name, bitmap);
                                    holder.picture_icon.setImageBitmap(bitmap);
                                }
                            });
                        }
                    }
                }).start();

            }

            holder.picture_icon.setTag(info.name);
            holder.picture_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pictureActivity.mIsQuerying) {
                        return;
                    }
                    info.isNormal = !info.isNormal;
                    pictureActivity.updateUi();
                    if (info.isNormal) {
                        holder.picture_check.setImageResource(R.mipmap.ram_normal);
                    } else {
                        holder.picture_check.setImageResource(R.mipmap.ram_passed);
                    }

                }

            });
            holder.picture_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pictureActivity.mIsQuerying) {
                        return;
                    }
                    pictureActivity.showBigPic(list);
                }
            });
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
    }
}
