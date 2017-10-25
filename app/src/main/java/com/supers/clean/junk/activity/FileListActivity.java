package com.supers.clean.junk.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.clean.db.CleanDBHelper;
import com.android.clean.filemanager.FileCategoryHelper;
import com.android.clean.filemanager.FileSortHelper;
import com.android.clean.filemanager.FileUtils;
import com.android.clean.util.PreData;
import com.android.client.AndroidSdk;
import com.supers.clean.junk.R;
import com.supers.clean.junk.adapter.FileAdapter;
import com.supers.clean.junk.entity.JunkInfo;
import com.android.clean.util.Util;
import com.supers.clean.junk.util.AdUtil;
import com.android.clean.util.Constant;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Ivy on 2017/4/21.
 */

public class FileListActivity extends BaseActivity {

    public static final String TAG = "FileListActivity";
    FrameLayout title_left;
    TextView title_name;
    ImageView title_right, clear;
    EditText search_edit_text;
    ListView file_list;
    Button file_button_clean;
    RelativeLayout file_clean_rl;
    ProgressBar file_progressbar;
    LinearLayout ll_ad;
    FrameLayout null_ll;
    FrameLayout file_fl;

    private FileCategoryHelper fileHelper;
    private FileAdapter adapter;
    private ArrayList<JunkInfo> fileList;
    private ArrayList<JunkInfo> listEdit;
    private Handler mHandler;
    private String name;
    private int nameId;
    private AlertDialog dialog;
    private FileCategoryHelper.FileCategory fc;
    private String Tag_file_1 = "eos_file_1";
    private View nativeView;

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        title_right = (ImageView) findViewById(R.id.title_right);
        search_edit_text = (EditText) findViewById(R.id.search_edit_text);
        clear = (ImageView) findViewById(R.id.clear);
        file_list = (ListView) findViewById(R.id.file_list);
        file_button_clean = (Button) findViewById(R.id.file_button_clean);
        file_clean_rl = (RelativeLayout) findViewById(R.id.file_clean_rl);
        file_progressbar = (ProgressBar) findViewById(R.id.file_progressbar);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
        null_ll = (FrameLayout) findViewById(R.id.null_ll);
        file_fl = (FrameLayout) findViewById(R.id.file_fl);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSdk.loadFullAd(AdUtil.DEFAULT, null);
        name = getIntent().getStringExtra("name");
        nameId = getIntent().getIntExtra("nameId", 0);
        if (name == null) {
            name = "apk";
        }
        setContentView(R.layout.layout_file_second);
        title_name.setText(nameId);
        mHandler = new Handler();
        loadAd();
        fileList = new ArrayList<>();
        listEdit = new ArrayList<>();
        adapter = new FileAdapter(this, name);
        setListenet();
        if (0 == getIntent().getIntExtra("count", 1)) {
            file_fl.setVisibility(View.GONE);
            null_ll.setVisibility(View.VISIBLE);
            return;
        }
        fileHelper = new FileCategoryHelper(this);
        initData();
        file_list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void loadAd() {
        if (PreData.getDB(this, Constant.FULL_FILE_1, 0) == 1) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AndroidSdk.showFullAd(AdUtil.DEFAULT);
                }
            }, 1000);
            tuiGuang();
        } else {
            addAd();
        }
    }

    private void addAd() {
        nativeView = AdUtil.getNativeAdView(Tag_file_1, R.layout.native_ad_3);
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
                } else if (TextUtils.equals("other", name)) {
                    fc = FileCategoryHelper.FileCategory.Other;
                } else if (TextUtils.equals("racent", name)) {
                    ArrayList<String> list = CleanDBHelper.getInstance(FileListActivity.this).getFileList(CleanDBHelper.TableType.RacentFile);
                    if (list != null && list.size() != 0) {

                        for (String info : list) {
                            File f = new File(info);
                            fileList.add(new JunkInfo(0, null, f.getName(), info, f.length(), false));
                        }
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
                    return;
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
        title_right.setOnClickListener(clickListener);
        clear.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_left:
                    onBackPressed();
                    break;
                case R.id.title_right:
                    toggleEditAnimation(true);
                    break;
                case R.id.clear:
                    toggleEditAnimation(false);
                    break;
                case R.id.file_button_clean:
                    ArrayList<JunkInfo> deleteList = new ArrayList<>();
                    if (fileList == null || fileList.size() == 0) {
                        return;
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

    private void toggleEditAnimation(final boolean isVisible) {
        final View searchView = findViewById(R.id.search_container);
        ObjectAnimator invis2vis = null;
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        if (isVisible) {
            invis2vis = ObjectAnimator.ofFloat(searchView, View.TRANSLATION_X, metrics.widthPixels, 0);
        } else {
            invis2vis = ObjectAnimator.ofFloat(searchView, View.TRANSLATION_X, 0, metrics.widthPixels);
        }
        invis2vis.setDuration(500);
        invis2vis.setInterpolator(new LinearInterpolator());
        invis2vis.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isVisible) {
                    adapter.upList(fileList);
                    adapter.notifyDataSetChanged();
                    searchView.setVisibility(View.GONE);
                } else {
                    search_edit_text.setText("");
                    search_edit_text.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void afterTextChanged(Editable s) {
                            upData(s.toString());
                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }
                    });
                }
            }
        });
        invis2vis.start();
        if (isVisible) {
            searchView.setVisibility(View.VISIBLE);
        }
    }

    private void upData(String string) {
        listEdit.clear();
        for (JunkInfo info : fileList) {
            if (info.label.contains(string)) {
                listEdit.add(info);
            }
        }
        adapter.upList(listEdit);
        adapter.notifyDataSetChanged();

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
                    if (info._id != 0) {
                        FileUtils.deleteCo(FileListActivity.this, fc, info._id);
                    } else {
                        CleanDBHelper.getInstance(FileListActivity.this).deleteItem(CleanDBHelper.TableType.RacentFile, info.path);
                    }
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
                listEdit.removeAll(deleteList);
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

    @Override
    public void onBackPressed() {
        setResult(1);
        finish();
    }
}
