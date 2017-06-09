package com.fast.clean.junk.ui;

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

import com.android.clean.filemanager.FileCategoryHelper;
import com.android.clean.filemanager.FileSortHelper;
import com.android.clean.filemanager.FileUtils;
import com.android.clean.util.PreData;
import com.android.client.AndroidSdk;
import com.fast.clean.junk.R;
import com.fast.clean.junk.adapter.FileAdapter;
import com.fast.clean.junk.entity.JunkInfo;
import com.fast.clean.junk.util.AdUtil;
import com.fast.clean.junk.util.Constant;

import java.util.ArrayList;

/**
 */

public class FileListActivity extends BaseActivity {

    public static final String TAG = "FileListActivity";
    ProgressBar file_progressbar;
    LinearLayout ll_ad;
    ImageView null_icon;
    FrameLayout title_left;
    TextView title_name;
    ListView file_list;
    Button file_button_clean;
    RelativeLayout file_clean_rl;
    LinearLayout null_ll;
    FrameLayout file_fl;

    private int nameId;
    private AlertDialog dialog;
    private FileCategoryHelper.FileCategory fc;
    private FileCategoryHelper fileHelper;
    private FileAdapter adapter;
    private ArrayList<JunkInfo> fileList;
    private Handler mHandler;
    private String name;
    private String Tag_file_1 = "acht_file_1";
    private View nativeView;

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        file_list = (ListView) findViewById(R.id.file_list);
        file_button_clean = (Button) findViewById(R.id.file_button_clean);
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

    @Override
    public void onBackPressed() {
        setResult(1);
        finish();
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

    private void loadAd() {
        if (PreData.getDB(this, Constant.FULL_FILE_1, 0) == 1) {
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
                } else if (TextUtils.equals("other", name)) {
                    fc = FileCategoryHelper.FileCategory.Other;
                }
                cursor = fileHelper.query(fc, FileSortHelper.SortMethod.size);
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        if (onDestroyed) {
                            return;
                        }
                        long _id = cursor.getLong(FileCategoryHelper.COLUMN_ID);
                        long size = Long.parseLong(cursor.getString(FileCategoryHelper.COLUMN_SIZE));
                        fileList.add(new JunkInfo(_id, null, com.android.clean.filemanager.Util.getNameFromFilepath(cursor.getString(FileCategoryHelper.COLUMN_PATH)),
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

    private void addAd() {
        nativeView = AdUtil.getNativeAdView(Tag_file_1, R.layout.native_ad_3);
        if (ll_ad != null && nativeView != null) {
            ViewGroup.LayoutParams layout_ad = ll_ad.getLayoutParams();
            ll_ad.setLayoutParams(layout_ad);
            ll_ad.addView(nativeView);
        }
    }

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
        dialog = new AlertDialog.Builder(FileListActivity.this).create();
        dialog.setView(view);
        dialog.show();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                long size = 0;
                for (JunkInfo info : deleteList) {
                    size += info.size;
                    FileUtils.deleteCo(FileListActivity.this, fc, info._id);
                }
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        for (JunkInfo info : deleteList) {
                            boolean deleteSuce = FileUtils.deleteFile(FileListActivity.this, info.path);
                            if (!deleteSuce) {
                                android.util.Log.e(TAG, "delete fail --" + info.path);
                            }
                        }
                    }
                }.start();

                fileList.removeAll(deleteList);
                adapter.notifyDataSetChanged();
                Intent intent = new Intent(FileListActivity.this, SuccessActivity.class);
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
