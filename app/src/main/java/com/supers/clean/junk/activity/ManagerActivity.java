package com.supers.clean.junk.activity;

import android.annotation.TargetApi;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
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
import com.eos.ui.demo.cross.CrossManager;
import com.eos.ui.demo.dialog.DialogManager;
import com.sample.lottie.LottieAnimationView;
import com.supers.clean.junk.R;
import com.supers.clean.junk.customeview.ListViewForScrollView;
import com.supers.clean.junk.util.Constant;
import com.supers.clean.junk.view.AppManagerView;
import com.supers.clean.junk.adapter.ManagerAdapter;
import com.android.clean.util.CommonUtil;
import com.supers.clean.junk.util.PreData;
import com.supers.clean.junk.entity.JunkInfo;
import com.supers.clean.junk.presenter.ManagerPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2017/3/2.
 */

public class ManagerActivity extends BaseActivity implements AppManagerView {

    FrameLayout title_left;
    TextView title_name;
    Button junk_button_clean;
    LinearLayout ll_ad_size, ll_ad_time, ll_ad_pinlv;
    TextView manager_shouquan;
    RelativeLayout manager_clean;
    FrameLayout fl_lot_manager_size, fl_lot_manager_time, fl_lot_manager_pinlv;
    LottieAnimationView lot_manager_size, lot_manager_time, lot_manager_pinlv;
    ViewPager doc_view_pager;
    TabLayout view_pager_tab;

    private ManagerPresenter managerPresenter;
    private ManagerAdapter adapter_size, adapter_time, adapter_pinlv;
    private MyReceiver receiver;
    private IntentFilter filter;
    private View nativeView1, nativeView2, nativeView3;

    private String TAG_MANAGER = "eos_manager";
    private ArrayList<String> titleList;
    private View view_size, view_time, view_pinlv, view_permiss;
    private ArrayList<View> viewList;
    private MyPagerAdaptre pagerAdapter;

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        junk_button_clean = (Button) findViewById(R.id.junk_button_clean);
        manager_clean = (RelativeLayout) findViewById(R.id.manager_clean);
        view_pager_tab = (TabLayout) findViewById(R.id.view_pager_tab);
        doc_view_pager = (ViewPager) findViewById(R.id.doc_view_pager);
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
        if (PreData.getDB(this, Constant.FULL_MANAGER, 0) == 1) {
            AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
            tuiGuang();
        } else {
            nativeView1 = CommonUtil.getNativeAdView(TAG_MANAGER, R.layout.native_ad_3);
            nativeView2 = CommonUtil.getNativeAdView(TAG_MANAGER, R.layout.native_ad_3);
            nativeView3 = CommonUtil.getNativeAdView(TAG_MANAGER, R.layout.native_ad_3);
            if (ll_ad_size != null && nativeView1 != null) {
                ViewGroup.LayoutParams layout_ad = ll_ad_size.getLayoutParams();
                ll_ad_size.setLayoutParams(layout_ad);
                ll_ad_size.addView(nativeView1);
            } else {
                tuiGuang();
                DialogManager.getCrossView(getApplicationContext(), extraData, "list1", "manager", true, new CrossManager.onCrossViewClickListener() {
                    @Override
                    public void onClick(View view) {

                    }

                    @Override
                    public void onLoadView(View view) {
                        if (view != null) {
                            ((ImageView) view.findViewById(R.id.cross_default_image)).setScaleType(ImageView.ScaleType.CENTER_CROP);
                            lot_manager_size = ((LottieAnimationView) view.findViewById(R.id.cross_default_lottie));
                            lot_manager_size.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            fl_lot_manager_size.setVisibility(View.VISIBLE);
                            lot_manager_size.pauseAnimation();
                            fl_lot_manager_size.addView(view, 0);
                        } else {
                            fl_lot_manager_size.setVisibility(View.GONE);
                        }
                    }
                });
            }
            if (ll_ad_time != null && nativeView2 != null) {
                ViewGroup.LayoutParams layout_ad = ll_ad_time.getLayoutParams();
                ll_ad_time.setLayoutParams(layout_ad);
                ll_ad_time.addView(nativeView2);
            } else {
                tuiGuang();
                DialogManager.getCrossView(getApplicationContext(), extraData, "list1", "manager", true, new CrossManager.onCrossViewClickListener() {
                    @Override
                    public void onClick(View view) {

                    }

                    @Override
                    public void onLoadView(View view) {
                        if (view != null) {
                            ((ImageView) view.findViewById(R.id.cross_default_image)).setScaleType(ImageView.ScaleType.CENTER_CROP);
                            lot_manager_time = ((LottieAnimationView) view.findViewById(R.id.cross_default_lottie));
                            lot_manager_time.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            fl_lot_manager_time.setVisibility(View.VISIBLE);
                            lot_manager_time.pauseAnimation();
                            fl_lot_manager_time.addView(view, 0);
                        } else {
                            fl_lot_manager_time.setVisibility(View.GONE);
                        }
                    }
                });
            }
            if (ll_ad_pinlv != null && nativeView3 != null) {
                ViewGroup.LayoutParams layout_ad = ll_ad_pinlv.getLayoutParams();
                ll_ad_pinlv.setLayoutParams(layout_ad);
                ll_ad_pinlv.addView(nativeView3);
            } else {
                tuiGuang();
                DialogManager.getCrossView(getApplicationContext(), extraData, "list1", "manager", true, new CrossManager.onCrossViewClickListener() {
                    @Override
                    public void onClick(View view) {

                    }

                    @Override
                    public void onLoadView(View view) {
                        if (view != null) {
                            ((ImageView) view.findViewById(R.id.cross_default_image)).setScaleType(ImageView.ScaleType.CENTER_CROP);
                            lot_manager_pinlv = ((LottieAnimationView) view.findViewById(R.id.cross_default_lottie));
                            lot_manager_pinlv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            fl_lot_manager_pinlv.setVisibility(View.VISIBLE);
                            lot_manager_pinlv.pauseAnimation();
                            fl_lot_manager_pinlv.addView(view, 0);
                        } else {
                            fl_lot_manager_pinlv.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }

    }

    @Override
    public void onClick() {
        title_left.setOnClickListener(onClickListener);
        junk_button_clean.setOnClickListener(onClickListener);
    }

    @Override
    public void initData(long cleanSize) {
        title_name.setText(R.string.main_manager_name);
        adapter_size = new ManagerAdapter(this, managerPresenter);
        adapter_time = new ManagerAdapter(this, managerPresenter);
        adapter_pinlv = new ManagerAdapter(this, managerPresenter);
        titleList = new ArrayList<>();
        titleList.add(getString(R.string.manager_sort_size));
        titleList.add(getString(R.string.manager_sort_time));
        titleList.add(getString(R.string.manager_sort_pinlv));
        view_pager_tab.addTab(view_pager_tab.newTab().setText(titleList.get(0)));
        view_pager_tab.addTab(view_pager_tab.newTab().setText(titleList.get(1)));
        view_pager_tab.addTab(view_pager_tab.newTab().setText(titleList.get(2)));

        view_size = LayoutInflater.from(this).inflate(R.layout.layout_manager_listview, null);
        view_time = LayoutInflater.from(this).inflate(R.layout.layout_manager_listview, null);
        view_pinlv = LayoutInflater.from(this).inflate(R.layout.layout_manager_listview, null);
        view_permiss = LayoutInflater.from(this).inflate(R.layout.layout_manager_permiss, null);
        initList();
        viewList = new ArrayList<>();
        viewList.add(view_size);
        viewList.add(view_time);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !isNoSwitch() && isNoOption()) {
            viewList.add(view_permiss);
        } else {
            viewList.add(view_pinlv);
        }
        pagerAdapter = new MyPagerAdaptre();
        doc_view_pager.setAdapter(pagerAdapter);
        view_pager_tab.setupWithViewPager(doc_view_pager);
        doc_view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
//                    adapter_size.notifyDataSetChanged();
                    if (lot_manager_time != null) {
                        lot_manager_time.pauseAnimation();
                    }
                    if (lot_manager_pinlv != null) {
                        lot_manager_pinlv.pauseAnimation();
                    }
                    if (lot_manager_size != null) {
                        lot_manager_size.playAnimation();
                    }
                } else if (position == 1) {
//                    adapter_time.notifyDataSetChanged();
                    if (lot_manager_size != null) {
                        lot_manager_size.pauseAnimation();
                    }
                    if (lot_manager_pinlv != null) {
                        lot_manager_pinlv.pauseAnimation();
                    }
                    if (lot_manager_time != null) {
                        lot_manager_time.playAnimation();
                    }
                } else {
//                    adapter_pinlv.notifyDataSetChanged();
                    if (lot_manager_size != null) {
                        lot_manager_size.pauseAnimation();
                    }
                    if (lot_manager_time != null) {
                        lot_manager_time.pauseAnimation();
                    }
                    if (lot_manager_pinlv != null) {
                        lot_manager_pinlv.playAnimation();
                    }

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        managerPresenter.addAdapterData();
        String fileSize = CommonUtil.convertStorage(cleanSize, true);
        if (TextUtils.isEmpty(fileSize)) {
            junk_button_clean.setText(getResources().getText(R.string.manager_button));
        } else {
            junk_button_clean.setText(getResources().getText(R.string.manager_button) + "(" + fileSize + ")");
        }
    }


    private void initList() {
        ListViewForScrollView listView_size = (ListViewForScrollView) view_size.findViewById(R.id.file_list);
        ListViewForScrollView listView_time = (ListViewForScrollView) view_time.findViewById(R.id.file_list);
        ListViewForScrollView listView_pinlv = (ListViewForScrollView) view_pinlv.findViewById(R.id.file_list);
        TextView manager_shouquan = (TextView) view_permiss.findViewById(R.id.manager_shouquan);
        ll_ad_size = (LinearLayout) view_size.findViewById(R.id.ll_ad);
        fl_lot_manager_size = (FrameLayout) view_size.findViewById(R.id.fl_lot_manager);
        ll_ad_time = (LinearLayout) view_time.findViewById(R.id.ll_ad);
        fl_lot_manager_time = (FrameLayout) view_time.findViewById(R.id.fl_lot_manager);
        ll_ad_pinlv = (LinearLayout) view_pinlv.findViewById(R.id.ll_ad);
        fl_lot_manager_pinlv = (FrameLayout) view_pinlv.findViewById(R.id.fl_lot_manager);
        listView_size.setAdapter(adapter_size);
        listView_time.setAdapter(adapter_time);
        listView_pinlv.setAdapter(adapter_pinlv);
        manager_shouquan.setOnClickListener(onClickListener);
    }

    class MyPagerAdaptre extends PagerAdapter {
        private int mChildCount = 0;

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public void notifyDataSetChanged() {
            mChildCount = getCount();
            super.notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            if (mChildCount > 0) {
                mChildCount--;
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
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
    }

    ;


    @Override
    public void updateAdapter(List<JunkInfo> listsize, List<JunkInfo> listtime, List<JunkInfo> listpinlv) {
        adapter_size.upList(listsize);
        adapter_time.upList(listtime);
        adapter_pinlv.upList(listpinlv);
        adapter_size.notifyDataSetChanged();
        adapter_time.notifyDataSetChanged();
        adapter_pinlv.notifyDataSetChanged();
    }


    @Override
    public void setCleanDAta(long size) {
        String fileSize = CommonUtil.convertStorage(size, true);
        if (TextUtils.isEmpty(fileSize)) {
            junk_button_clean.setText(getResources().getText(R.string.manager_button));
            manager_clean.setVisibility(View.GONE);
        } else {
            junk_button_clean.setText(getResources().getText(R.string.manager_button) + "(" + fileSize + ")");
            manager_clean.setVisibility(View.VISIBLE);
        }
        adapter_size.notifyDataSetChanged();
        adapter_time.notifyDataSetChanged();
        adapter_pinlv.notifyDataSetChanged();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_left:
                    onBackPressed();
                    break;
                case R.id.manager_shouquan:
                    Intent intent = new Intent(
                            Settings.ACTION_USAGE_ACCESS_SETTINGS);
                    startActivityForResult(intent, 100);
                    break;
                case R.id.junk_button_clean:
                    if (doc_view_pager.getCurrentItem() == 0) {
                        managerPresenter.bleachFile(adapter_size.getData());
                    } else if (doc_view_pager.getCurrentItem() == 1) {
                        managerPresenter.bleachFile(adapter_time.getData());
                    } else {
                        managerPresenter.bleachFile(adapter_pinlv.getData());
                    }
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
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private boolean isNoSwitch() {
        long ts = System.currentTimeMillis();
        UsageStatsManager usageStatsManager = (UsageStatsManager) getApplicationContext()
                .getSystemService(USAGE_STATS_SERVICE);
        List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_BEST, 0, ts);
        if (queryUsageStats == null || queryUsageStats.isEmpty()) {
            return false;
        }
        return true;
    }

    private boolean isNoOption() {
        PackageManager packageManager = getApplicationContext()
                .getPackageManager();
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AndroidSdk.onResumeWithoutTransition(this);
        if (lot_manager_size != null) {
            lot_manager_size.playAnimation();
        }
        if (lot_manager_time != null) {
            lot_manager_time.playAnimation();
        }
        if (lot_manager_pinlv != null) {
            lot_manager_pinlv.playAnimation();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (lot_manager_size != null) {
            lot_manager_size.pauseAnimation();
        }
        if (lot_manager_time != null) {
            lot_manager_time.pauseAnimation();
        }
        if (lot_manager_pinlv != null) {
            lot_manager_pinlv.pauseAnimation();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !isNoSwitch() && isNoOption()) {
            } else {
                viewList.remove(2);
                viewList.add(view_pinlv);
                pagerAdapter.notifyDataSetChanged();
            }
        }
    }
}
