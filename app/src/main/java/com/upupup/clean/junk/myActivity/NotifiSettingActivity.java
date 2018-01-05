package com.upupup.clean.junk.myActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.upupup.clean.core.CleanManager;
import com.upupup.clean.db.CleanDBHelper;
import com.upupup.clean.junk.util.AdUtil;
import com.upupup.clean.util.PreData;
import com.upupup.clean.junk.R;
import com.upupup.clean.junk.myAdapter.NotifiSettingAdapter;
import com.upupup.clean.entity.JunkInfo;
import com.upupup.clean.junk.util.Constant;

import java.util.ArrayList;
import java.util.List;

import static com.upupup.clean.db.CleanDBHelper.TableType.Notification;

/**
 */

public class NotifiSettingActivity extends BaseActivity {
    FrameLayout title_left;
    View view_set;
    Button notifi_button_clean;
    RelativeLayout notifi_setting_t;
    ImageView notifi_all_check;
    TextView title_name;
    NotifiSettingAdapter adapter;
    LinearLayout ll_ad;
    FrameLayout ad_fl;
    ListView list_si;
    MyApplication myApplication;
    ArrayList<JunkInfo> list;
    List<String> isnotifiWhiteList = CleanDBHelper.getInstance(this).getWhiteList(Notification);
    Handler myHandler;

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        list_si = (ListView) findViewById(R.id.list_si);
        title_name = (TextView) findViewById(R.id.title_name);
        view_set = findViewById(R.id.view_set);
        notifi_button_clean = (Button) findViewById(R.id.notifi_button_clean);
        notifi_setting_t = (RelativeLayout) findViewById(R.id.notifi_setting_t);
        notifi_all_check = (ImageView) findViewById(R.id.notifi_all_check);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
        ad_fl = (FrameLayout) findViewById(R.id.ad_fl);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_notifi);
        myHandler = new Handler();
        myApplication = (MyApplication) getApplication();
        title_name.setText(R.string.side_notifi);
        notifi_setting_t.setVisibility(View.VISIBLE);
        notifi_button_clean.setVisibility(View.GONE);
        if (PreData.getDB(this, Constant.KEY_NOTIFI, true)) {
            notifi_all_check.setImageResource(R.mipmap.side_check_passed);
            view_set.setVisibility(View.GONE);
        } else {
            notifi_all_check.setImageResource(R.mipmap.side_check_normal);
            view_set.setVisibility(View.VISIBLE);
        }
        adapter = new NotifiSettingAdapter(this);
        list = CleanManager.getInstance(this).getAppList();
        ArrayList<JunkInfo> listSoft = new ArrayList<>();
        for (JunkInfo info : list) {
            if (!isnotifiWhiteList.contains(info.pkg)) {
                listSoft.add(info);
            } else {
                info.isnotifiWhiteList = true;
            }
        }
        list.removeAll(listSoft);
        list.addAll(0, listSoft);
        adapter.addDataList(list);
        list_si.setAdapter(adapter);
        setListener();
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                addAd();
            }
        }, 1000);
    }

    private void addAd() {
        View nativeView = AdUtil.getNativeAdView("", R.layout.native_ad_3);
        if (ll_ad != null && nativeView != null) {
            ll_ad.addView(nativeView);
            AdUtil.startBannerAnimation(this, ad_fl);
        }
    }

    View.OnClickListener nOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_left:
                    onBackPressed();
                    break;
                case R.id.notifi_all_check:
                    if (PreData.getDB(NotifiSettingActivity.this, Constant.KEY_NOTIFI, true)) {
                        PreData.putDB(NotifiSettingActivity.this, Constant.KEY_NOTIFI, false);
                        notifi_all_check.setImageResource(R.mipmap.side_check_normal);
                        view_set.setVisibility(View.VISIBLE);
                    } else {
                        PreData.putDB(NotifiSettingActivity.this, Constant.KEY_NOTIFI, true);
                        notifi_all_check.setImageResource(R.mipmap.side_check_passed);
                        view_set.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    };

    private void setListener() {
        title_left.setOnClickListener(nOnClickListener);
        notifi_all_check.setOnClickListener(nOnClickListener);
    }

    @Override
    public void onBackPressed() {
        setResult(1);
        finish();
    }
}
