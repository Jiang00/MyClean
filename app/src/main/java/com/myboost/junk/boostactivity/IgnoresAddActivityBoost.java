package com.myboost.junk.boostactivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.myboost.clean.core.CleanManager;
import com.myboost.clean.entity.JunkInfo;
import com.myboost.clean.privacydb.CleanDBHelper;
import com.myboost.junk.R;
import com.myboost.junk.customadapterboost.IgnoreListViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2017/3/8.
 */

public class IgnoresAddActivityBoost extends BaseActivity {
    EditText search_edit_text;
    ListView list_si;
    IgnoreListViewAdapter adapter;
    private List<JunkInfo> white_list, listEdit;
    TextView title_name;
    FrameLayout title_left;
    private boolean search;
    private List<String> whiteList;

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_left:
                    onBackPressed();
                    break;
            }
        }
    };

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        list_si = (ListView) findViewById(R.id.list_si);
        search_edit_text = (EditText) findViewById(R.id.search_edit_text);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_white_list_add);
        title_name.setText(R.string.white_list_add_name);
        title_left.setOnClickListener(clickListener);
        white_list = new ArrayList<>();
        listEdit = new ArrayList<>();
        whiteList = CleanDBHelper.getInstance(this).getWhiteList(CleanDBHelper.TableType.Ram);
        adapter = new IgnoreListViewAdapter(this);
        list_si.setAdapter(adapter);
        initData();

        if (search) {
            initData();
            search = false;
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
            search = true;
        }

    }

    private void upData(String string) {
        listEdit.clear();
        for (JunkInfo info : white_list) {
            if (info.label.contains(string)) {
                listEdit.add(info);
            }
        }
        adapter.upList(listEdit);
        adapter.notifyDataSetChanged();
    }

    private void initData() {
        white_list.clear();
        for (JunkInfo info : CleanManager.getInstance(this).getAppList()) {
            if (!whiteList.contains(info.pkg)) {
                white_list.add(info);
            }
        }
        adapter.upList(white_list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        setResult(1);
        finish();
    }
}
