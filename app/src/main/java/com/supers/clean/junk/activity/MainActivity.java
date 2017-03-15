package com.supers.clean.junk.activity;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.client.AndroidSdk;
import com.eos.manager.page.SecuritySharPFive;
import com.eos.module.charge.saver.Util.Constants;
import com.eos.module.charge.saver.Util.Utils;
import com.eos.manager.AppLockPatternEosActivity;
import com.supers.clean.junk.R;
import com.supers.clean.junk.View.MainView;
import com.supers.clean.junk.View.adapter.SideAdapter;
import com.supers.clean.junk.modle.CommonUtil;
import com.supers.clean.junk.modle.PreData;
import com.supers.clean.junk.modle.entity.Contents;
import com.supers.clean.junk.modle.entity.JunkInfo;
import com.supers.clean.junk.myView.CustomRoundCpu;
import com.supers.clean.junk.myView.MainScrollView;
import com.supers.clean.junk.myView.PullToRefreshLayout;
import com.supers.clean.junk.presenter.MainPresenter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements MainView {

    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int NOMAL = 2;

    public static MainActivity instance;

    //FrameLayout main_all_cercle;
    MainScrollView main_scroll_view;
    PullToRefreshLayout main_pull_refresh;
    FrameLayout main_scale_all;
    ImageView iv_title_right;
    ImageView iv_title_left;
    RelativeLayout main_cpu_air_button, main_sd_air_button, main_ram_air_button;
    CustomRoundCpu main_custom_cpu, main_custom_sd, main_custom_ram;
    TextView main_cpu_temp, main_sd_per, main_sd_size, main_ram_per, main_ram_size;
    LinearLayout main_air_all;
    LinearLayout main_junk_button, main_ram_button, main_manager_button, main_cooling_button, main_applock_button, main_theme_button;
    LinearLayout main_rotate_all;
    TextView main_rotate_bad;
    LinearLayout main_rotate_good;
    LinearLayout main_msg_button;
    TextView main_msg_ram_percent, main_msg_sd_percent, main_msg_sd_unit, main_msg_cpu_percent;
    TextView main_gurad_num;
    ImageView main_guard_rotate;
    FrameLayout main_guard_all;
    ListView side_listView;
    DrawerLayout main_drawer;
    LinearLayout ll_ad, ll_ad_side;

    private String TAG_MAIN = "eos_main";
    private String TAG_HUA = "eos_hua";
    private String TAG_SIDE = "eos_side";

    private float firstY;
    private DisplayMetrics dm;
    private int cercleHeight;
    private boolean first = true;
    private int cercle_value;
    private boolean isScroll;
    private Handler handler;
    private ViewGroup.LayoutParams cercle_linearParams;
    private ValueAnimator valueAnimator;
    private MainPresenter mainPresenter;
    private SideAdapter adapter;
    private long mExitTime;
    private int temp;

    @Override
    protected void findId() {
        super.findId();
        main_drawer = (DrawerLayout) findViewById(R.id.main_drawer);
        //main_all_cercle = (FrameLayout) findViewById(R.id.main_all_cercle);
        main_scroll_view = (MainScrollView) findViewById(R.id.main_scroll_view);
        main_pull_refresh = (PullToRefreshLayout) findViewById(R.id.main_pull_refresh);
        main_scale_all = (FrameLayout) findViewById(R.id.main_scale_all);
        iv_title_right = (ImageView) findViewById(R.id.iv_title_right);
        iv_title_left = (ImageView) findViewById(R.id.iv_title_left);

        main_junk_button = (LinearLayout) findViewById(R.id.main_junk_button);
        main_ram_button = (LinearLayout) findViewById(R.id.main_ram_button);
        main_manager_button = (LinearLayout) findViewById(R.id.main_manager_button);
        main_cooling_button = (LinearLayout) findViewById(R.id.main_cooling_button);
        main_applock_button = (LinearLayout) findViewById(R.id.main_applock_button);
        main_theme_button = (LinearLayout) findViewById(R.id.main_theme_button);
        main_rotate_all = (LinearLayout) findViewById(R.id.main_rotate_all);
        main_rotate_bad = (TextView) findViewById(R.id.main_rotate_bad);
        main_rotate_good = (LinearLayout) findViewById(R.id.main_rotate_good);
        main_msg_button = (LinearLayout) findViewById(R.id.main_msg_button);
        main_msg_ram_percent = (TextView) findViewById(R.id.main_msg_ram_percent);
        main_msg_sd_percent = (TextView) findViewById(R.id.main_msg_sd_percent);
        main_msg_sd_unit = (TextView) findViewById(R.id.main_msg_sd_unit);
        main_msg_cpu_percent = (TextView) findViewById(R.id.main_msg_cpu_percent);
        main_gurad_num = (TextView) findViewById(R.id.main_gurad_num);
        main_guard_rotate = (ImageView) findViewById(R.id.main_guard_rotate);
        main_guard_all = (FrameLayout) findViewById(R.id.main_guard_all);
        side_listView = (ListView) findViewById(R.id.side_listView);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
        ll_ad_side = (LinearLayout) findViewById(R.id.ll_ad_side);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_dra);
        final ArrayList<View> arrayList = new ArrayList<>();

        View view = LayoutInflater.from(this).inflate(R.layout.main_circle, null);
        main_cpu_air_button = (RelativeLayout) view.findViewById(R.id.main_cpu_air_button);
        main_custom_cpu = (CustomRoundCpu) view.findViewById(R.id.main_custom_cpu);
        main_cpu_temp = (TextView) view.findViewById(R.id.main_cpu_temp);
        main_sd_air_button = (RelativeLayout) view.findViewById(R.id.main_sd_air_button);
        main_custom_sd = (CustomRoundCpu) view.findViewById(R.id.main_custom_sd);
        main_sd_per = (TextView) view.findViewById(R.id.main_sd_per);
        main_sd_size = (TextView) view.findViewById(R.id.main_sd_size);
        main_ram_air_button = (RelativeLayout) view.findViewById(R.id.main_ram_air_button);
        main_custom_ram = (CustomRoundCpu) view.findViewById(R.id.main_custom_ram);
        main_ram_per = (TextView) view.findViewById(R.id.main_ram_per);
        main_ram_size = (TextView) view.findViewById(R.id.main_ram_size);
        main_air_all = (LinearLayout) view.findViewById(R.id.main_air_all);


        View adView = CommonUtil.getNativeAdView(TAG_HUA, R.layout.native_ad);
        arrayList.add(view);
        if (adView != null) {
            arrayList.add(adView);
        }
        final ViewPager viewpager = (ViewPager) findViewById(R.id.viewpager);

        viewpager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return arrayList.size();
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(arrayList.get(position), 0);
                return arrayList.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(arrayList.get(position));
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }
        });

        if (adView == null) {
            View pageView = findViewById(R.id.pageindicatorview);
            pageView.setVisibility(View.GONE);
        } else {
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewpager.setCurrentItem(1);
                }
            }, 3000);
        }
        mainPresenter = new MainPresenter(this, this);
        mainPresenter.init();
        mainPresenter.setDrawerLeftEdgeSize(main_drawer, 0.1f);
        initHandler();

        AndroidSdk.track("主页面", "进入主页面", "", 1);
    }

    private void initHandler() {
        handler = new Handler();
    }

    //初始化监听
    public void onClick() {
        //main_scroll_view.setOnTouchListener(scrollViewTouchListener);
        main_pull_refresh.setOnRefreshListener(refreshListener);
        iv_title_right.setOnClickListener(onClickListener);
        iv_title_left.setOnClickListener(onClickListener);
        main_cpu_air_button.setOnClickListener(onClickListener);
        main_sd_air_button.setOnClickListener(onClickListener);
        main_ram_air_button.setOnClickListener(onClickListener);
        main_air_all.setOnClickListener(onClickListener);
        main_junk_button.setOnClickListener(onClickListener);
        main_ram_button.setOnClickListener(onClickListener);
        main_manager_button.setOnClickListener(onClickListener);
        main_cooling_button.setOnClickListener(onClickListener);
        main_applock_button.setOnClickListener(onClickListener);
        main_theme_button.setOnClickListener(onClickListener);
        main_rotate_bad.setOnClickListener(onClickListener);
        main_rotate_good.setOnClickListener(onClickListener);
        main_msg_button.setOnClickListener(onClickListener);

    }

    //初始化中间的高度
    public void initCercleHight() {
        //改变尺寸
        /*dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(dm);*/
        //cercle_linearParams = main_all_cercle.getLayoutParams();
        //int ac = getStatusHeight(this);
        //cercle_linearParams.height = dm.heightPixels - ac - dp2px(56) - dp2px(185) - dp2px(64);
        //main_all_cercle.setLayoutParams(cercle_linearParams);
    }

    @Override
    public void initCpu(final int temp) {
        this.temp = temp;
        main_custom_cpu.startProgress(false, temp);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                main_cpu_temp.setText(String.valueOf(temp) + "℃");
                main_msg_cpu_percent.setText(String.valueOf(temp) + "℃");
            }
        });

    }

    @Override
    public void initSd(final int percent, final String size, final long sd_kongxian) {
        main_custom_sd.startProgress(true, percent);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                main_sd_per.setText(String.valueOf(percent) + "%");
                main_sd_size.setText(size);
                main_msg_sd_percent.setText(CommonUtil.getFileSize2(sd_kongxian));
                if (sd_kongxian < 1024) {
                    main_msg_sd_unit.setText("B");
                } else if (sd_kongxian < 1048576) {
                    main_msg_sd_unit.setText("KB");
                } else if (sd_kongxian < 1073741824) {
                    main_msg_sd_unit.setText("MB");
                } else {
                    main_msg_sd_unit.setText("GB");
                }
            }
        });

    }

    @Override
    public void initRam(final int percent, final String size) {
        main_custom_ram.startProgress(false, percent);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                main_ram_per.setText(String.valueOf(percent) + "%");
                main_ram_size.setText(size);
                main_msg_ram_percent.setText(String.valueOf(percent) + "%");
            }
        });
    }

    @Override
    public void initGuard(int num, RotateAnimation rotateAnimation) {
        if (num != -1) {
            main_gurad_num.setText(String.valueOf(num));
        }
        main_guard_rotate.startAnimation(rotateAnimation);
    }

    @Override
    public void initSideData() {
        if (adapter == null) {
            adapter = new SideAdapter(this);
            side_listView.setAdapter(adapter);
        }
        adapter.clear();
        adapter.addData(new JunkInfo(R.string.side_charging, R.mipmap.side_charging, (boolean) Utils.readData(this, Constants.CHARGE_SAVER_SWITCH, true)));//充电屏保
        adapter.addData(new JunkInfo(R.string.side_float, R.mipmap.side_float, PreData.getDB(this, Contents.FlOAT_SWITCH, true)));//桌面悬浮球
        adapter.addData(new JunkInfo(R.string.side_junk, R.mipmap.side_junk));//垃圾清理
        adapter.addData(new JunkInfo(R.string.side_ram, R.mipmap.side_ram));//内存加速
        adapter.addData(new JunkInfo(R.string.side_manager, R.mipmap.side_manager));//应用管理
        adapter.addData(new JunkInfo(R.string.side_theme, R.mipmap.side_theme));//主题
        adapter.addData(new JunkInfo(R.string.side_setting, R.mipmap.side_setting));//设置
        adapter.addData(new JunkInfo(R.string.side_rotate, R.mipmap.side_rotate));//好评

    }

    @Override
    public void loadAirAnimator(TranslateAnimation translate) {
        main_air_all.startAnimation(translate);
    }

    @Override
    public void setRotateGone() {
        main_rotate_all.setVisibility(View.GONE);
    }

    @Override
    public void openDrawer() {
        main_drawer.openDrawer(GravityCompat.START);
    }

    @Override
    public void closeDrawer() {
        if (main_drawer.isDrawerOpen(GravityCompat.START)) {
            main_drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void loadFullAd() {
        if (PreData.getDB(this, Contents.FULL_MAIN, 0) == 1) {
            AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
        } else {
            View nativeView = CommonUtil.getNativeAdView(TAG_MAIN, R.layout.native_ad);
            if (ll_ad != null && nativeView != null) {
                ViewGroup.LayoutParams layout_ad = ll_ad.getLayoutParams();
                Log.e("aaa", "=====" + layout_ad.height);
                if (nativeView.getHeight() <= CommonUtil.dp2px(250)) {
                    layout_ad.height = CommonUtil.dp2px(250);
                }
                ll_ad.setLayoutParams(layout_ad);
                ll_ad.addView(nativeView);
                main_scroll_view.fullScroll(ScrollView.FOCUS_UP);
            }
            View nativeView_side = CommonUtil.getNativeAdView(TAG_SIDE, R.layout.native_ad);
            if (ll_ad_side != null && nativeView_side != null) {
                ViewGroup.LayoutParams layout_ad = ll_ad_side.getLayoutParams();
                layout_ad.height = nativeView.getMeasuredHeight();
                ll_ad_side.setLayoutParams(layout_ad);
                ll_ad_side.addView(nativeView_side);
            }

        }
    }

    //scrollView 监听
   /* View.OnTouchListener scrollViewTouchListener = new View.OnTouchListener() {
        int state = NOMAL;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Log.e("rqy", "scrollViewTouchListener,v=" + v);
            if (first) {
                cercle_value = cercle_linearParams.height;
                first = false;
            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.e("rqy", "scrollViewTouchListener,ACTION_DOWN");
                    cercleHeight = cercle_linearParams.height;
                    isScroll = main_scroll_view.getScrollY() > 0;
                    firstY = event.getY();
                    Log.e("rqy", "firstY=" + firstY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.e("rqy", "scrollViewTouchListener,ACTION_MOVE--isScroll=" + isScroll);
                    if (isScroll) {
                        break;
                    }
                    float y = event.getY();

                    Log.e("rqy", "firstY=" + firstY + "--y=" + y);
                    float deltaY = firstY - y;// 滑动距离

                    Log.e("rqy", "deltaY=" + deltaY + "--state=" + state);
                    */
    /**
     * 对于初次Touch操作要判断方位：UP OR DOWN
     **//*
                    if (deltaY > 10 && state == NOMAL) {
                        state = UP;
                    } else if (deltaY < -10 && state == NOMAL) {
                        state = DOWN;
                    } else if (deltaY >= -10 && deltaY <= 10 && state == NOMAL) {
                        break;
                    }
                    main_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    if (state == UP) {
                        Log.e("rqy", "scrollViewTouchListener,UP");
                        if (cercleHeight == cercle_value) {
                            if ((int) (cercleHeight - deltaY) > cercle_value) {
                                cercle_linearParams.height = cercle_value;
                            } else if ((int) (cercleHeight - deltaY) < 0) {
                                cercle_linearParams.height = 0;
                            } else {
                                cercle_linearParams.height = (int) (cercleHeight - deltaY);
                            }
                            main_scale_all.setScaleY((float) cercle_linearParams.height / cercle_value);
                            main_scale_all.setScaleX((float) cercle_linearParams.height / cercle_value);
                            main_all_cercle.setLayoutParams(cercle_linearParams);
                            main_scroll_view.setShutTouch(true);
                        } else if (cercleHeight == 0) {
                            main_scroll_view.setShutTouch(false);
                        }
                    } else if (state == DOWN) {
                        Log.e("rqy", "scrollViewTouchListener,DOWN");
                        if (cercleHeight == cercle_value) {
                            main_scroll_view.setShutTouch(true);
                        } else if (cercleHeight == 0) {
                            if (main_scroll_view.getScrollY() > 0) {
                                main_scroll_view.setShutTouch(false);
                                break;
                            }
                            main_scroll_view.setShutTouch(true);
                            if ((int) (cercleHeight - deltaY) > cercle_value) {
                                cercle_linearParams.height = cercle_value;
                            } else if ((int) (cercleHeight - deltaY) < 0) {
                                cercle_linearParams.height = 0;
                            } else {
                                cercle_linearParams.height = (int) (cercleHeight - deltaY);
                            }
                            main_scale_all.setScaleY((float) cercle_linearParams.height / cercle_value);
                            main_scale_all.setScaleX((float) cercle_linearParams.height / cercle_value);
                            main_all_cercle.setLayoutParams(cercle_linearParams);
                        }
                        Log.e("move", "===" + cercleHeight + "===" + cercle_linearParams.height);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    Log.e("rqy", "scrollViewTouchListener,ACTION_UP");
                    if (isScroll) {
                        state = NOMAL;
                        break;
                    }
                    if (state == NOMAL) {
                        break;
                    }
                    Log.e("up", "===" + cercleHeight + "===" + cercle_linearParams.height);
                    if (state == UP) {
                        if (cercleHeight == cercle_value && cercle_linearParams.height != cercleHeight) {
                            if (cercle_linearParams.height < (cercle_value / 2)) {
                                valueAnimator = ValueAnimator.ofInt(cercle_linearParams.height, 0);
                            } else {
                                valueAnimator = ValueAnimator.ofInt(cercle_linearParams.height, cercle_value);
                            }
                            startValueAnimator();
                        }
                    } else {
                        if (cercleHeight == 0 && cercle_linearParams.height != cercleHeight) {
                            if (cercle_linearParams.height < (cercle_value / 2)) {
                                valueAnimator = ValueAnimator.ofInt(cercle_linearParams.height, 0);
                            } else {
                                valueAnimator = ValueAnimator.ofInt(cercle_linearParams.height, cercle_value);
                            }
                            startValueAnimator();
                        }
                    }
                    state = NOMAL;
                    break;
            }
            return false;
        }
    };*/


    //上拉刷新监听
    PullToRefreshLayout.OnRefreshListener refreshListener = new PullToRefreshLayout.OnRefreshListener() {
        // 下拉刷新操作
        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        }

        //上拉加载操作
        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
            AndroidSdk.track("主页面", "刷新成功", "", 1);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
                    main_pull_refresh.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }
            }, 500);
        }
    };


  /*  public void loadValueAnimator(ValueAnimator animation) {
        int hight = (int) animation.getAnimatedValue();
        Log.e("animaterh", "===" + cercleHeight + "===" + hight);
        cercle_linearParams.height = hight;
        //main_all_cercle.setLayoutParams(cercle_linearParams);
        main_scale_all.setScaleY((float) cercle_linearParams.height / );
        main_scale_all.setScaleX((float) cercle_linearParams.height / cercle_value);
    }*/

    //点击事件监听
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_title_left:
                    mainPresenter.openDrawer();
                    AndroidSdk.track("主页面", "点击进入侧边栏按钮", "", 1);
                    break;
                case R.id.iv_title_right:
                    AndroidSdk.track("主页面", "点击进入设置页面", "", 1);
                    mainPresenter.jumpToActivity(SettingActivity.class, 1);
                    break;
                case R.id.main_cpu_air_button:
                    AndroidSdk.track("主页面", "点击cpu球进入硬件信息页面", "", 1);
                    mainPresenter.jumpToActivity(MessageActivity.class, 1);
                    break;
                case R.id.main_sd_air_button:
                    AndroidSdk.track("主页面", "点击sd球进入垃圾清理页面", "", 1);
                    mainPresenter.jumpToActivity(JunkActivity.class, 1);
                    break;
                case R.id.main_ram_air_button:
                    AndroidSdk.track("主页面", "点击ram球进入内存加速页面", "", 1);
                    mainPresenter.jumpToActivity(RamAvtivity.class, 1);
                    break;
                case R.id.main_air_all:
                    AndroidSdk.track("主页面", "点击火箭进入清理所有界面", "", 1);
                    mainPresenter.jumpToActivity(JunkAndRamActivity.class, 1);
                    break;
                case R.id.main_junk_button:
                    AndroidSdk.track("主页面", "点击垃圾清理按钮", "", 1);
                    mainPresenter.jumpToActivity(JunkActivity.class, 1);
                    break;
                case R.id.main_ram_button:
                    AndroidSdk.track("主页面", "点击ram清理按钮", "", 1);
                    mainPresenter.jumpToActivity(RamAvtivity.class, 1);
                    break;
                case R.id.main_manager_button:
                    AndroidSdk.track("主页面", "点击应用管理按钮", "", 1);
                    mainPresenter.jumpToActivity(ManagerActivity.class, 1);
                    break;
                case R.id.main_cooling_button:
                    AndroidSdk.track("主页面", "点击降温按钮", "", 1);
                    mainPresenter.jumpToActivity(CoolingActivity.class, 1);
                    break;
                case R.id.main_applock_button:
                    AndroidSdk.track("主页面", "点击applock按钮", "", 1);
                    Intent intent = new Intent(MainActivity.this, AppLockPatternEosActivity.class);
                    intent.putExtra("is_main", true);
                    startActivity(intent);
//                    mainPresenter.jumpToActivity(CoolingActivity.class, 1);
                    break;
                case R.id.main_theme_button:
                    AndroidSdk.track("主页面", "点击主题按钮", "", 1);
                    mainPresenter.jumpToActivity(ThemeActivity.class, 1);
                    break;
                case R.id.main_rotate_bad:
                    AndroidSdk.track("主页面", "点击好评bad按钮", "", 1);
                    mainPresenter.clickRotate(false);
                    break;
                case R.id.main_rotate_good:
                    AndroidSdk.track("主页面", "点击好评good按钮", "", 1);
                    mainPresenter.clickRotate(true);
                    break;
                case R.id.main_msg_button:
                    mainPresenter.jumpToActivity(MessageActivity.class, 1);
                    break;

            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 50) {
            initSideData();
            adapter.notifyDataSetChanged();
        }
        if (resultCode == 2) {
            int wendu = data.getIntExtra("wendu", 0);
            temp -= wendu;
            if (temp == 0) {
                temp = 40;
            }
            initCpu(temp);
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mainPresenter.reStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        AndroidSdk.onResumeWithoutTransition(this);
    }

    public void onBackPressed() {
        if (main_drawer.isDrawerOpen(GravityCompat.START)) {
            main_drawer.closeDrawer(GravityCompat.START);
        } else if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(this, getString(R.string.main_back_pressed), Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();

        } else {
            super.onBackPressed();
        }
    }


}
