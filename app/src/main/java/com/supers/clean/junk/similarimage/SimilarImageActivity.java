package com.supers.clean.junk.similarimage;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.supers.clean.junk.R;
import com.tonicartos.superslim.LayoutManager;

import java.util.ArrayList;

public class SimilarImageActivity extends AppCompatActivity {

    private static final int PROGRESS_END = 0;
    private static final int PROGRESS_START = 1;
    private static final int PROGRESS_PROGRESS = 2;
    RecyclerView mRecyclerView;
    LinearLayout mParentPanel;
    ProgressBar mPb;
    TextView mImageProgress;

    private ViewHolder mViews;

    private SimilarPictureAdapter mAdapter;

    ImageHelper imageHelper;

    ArrayList<ArrayList<ImageInfo>> similarItems;


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PROGRESS_END:
                    mPb.setVisibility(View.GONE);
                    mImageProgress.setVisibility(View.GONE);
                    mAdapter = new SimilarPictureAdapter(SimilarImageActivity.this.getApplicationContext(), 18, similarItems);
                    mAdapter.setMarginsFixed(true);
                    mAdapter.setHeaderDisplay(18);
                    mViews.setAdapter(mAdapter);
                    break;
                case PROGRESS_START:
                    mPb.setMax(msg.arg1);
                    break;
                case PROGRESS_PROGRESS:
                    mPb.setProgress(msg.arg1);
                    mImageProgress.setText("解析图片" + msg.arg1 + "/" + mPb.getMax());
                    break;
                default:
                    break;
            }

            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mParentPanel = (LinearLayout) this.findViewById(R.id.parentPanel);
        mRecyclerView = (RecyclerView) this.findViewById(R.id.similar_imagelist);
        mImageProgress = (TextView) this.findViewById(R.id.image_progress);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mPb = (ProgressBar) this.findViewById(R.id.pb);


        mViews = new ViewHolder(mRecyclerView);
        mViews.initViews(new LayoutManager(this));

        new Thread(new Runnable() {
            @Override
            public void run() {
                long startTime = System.currentTimeMillis();
                query();
                long endTime = System.currentTimeMillis();
                Log.e("rqy", "time=" + (endTime - startTime));
            }
        }).start();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            mAdapter.setmTextVisible(!mAdapter.ismTextVisible());
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    private void query() {
        imageHelper = new ImageHelper();
        imageHelper.querySimilarImage(this, new ImageHelper.OnQuerySimilarPicCallBack() {
            @Override
            public void startAsync(ArrayList<ImageInfo> localImageList) {
                Message msg = mHandler.obtainMessage();//同 new Message();
                msg.what = PROGRESS_START;
                msg.arg1 = localImageList.size();
                mHandler.sendMessage(msg);
            }

            @Override
            public void endAsync(ArrayList<ImageInfo> localImageList, ArrayList<ArrayList<ImageInfo>> localImages) {
                similarItems = localImages;
                Message msg = mHandler.obtainMessage();//同 new Message();
                msg.what = PROGRESS_END;
                mHandler.sendMessage(msg);
            }

            @Override
            public void startAsyncPic(int i, ArrayList<ImageInfo> localImageList) {
                long totalSize = localImageList.size();
                int progress = (int) ((double) i / totalSize) * 100;
                mPb.setProgress(progress);
                Message msg2 = mHandler.obtainMessage();//同 new Message();
                msg2.arg1 = i;
                msg2.what = PROGRESS_PROGRESS;
                mHandler.sendMessage(msg2);
            }

            @Override
            public void endAsyncPic(int i, ArrayList<ImageInfo> localImageList) {

            }

            @Override
            public void haveQuerySimilarPic(int i, ArrayList<ImageInfo> localImageList, ArrayList<ArrayList<ImageInfo>> localImages,
                                            int bestImageIndex, long totalSize) {
                Log.e("rqy", "haveQuerySimilarPic--i=" + i + "--bestImageIndex=" + bestImageIndex + "--totalSize=" + totalSize);
            }
        });
        if (similarItems.isEmpty()) {
            return;
        }

    }

      /*  Collections.sort(imageList, new Comparator<LocalImage>() {

            @Override
            public int compare(LocalImage localImage1, LocalImage localImage2) {
                return localImage1.getSourceHashCode().compareTo(localImage2.getSourceHashCode());
            }
        });*/


      /*  Collections.sort(imageList, new Comparator<LocalImage>() {

            @Override
            public int compare(LocalImage localImage1, LocalImage localImage2) {
                return localImage1.getAvgPixel() - (localImage2.getAvgPixel());
           }
        });*/

        /*int index1 = 0;
        int index2 = 1;
        ArrayList<LocalImage> similarItem = new ArrayList<>();
        while (index2 < localImages.size()) {
            LocalImage first = localImages.get(index1);
            LocalImage second = localImages.get(index2);
            Log.e("rqy", "index1=" + index1 + "--index2=" + index2);
            if (ImageHelper.similarCondition(first, second)) {
                if (similarItem.indexOf(first) < 0) {
                    similarItem.add(first);
                }
                if (similarItem.indexOf(second) < 0) {
                    similarItem.add(second);
                }
            } else {
                if (similarItem.size() > 1) {
                    similarItems.add(similarItem);
                }
                similarItem = new ArrayList<>();
            }
            index1++;
            index2++;
        }*/


    private static class ViewHolder {

        private final RecyclerView mRecyclerHolderView;


        public ViewHolder(RecyclerView view) {
            mRecyclerHolderView = view;
        }

        public void initViews(LayoutManager lm) {
            mRecyclerHolderView.setLayoutManager(lm);
        }

        public void scrollToPosition(int position) {
            mRecyclerHolderView.scrollToPosition(position);
        }

        public void setAdapter(RecyclerView.Adapter<?> adapter) {
            mRecyclerHolderView.setAdapter(adapter);
        }

        public void smoothScrollToPosition(int position) {
            mRecyclerHolderView.smoothScrollToPosition(position);
        }

    }

}
