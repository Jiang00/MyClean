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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
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
import com.android.clean.filemanager.FileInfo;
import com.android.clean.filemanager.FileSortHelper;
import com.android.clean.filemanager.FileUtils;
import com.android.clean.filemanager.MultipleItem;
import com.android.clean.util.Constant;
import com.android.clean.util.PreData;
import com.android.client.AndroidSdk;
import com.supers.clean.junk.R;
import com.supers.clean.junk.adapter.FileAdapter;
import com.supers.clean.junk.adapter.FilesdAdapter;
import com.supers.clean.junk.entity.JunkInfo;
import com.supers.clean.junk.util.AdUtil;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SDCardActivity extends BaseActivity {
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
    ImageView null_icon;
    FrameLayout null_ll;
    FrameLayout file_fl;

    private FilesdAdapter adapter;
    private Handler mHandler;
    private String name;
    private int nameId;
    private AlertDialog dialog;
    private FileCategoryHelper.FileCategory fc;
    private String Tag_file_1 = "eos_file_1";
    private View nativeView;

    private List<FileInfo> fileInfos = new ArrayList<>();
    private List<MultipleItem> mMultipleItems;
    private File mCurrentPathFile = null;
    private File mSDCardPath = null;
    private String path;

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
        null_icon = (ImageView) findViewById(R.id.null_icon);
        null_ll = (FrameLayout) findViewById(R.id.null_ll);
        file_fl = (FrameLayout) findViewById(R.id.file_fl);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!PreData.getDB(this, Constant.BILL_YOUXIAO, true)) {
            AndroidSdk.loadFullAd(AdUtil.DEFAULT, null);
        }
        name = getIntent().getStringExtra("name");
        nameId = getIntent().getIntExtra("nameId", 0);
        if (name == null) {
            name = "sdcard";
        }
        setContentView(R.layout.layout_file_second);
        title_name.setText(nameId);
        mHandler = new Handler();
        loadAd();
        setListenet();
        file_progressbar.setVisibility(View.GONE);
        mMultipleItems = new ArrayList<>();
        path = getIntent().getStringExtra("path");
        mSDCardPath = new File(path);
        adapter = new FilesdAdapter(this, mMultipleItems);
        file_list.setAdapter(adapter);
        showFiles(mSDCardPath);
        file_list.setVisibility(View.VISIBLE);
    }

    private void loadAd() {
        if (PreData.getDB(this, Constant.FULL_FILE_1, 0) == 1) {
            if (!PreData.getDB(this, Constant.BILL_YOUXIAO, true)) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AndroidSdk.showFullAd(AdUtil.DEFAULT);
                    }
                }, 1000);
                tuiGuang();
            }
        } else {
            addAd();
        }
    }

    private void addAd() {
        nativeView = AdUtil.getNativeAdView(this, Tag_file_1, R.layout.native_ad_3);
        if (ll_ad != null && nativeView != null) {
            ViewGroup.LayoutParams layout_ad = ll_ad.getLayoutParams();
            ll_ad.setLayoutParams(layout_ad);
            ll_ad.addView(nativeView);
        }
    }


    private void showFiles(File folder) {

        mMultipleItems.clear();
        //当前目录
//        tv_path.setText(folder.getAbsolutePath());
        mCurrentPathFile = folder;
        File[] files = fileFilter(folder);
        if (null == files || files.length == 0) {
            null_ll.setVisibility(View.VISIBLE);
            Log.e("files", "files::为空啦");
        } else {
            null_ll.setVisibility(View.INVISIBLE);
            //获取文件信息
            fileInfos = getFileInfosFromFileArray(files);
            for (int i = 0; i < fileInfos.size(); i++) {
                if (fileInfos.get(i).isDir) {
                    Log.e("mMultipleItems", "==isDir");
                    mMultipleItems.add(new MultipleItem(MultipleItem.FOLD, fileInfos.get(i)));
                } else {
                    Log.e("mMultipleItems", "=!=isDir");
                    mMultipleItems.add(new MultipleItem(MultipleItem.FILE, fileInfos.get(i)));
                }

            }

            //查询本地数据库，如果之前有选择的就显示打钩
//            List<FileInfo> mList = FileDao.queryAll();
//            for (int i = 0; i < fileInfos.size(); i++) {
//                for (FileInfo fileInfo : mList) {
//                    if (fileInfo.getFileName().equals(fileInfos.get(i).getFileName())) {
//                        fileInfos.get(i).setIsCheck(true);
//                    }
//                }
//            }
        }
        adapter.upList(mMultipleItems);
        adapter.notifyDataSetChanged();
    }

    private void upData(String s) {
        mMultipleItems.clear();
        //当前目录
//        tv_path.setText(folder.getAbsolutePath());
        File[] files = fileFilter(mCurrentPathFile);
        if (null == files || files.length == 0) {
            null_ll.setVisibility(View.VISIBLE);
            Log.e("files", "files::为空啦");
        } else {
            null_ll.setVisibility(View.INVISIBLE);
            //获取文件信息
            fileInfos = getFileInfosFromFileArray(files);
            for (int i = 0; i < fileInfos.size(); i++) {
                if (!fileInfos.get(i).fileName.contains(s)) {
                    continue;
                }
                if (fileInfos.get(i).isDir) {
                    Log.e("mMultipleItems", "==isDir");
                    mMultipleItems.add(new MultipleItem(MultipleItem.FOLD, fileInfos.get(i)));
                } else {
                    Log.e("mMultipleItems", "=!=isDir");
                    mMultipleItems.add(new MultipleItem(MultipleItem.FILE, fileInfos.get(i)));
                }

            }

            //查询本地数据库，如果之前有选择的就显示打钩
//            List<FileInfo> mList = FileDao.queryAll();
//            for (int i = 0; i < fileInfos.size(); i++) {
//                for (FileInfo fileInfo : mList) {
//                    if (fileInfo.getFileName().equals(fileInfos.get(i).getFileName())) {
//                        fileInfos.get(i).setIsCheck(true);
//                    }
//                }
//            }
        }
        adapter.upList(mMultipleItems);
        adapter.notifyDataSetChanged();
    }

    /**
     * 文件过滤,将手机中隐藏的文件给过滤掉
     */
    public static File[] fileFilter(File file) {
        File[] files = file.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return !pathname.isHidden();
            }
        });
        return files;
    }

    public static List<FileInfo> getFileInfosFromFileArray(File[] files) {
        List<FileInfo> fileInfos = new ArrayList<>();
        for (File file : files) {
            FileInfo fileInfo = getFileInfoFromFile(file);
            fileInfos.add(fileInfo);
        }
        Collections.sort(fileInfos, new FileNameComparator());
        return fileInfos;
    }

    /**
     * 根据文件名进行比较排序
     */
    public static class FileNameComparator implements Comparator<FileInfo> {
        protected final static int
                FIRST = -1,
                SECOND = 1;

        @Override
        public int compare(FileInfo lhs, FileInfo rhs) {
            if (lhs.isDir || rhs.isDir) {
                if (lhs.isDir == rhs.isDir)
                    return lhs.fileName.compareToIgnoreCase(rhs.fileName);
                else if (lhs.isDir) return FIRST;
                else return SECOND;
            }
            return lhs.fileName.compareToIgnoreCase(rhs.fileName);
        }
    }

    public static FileInfo getFileInfoFromFile(File file) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.fileName = (file.getName());
        fileInfo.filePath = (file.getPath());
        fileInfo.fileSize = (file.length());
        fileInfo.isDir = (file.isDirectory());
        if (fileInfo.isDir) {
            File[] files = fileFilter(file);
            if (null != files) {
                fileInfo.count = files.length;
            }
        }
        fileInfo.fileTime = (getFileLastModifiedTime(file));
        int lastDotIndex = file.getName().lastIndexOf(".");
        if (lastDotIndex > 0) {
            String fileSuffix = file.getName().substring(lastDotIndex + 1);
            fileInfo.suffix = (fileSuffix);
        }
        return fileInfo;
    }

    /**
     * 读取文件的最后修改时间的方法
     */
    public static String getFileLastModifiedTime(File f) {
        Calendar cal = Calendar.getInstance();
        long time = f.lastModified();
        SimpleDateFormat formatter = new
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cal.setTimeInMillis(time);
        return formatter.format(cal.getTime());
    }

    private void setListenet() {
        title_left.setOnClickListener(clickListener);
        title_right.setOnClickListener(clickListener);
        clear.setOnClickListener(clickListener);
        file_button_clean.setOnClickListener(clickListener);
        file_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mMultipleItems.get(position).getData().isDir) {
                    showFiles(new File(mMultipleItems.get(position).getData().filePath));
                    if (search_edit_text != null && search_edit_text.getText() != null && search_edit_text.getText().length() != 0) {
                        upData(search_edit_text.getText().toString());
                    }
                } else {
                    FileUtils.openFile(SDCardActivity.this, (mMultipleItems.get(position).getData().filePath));
                }
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
                    toggleEditAnimation(true);
                    break;
                case R.id.clear:
                    toggleEditAnimation(false);
                    break;
                case R.id.file_button_clean:
                    ArrayList<MultipleItem> deleteList = new ArrayList<>();
                    if (mMultipleItems == null || mMultipleItems.size() == 0) {
                        return;
                    }
                    for (MultipleItem info : mMultipleItems) {
                        if (info.getData().isSelected) {
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
                    search_edit_text.setText("");
                    showFiles(mCurrentPathFile);
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


    private void showDia(final ArrayList<MultipleItem> deleteList) {
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
            title.setText(deleteList.get(0).getData().fileName);
        } else {
            title.setText(R.string.delete_queren);
        }
        message.setText(getString(R.string.delete_2, deleteList.size()));
        dialog = new AlertDialog.Builder(SDCardActivity.this).create();
        dialog.setView(view);
        dialog.show();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                long size = 0;
                for (MultipleItem info : deleteList) {
                    size += info.getData().fileSize;
//                    FileUtils.deleteCo(SDCardActivity.this, fc, info.getData()._id);
                }
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        for (MultipleItem info : deleteList) {
                            boolean deleteSuce = FileUtils.deleteFile(SDCardActivity.this, info.getData().filePath);
                            if (!deleteSuce) {
                                android.util.Log.e(TAG, "delete fail --" + info.getData().filePath);
                            }
                        }
                    }
                }.start();

                mMultipleItems.removeAll(deleteList);
                adapter.notifyDataSetChanged();
                Intent intent = new Intent(SDCardActivity.this, SuccessActivity.class);
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
        if (mSDCardPath.getAbsolutePath().equals(mCurrentPathFile.getAbsolutePath())) {
            setResult(1);
            finish();
        } else {
            mCurrentPathFile = mCurrentPathFile.getParentFile();
            showFiles(mCurrentPathFile);
            if (search_edit_text != null && search_edit_text.getText() != null && search_edit_text.getText().length() != 0) {
                upData(search_edit_text.getText().toString());
            }
        }
    }
}
