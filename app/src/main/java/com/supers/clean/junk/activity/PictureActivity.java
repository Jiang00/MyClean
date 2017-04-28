package com.supers.clean.junk.activity;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.supers.clean.junk.R;
import com.supers.clean.junk.adapter.RecycleViewAdapter;
import com.supers.clean.junk.customeview.LineProgressView;
import com.supers.clean.junk.similarimage.ImageHelper;
import com.supers.clean.junk.similarimage.ImageInfo;
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
    TextView picture_path;
    TextView picture_danwei;
    TextView picture_size;
    Button picture_button;
    TextView picture_scan;
    ProgressBar picture_progressbar;
    LinearLayout picture_other;
    LineProgressView picture_line;
    RecyclerView picture_recycle;

    private RecycleViewAdapter adapter;
    private ImageHelper imageHelper;
    AlertDialog dialog;
    private long allSize;
    private long delete;


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
        picture_other = (LinearLayout) findViewById(R.id.picture_other);
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
                    picture_line.setVisibility(View.GONE);
                    picture_path.setVisibility(View.GONE);
                    if (adapter.getItemCount() == 0) {
                        picture_progressbar.setVisibility(View.GONE);
                        picture_other.setVisibility(View.VISIBLE);
                    } else {
                        picture_scan.setText(R.string.picture_jianyi);
                        picture_button.setVisibility(View.VISIBLE);
                    }
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
        title_right.setVisibility(View.VISIBLE);
        title_right.setImageResource(R.mipmap.picture_right);
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
        picture_other.setOnClickListener(clickListener);
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
                allSize = totalSize;
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
                    CommonUtil.track("相似图片页面", "点击进入回收站", "", 1);
                    jumpToActivity(PictureHuiActivity.class, 1);
                    break;
                case R.id.picture_button:
                    CommonUtil.track("相似图片页面", "点击清理按钮,弹出确认弹窗", "", 1);
                    jumpToActivity(PictureHuiActivity.class, 1);
                    ArrayList<Bitmap> bitmaps = adapter.checkDate();
                    int num = bitmaps.size();
                    if (num == 0) {
                        showToast(getString(R.string.delete));
                        return;
                    }
                    deleteDialog(bitmaps);
                    break;
                case R.id.picture_other:
                    CommonUtil.track("相似图片页面", "点击检测其他垃圾", "", 1);
                    jumpTo(JunkActivity.class);
                    onBackPressed();
                    break;
            }

        }
    };

    private void deleteDialog(final ArrayList<Bitmap> checkDate) {
        final int deleteSize = checkDate.size();
        View view = View.inflate(this, R.layout.dialog_picture, null);
        TextView message = (TextView) view.findViewById(R.id.message);
        final TextView ok = (TextView) view.findViewById(R.id.ok);
        TextView cancle = (TextView) view.findViewById(R.id.cancle);
        final TextView count = (TextView) view.findViewById(R.id.count);
        final LinearLayout image_list = (LinearLayout) view.findViewById(R.id.image_list);
        final ProgressBar delete_progress = (ProgressBar) view.findViewById(R.id.delete_progress);
        ImageView image_1 = (ImageView) view.findViewById(R.id.image_1);
        message.setText(getString(R.string.picture_1, checkDate.size()));
        image_1.setImageBitmap(checkDate.get(0));
        if (deleteSize >= 2 && null != checkDate.get(1)) {
            ImageView image_2 = (ImageView) view.findViewById(R.id.image_2);
            image_2.setImageBitmap(checkDate.get(1));
        }
        if (deleteSize >= 3 && null != checkDate.get(2)) {
            ImageView image_3 = (ImageView) view.findViewById(R.id.image_3);
            image_3.setImageBitmap(checkDate.get(2));
        }
        if (deleteSize >= 4 && null != checkDate.get(3)) {
            ImageView image_4 = (ImageView) view.findViewById(R.id.image_4);
            image_4.setImageBitmap(checkDate.get(3));
        }
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtil.track("相似图片页面", "点击确认弹窗关闭按钮", "", 1);
                dialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtil.track("相似图片页面", "点击确认弹窗ok按钮", "", 1);
                ok.setOnClickListener(null);
                image_list.setVisibility(View.GONE);
                delete_progress.setVisibility(View.VISIBLE);
                adapter.upData(new RecycleViewAdapter.RecycleViewCallBack() {
                    int num = 0;

                    @Override
                    public void deleteItemCallback(long fileSize) {
                        delete += fileSize;
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                delete_progress.setProgress(++num * 100 / deleteSize);
                                count.setText(deleteSize + "/" + num);
                            }
                        });
                    }

                    @Override
                    public void deleteSuccCallback(final ArrayList<ArrayList<ImageInfo>> list) {
                        dialog.dismiss();
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                long oldSize = allSize;
                                allSize = 0;
                                for (ArrayList<ImageInfo> info : list) {
                                    for (ImageInfo i : info) {
                                        allSize += i.fileSize;
                                    }
                                }
                                picture_size.setText(CommonUtil.convertStorage(allSize));
                                picture_danwei.setText(CommonUtil.convertStorageDanwei(allSize));
                                adapter.notifyDataSetChanged();
                                if (adapter.getItemCount() == 0) {
                                    picture_progressbar.setVisibility(View.GONE);
                                    picture_other.setVisibility(View.VISIBLE);
                                }
                                Bundle bundle = new Bundle();
                                bundle.putLong("size", oldSize - allSize);
                                bundle.putString("from", "picture");
                                jumpToActivity(SuccessActivity.class, bundle, 1);
                            }
                        });
                    }
                });
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

}
