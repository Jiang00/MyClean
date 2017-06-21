package com.fast.clean.junk.ui;

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
import android.os.Handler;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fast.clean.entity.JunkInfo;
import com.fast.clean.mutil.PreData;
import com.fast.clean.mutil.Util;
import com.fast.clean.junk.R;
import com.fast.clean.junk.adapter.ManagerAdapter;
import com.fast.clean.junk.presenter.ManagerPresenter;
import com.fast.clean.junk.util.AdUtil;
import com.fast.clean.junk.util.Constant;
import com.fast.clean.junk.view.AppManagerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2017/3/2.
 */

public class AppManagerActivity extends BaseActivity implements AppManagerView {

    FrameLayout title_left;
    RelativeLayout manager_clean;
    ViewPager doc_view_pager;
    TextView title_name;
    Button junk_button_clean;
    LinearLayout ll_ad_size, ll_ad_time, ll_ad_pinlv;
    TextView manager_shouquan;
    TabLayout view_pager_tab;

    private ManagerAdapter adapter_size, adapter_time, adapter_pinlv;
    private ManagerPresenter managerPresenter;
    private View nativeView1, nativeView2, nativeView3;
    private MyReceiver receiver;
    private IntentFilter filter;

    private String TAG_MANAGER = "acht_manager";
    private ArrayList<String> titleList;
    private ArrayList<View> viewList;
    private MyPagerAdaptre pagerAdapter;
    private View view_size, view_time, view_pinlv, view_permiss;
    public Handler myHandler;

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        view_pager_tab = (TabLayout) findViewById(R.id.view_pager_tab);
        junk_button_clean = (Button) findViewById(R.id.junk_button_clean);
        manager_clean = (RelativeLayout) findViewById(R.id.manager_clean);
        doc_view_pager = (ViewPager) findViewById(R.id.doc_view_pager);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_manager);
        myHandler = new Handler();
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
//            AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
        } else {
            nativeView1 = AdUtil.getNativeAdView(TAG_MANAGER, R.layout.native_ad_3);
            nativeView2 = AdUtil.getNativeAdView(TAG_MANAGER, R.layout.native_ad_3);
            nativeView3 = AdUtil.getNativeAdView(TAG_MANAGER, R.layout.native_ad_3);
            if (ll_ad_size != null && nativeView1 != null) {
                ViewGroup.LayoutParams layout_ad = ll_ad_size.getLayoutParams();
                ll_ad_size.setLayoutParams(layout_ad);
                ll_ad_size.addView(nativeView1);
            } else {
            }
            if (ll_ad_time != null && nativeView2 != null) {
                ViewGroup.LayoutParams layout_ad = ll_ad_time.getLayoutParams();
                ll_ad_time.setLayoutParams(layout_ad);
                ll_ad_time.addView(nativeView2);
            } else {
            }

        }

    }

    @Override
    public void onClick() {
        title_left.setOnClickListener(onClickListener);
        junk_button_clean.setOnClickListener(onClickListener);
    }

    private void initList() {
        ListView listView_size = (ListView) view_size.findViewById(R.id.file_list);
        ListView listView_time = (ListView) view_time.findViewById(R.id.file_list);
        ListView listView_pinlv = (ListView) view_pinlv.findViewById(R.id.file_list);
        TextView manager_shouquan = (TextView) view_permiss.findViewById(R.id.manager_shouquan);
        ll_ad_size = (LinearLayout) view_size.findViewById(R.id.ll_ad);
        ll_ad_time = (LinearLayout) view_time.findViewById(R.id.ll_ad);
        ll_ad_pinlv = (LinearLayout) view_pinlv.findViewById(R.id.ll_ad);
        listView_size.setAdapter(adapter_size);
        listView_time.setAdapter(adapter_time);
        listView_pinlv.setAdapter(adapter_pinlv);
        manager_shouquan.setOnClickListener(onClickListener);
    }

    @Override
    public void initData(long cleanSize) {
        title_name.setText(R.string.main_manager_name);
        adapter_size = new ManagerAdapter(this, managerPresenter);
        adapter_time = new ManagerAdapter(this, managerPresenter);
        adapter_pinlv = new ManagerAdapter(this, managerPresenter);
        managerPresenter.addAdapterData();
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
                } else if (position == 1) {
//                    adapter_time.notifyDataSetChanged();
                } else {
//                    adapter_pinlv.notifyDataSetChanged();

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        managerPresenter.addAdapterData();
        String fileSize = Util.convertStorage(cleanSize, true);
        if (TextUtils.isEmpty(fileSize)) {
            junk_button_clean.setText(getResources().getText(R.string.manager_button));
        } else {
            junk_button_clean.setText(getResources().getText(R.string.manager_button) + "(" + fileSize + ")");
        }
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
//            container.addView(viewList.get(position));

            try {
                if (viewList.get(position).getParent() == null)
                    ((ViewPager) container).addView(viewList.get(position), 0);
                else {
                    ((ViewGroup) viewList.get(position).getParent()).removeView(viewList.get(position));
                    ((ViewPager) container).addView(viewList.get(position), 0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


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
    public void updateAdapter(final List<JunkInfo> listsize, final List<JunkInfo> listtime, final List<JunkInfo> listpinlv) {
        myHandler.post(new Runnable() {
            @Override
            public void run() {
                adapter_size.upList(listsize);
                adapter_size.notifyDataSetChanged();
                adapter_time.upList(listtime);
                adapter_time.notifyDataSetChanged();
                adapter_pinlv.upList(listpinlv);
                adapter_pinlv.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
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
//        AndroidSdk.onResumeWithoutTransition(this);

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

    @Override
    public void setCleanDAta(long size) {
        String fileSize = Util.convertStorage(size, true);
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

}
