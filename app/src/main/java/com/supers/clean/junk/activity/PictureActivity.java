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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.supers.clean.junk.R;
import com.supers.clean.junk.customeview.LineProgressView;
import com.supers.clean.junk.customeview.MyGridLayoutManager;
import com.supers.clean.junk.entity.JunkInfo;
import com.supers.clean.junk.filemanager.FileUtils;
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
    private static final int PICTHRE_SUCC = 1;

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
        picture_scroll.setClickable(false);
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
                    int size = picture_item.getChildCount();
                    if (size == 0) {
                        return;
                    }
                    for (int i = 0; i < size; i++) {
                        RecyclerView recye = (RecyclerView) picture_item.getChildAt(i);
                        HomeAdapter adapter = (HomeAdapter) recye.getAdapter();

                    }

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

        public void cleanData() {
            for (ImageInfo info : list) {
                if (info.isNormal) {

                }
            }
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
            Bitmap bitmap = imageHelper.pathWithScaledBitmap(PictureActivity.this, info.path, CommonUtil.dp2px(112), CommonUtil.dp2px(112));
            holder.picture_icon.setImageBitmap(bitmap);
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
    }
}
