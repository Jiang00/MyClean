package com.mutter.clean.junk.myActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mutter.clean.filemanager.FileCategoryHelper;
import com.mutter.clean.filemanager.FileSortHelper;
import com.mutter.clean.filemanager.FileUtils;
import com.mutter.clean.util.PreData;
import com.android.client.AndroidSdk;
import com.mutter.clean.filemanager.Util;
import com.mutter.clean.junk.R;
import com.mutter.clean.junk.myAdapter.FileAdapter;
import com.mutter.clean.junk.entity.JunkInfo;
import com.mutter.clean.junk.util.AdUtil;
import com.mutter.clean.junk.util.Constant;

import java.util.ArrayList;

/**
 */

public class DocFileActivity extends BaseActivity {

    ProgressBar file_progressbar;
    ViewPager doc_view_pager;
    private static final String TAG = "DocFileActivity";
    TextView title_name;
    FrameLayout title_left;
    LinearLayout null_ll;
    Button file_button_clean;
    TabLayout view_pager_tab;
    FrameLayout file_fl;

    private FileCategoryHelper fileHelper;
    private ArrayList<JunkInfo> docList, txtList, pdfList;
    private Handler mHandler;
    private ArrayList<String> titleList;
    private ArrayList<View> viewList;
    private View view_doc;
    private View view_txt;
    private View view_pdf;
    private AlertDialog dialog;
    FileAdapter adapter_doc, adapter_txt, adapter_pdf;
    private FileCategoryHelper.FileCategory fc_clean;
    private String TAG_FILE_2 = "mutter_file_2";
    private View nativeView;
    private LinearLayout ll_ad;

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        file_button_clean = (Button) findViewById(R.id.file_button_clean);
        file_progressbar = (ProgressBar) findViewById(R.id.file_progressbar);
        doc_view_pager = (ViewPager) findViewById(R.id.doc_view_pager);
        view_pager_tab = (TabLayout) findViewById(R.id.view_pager_tab);
        null_ll = (LinearLayout) findViewById(R.id.null_ll);
        file_fl = (FrameLayout) findViewById(R.id.file_fl);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_file_doc);
        AndroidSdk.loadFullAd(AndroidSdk.FULL_TAG_PAUSE);
        title_name.setText(R.string.file_txt);
        loadAd();
        setListenet();
        if (0 == getIntent().getIntExtra("count", 1)) {
            null_ll.setVisibility(View.VISIBLE);
            file_fl.setVisibility(View.GONE);
            return;
        }
        mHandler = new Handler();
        fileHelper = new FileCategoryHelper(this);
        adapter_doc = new FileAdapter(this);
        adapter_txt = new FileAdapter(this);
        adapter_pdf = new FileAdapter(this);
        docList = new ArrayList<>();
        txtList = new ArrayList<>();
        pdfList = new ArrayList<>();
        viewList = new ArrayList<>();
        initData();

    }



    private void addAd() {
        nativeView = AdUtil.getNativeAdView(TAG_FILE_2, R.layout.native_ad_3);
        if (ll_ad != null && nativeView != null) {
            ViewGroup.LayoutParams layout_ad = ll_ad.getLayoutParams();
            ll_ad.setLayoutParams(layout_ad);
            ll_ad.addView(nativeView);
            ll_ad.setVisibility(View.VISIBLE);
        }
    }

    private void initCur() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                queryData(FileCategoryHelper.FileCategory.Word, docList);
                queryData(FileCategoryHelper.FileCategory.Txt, txtList);
                queryData(FileCategoryHelper.FileCategory.Pdf, pdfList);
                mHandler.post(new Runnable() {
                    public void run() {
                        file_progressbar.setVisibility(View.GONE);
                        adapter_doc.upList(docList);
                        adapter_doc.notifyDataSetChanged();
                        adapter_txt.upList(txtList);
                        adapter_txt.notifyDataSetChanged();
                        adapter_pdf.upList(pdfList);
                        adapter_pdf.notifyDataSetChanged();
                    }
                });
            }
        }).start();

    }

    private void queryData(FileCategoryHelper.FileCategory fc, ArrayList<JunkInfo> list) {
        Cursor cursorDoc = fileHelper.query(fc, FileSortHelper.SortMethod.size);
        if (cursorDoc != null && cursorDoc.moveToFirst()) {
            do {
                if (onDestroyed) {
                    return;
                }
                long _id = cursorDoc.getLong(FileCategoryHelper.COLUMN_ID);
                long size = Long.parseLong(cursorDoc.getString(FileCategoryHelper.COLUMN_SIZE));
                Drawable icon = null;
                if (fc == FileCategoryHelper.FileCategory.Word) {
                    icon = ContextCompat.getDrawable(DocFileActivity.this, R.mipmap.file_doc_icon);
                } else if (fc == FileCategoryHelper.FileCategory.Txt) {
                    icon = ContextCompat.getDrawable(DocFileActivity.this, R.mipmap.file_txt_icon);
                } else if (fc == FileCategoryHelper.FileCategory.Pdf) {
                    icon = ContextCompat.getDrawable(DocFileActivity.this, R.mipmap.file_pd_icon);
                }
                list.add(new JunkInfo(_id, icon, Util.getNameFromFilepath(cursorDoc.getString(FileCategoryHelper.COLUMN_PATH)),
                        cursorDoc.getString(FileCategoryHelper.COLUMN_PATH), size, false));
            } while (cursorDoc.moveToNext());
            cursorDoc.close();
        }
    }
    private void loadAd() {
        if (PreData.getDB(this, Constant.FULL_FILE_2, 0) == 1) {
            AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
        } else {
            addAd();
        }
    }
    private void initData() {
        initCur();
        titleList = new ArrayList<>();
        titleList.add("DOC");
        titleList.add("TXT");
        titleList.add("PDF");
        view_pager_tab.addTab(view_pager_tab.newTab().setText(titleList.get(0)));
        view_pager_tab.addTab(view_pager_tab.newTab().setText(titleList.get(1)));
        view_pager_tab.addTab(view_pager_tab.newTab().setText(titleList.get(2)));

        view_doc = LayoutInflater.from(this).inflate(R.layout.layout_listview, null);
        view_txt = LayoutInflater.from(this).inflate(R.layout.layout_listview, null);
        view_pdf = LayoutInflater.from(this).inflate(R.layout.layout_listview, null);
        ll_ad = (LinearLayout) view_doc.findViewById(R.id.ll_ad);
        initDoc();
        initTxt();
        initPdf();
        viewList.add(view_doc);
        viewList.add(view_txt);
        viewList.add(view_pdf);
        doc_view_pager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewList.get(position));
                return viewList.get(position);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titleList.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(viewList.get(position));
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }
        });
        view_pager_tab.setupWithViewPager(doc_view_pager);
    }

    private void initDoc() {
        ListView listView_doc = (ListView) view_doc.findViewById(R.id.file_list);
        ListView listView_txt = (ListView) view_txt.findViewById(R.id.file_list);
        ListView listView_pdf = (ListView) view_pdf.findViewById(R.id.file_list);
        listView_doc.setAdapter(adapter_doc);
        listView_txt.setAdapter(adapter_txt);
        listView_pdf.setAdapter(adapter_pdf);
    }
    private void showDia(final ArrayList<JunkInfo> deleteList) {
        if (deleteList.size() == 0) {
            showToast(getString(R.string.delete));
            return;
        }
        View view = View.inflate(this, R.layout.dialog_picture, null);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView message = (TextView) view.findViewById(R.id.message);
        TextView ok = (TextView) view.findViewById(R.id.ok);
        TextView cancle = (TextView) view.findViewById(R.id.cancle);

//        if (deleteList.size() == 1) {
//            title.setText(deleteList.get(0).name);
//        } else {
//            title.setText(R.string.delete_queren);
//        }
//        message.setText(getString(R.string.delete_2, deleteList.size()));
        dialog = new AlertDialog.Builder(DocFileActivity.this).create();
        dialog.setView(view);
        dialog.show();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                long size = 0;
                for (JunkInfo info : deleteList) {
                    size += info.size;
                    FileUtils.deleteCo(DocFileActivity.this, fc_clean, info._id);
                }
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        for (JunkInfo info : deleteList) {
                            boolean deleteSuce = FileUtils.deleteFile(DocFileActivity.this, info.path);
                            if (!deleteSuce) {
                                android.util.Log.e(TAG, "delete fail --" + info.path);
                            }
                        }
                    }
                }.start();
                if (doc_view_pager.getCurrentItem() == 0) {
                    docList.removeAll(deleteList);
                    adapter_doc.notifyDataSetChanged();
                } else if (doc_view_pager.getCurrentItem() == 1) {
                    txtList.removeAll(deleteList);
                    adapter_txt.notifyDataSetChanged();
                } else {
                    pdfList.removeAll(deleteList);
                    adapter_pdf.notifyDataSetChanged();
                }
                Intent intent = new Intent(DocFileActivity.this, SuccessActivity.class);
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

    private void initTxt() {
    }

    private void initPdf() {
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
                    if (doc_view_pager.getCurrentItem() == 0) {
                        fc_clean = FileCategoryHelper.FileCategory.Word;
                        for (JunkInfo info : docList) {
                            if (info.isChecked) {
                                deleteList.add(info);
                            }
                        }
                    } else if (doc_view_pager.getCurrentItem() == 1) {
                        fc_clean = FileCategoryHelper.FileCategory.Txt;
                        for (JunkInfo info : txtList) {
                            if (info.isChecked) {
                                deleteList.add(info);
                            }
                        }
                    } else {
                        fc_clean = FileCategoryHelper.FileCategory.Pdf;
                        for (JunkInfo info : pdfList) {
                            if (info.isChecked) {
                                deleteList.add(info);
                            }
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
}
