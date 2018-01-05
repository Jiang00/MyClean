package com.mutter.clean.junk.myActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.client.AdListener;
import com.android.client.AndroidSdk;
import com.mutter.clean.junk.R;
import com.mutter.clean.junk.entity.SideInfo;
import com.mutter.clean.junk.fragment.CoolingFragment;
import com.mutter.clean.junk.fragment.JunkFragment;
import com.mutter.clean.junk.fragment.RamFragment;
import com.mutter.clean.junk.myAdapter.SideAdapter;
import com.mutter.clean.junk.myview.ListViewForScrollView;
import com.mutter.clean.junk.util.Constant;
import com.mutter.clean.junk.view.MainFullLayout;
import com.mutter.clean.util.LoadManager;
import com.mutter.clean.util.PreData;
import com.mutter.module.charge.saver.Util.Constants;
import com.mutter.module.charge.saver.Util.Utils;
import com.sample.lottie.LottieAnimationView;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    FrameLayout tab_1, tab_2, tab_3;
    TextView tab_1_name, tab_2_name, tab_3_name;
    View tab_1_line, tab_2_line, tab_3_line;
    ViewPager main_pager;
    ListViewForScrollView side_listView;
    DrawerLayout main_drawer;
    ImageView iv_title_left;
    LottieAnimationView loading_libao;
    LottieAnimationView main_full;
    FrameLayout load_fl;


    ArrayList<Fragment> pager_list;
    MyAdapter adapter;
    SideAdapter sideAdapter;
    private JunkFragment junkFragment;
    private RamFragment ramFragment;
    private CoolingFragment coolingFragment;
    FragmentManager fragmentManager;
    Handler handler;
    private AlertDialog dialog;
    int pager_position;
    boolean load_success, adShow;

    @Override
    protected void findId() {
        super.findId();
        tab_1 = (FrameLayout) findViewById(R.id.tab_1);
        tab_2 = (FrameLayout) findViewById(R.id.tab_2);
        tab_3 = (FrameLayout) findViewById(R.id.tab_3);
        tab_1_name = (TextView) findViewById(R.id.tab_1_name);
        tab_2_name = (TextView) findViewById(R.id.tab_2_name);
        tab_3_name = (TextView) findViewById(R.id.tab_3_name);
        tab_1_line = findViewById(R.id.tab_1_line);
        tab_2_line = findViewById(R.id.tab_2_line);
        tab_3_line = findViewById(R.id.tab_3_line);
        main_pager = (ViewPager) findViewById(R.id.main_pager);
        side_listView = (ListViewForScrollView) findViewById(R.id.side_listView);
        main_drawer = (DrawerLayout) findViewById(R.id.main_drawer);
        iv_title_left = (ImageView) findViewById(R.id.iv_title_left);
        loading_libao = (LottieAnimationView) findViewById(R.id.loading_libao);
        main_full = (LottieAnimationView) findViewById(R.id.main_full);
        load_fl = (FrameLayout) findViewById(R.id.load_fl);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "ramFragment", ramFragment);
        getSupportFragmentManager().putFragment(outState, "junkFragment", junkFragment);
        if (coolingFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "coolingFragment", coolingFragment);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e("fragment", "onConfigurationChanged");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dra);
        fragmentManager = getSupportFragmentManager();
        if (getIntent() != null && TextUtils.equals("notifi", getIntent().getStringExtra("from"))) {
        } else {
            if (PreData.getDB(this, Constant.FULL_START, 0) == 1) {
                AndroidSdk.showFullAd("loading_full", new AdListener() {
                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                        Log.e("adadad", "==onAdClicked");
                    }

                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        Log.e("adadad", "==onAdClosed");
                    }

                    @Override
                    public void onAdShow() {
                        super.onAdShow();
                        adShow = true;
                        Log.e("adadad", "==onAdShow");
                    }
                });
            }
        }
        handler = new Handler();
        if (savedInstanceState == null) {
            ramFragment = new RamFragment(adShow);
            junkFragment = new JunkFragment();
            coolingFragment = new CoolingFragment();
        } else {
            ramFragment = (RamFragment) getSupportFragmentManager().getFragment(savedInstanceState, "ramFragment");
            junkFragment = (JunkFragment) getSupportFragmentManager().getFragment(savedInstanceState, "junkFragment");
            try {
                coolingFragment = (CoolingFragment) getSupportFragmentManager().getFragment(savedInstanceState, "coolingFragment");
                if (coolingFragment == null) {
                    coolingFragment = new CoolingFragment();
                }
            } catch (Exception e) {
                if (coolingFragment == null) {
                    coolingFragment = new CoolingFragment();
                }
            }

        }
        initData();
        initSide();
        setDrawerLeftEdgeSize(main_drawer, 0.1f);
        iv_title_left.setOnClickListener(click);
        loading_libao.setOnClickListener(click);
        main_full.setOnClickListener(click);
        tab_1.setOnClickListener(click);
        tab_2.setOnClickListener(click);
        tab_3.setOnClickListener(click);
    }

    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.iv_title_left) {
                main_drawer.openDrawer(GravityCompat.START);
            } else if (i == R.id.main_full) {
                handler.removeCallbacks(runnable);
                if (load_success) {
                    AndroidSdk.showFullAd("loading_full");
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        load_fl.setVisibility(View.GONE);
                    }
                }, 500);
            } else if (i == R.id.loading_libao) {
                load_fl.setVisibility(View.VISIBLE);
                main_full.playAnimation();
                load_success = false;
                AndroidSdk.loadFullAd("loading_full", new AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        load_fl.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAdLoadSuccess() {
                        super.onAdLoadSuccess();
                        load_success = true;
                        Log.e("load_success", "load_success");
                    }
                });
                handler.postDelayed(runnable, 4000);
            } else if (i == R.id.tab_1) {
                main_pager.setCurrentItem(0);
            } else if (i == R.id.tab_2) {
                main_pager.setCurrentItem(1);
            } else if (i == R.id.tab_3) {
                main_pager.setCurrentItem(2);
            }

        }
    };
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (load_success) {
                AndroidSdk.showFullAd("loading_full");
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    main_full.pauseAnimation();
                    load_fl.setVisibility(View.GONE);
                }
            }, 500);

        }
    };

    private void initSide() {
        if (sideAdapter == null) {
            sideAdapter = new SideAdapter(this);
            side_listView.setAdapter(sideAdapter);
        }
        sideAdapter.clear();
        sideAdapter.addData(new SideInfo(R.mipmap.side_junk, R.string.side_junk));//垃圾清理
        sideAdapter.addData(new SideInfo(R.mipmap.side_ram, R.string.side_ram));//内存
        sideAdapter.addData(new SideInfo(R.mipmap.side_battery, R.string.side_charging));//cPu
        sideAdapter.addData(new SideInfo(R.mipmap.side_manager, R.string.side_manager));//cPu
        sideAdapter.addData(new SideInfo(R.mipmap.side_power, R.string.side_power));//cPu
        sideAdapter.addData(new SideInfo(R.mipmap.side_picture, R.string.side_picture));//cPu
        sideAdapter.addData(new SideInfo(R.mipmap.side_setting, R.string.setting_name));//设置
        sideAdapter.addData(new SideInfo(R.mipmap.side_rotate, R.string.side_rotate));//好评
        sideAdapter.notifyDataSetChanged();
    }


    private void initData() {

        pager_list = new ArrayList<>();
        pager_list.add(ramFragment);
        pager_list.add(junkFragment);
        pager_list.add(coolingFragment);
        adapter = new MyAdapter(getSupportFragmentManager());
        main_pager.setAdapter(adapter);
        main_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pager_position = position;
                if (position == 0) {
                    tab_1_line.setVisibility(View.VISIBLE);
                    tab_2_line.setVisibility(View.GONE);
                    tab_3_line.setVisibility(View.GONE);
                    tab_1_name.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white_100));
                    tab_1_name.setTextSize(getXmlDef(R.dimen.s24));
                    tab_2_name.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white_70));
                    tab_2_name.setTextSize(getXmlDef(R.dimen.s17));
                    tab_3_name.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white_70));
                    tab_3_name.setTextSize(getXmlDef(R.dimen.s17));
                    ramFragment.startKedu();
                } else if (position == 1) {
                    tab_1_line.setVisibility(View.GONE);
                    tab_2_line.setVisibility(View.VISIBLE);
                    tab_3_line.setVisibility(View.GONE);
                    tab_2_name.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white_100));
                    tab_2_name.setTextSize(getXmlDef(R.dimen.s24));
                    tab_1_name.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white_70));
                    tab_1_name.setTextSize(getXmlDef(R.dimen.s17));
                    tab_3_name.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white_70));
                    tab_3_name.setTextSize(getXmlDef(R.dimen.s17));
                    junkFragment.startJunk();
                } else if (position == 2) {
                    tab_1_line.setVisibility(View.GONE);
                    tab_2_line.setVisibility(View.GONE);
                    tab_3_line.setVisibility(View.VISIBLE);
                    tab_3_name.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white_100));
                    tab_3_name.setTextSize(getXmlDef(R.dimen.s24));
                    tab_2_name.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white_70));
                    tab_2_name.setTextSize(getXmlDef(R.dimen.s17));
                    tab_1_name.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white_70));
                    tab_1_name.setTextSize(getXmlDef(R.dimen.s17));
                    coolingFragment.startFen();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            return pager_list.get(position);
        }


        @Override
        public int getCount() {
            return pager_list.size();
        }
    }

    @Override
    public void onBackPressed() {
        if (load_fl.getVisibility() == View.VISIBLE) {
            load_fl.setVisibility(View.GONE);
            main_full.pauseAnimation();
            handler.removeCallbacksAndMessages(null);
            return;
        }
        if (main_drawer.isDrawerOpen(GravityCompat.START)) {
            main_drawer.closeDrawer(GravityCompat.START);
        } else {
            if (PreData.getDB(this, Constant.FULL_EXIT, 0) == 1) {
                AndroidSdk.showFullAd("exit_full");
            }
            showExitDialog();
        }

    }

    private void showExitDialog() {
        View view = View.inflate(this, R.layout.dialog_exit, null);
        TextView exit_queren = (TextView) view.findViewById(R.id.exit_queren);
        TextView exit_quxiao = (TextView) view.findViewById(R.id.exit_quxiao);
        LinearLayout ll_ad = (LinearLayout) view.findViewById(R.id.ll_ad);
        exit_queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        exit_quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog = new AlertDialog.Builder(this, R.style.exit_dialog).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(view);
        dialog.show();
        if (PreData.getDB(this, Constant.FULL_EXIT, 0) == 1) {
            View nativeView = getNativeAdView("exit_native", R.layout.native_ad_2);
            if (ll_ad != null && nativeView != null) {
                ll_ad.addView(nativeView);
                ll_ad.setVisibility(View.VISIBLE);
            }
        }
    }

    public static View getNativeAdView(String tag, @LayoutRes int layout) {
        if (!AndroidSdk.hasNativeAd(tag)) {
            return null;
        }
        View nativeView = AndroidSdk.peekNativeAdViewWithLayout(tag, layout, null);
        if (nativeView == null) {
            return null;
        }

        if (nativeView != null) {
            ViewGroup viewParent = (ViewGroup) nativeView.getParent();
            if (viewParent != null) {
                viewParent.removeAllViews();
            }
        }
        return nativeView;
    }

    //设置侧边栏滑出距离,从屏幕哪里可以滑出
    public void setDrawerLeftEdgeSize(DrawerLayout drawerLayout, float displayWidthPercentage) {
        if (drawerLayout == null) return;
        try {
            Field leftDraggerField = drawerLayout.getClass().getDeclaredField("mLeftDragger");
            leftDraggerField.setAccessible(true);
            ViewDragHelper leftDragger = (ViewDragHelper) leftDraggerField.get(drawerLayout);
            Field edgeSizeField = leftDragger.getClass().getDeclaredField("mEdgeSize");
            edgeSizeField.setAccessible(true);
            int edgeSize = edgeSizeField.getInt(leftDragger);
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            edgeSizeField.setInt(leftDragger, Math.max(edgeSize, (int) (dm.widthPixels * displayWidthPercentage)));
        } catch (Exception e) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.SETTING_RESUIL) {
            initSide();
        }
        if (pager_position == 0) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    ramFragment.startKedu();
                }
            });
        } else if (pager_position == 1) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    junkFragment.startJunk();
                }
            });
        } else if (pager_position == 2) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    coolingFragment.startFen();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (main_full != null) {
            main_full.cancelAnimation();
        }
        if (loading_libao != null) {
            loading_libao.cancelAnimation();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PreData.getDB(this, Constant.FULL_EXIT, 0) == 1) {
            AndroidSdk.loadFullAd("exit_full", null);
        }
        AndroidSdk.loadFullAd("clean_full", null);
    }
}
