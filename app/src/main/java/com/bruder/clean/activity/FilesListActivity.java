package com.bruder.clean.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.bruder.clean.entity.JunkInfo;
import com.bruder.clean.junk.R;
import com.bruder.clean.myadapter.FileAdapter;
import com.bruder.clean.util.Constant;
import com.bruder.clean.util.UtilAd;
import com.cleaner.filemanager.FileCategoryHelper;
import com.cleaner.filemanager.SortHelperFile;
import com.cleaner.filemanager.Util;
import com.cleaner.filemanager.UtilsFile;
import com.cleaner.util.DataPre;

import java.util.ArrayList;

/**
 */

public class FilesListActivity extends BaseActivity {

    public static final String TAG = "FilesListActivity";
    FrameLayout title_left;
    TextView title_name;
    ListView file_list;
    Button file_button_clean;
    RelativeLayout file_clean_rl;
    ProgressBar file_progressbar;
    LinearLayout ll_ad;
    ImageView null_icon;
    LinearLayout null_ll;
    FrameLayout file_fl;

    private FileCategoryHelper fileHelper;
    private FileAdapter adapter;
    private ArrayList<JunkInfo> fileList;
    private Handler mHandler;
    private String name;
    private int nameId;
    private AlertDialog dialog;
    private FileCategoryHelper.FileCategory fc;
    private String Tag_file_1 = "bruder_file_1";//
    private View nativeView;

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        file_list = (ListView) findViewById(R.id.file_list);
        file_button_clean = (Button) findViewById(R.id.junk_button_clean1);
        file_clean_rl = (RelativeLayout) findViewById(R.id.file_clean_rl);
        file_progressbar = (ProgressBar) findViewById(R.id.file_progressbar);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
        null_icon = (ImageView) findViewById(R.id.null_icon);
        null_ll = (LinearLayout) findViewById(R.id.null_ll);
        file_fl = (FrameLayout) findViewById(R.id.file_fl);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSdk.loadFullAd(UtilAd.DEFAULT_FULL,null);
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
        fileHelper = new FileCategoryHelper(this);
        fileList = new ArrayList<>();
        initData();
        adapter = new FileAdapter(this, name);
        file_list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void loadAd() {
        if (DataPre.getDB(this, Constant.FULL_FILE_1, 0) == 1) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AndroidSdk.showFullAd(UtilAd.DEFAULT_FULL);
                }
            }, 1000);
        } else {
            addAd();
        }
    }

    private void addAd() {
        nativeView = UtilAd.getNativeAdView(Tag_file_1, R.layout.native_ad_3);
        if (ll_ad != null && nativeView != null) {
            ViewGroup.LayoutParams layout_ad = ll_ad.getLayoutParams();
            ll_ad.setLayoutParams(layout_ad);
            ll_ad.addView(nativeView);
        }
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = null;
                if (TextUtils.equals("apk", name)) {
                    fc = FileCategoryHelper.FileCategory.Apk;
                } else if (TextUtils.equals("zip", name)) {
                    fc = FileCategoryHelper.FileCategory.Zip;
                } else if (TextUtils.equals("music", name)) {
                    fc = FileCategoryHelper.FileCategory.Music;
                } else if (TextUtils.equals("video", name)) {
                    fc = FileCategoryHelper.FileCategory.Video;
                } else if (TextUtils.equals("picture", name)) {
                    fc = FileCategoryHelper.FileCategory.Picture;
                } else if (TextUtils.equals("other", name)) {
                    fc = FileCategoryHelper.FileCategory.Other;
                }
                cursor = fileHelper.query(fc, SortHelperFile.SortMethod.size);
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        if (onDestroyed) {
                            return;
                        }
                        long _id = cursor.getLong(FileCategoryHelper.COLUMN_ID);
                        long size = Long.parseLong(cursor.getString(FileCategoryHelper.COLUMN_SIZE));
                        fileList.add(new JunkInfo(_id, null, Util.getNameFromFilepath(cursor.getString(FileCategoryHelper.COLUMN_PATH)),
                                cursor.getString(FileCategoryHelper.COLUMN_PATH), size, false));
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

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_left:
                    onBackPressed();
                    break;
                case R.id.junk_button_clean1:
                    ArrayList<JunkInfo> deleteList = new ArrayList<>();
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

    private void showDia(final ArrayList<JunkInfo> deleteList) {
        if (deleteList.size() == 0) {
            showToast(getString(R.string.delete));
            return;
        }
        View view = View.inflate(this, R.layout.dialog_file, null);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView message = (TextView) view.findViewById(R.id.message);
        TextView ok = (TextView) view.findViewById(R.id.ok);
        TextView cancle = (TextView) view.findViewById(R.id.cancle);

        if (deleteList.size() == 1) {
            title.setText(deleteList.get(0).name);
        } else {
            title.setText(R.string.delete_queren);
        }
        message.setText(getString(R.string.delete_2, deleteList.size()));
        dialog = new AlertDialog.Builder(FilesListActivity.this).create();
        dialog.setView(view);
        dialog.show();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                long size = 0;
                for (JunkInfo info : deleteList) {
                    size += info.size;
                    UtilsFile.deleteCo(FilesListActivity.this, fc, info._id);
                }
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        for (JunkInfo info : deleteList) {
                            boolean deleteSuce = UtilsFile.deleteFile(FilesListActivity.this, info.path);
                            if (!deleteSuce) {
                                android.util.Log.e(TAG, "delete fail --" + info.path);
                            }
                        }
                    }
                }.start();

                fileList.removeAll(deleteList);
                adapter.notifyDataSetChanged();
                Intent intent = new Intent(FilesListActivity.this, SuccessingActivity.class);
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

    @Override
    public void onBackPressed() {
        setResult(1);
        finish();
    }
}