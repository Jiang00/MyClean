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
import android.support.v7.widget.LinearLayoutManager;
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
import com.supers.clean.junk.adapter.RecycleViewAdapter;
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
    TextView picture_path;
    TextView picture_danwei;
    TextView picture_size;
    Button picture_button;
    TextView picture_scan;
    ProgressBar picture_progressbar;
    LineProgressView picture_line;
    RecyclerView picture_recycle;

    private RecycleViewAdapter adapter;
    private ImageHelper imageHelper;


    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        title_right = (ImageView) findViewById(R.id.title_right);
        picture_path = (TextView) findViewById(R.id.picture_path);
        picture_size = (TextView) findViewById(R.id.picture_size);
        picture_danwei = (TextView) findViewById(R.id.picture_danwei);
        picture_button = (Button) findViewById(R.id.picture_button);
        picture_scan = (TextView) findViewById(R.id.picture_scan);
        picture_progressbar = (ProgressBar) findViewById(R.id.picture_progressbar);
        picture_line = (LineProgressView) findViewById(R.id.picture_line);
        picture_recycle = (RecyclerView) findViewById(R.id.picture_recycle);

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
        picture_recycle.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecycleViewAdapter(this);
        picture_recycle.setAdapter(adapter);
        picture_recycle.setItemAnimator(new DefaultItemAnimator());
        new Thread(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        }).start();
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
                        adapter.addData(list_item, 0);
                        adapter.notifyDataSetChanged();
//                        addItemView(list_item, dp4, dp6, dp8);
                    }
                });
            }

        });

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

                            Log.e("picture", System.currentTimeMillis() + "");
                            adapter.upData();
//                            for (int i = 0; i < size; i++) {
//                                RecyclerView recye = (RecyclerView) picture_item.getChildAt(i);
//                                final HomeAdapter adapter = (HomeAdapter) recye.getAdapter();
//                                adapter.cleanData();
//                                try {
//                                    Thread.sleep(10);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                            }
                            Log.e("picture", System.currentTimeMillis() + "");
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }).start();

                    break;
            }

        }
    };

}
