package com.supers.clean.junk.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.clean.core.CleanManager;
import com.android.clean.db.CleanDBHelper;
import com.supers.clean.junk.R;
import com.supers.clean.junk.adapter.WhiteListAdapter;
import com.android.clean.entity.JunkInfo;
import com.supers.clean.junk.util.AdUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2017/3/8.
 */

public class WhiteListAddActivity extends BaseActivity {
    FrameLayout title_left;
    TextView title_name;
    ImageView title_right;
    ListView list_si;
    ImageButton clear;
    EditText search_edit_text;

    WhiteListAdapter adapter;
    private boolean search;
    private List<JunkInfo> white_list, listEdit;
    private List<String> whiteList;
    private LinearLayout ll_ad;


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
        addAd();
    }

    private void addAd() {
        View native_xiao = AdUtil.getNativeAdView(this,"", R.layout.native_ad_3);
        if (ll_ad != null && native_xiao != null) {
            ll_ad.addView(native_xiao);
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
        adapter.upList(white_list);
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
                    toggleEditAnimation(true);
                    break;
                case R.id.clear:
                    toggleEditAnimation(false);
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
                    initData();
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
