package com.mutter.clean.junk.myActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
import com.mutter.clean.junk.util.Constant;
import com.mutter.clean.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2017/3/8.
 */

public class IgnoreListAvtivity extends BaseActivity {
    TextView title_name;
    ListView listView;
    LinearLayout white_wu;
    FrameLayout title_left;
    List<JunkInfo> white_list;
    private List<String> whiteList;
    ImageView title_right;
    WhiteListAdapter adapter;
    LinearLayout ll_ad;
    FrameLayout ad_fl;
    private View nativeView;
    Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ignore);
        handler = new Handler();
        title_name.setText(R.string.white_list_name);
        title_right.setVisibility(View.VISIBLE);
        title_left.setOnClickListener(clickListener);
        title_right.setOnClickListener(clickListener);

        white_list = new ArrayList<>();
        adapter = new WhiteListAdapter(this);
        listView.setAdapter(adapter);
        initDAta();
        initAd();
    }

    private void initAd() {
        nativeView = AdUtil.getNativeAdView("", R.layout.native_ad_3);
        if (ll_ad != null && nativeView != null) {
            ll_ad.addView(nativeView);
//            AdUtil.startBannerAnimation(this,ad_fl);
            ad_fl.setVisibility(View.VISIBLE);
        }
    }

    private void initDAta() {
        whiteList = CleanDBHelper.getInstance(this).getWhiteList(CleanDBHelper.TableType.Ram);
        white_list.clear();
        for (JunkInfo info : CleanManager.getInstance(this).getAppList()) {
            if (whiteList.contains(info.pkg)) {
                info.isWhiteList = true;
                white_list.add(info);
            }
        }
        adapter.upList(white_list);
        adapter.notifyDataSetChanged();
        if (white_list.size() == 0) {
            white_wu.setVisibility(View.VISIBLE);
        } else {
            white_wu.setVisibility(View.INVISIBLE);
        }
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_left:
                    onBackPressed();
                    break;
                case R.id.title_right:
                    Intent intent = new Intent(IgnoreListAvtivity.this, IgnoreAddActivity.class);
                    IgnoreListAvtivity.this.startActivityForResult(intent, 1);
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        setResult(Constant.WHITE_RESUIL);
        finish();
    }

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        title_right = (ImageView) findViewById(R.id.title_right);
        listView = (ListView) findViewById(R.id.list_si);
        white_wu = (LinearLayout) findViewById(R.id.white_wu);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
        ad_fl = (FrameLayout) findViewById(R.id.ad_fl);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            initDAta();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
