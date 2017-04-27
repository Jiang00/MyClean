package com.supers.clean.junk.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.supers.clean.junk.R;
import com.supers.clean.junk.customeview.MyGridLayoutManager;
import com.supers.clean.junk.entity.JunkInfo;
import com.supers.clean.junk.similarimage.ImageHelper;
import com.supers.clean.junk.similarimage.ImageInfo;
import com.supers.clean.junk.similarimage.SimilarImageActivity;
import com.supers.clean.junk.similarimage.SimilarPictureAdapter;
import com.supers.clean.junk.util.CommonUtil;

import java.util.ArrayList;

/**
 * Created by Ivy on 2017/4/27.
 */

public class PictureActivity extends BaseActivity {
    private static final int PICTHRE_PATH = 0;
    private static final int PICTHRE_SIZE = 1;

    FrameLayout title_left;
    TextView title_name;
    ImageView title_right;
    LinearLayout picture_item;
    TextView picture_path;
    TextView picture_danwei;
    TextView picture_size;

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

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PICTHRE_PATH:
                    picture_path.setText((String) msg.obj);
                    break;
                case PICTHRE_SIZE:
                    picture_size.setText((String) msg.obj);
                    picture_danwei.setText((String) msg.obj);
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
        title_left.setOnClickListener(clickListener);
        title_right.setOnClickListener(clickListener);
        imageHelper = new ImageHelper();
        initData();

    }

    private void initData() {
        int dp4 = (int) getResources().getDimension(R.dimen.d4);
        int dp6 = (int) getResources().getDimension(R.dimen.d6);
        int dp8 = (int) getResources().getDimension(R.dimen.d8);

        imageHelper.querySimilarImage(this, new ImageHelper.OnQuerySimilarPicCallBack() {
            @Override
            public void startAsync(ArrayList<ImageInfo> localImageList) {

            }

            @Override
            public void endAsync(ArrayList<ImageInfo> localImageList, ArrayList<ArrayList<ImageInfo>> localImages) {

            }

            @Override
            public void startAsyncPic(int i, ArrayList<ImageInfo> localImageList) {
                Message msg = mHandler.obtainMessage();//同 new Message();
                msg.what = PICTHRE_PATH;
                msg.obj = localImageList.get(i).path;
                mHandler.sendMessage(msg);
            }

            @Override
            public void endAsyncPic(int i, ArrayList<ImageInfo> localImageList) {

            }

            @Override
            public void haveQuerySimilarPic(int i, ArrayList<ImageInfo> similarImage, ArrayList<ArrayList<ImageInfo>> totalSimilarImage, int bestImageIndex, long totalSize) {
                Message msg = mHandler.obtainMessage();//同 new Message();
                msg.what = PICTHRE_SIZE;
                msg.obj = localImageList.get(i).path;
                mHandler.sendMessage(msg);
            }
        });

        addItemView(datalist, dp4, dp6, dp8);
        RecyclerView recycle_1 = new RecyclerView(this);
        recycle_1.setOverScrollMode(View.OVER_SCROLL_NEVER);

        recycle_1.setPadding(dp6, dp4, dp6, dp4);

        recycle_1.setBackgroundColor(ContextCompat.getColor(this, R.color.white_100));
        gridLayoutManager = new MyGridLayoutManager(this, 3);
        recycle_1.setLayoutManager(gridLayoutManager);
        recycle_1.setAdapter(new HomeAdapter());

        recycle_1.setItemAnimator(new DefaultItemAnimator());
        RecyclerView recycle_2 = new RecyclerView(this);
        recycle_2.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recycle_2.setPadding(dp6, dp4, dp6, dp4);
        recycle_2.setBackgroundColor(ContextCompat.getColor(this, R.color.white_100));

        gridLayoutManager = new MyGridLayoutManager(this, 3);
        recycle_2.setLayoutManager(gridLayoutManager);
        recycle_2.setAdapter(new HomeAdapter());
        recycle_2.setItemAnimator(new DefaultItemAnimator());
        RecyclerView recycle_3 = new RecyclerView(this);
        recycle_3.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recycle_3.setPadding(dp6, dp4, dp6, dp4);

        recycle_3.setBackgroundColor(ContextCompat.getColor(this, R.color.white_100));
        gridLayoutManager = new MyGridLayoutManager(this, 3);
        recycle_3.setLayoutManager(gridLayoutManager);
        recycle_3.setAdapter(new HomeAdapter());
        recycle_3.setItemAnimator(new DefaultItemAnimator());
        RecyclerView recycle_4 = new RecyclerView(this);
        recycle_4.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recycle_4.setPadding(dp6, dp4, dp6, dp4);

        recycle_4.setBackgroundColor(ContextCompat.getColor(this, R.color.white_100));
        gridLayoutManager = new MyGridLayoutManager(this, 3);
        recycle_4.setLayoutManager(gridLayoutManager);
        recycle_4.setAdapter(new HomeAdapter());
        recycle_4.setItemAnimator(new DefaultItemAnimator());
        picture_item.addView(recycle_1);
        picture_item.addView(recycle_2);
        picture_item.addView(recycle_3);
        picture_item.addView(recycle_4);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) recycle_1.getLayoutParams();
        layoutParams.bottomMargin = dp8;
        recycle_1.setLayoutParams(layoutParams);
        recycle_3.setLayoutParams(layoutParams);
        recycle_3.setLayoutParams(layoutParams);
        recycle_4.setLayoutParams(layoutParams);


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
        picture_item.addView(recycleView);
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
            }

        }
    };

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
        ArrayList<ImageInfo> list;
        int bastPosition;

        public HomeAdapter(ArrayList<ImageInfo> list) {
            this.list = list;
            bastPosition = imageHelper.getBestImageIndex(list);
            this.list.get(bastPosition).isNormal = true;
        }

        public HomeAdapter() {

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
        public void onBindViewHolder(PictureActivity.HomeAdapter.MyViewHolder holder, final int position) {
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
            Bitmap bitmap = imageHelper.loadBitmapFromFile(info.path, CommonUtil.dp2px(112), CommonUtil.dp2px(112));
            holder.picture_icon.setImageBitmap(bitmap);
            holder.picture_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    info.isNormal = !info.isNormal;
                    notifyDataSetChanged();
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
    }
}
