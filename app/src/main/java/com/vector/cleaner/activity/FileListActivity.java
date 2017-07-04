package com.vector.cleaner.activity;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vector.mcleaner.file.FileCategoryHelper;
import com.vector.mcleaner.file.FileSortHelper;
import com.vector.mcleaner.file.FileUtil;
import com.vector.mcleaner.file.FileUtils;
import com.vector.mcleaner.mutil.PreData;
import com.android.client.AndroidSdk;
import com.vector.cleaner.R;
import com.vector.cleaner.madapter.FilesAdapter;
import com.vector.cleaner.entity.FileInfo;
import com.vector.cleaner.utils.AdUtil;
import com.vector.cleaner.utils.Constant;

import java.util.ArrayList;

/**
 */

public class FileListActivity extends BaseActivity {

    public static final String TAG = "FileListActivity";
    TextView title_name;
    ListView file_list;
    Button file_button_clean;
    ProgressBar file_progressbar;
    LinearLayout ll_ad;
    FrameLayout title_left;
    RelativeLayout file_clean_rl;
    LinearLayout null_ll;
    FrameLayout file_fl;

    private int nameId;
    private AlertDialog dialog;
    private FileCategoryHelper.FileCategory fc;
    private FileCategoryHelper fileHelper;
    private FilesAdapter adapter;
    private ArrayList<FileInfo> fileList;
    private Handler mHandler;
    private String name;
    private String Tag_file_1 = "vector_file_1";
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
            return;
        }
        fileHelper = new FileCategoryHelper(this);
        fileList = new ArrayList<>();
        initData();
        adapter = new FilesAdapter(this, name);
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
                    ArrayList<FileInfo> deleteList = new ArrayList<>();
                    if (fileList == null || fileList.size() == 0) {
                        break;
                    }
                    for (FileInfo info : fileList) {
                        if (info.isChecked) {
                            deleteList.add(info);
                        }
                    }
                    showDia(deleteList);
                    break;
            }
        }
    };


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
                        fileList.add(new FileInfo(_id, null, FileUtil.getNameFromFilepath(cursor.getString(FileCategoryHelper.COLUMN_PATH)),
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

    private void showDia(final ArrayList<FileInfo> deleteList) {
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
                for (FileInfo info : deleteList) {
                    size += info.size;
                    FileUtils.deleteCo(FileListActivity.this, fc, info._id);
                }
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        for (FileInfo info : deleteList) {
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
