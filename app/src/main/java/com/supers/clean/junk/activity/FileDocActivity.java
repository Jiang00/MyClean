package com.supers.clean.junk.activity;

import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.supers.clean.junk.filemanager.Util;

import java.util.ArrayList;

/**
 * Created by Ivy on 2017/4/21.
 */

public class FileDocActivity extends BaseActivity {

    FrameLayout title_left;
    TextView title_name;
    Button file_button_clean;
    RelativeLayout file_clean_rl;
    ProgressBar file_progressbar;
    ViewPager doc_view_pager;
    TabLayout view_pager_tab;


    private FileCategoryHelper fileHelper;
    private ArrayList<JunkInfo> docList, txtList, pdfList;
    private Handler mHandler;
    private ArrayList<String> titleList;
    private ArrayList<View> viewList;
    private View view_doc;
    private View view_txt;
    private View view_pdf;
    FileAdapter adapter_doc, adapter_txt, adapter_pdf;

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        file_button_clean = (Button) findViewById(R.id.file_button_clean);
        file_clean_rl = (RelativeLayout) findViewById(R.id.file_clean_rl);
        file_progressbar = (ProgressBar) findViewById(R.id.file_progressbar);
        doc_view_pager = (ViewPager) findViewById(R.id.doc_view_pager);
        view_pager_tab = (TabLayout) findViewById(R.id.view_pager_tab);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_file_doc);
        title_name.setText(R.string.file_txt);
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
        setListenet();
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
                        adapter_doc.addDataList(docList);
                        adapter_doc.notifyDataSetChanged();
                        adapter_txt.addDataList(txtList);
                        adapter_txt.notifyDataSetChanged();
                        adapter_pdf.addDataList(pdfList);
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
                long size = Long.parseLong(cursorDoc.getString(FileCategoryHelper.COLUMN_SIZE));
                Drawable icon = null;
                if (fc == FileCategoryHelper.FileCategory.Word) {
                    icon = ContextCompat.getDrawable(FileDocActivity.this, R.mipmap.file_doc_icon);
                } else if (fc == FileCategoryHelper.FileCategory.Txt) {
                    icon = ContextCompat.getDrawable(FileDocActivity.this, R.mipmap.file_txt_icon);
                } else if (fc == FileCategoryHelper.FileCategory.Pdf) {
                    icon = ContextCompat.getDrawable(FileDocActivity.this, R.mipmap.file_pd_icon);
                }
                list.add(new JunkInfo(icon, Util.getNameFromFilepath(cursorDoc.getString(FileCategoryHelper.COLUMN_PATH)),
                        cursorDoc.getString(FileCategoryHelper.COLUMN_PATH), size, false));
            } while (cursorDoc.moveToNext());
            cursorDoc.close();
        }
    }

    private void initData() {
        initCur();
        titleList = new ArrayList<String>();
        titleList.add("DOC");
        titleList.add("TXT");
        titleList.add("PDF");
        view_pager_tab.addTab(view_pager_tab.newTab().setText(titleList.get(0)));
        view_pager_tab.addTab(view_pager_tab.newTab().setText(titleList.get(1)));
        view_pager_tab.addTab(view_pager_tab.newTab().setText(titleList.get(2)));

        view_doc = LayoutInflater.from(this).inflate(R.layout.layout_listview, null);
        view_txt = LayoutInflater.from(this).inflate(R.layout.layout_listview, null);
        view_pdf = LayoutInflater.from(this).inflate(R.layout.layout_listview, null);
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
//                    for (JunkInfo info : fileList) {
//                        if (info.isChecked) {
//                            FileUtils.deleteFile(info.path);
//                        }
//                    }
                    break;
            }
        }
    };
}
