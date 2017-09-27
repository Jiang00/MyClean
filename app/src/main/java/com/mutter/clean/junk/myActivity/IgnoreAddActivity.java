package com.mutter.clean.junk.myActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mutter.clean.core.CleanManager;
import com.mutter.clean.db.CleanDBHelper;
import com.mutter.clean.junk.R;
import com.mutter.clean.junk.myAdapter.WhiteListAdapter;
import com.mutter.clean.entity.JunkInfo;
import com.mutter.clean.junk.util.AdUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2017/3/8.
 */

public class IgnoreAddActivity extends BaseActivity {
    FrameLayout title_left;
    ListView list_si;
    ImageButton clear;
    TextView title_name;
    ImageView title_right;
    EditText search_edit_text;

    WhiteListAdapter adapter;
    private boolean search;
    private List<JunkInfo> white_list, listEdit;
    private List<String> whiteList;
    LinearLayout ll_ad;
    private View nativeView;

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        title_right = (ImageView) findViewById(R.id.title_right);
        list_si = (ListView) findViewById(R.id.list_si);
        clear = (ImageButton) findViewById(R.id.clear);
        search_edit_text = (EditText) findViewById(R.id.search_edit_text);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_white_list_add);
        title_name.setText(R.string.white_list_add_name);
        title_left.setOnClickListener(clickListener);
        title_right.setOnClickListener(clickListener);
        clear.setOnClickListener(clickListener);
        white_list = new ArrayList<>();
        listEdit = new ArrayList<>();
        whiteList = CleanDBHelper.getInstance(this).getWhiteList(CleanDBHelper.TableType.Ram);
        adapter = new WhiteListAdapter(this);
        list_si.setAdapter(adapter);
        initData();
        initAd();
    }

    private void initAd() {
        nativeView = AdUtil.getNativeAdView("", R.layout.native_ad_3);
        if (ll_ad != null && nativeView != null) {
            ll_ad.addView(nativeView);
            ll_ad.setVisibility(View.VISIBLE);
        }
    }

    private void initData() {
        white_list.clear();
        for (JunkInfo info : CleanManager.getInstance(this).getAppList()) {
            if (!whiteList.contains(info.pkg)) {
                white_list.add(info);
            }
        }
        adapter.addDataList(white_list);
        adapter.notifyDataSetChanged();
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_left:
                    onBackPressed();
                    break;
                case R.id.title_right:
                    toggleEditAnimation();
                    break;
                case R.id.clear:
                    toggleEditAnimation();
                    break;
            }
        }
    };

    private void toggleEditAnimation() {
        final View searchView = findViewById(R.id.search_container);
        View normalView = findViewById(R.id.normal_bar);

        final View visibleView, invisibleView;
        if (searchView.getVisibility() == View.GONE) {
            visibleView = normalView;
            invisibleView = searchView;
        } else {
            visibleView = searchView;
            invisibleView = normalView;
        }

        visibleView.setVisibility(View.GONE);
        invisibleView.setVisibility(View.VISIBLE);

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

    @Override
    public void onBackPressed() {
        setResult(1);
        finish();
    }
}
