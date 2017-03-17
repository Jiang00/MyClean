package com.supers.clean.junk.activity;

import android.app.AlertDialog;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.supers.clean.junk.R;
import com.supers.clean.junk.View.AppManagerView;
import com.supers.clean.junk.View.adapter.ManagerAdapter;
import com.supers.clean.junk.modle.CommonUtil;
import com.supers.clean.junk.modle.PreData;
import com.supers.clean.junk.modle.entity.Contents;
import com.supers.clean.junk.modle.entity.JunkInfo;
import com.supers.clean.junk.myView.ListViewForScrollView;
import com.supers.clean.junk.presenter.ManagerPresenter;

import java.util.List;

/**
 * Created by on 2017/3/2.
 */

public class ManagerActivity extends BaseActivity implements AppManagerView {

    FrameLayout title_left;
    TextView title_name;
    FrameLayout manager_button_size, manager_button_time, manager_button_pinlv;
    ImageView manager_sort_size_backg, manager_sort_time_backg, manager_sort_pinlv_backg;
    TextView manager_sort_size_name, manager_sort_time_name, manager_sort_pinlv_name;
    Button junk_button_clean;
    ListViewForScrollView junk_list_all;
    LinearLayout ll_ad;
    LinearLayout manager_permision;
    TextView manager_shouquan;
    RelativeLayout manager_clean;

    private ManagerPresenter managerPresenter;
    private ManagerAdapter adapterManager;
    private MyReceiver receiver;
    private IntentFilter filter;
    private View nativeView;

    private String TAG_MANAGER = "eos_manager";

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        manager_button_size = (FrameLayout) findViewById(R.id.manager_button_size);
        manager_button_time = (FrameLayout) findViewById(R.id.manager_button_time);
        manager_button_pinlv = (FrameLayout) findViewById(R.id.manager_button_pinlv);
        manager_sort_size_backg = (ImageView) findViewById(R.id.manager_sort_size_backg);
        manager_sort_time_backg = (ImageView) findViewById(R.id.manager_sort_time_backg);
        manager_sort_pinlv_backg = (ImageView) findViewById(R.id.manager_sort_pinlv_backg);
        manager_sort_size_name = (TextView) findViewById(R.id.manager_sort_size_name);
        manager_sort_time_name = (TextView) findViewById(R.id.manager_sort_time_name);
        manager_sort_pinlv_name = (TextView) findViewById(R.id.manager_sort_pinlv_name);
        junk_button_clean = (Button) findViewById(R.id.junk_button_clean);
        junk_list_all = (ListViewForScrollView) findViewById(R.id.junk_list_all);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
        manager_permision = $(R.id.manager_permision);
        manager_shouquan = $(R.id.manager_shouquan);
        manager_clean = $(R.id.manager_clean);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_manager);
        managerPresenter = new ManagerPresenter(this, this);
        managerPresenter.init();
        receiver = new MyReceiver();
        filter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        registerReceiver(receiver, filter);

    }

    @Override
    public void loadFullAd() {
        if (PreData.getDB(this, Contents.FULL_MANAGER, 0) == 1) {
            AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
        } else {
            nativeView = CommonUtil.getNativeAdView(TAG_MANAGER, R.layout.native_ad_full);
            if (ll_ad != null && nativeView != null) {
                ViewGroup.LayoutParams layout_ad = ll_ad.getLayoutParams();
                if (nativeView.getHeight() <= CommonUtil.dp2px(250)) {
                    layout_ad.height = CommonUtil.dp2px(250);
                }
                ll_ad.setLayoutParams(layout_ad);
                ll_ad.addView(nativeView);
            }
        }

    }

    @Override
    public void onClick() {
        title_left.setOnClickListener(onClickListener);
        junk_button_clean.setOnClickListener(onClickListener);
        manager_button_size.setOnClickListener(onClickListener);
        manager_button_time.setOnClickListener(onClickListener);
        manager_button_pinlv.setOnClickListener(onClickListener);
    }

    @Override
    public void initData(long cleanSize) {
        title_name.setText(R.string.main_manager_name);
        adapterManager = new ManagerAdapter(this, managerPresenter);
        junk_list_all.setAdapter(adapterManager);
        managerPresenter.addAdapterData();
        String fileSize = CommonUtil.getFileSize4(cleanSize);
        if (TextUtils.isEmpty(fileSize)) {
            junk_button_clean.setText(getResources().getText(R.string.manager_button));
        } else {
            junk_button_clean.setText(getResources().getText(R.string.manager_button) + "(" + fileSize + ")");
        }
    }

    @Override
    public void addAppManagerdata(List<JunkInfo> list) {
        adapterManager.clear();
        adapterManager.addDataList(list);
//        adapterManager.upList(list);
        adapterManager.notifyDataSetChanged();
    }


    @Override
    public void setCleanDAta(long size) {
        String fileSize = CommonUtil.getFileSize4(size);
        if (TextUtils.isEmpty(fileSize)) {
            junk_button_clean.setText(getResources().getText(R.string.manager_button));
        } else {
            junk_button_clean.setText(getResources().getText(R.string.manager_button) + "(" + fileSize + ")");
        }
        if (manager_clean.getVisibility() == View.GONE) {
            manager_clean.setVisibility(View.VISIBLE);
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_left:
                    onBackPressed();
                    break;
                case R.id.manager_button_size:
                    manager_sort_size_name.setTextColor(getResources().getColorStateList(R.color.white_100));
                    manager_sort_time_name.setTextColor(getResources().getColorStateList(R.color.manager_sort_text));
                    manager_sort_pinlv_name.setTextColor(getResources().getColorStateList(R.color.manager_sort_text));
                    manager_sort_size_backg.setVisibility(View.VISIBLE);
                    manager_sort_time_backg.setVisibility(View.INVISIBLE);
                    manager_sort_pinlv_backg.setVisibility(View.INVISIBLE);
                    managerPresenter.sortList(ManagerPresenter.SIZE_TYPE);
                    manager_permision.setVisibility(View.INVISIBLE);
                    junk_list_all.setVisibility(View.VISIBLE);
                    break;
                case R.id.manager_button_time:
                    manager_sort_size_name.setTextColor(getResources().getColorStateList(R.color.manager_sort_text));
                    manager_sort_time_name.setTextColor(getResources().getColorStateList(R.color.white_100));
                    manager_sort_pinlv_name.setTextColor(getResources().getColorStateList(R.color.manager_sort_text));
                    manager_sort_size_backg.setVisibility(View.INVISIBLE);
                    manager_sort_time_backg.setVisibility(View.VISIBLE);
                    manager_sort_pinlv_backg.setVisibility(View.INVISIBLE);
                    managerPresenter.sortList(ManagerPresenter.TIME_TYPE);
                    manager_permision.setVisibility(View.INVISIBLE);
                    junk_list_all.setVisibility(View.VISIBLE);
                    break;
                case R.id.manager_button_pinlv:
                    manager_sort_size_name.setTextColor(getResources().getColorStateList(R.color.manager_sort_text));
                    manager_sort_time_name.setTextColor(getResources().getColorStateList(R.color.manager_sort_text));
                    manager_sort_pinlv_name.setTextColor(getResources().getColorStateList(R.color.white_100));
                    manager_sort_size_backg.setVisibility(View.INVISIBLE);
                    manager_sort_time_backg.setVisibility(View.INVISIBLE);
                    manager_sort_pinlv_backg.setVisibility(View.VISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !isNoSwitch()) {
                        manager_permision.setVisibility(View.VISIBLE);
                        junk_list_all.setVisibility(View.GONE);
                        manager_shouquan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(
                                        Settings.ACTION_USAGE_ACCESS_SETTINGS);
                                startActivity(intent);
                            }
                        });
                    } else {
                        manager_permision.setVisibility(View.INVISIBLE);
                        junk_list_all.setVisibility(View.VISIBLE);
                    }
                    managerPresenter.sortList(ManagerPresenter.PINLV_TYPE);
                    break;
                case R.id.junk_button_clean:
                    managerPresenter.bleachFile(adapterManager.getData());
                    break;
            }

        }
    };


    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            // TODO Auto-generated method stub
            String packageName = intent.getData().getSchemeSpecificPart();
            managerPresenter.unloadSuccess(packageName);

        }

    }

    //判断“有权查看使用权限的应用”这个选项的APP有没有打开
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private boolean isNoSwitch() {
        long ts = System.currentTimeMillis();
        UsageStatsManager usageStatsManager = (UsageStatsManager) getApplicationContext()
                .getSystemService("usagestats");
        List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_BEST, 0, ts);
        if (queryUsageStats == null || queryUsageStats.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public void reStart() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !isNoSwitch()) {
            manager_permision.setVisibility(View.VISIBLE);
            junk_list_all.setVisibility(View.GONE);
            manager_shouquan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(
                            Settings.ACTION_USAGE_ACCESS_SETTINGS);
                    startActivity(intent);
                }
            });
        } else {
            manager_permision.setVisibility(View.INVISIBLE);
            junk_list_all.setVisibility(View.VISIBLE);
            managerPresenter.sortList(ManagerPresenter.PINLV_TYPE);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        managerPresenter.restart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AndroidSdk.onResumeWithoutTransition(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
