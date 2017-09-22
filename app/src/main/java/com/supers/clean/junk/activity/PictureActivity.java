package com.supers.clean.junk.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.clean.similarimage.ImageHelper;
import com.android.clean.similarimage.ImageInfo;
import com.android.clean.util.PreData;
import com.android.clean.util.Util;
import com.android.client.AndroidSdk;
import com.fraumobi.call.Utils.BadgerCount;
import com.supers.clean.junk.R;
import com.supers.clean.junk.adapter.RecycleViewAdapter;
import com.android.clean.similarimage.RecyclerDbHelper;
import com.supers.clean.junk.util.AdUtil;
import com.android.clean.util.Constant;

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
    RecyclerView picture_recycle;
    ViewPager picture_pager;
    FrameLayout pager_fl;
    ImageView pic_pager_left;
    TextView pic_pager_title;
    LinearLayout pic_pager_delete;
    LinearLayout pic_pager_check;
    ImageView pic_pager_check_iv;

    private RecycleViewAdapter adapter;
    private PagerAdapter pagerAdapter;
    private ImageHelper imageHelper;
    private ArrayList<View> pagerView;
    public ArrayList<ArrayList<ImageInfo>> allList;
    AlertDialog dialog;
    public long allSize = 0;

    public boolean mIsQuerying = false;


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
        picture_recycle = (RecyclerView) findViewById(R.id.picture_recycle);
        picture_pager = (ViewPager) findViewById(R.id.picture_pager);
        pager_fl = (FrameLayout) findViewById(R.id.pager_fl);
        pic_pager_left = (ImageView) findViewById(R.id.pic_pager_left);
        pic_pager_title = (TextView) findViewById(R.id.pic_pager_title);
        pic_pager_delete = (LinearLayout) findViewById(R.id.pic_pager_delete);
        pic_pager_check = (LinearLayout) findViewById(R.id.pic_pager_check);
        pic_pager_check_iv = (ImageView) findViewById(R.id.pic_pager_check_iv);

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PICTHRE_PATH:
                    picture_path.setText((String) msg.obj);
                    break;
                case PICTHRE_SUCC:
                    picture_path.setVisibility(View.GONE);
                    if (adapter == null || adapter.getItemCount() == 0) {
                        picture_progressbar.setVisibility(View.GONE);
                        picture_other.setVisibility(View.VISIBLE);
                        picture_scan.setText("");
                    } else {
                        picture_scan.setText(R.string.picture_jianyi);
                        Animation animation1 = AnimationUtils.loadAnimation(PictureActivity.this, R.anim.translate_notifi);
                        picture_button.startAnimation(animation1);
                        picture_button.setVisibility(View.VISIBLE);
                        picture_button.setText(getString(R.string.picture_14) + "( " + msg.arg1 + " )");
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
        PreData.putDB(this, Constant.HONG_PHOTO, false);
        BadgerCount.setCount(this);
        AndroidSdk.loadFullAd(AdUtil.DEFAULT,null);
        title_name.setText(R.string.side_picture);
        title_right.setVisibility(View.VISIBLE);
        title_right.setImageResource(R.mipmap.picture_right);
        imageHelper = new ImageHelper();
        pagerView = new ArrayList<>();
        picture_recycle.setLayoutManager(new LinearLayoutManager(this));

        picture_recycle.setItemAnimator(new DefaultItemAnimator());
        new Thread(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        }).start();
        clickListen();
        loadAd();
    }

    private void loadAd() {
        if (PreData.getDB(this, Constant.PICTURE, 0) == 1) {
            AndroidSdk.showFullAd(AdUtil.DEFAULT);
        }
    }

    private void clickListen() {
        title_left.setOnClickListener(clickListener);
        title_right.setOnClickListener(clickListener);
        picture_button.setOnClickListener(clickListener);
        picture_other.setOnClickListener(clickListener);
        pic_pager_left.setOnClickListener(clickListener);
    }

    public void bigPicture(final ArrayList<ImageInfo> list) {
        pic_pager_title.setText("1/" + list.size());
        if (list.get(0).isNormal) {
            pic_pager_check_iv.setImageResource(R.mipmap.picture_normal);
        } else {
            pic_pager_check_iv.setImageResource(R.mipmap.picture_passed);
        }
        pic_pager_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = picture_pager.getCurrentItem();
                list.get(position).isNormal = !list.get(position).isNormal;
                if (list.get(position).isNormal) {
                    pic_pager_check_iv.setImageResource(R.mipmap.picture_normal);
                } else {
                    pic_pager_check_iv.setImageResource(R.mipmap.picture_passed);
                }
                updateUi();
            }
        });

        pic_pager_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = picture_pager.getCurrentItem();
                RecyclerDbHelper.getInstance(PictureActivity.this).putImageToRecycler(list.get(position));
                View view = pagerView.get(position);
                pagerView.remove(position);
                picture_pager.removeView(view);
                pagerAdapter.notifyDataSetChanged();
                list.remove(position);
                if (pagerView.size() <= 1) {
                    pager_fl.setVisibility(View.GONE);
                    adapter.deleteItem();
                }
                pic_pager_title.setText((picture_pager.getCurrentItem() + 1) + "/" + list.size());
                updateUi();
            }
        });
        pagerView.clear();
        for (int i = 0; i < list.size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.layout_picture_pager, null);
            final ImageView icon = (ImageView) view.findViewById(R.id.pic_pager_icon);
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
//                    final Bitmap bitma = imageHelper.getImageThumbnail(PictureActivity.this, list.get(finalI).originId, MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
                    final Bitmap bitma = imageHelper.pathWithScaledBitmap(PictureActivity.this, list.get(finalI).path);
                    //final Bitmap bitma = imageHelper.getImageThumbnail(PictureActivity.this, list.get(finalI).originId, MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            icon.setImageBitmap(bitma);
                        }
                    });
                }
            }).start();


            pagerView.add(view);
        }
        picture_pager.setAdapter(pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return pagerView.size();
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(pagerView.get(position), 0);
                return pagerView.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                View view = (View) object;
                container.removeView(view);
                view = null;
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }
        });
        picture_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                pic_pager_title.setText((position + 1) + "/" + list.size());
                Log.e("pager", position + "=2");
                if (position > list.size() - 1) {
                    return;
                }
                if (list.get(position).isNormal) {
                    pic_pager_check_iv.setImageResource(R.mipmap.picture_normal);
                } else {
                    pic_pager_check_iv.setImageResource(R.mipmap.picture_passed);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void initData() {

        imageHelper.querySimilarImage(this, new ImageHelper.OnQuerySimilarPicCallBack() {
            @Override
            public void startQuery() {
                mIsQuerying = true;
            }

            @Override
            public void endQuery(ArrayList<ImageInfo> localImageList, ArrayList<ArrayList<ImageInfo>> localImages, long totalSize, long totalCount) {
                long count = 0;
                for (int i = 0; i < localImages.size(); i++) {
                    ArrayList<ImageInfo> arrayList = localImages.get(i);
                    for (ImageInfo imageInfo : arrayList) {
                        if (!imageInfo.isNormal) {
                            allSize += imageInfo.fileSize;
                            count++;
                        }
                    }
                }
                mIsQuerying = false;
                Message msg = mHandler.obtainMessage();//同 new Message();
                msg.what = PICTHRE_SUCC;
                msg.arg1 = (int) count;
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
            public void haveQuerySimilarPic(int i, ArrayList<ImageInfo> similarImage, final ArrayList<ArrayList<ImageInfo>> totalSimilarImage, final long totalSize) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        long size = 0;
                        for (int i = 0; i < totalSimilarImage.size(); i++) {
                            ArrayList<ImageInfo> arrayList = totalSimilarImage.get(i);
                            for (ImageInfo imageInfo : arrayList) {
                                if (!imageInfo.isNormal) {
                                    size += imageInfo.fileSize;
                                }
                            }
                        }
                        picture_progressbar.setVisibility(View.GONE);
                        picture_size.setText(Util.convertStorage(size, false));
                        picture_danwei.setText(Util.convertStorageDanwei(size));
//                        adapter.addData(list_item, 0);
                        allList = totalSimilarImage;
                        if (adapter == null) {
                            adapter = new RecycleViewAdapter(PictureActivity.this, totalSimilarImage, imageHelper, mHandler);
                            picture_recycle.setAdapter(adapter);
                        } else {
                            adapter.upData(totalSimilarImage);
                            adapter.notifyDataSetChanged();
                        }
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
                    AdUtil.track("相似图片页面", "点击进入回收站", "", 1);
                    jumpToActivity(PictureHuiActivity.class, 1);
                    break;
                case R.id.picture_button:
                    AdUtil.track("相似图片页面", "点击清理按钮,弹出确认弹窗", "", 1);
                    ArrayList<Bitmap> bitmaps = adapter.checkDate();
                    int num = bitmaps.size();
                    if (num == 0) {
                        showToast(getString(R.string.delete));
                        return;
                    }
                    deleteDialog(bitmaps);
                    break;
                case R.id.picture_other:
                    AdUtil.track("相似图片页面", "点击检测其他垃圾", "", 1);
                    jumpTo(JunkActivity.class);
                    onBackPressed();
                    break;
                case R.id.pic_pager_left:
                    pager_fl.setVisibility(View.GONE);
                    picture_pager.setAdapter(null);
                    adapter.notifyDataSetChanged();
                    break;
            }

        }
    };

    private void deleteDialog(final ArrayList<Bitmap> checkDate) {
        final int deleteSize = checkDate.size();
        View view = View.inflate(this, R.layout.dialog_picture, null);
        TextView message = (TextView) view.findViewById(R.id.message);
        final TextView ok = (TextView) view.findViewById(R.id.ok);
        final TextView cancle = (TextView) view.findViewById(R.id.cancle);
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
                AdUtil.track("相似图片页面", "点击确认弹窗关闭按钮", "", 1);
                dialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdUtil.track("相似图片页面", "点击确认弹窗ok按钮", "", 1);
                ok.setOnClickListener(null);
                cancle.setOnClickListener(null);
                image_list.setVisibility(View.GONE);
                delete_progress.setVisibility(View.VISIBLE);
                adapter.delete(new RecycleViewAdapter.RecycleViewCallBack() {
                    int num = 0;

                    @Override
                    public void deleteItemCallback(long fileSize) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                delete_progress.setProgress(++num * 100 / deleteSize);
                                count.setText(deleteSize + "/" + num);
                            }
                        });
                    }

                    @Override
                    public void deleteSuccessCallback(final ArrayList<ArrayList<ImageInfo>> list) {
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
                                updateUi();
                                adapter.notifyDataSetChanged();
                                if (adapter.getItemCount() == 0) {
                                    picture_progressbar.setVisibility(View.GONE);
                                    picture_other.setVisibility(View.VISIBLE);
                                }
                                Bundle bundle = new Bundle();
                                bundle.putLong("sizeP", oldSize - allSize);
                                bundle.putInt("sizePic", deleteSize);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 5) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (pager_fl.getVisibility() == (View.VISIBLE)) {
            pager_fl.setVisibility(View.GONE);
            picture_pager.setAdapter(null);
            adapter.notifyDataSetChanged();
            return;
        }
        finish();
    }

    public void showBigPic(ArrayList<ImageInfo> list) {
        pager_fl.setVisibility(View.VISIBLE);
        bigPicture(list);
    }


    public void updateUi() {
        long size = 0;
        long count = 0;
        for (int i = 0; i < allList.size(); i++) {
            ArrayList<ImageInfo> arrayList = allList.get(i);
            for (ImageInfo imageInfo : arrayList) {
                if (!imageInfo.isNormal) {
                    size += imageInfo.fileSize;
                    count++;
                }
            }
        }
        picture_button.setText(getString(R.string.picture_14) + "( " + count + " )");
        picture_size.setText(Util.convertStorage(size, false));
        picture_danwei.setText(Util.convertStorageDanwei(size));

    }
}
