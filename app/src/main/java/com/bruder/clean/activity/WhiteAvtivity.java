package com.bruder.clean.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bruder.clean.junk.R;
import com.bruder.clean.myadapter.MyWhiteListAdapter;
import com.bruder.clean.util.Constant;
import com.bruder.clean.util.UtilAd;
import com.cleaner.entity.JunkInfo;
import com.cleaner.heart.CleanManager;
import com.cleaner.sqldb.CleanDBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2017/3/8.
 */

public class WhiteAvtivity extends BaseActivity {
    FrameLayout title_left;
    List<JunkInfo> white_list;
    ListView listView;
    TextView white_wu;
    MyWhiteListAdapter adapter;
    private List<String> whiteList;
    TextView title_name;
    ImageView title_right;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ignore);
        title_name.setText(R.string.white_list_name);
        title_right.setVisibility(View.VISIBLE);
        title_left.setOnClickListener(clickListener);
        title_right.setOnClickListener(clickListener);

        white_list = new ArrayList<>();
        adapter = new MyWhiteListAdapter(this);
        listView.setAdapter(adapter);
        initDAta();
        addAd();
    }

    private void addAd() {
        View nativeView = UtilAd.getNativeAdView("", R.layout.native_ad_3);
        LinearLayout ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
        if (ll_ad != null && nativeView != null) {
            ll_ad.addView(nativeView);
        }
    }

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        title_right = (ImageView) findViewById(R.id.title_right);
        listView = (ListView) findViewById(R.id.list_si);
        white_wu = (TextView) findViewById(R.id.white_wu);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_left:
                    onBackPressed();
                    break;
                case R.id.title_right:
                    Intent intent = new Intent(WhiteAvtivity.this, WhiteAddActivity.class);
                    WhiteAvtivity.this.startActivityForResult(intent, 1);
                    break;
            }
        }
    };

    private void initDAta() {
        white_list.clear();
        whiteList = CleanDBHelper.getInstance(this).getWhiteList(CleanDBHelper.TableType.Ram);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            initDAta();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(Constant.WHITE_RESUIL);
        finish();
    }
}
