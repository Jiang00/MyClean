package com.supers.clean.junk.activity;

import android.content.Intent;
import android.database.Cursor;
import android.media.midi.MidiDeviceStatus;
import android.media.midi.MidiSender;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.supers.clean.junk.R;
import com.supers.clean.junk.adapter.FileAdapter;
import com.supers.clean.junk.entity.JunkInfo;
import com.supers.clean.junk.filemanager.FileCategoryHelper;
import com.supers.clean.junk.filemanager.FileSortHelper;
import com.supers.clean.junk.filemanager.FileUtils;
import com.supers.clean.junk.filemanager.Util;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Ivy on 2017/4/21.
 */

public class FileListActivity extends BaseActivity {
    FrameLayout title_left;
    TextView title_name;
    ListView file_list;
    Button file_button_clean;
    RelativeLayout file_clean_rl;
    ProgressBar file_progressbar;

    private FileCategoryHelper fileHelper;
    private FileAdapter adapter;
    private ArrayList<JunkInfo> fileList;
    private Handler mHandler;
    private String name;
    private int nameId;
    private AlertDialog dialog;

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        file_list = (ListView) findViewById(R.id.file_list);
        file_button_clean = (Button) findViewById(R.id.file_button_clean);
        file_clean_rl = (RelativeLayout) findViewById(R.id.file_clean_rl);
        file_progressbar = (ProgressBar) findViewById(R.id.file_progressbar);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = getIntent().getStringExtra("name");
        nameId = getIntent().getIntExtra("nameId", 0);
        if (name == null) {
            name = "apk";
        }
        setContentView(R.layout.layout_file_second);
        title_name.setText(nameId);
        mHandler = new Handler();
        fileHelper = new FileCategoryHelper(this);
        fileList = new ArrayList<>();
        initData();
        adapter = new FileAdapter(this, name);
        file_list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        setListenet();
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = null;
                if (TextUtils.equals("apk", name)) {
                    cursor = fileHelper.query(FileCategoryHelper.FileCategory.Apk, FileSortHelper.SortMethod.size);
                } else if (TextUtils.equals("zip", name)) {
                    cursor = fileHelper.query(FileCategoryHelper.FileCategory.Zip, FileSortHelper.SortMethod.size);
                } else if (TextUtils.equals("music", name)) {
                    cursor = fileHelper.query(FileCategoryHelper.FileCategory.Music, FileSortHelper.SortMethod.size);
                } else if (TextUtils.equals("video", name)) {
                    cursor = fileHelper.query(FileCategoryHelper.FileCategory.Video, FileSortHelper.SortMethod.size);
                } else if (TextUtils.equals("other", name)) {
                    cursor = fileHelper.query(FileCategoryHelper.FileCategory.Other, FileSortHelper.SortMethod.size);
                }
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        if (onDestroyed) {
                            return;
                        }
                        long size = Long.parseLong(cursor.getString(FileCategoryHelper.COLUMN_SIZE));
                        fileList.add(new JunkInfo(null, Util.getNameFromFilepath(cursor.getString(FileCategoryHelper.COLUMN_PATH)),
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
                    file_progressbar.setVisibility(View.GONE);
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
                case R.id.file_button_clean:
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

    private void notifyFileSystemChanged(String path) {
        if (path == null)
            return;
        final File f = new File(path);
        final Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent mediaScanIntent = new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(f.getParentFile()); //out is your output file
            mediaScanIntent.setData(contentUri);
            sendBroadcast(mediaScanIntent);
        } else {
            sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://"
                            + Environment.getExternalStorageDirectory() + f.getParentFile())));
        }
//        if (f.isDirectory()) {
//        if (f.getParentFile().isDirectory()) {
//            intent = new Intent(Intent.ACTION_MEDIA_MOUNTED);
////            intent.setClassName("com.android.providers.media", "com.android.providers.media.MediaScannerReceiver");
//            intent.setData(Uri.fromFile(f.getParentFile()));
//            Log.e("delete", "isDirectory");
//        } else {
//            intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//            intent.setData(Uri.fromFile(f.getParentFile()));
//            Log.e("delete", "ontisDirectory");
//        }
//        sendBroadcast(intent);
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
                for (JunkInfo info : deleteList) {
                    FileUtils.deleteFile(info.path);
                    notifyFileSystemChanged(info.path);
                }
                fileList.removeAll(deleteList);
                adapter.notifyDataSetChanged();
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
