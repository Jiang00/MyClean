package com.easy.junk.easyactivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easy.clean.filemanager.PhoneFileCategoryHelper;
import com.easy.clean.filemanager.FileSortHelper;
import com.easy.clean.filemanager.UtilsFile;
import com.easy.clean.easyutils.PreData;
import com.android.client.AndroidSdk;
import com.easy.junk.R;
import com.easy.junk.easycustomadapter.PhoneFileAdapter;
import com.easy.junk.easymodel.JunkInfo;
import com.easy.junk.easytools.SetAdUtil;
import com.easy.junk.easytools.MyConstant;
import com.easy.clean.filemanager.Util;

import java.util.ArrayList;

/**
 */

public class EasyFileActivity extends BaseActivity {

    private String Tag_file_1 = "easy_file_1";
    public static final String TAG = "EasyFileActivity";
    ProgressBar file_progressbar;
    LinearLayout ll_ad;
    TextView file_button_clean;
    ImageView null_icon;
    FrameLayout title_left;
    TextView title_name;
    FrameLayout file_fl;
    private int nameId;
    ListView file_list;
    private ArrayList<JunkInfo> fileList;
    private Handler mHandler;
    private String name;
    RelativeLayout file_clean_rl;
    private PhoneFileCategoryHelper fileHelper;
    private PhoneFileAdapter adapter;
    private View nativeView;
    LinearLayout null_ll;
    private AlertDialog dialog;
    private PhoneFileCategoryHelper.FileCategory fc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSdk.loadFullAd(AndroidSdk.FULL_TAG_PAUSE);
        name = getIntent().getStringExtra("name");
        nameId = getIntent().getIntExtra("nameId", 0);
        if (name == null) {
            name = "apk";
        }
        setContentView(R.layout.layout_file_second);
        title_name.setText(nameId);
        mHandler = new Handler();
        loadAd();
        setListenet();
        //沒有的时候
        if (0 == getIntent().getIntExtra("count", 1)) {
            file_fl.setVisibility(View.GONE);
            null_ll.setVisibility(View.VISIBLE);
            if (TextUtils.equals("apk", name)) {
                null_icon.setImageResource(R.mipmap.null_apk);
            } else if (TextUtils.equals("zip", name)) {
                null_icon.setImageResource(R.mipmap.null_zip);
            } else if (TextUtils.equals("music", name)) {
                null_icon.setImageResource(R.mipmap.null_music);
            } else if (TextUtils.equals("video", name)) {
                null_icon.setImageResource(R.mipmap.null_video);
            } else if (TextUtils.equals("other", name)) {
                null_icon.setImageResource(R.mipmap.null_other);
            }
            return;
        }
        fileHelper = new PhoneFileCategoryHelper(this);
        fileList = new ArrayList<>();
        initData();
        adapter = new PhoneFileAdapter(this, name);
        file_list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_left:
                    onBackPressed();
                    break;
                case R.id.file_button_clean:
                    ArrayList<JunkInfo> deleteList = new ArrayList<>();
                    if (fileList == null || fileList.size() == 0) {
                        break;
                    }
                    for (JunkInfo info : fileList) {
                        if (info.isChecked) {
                            deleteList.add(info);
                        }
                    }
                    showDia(deleteList);
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        setResult(1);
        finish();
    }

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        file_list = (ListView) findViewById(R.id.file_list);
        file_button_clean = (TextView) findViewById(R.id.file_button_clean);
        file_clean_rl = (RelativeLayout) findViewById(R.id.file_clean_rl);
        file_progressbar = (ProgressBar) findViewById(R.id.file_progressbar);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
        null_icon = (ImageView) findViewById(R.id.null_icon);
        null_ll = (LinearLayout) findViewById(R.id.null_ll);
        file_fl = (FrameLayout) findViewById(R.id.file_fl);

    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = null;
                if (TextUtils.equals("apk", name)) {
                    fc = PhoneFileCategoryHelper.FileCategory.Apk;
                } else if (TextUtils.equals("zip", name)) {
                    fc = PhoneFileCategoryHelper.FileCategory.Zip;
                } else if (TextUtils.equals("music", name)) {
                    fc = PhoneFileCategoryHelper.FileCategory.Music;
                } else if (TextUtils.equals("video", name)) {
                    fc = PhoneFileCategoryHelper.FileCategory.Video;
                } else if (TextUtils.equals("other", name)) {
                    fc = PhoneFileCategoryHelper.FileCategory.Other;
                }

                cursor = fileHelper.query(fc, FileSortHelper.SortMethod.size);
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        if (onDestroyed) {
                            return;
                        }
                        long _id = cursor.getLong(PhoneFileCategoryHelper.COLUMN_ID);
                        long size = Long.parseLong(cursor.getString(PhoneFileCategoryHelper.COLUMN_SIZE));
                        fileList.add(new JunkInfo(_id, null, Util.getNameFromFilepath(cursor.getString(PhoneFileCategoryHelper.COLUMN_PATH)),
                                cursor.getString(PhoneFileCategoryHelper.COLUMN_PATH), size, false));
                    } while (cursor.moveToNext());
                    cursor.close();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            file_progressbar.setVisibility(View.GONE);
                            adapter.upList(fileList);
                            adapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            file_progressbar.setVisibility(View.GONE);

                        }
                    });
                }
            }
        }).start();
    }

    private void setListenet() {
        title_left.setOnClickListener(clickListener);
        file_button_clean.setOnClickListener(clickListener);
    }


    private void addAd() {
        nativeView = SetAdUtil.getNativeAdView(Tag_file_1, R.layout.native_ad_3);
        if (ll_ad != null && nativeView != null) {
            ViewGroup.LayoutParams layout_ad = ll_ad.getLayoutParams();
            ll_ad.setLayoutParams(layout_ad);
            ll_ad.addView(nativeView);
        }
    }

    private void loadAd() {
        if (PreData.getDB(this, MyConstant.FULL_FILE_1, 0) == 1) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
                }
            }, 1000);
        } else {
            addAd();
        }
    }

    private void showDia(final ArrayList<JunkInfo> deleteList) {
        if (deleteList.size() == 0) {
            showToast(getString(R.string.delete));
            return;
        }
        View view = View.inflate(this, R.layout.dialog_file, null);
        TextView message = (TextView) view.findViewById(R.id.message);
        TextView ok = (TextView) view.findViewById(R.id.ok);
        TextView cancle = (TextView) view.findViewById(R.id.cancle);

        message.setText(getString(R.string.delete_2, deleteList.size()));
        dialog = new AlertDialog.Builder(EasyFileActivity.this).create();
        dialog.setView(view);
        dialog.show();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                long size = 0;
                for (JunkInfo info : deleteList) {
                    size += info.size;
                    UtilsFile.deleteCo(EasyFileActivity.this, fc, info._id);
                }
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        for (JunkInfo info : deleteList) {
                            boolean deleteSuce = UtilsFile.deleteFile(EasyFileActivity.this, info.path);
                            if (!deleteSuce) {
                                android.util.Log.e(TAG, "delete fail --" + info.path);
                            }
                        }
                    }
                }.start();

                fileList.removeAll(deleteList);
                adapter.notifyDataSetChanged();
                Intent intent = new Intent(EasyFileActivity.this, EasySucceedActivity.class);
                intent.putExtra("sizeF", size);
                intent.putExtra("from", "file");
                startActivity(intent);
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
