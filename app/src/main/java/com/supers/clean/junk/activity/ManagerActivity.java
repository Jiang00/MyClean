package com.supers.clean.junk.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.supers.clean.junk.R;
import com.supers.clean.junk.View.AppManagerView;
import com.supers.clean.junk.View.adapter.ManagerAdapter;
import com.supers.clean.junk.modle.CommonUtil;
import com.supers.clean.junk.modle.PreData;
import com.supers.clean.junk.modle.entity.Contents;
import com.supers.clean.junk.modle.entity.JunkInfo;
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
    ListView junk_list_all;
    LinearLayout ll_ad;
    private ManagerPresenter managerPresenter;
    private ManagerAdapter adapterManager;
    private Handler myHandler;
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
        junk_list_all = (ListView) findViewById(R.id.junk_list_all);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_manager);
        managerPresenter = new ManagerPresenter(this, this);
        managerPresenter.init();
        receiver = new MyReceiver();
        filter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
        System.out.println(Intent.ACTION_PACKAGE_REMOVED);
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
                layout_ad.height = nativeView.getMeasuredHeight();
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
        if (TextUtils.equals(fileSize, "0B")) {
            junk_button_clean.setText(getResources().getText(R.string.manager_button));
        } else {
            junk_button_clean.setText(getResources().getText(R.string.manager_button) + "(" + fileSize + ")");
        }
    }

    @Override
    public void addAppManagerdata(List<JunkInfo> list) {
        adapterManager.upList(list);
        adapterManager.notifyDataSetChanged();
    }


    @Override
    public void setCleanDAta(long size) {
        String fileSize = CommonUtil.getFileSize4(size);
        if (TextUtils.equals(fileSize, "0B")) {
            junk_button_clean.setText(getResources().getText(R.string.manager_button));
        } else {
            junk_button_clean.setText(getResources().getText(R.string.manager_button) + "(" + fileSize + ")");
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

                    break;
                case R.id.manager_button_time:
                    manager_sort_size_name.setTextColor(getResources().getColorStateList(R.color.manager_sort_text));
                    manager_sort_time_name.setTextColor(getResources().getColorStateList(R.color.white_100));
                    manager_sort_pinlv_name.setTextColor(getResources().getColorStateList(R.color.manager_sort_text));
                    manager_sort_size_backg.setVisibility(View.INVISIBLE);
                    manager_sort_time_backg.setVisibility(View.VISIBLE);
                    manager_sort_pinlv_backg.setVisibility(View.INVISIBLE);
                    managerPresenter.sortList(ManagerPresenter.TIME_TYPE);
                    break;
                case R.id.manager_button_pinlv:
                    manager_sort_size_name.setTextColor(getResources().getColorStateList(R.color.manager_sort_text));
                    manager_sort_time_name.setTextColor(getResources().getColorStateList(R.color.manager_sort_text));
                    manager_sort_pinlv_name.setTextColor(getResources().getColorStateList(R.color.white_100));
                    manager_sort_size_backg.setVisibility(View.INVISIBLE);
                    manager_sort_time_backg.setVisibility(View.INVISIBLE);
                    manager_sort_pinlv_backg.setVisibility(View.VISIBLE);
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
