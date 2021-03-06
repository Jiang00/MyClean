package com.supers.clean.junk.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.clean.core.CleanManager;
import com.android.clean.powerclean.CustomerAccessibilityService;
import com.android.clean.util.LoadManager;
import com.android.clean.util.PreData;
import com.android.clean.util.Util;
import com.android.client.AndroidSdk;
import com.supers.clean.junk.R;
import com.supers.clean.junk.customeview.PowerWidgetContainer;
import com.android.clean.entity.JunkInfo;
import com.supers.clean.junk.customeview.WidgetContainer;
import com.supers.clean.junk.service.NotificationService;
import com.supers.clean.junk.util.AdUtil;
import com.android.clean.util.Constant;
import com.supers.clean.junk.util.BadgerCount;

import java.util.ArrayList;

/**
 * Created by Ivy on 2017/4/7.
 */

public class PowerActivity extends BaseActivity {
    FrameLayout title_left;
    TextView title_name;
    RecyclerView power_recycler;
    TextView power_size;
    Button junk_button_clean;
    Handler mHandler;
    private GridLayoutManager mLayoutManager;
    private HomeAdapter homeAdapter, containerAdapter;
    private MyApplication cleanApplication;
    private ArrayList<JunkInfo> startList;
    private View containerView;
    private RecyclerView containerView_recyclerView;
    private TextView containerView_power_size;
    private Button containerView_junk_button_clean;
    private PowerWidgetContainer container;
    private int count = 0;
    private LinearLayout ll_ad;
    private ArrayList<View> viewList;
    ViewPager view_pager;
    private AlertDialog dialog;

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        power_recycler = (RecyclerView) findViewById(R.id.power_recycler);

        junk_button_clean = (Button) findViewById(R.id.junk_button_clean);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
        view_pager = (ViewPager) findViewById(R.id.view_pager);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_power);
        registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        if (!PreData.getDB(this, Constant.BILL_YOUXIAO, true)) {
            AndroidSdk.loadFullAd(AdUtil.DEFAULT, null);
        }
        mHandler = new Handler();
        startService(new Intent(this, CustomerAccessibilityService.class).putExtra("isDis", false));
        initData();
        title_name.setText(R.string.side_power);

        power_size.setText(getString(R.string.power_1, startList.size() + "") + " ");
        mLayoutManager = new GridLayoutManager(this, 4);//设置为一个4列的纵向网格布局
        power_recycler.setLayoutManager(mLayoutManager);
        power_recycler.setAdapter(homeAdapter = new HomeAdapter(false));
        // 设置item动画
        power_recycler.setItemAnimator(new DefaultItemAnimator());
        junk_button_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdUtil.track("深度清理页面", "点击清理", "", 1);
                if (!Util.isAccessibilitySettingsOn(PowerActivity.this)) {
                    permissIntent();
                    return;
                }
                junk_button_clean.setOnClickListener(null);
//                showPackageDetail(startList.get(0).packageName);
                containerView = View.inflate(PowerActivity.this, R.layout.layout_power, null);
                container = new PowerWidgetContainer.Builder()
                        .setHeight(PowerWidgetContainer.MATCH_PARENT)
                        .setWidth(PowerWidgetContainer.MATCH_PARENT)
                        .setOrientation(PowerWidgetContainer.PORTRAIT)
                        .build(PowerActivity.this.getApplicationContext());
                container.addView(containerView, container.makeLayoutParams(PowerWidgetContainer.MATCH_PARENT,
                        PowerWidgetContainer.MATCH_PARENT, Gravity.CENTER));
                container.addToWindow();
                setContainer();

            }
        });
        title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (TextUtils.equals("GBoost", getIntent().getStringExtra("from"))) {
//            title_name.setText(R.string.gboost_11);
            for (JunkInfo info : startList) {
                if (TextUtils.equals(info.pkg, getIntent().getStringExtra("packageName"))) {
                    startList.remove(info);
                    break;
                }
            }
            if (startList.size() == 0) {
                if (!TextUtils.isEmpty(getIntent().getStringExtra("packageName"))) {
                    Util.doStartApplicationWithPackageName(PowerActivity.this, getIntent().getStringExtra("packageName"));
                    if (PreData.getDB(PowerActivity.this, Constant.TONGZHILAN_SWITCH, true)) {
                        Intent intent = new Intent(PowerActivity.this, NotificationService.class);
                        intent.putExtra("from", "gboost");
                        startService(intent);
                    }
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("count", count);
                    bundle.putString("from", "GBoost");
                    jumpToActivity(SuccessActivity.class, bundle, 1);
                }
                onBackPressed();
                return;
            }
            junk_button_clean.callOnClick();
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                if (widgetContainer != null) {
                    widgetContainer.removeFromWindow();
                }
            }
        }
    };

    public void permissIntent() {
        try {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivityForResult(intent, 100);
        } catch (Exception e) {
            e.printStackTrace();
            AdUtil.track("深度清理页面", "进入辅助功能失败:" + Build.MODEL, "", 1);
        }
        mHandler.postDelayed(runnable_acc, 1500);
    }

    private WidgetContainer widgetContainer;
    Runnable runnable_acc = new Runnable() {
        @Override
        public void run() {
            View view = LayoutInflater.from(PowerActivity.this).inflate(R.layout.layout_permission_power, null);
            widgetContainer = new WidgetContainer(PowerActivity.this.getApplicationContext(), Gravity.NO_GRAVITY, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT, true);
            widgetContainer.addView(view);
            widgetContainer.addToWindow();
            widgetContainer.setWidgetListener(new WidgetContainer.IWidgetListener() {
                @Override
                public boolean onBackPressed() {
                    return false;
                }

                @Override
                public boolean onMenuPressed() {
                    return false;
                }

                @Override
                public void onClick() {
                    widgetContainer.removeFromWindow();
                }
            });
        }
    };

    private void setContainer() {
        containerView_recyclerView = (RecyclerView) containerView.findViewById(R.id.power_recycler);
        containerView_power_size = (TextView) containerView.findViewById(R.id.power_size);
        containerView.findViewById(R.id.view_con).setVisibility(View.VISIBLE);
        containerView.findViewById(R.id.view_pager).setVisibility(View.GONE);
        TextView name = (TextView) containerView.findViewById(R.id.title_name);
        containerView_junk_button_clean = (Button) containerView.findViewById(R.id.junk_button_clean);
        name.setText(R.string.side_power);
        containerView_power_size.setText(getString(R.string.power_1, startList.size() + "") + " ");
        containerView_junk_button_clean.setVisibility(View.GONE);
        containerView_recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        containerView_recyclerView.setAdapter(containerAdapter = new HomeAdapter(true));
        // 设置item动画
        containerView_recyclerView.setItemAnimator(new DefaultItemAnimator());
        mHandler.postDelayed(runnable, 100);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < startList.size(); i++) {
                if (startList.get(i).isChecked) {
                    count++;
                    showPackageDetail(startList.get(i).pkg);

                    View view = containerView_recyclerView.getChildAt(i);
                    if (view == null) {
                        return;
                    }
                    animatorView(view, i);
                    return;
                }
            }
            junk_button_clean.setBackgroundResource(R.drawable.shape_button_ffffff);
            junk_button_clean.setTextColor(ContextCompat.getColor(PowerActivity.this, R.color.main_circle_backg));
            power_size.setText(getString(R.string.power_1, 0 + "") + " ");
            homeAdapter.notifyDataSetChanged();
            if (TextUtils.equals("GBoost", getIntent().getStringExtra("from"))) {
                if (!TextUtils.isEmpty(getIntent().getStringExtra("packageName"))) {
                    Util.doStartApplicationWithPackageName(PowerActivity.this, getIntent().getStringExtra("packageName"));
                    if (PreData.getDB(PowerActivity.this, Constant.TONGZHILAN_SWITCH, true)) {
                        Intent intent = new Intent(PowerActivity.this, NotificationService.class);
                        intent.putExtra("from", "gboost");
                        startService(intent);
                    }

                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("count", count);
                    bundle.putString("from", "GBoost");
                    jumpToActivity(SuccessActivity.class, bundle, 1);
                }
                onBackPressed();
            } else {
                Bundle bundle = new Bundle();
                bundle.putInt("count", count);
                bundle.putString("from", "power");
                jumpToActivity(SuccessActivity.class, bundle, 1);
            }
            container.removeFromWindow();
        }
    };

    private void animatorView(final View view, final int i) {
        AnimatorSet set = new AnimatorSet();
        View icon = view.findViewById(R.id.recyc_icon);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(icon, "scaleX", 1f, 0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(icon, "scaleY", 1f, 0f);
        ObjectAnimator rotate = ObjectAnimator.ofFloat(icon, "rotation", 0f, 360f);
        set.setDuration(2000)
                .addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        CleanManager.getInstance(PowerActivity.this).removeRamSelfBoot(startList.get(i));
                        startList.remove(i);
                        containerView_power_size.setText(getString(R.string.power_1, startList.size() + "") + " ");
                        containerAdapter.notifyItemRemoved(i);
                        containerAdapter.reChangesData(i);
                        mHandler.post(runnable);
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                    }
                });
        set.play(scaleX).with(scaleY).with(rotate);
        set.start();
    }

    private void showPackageDetail(String packageName) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", packageName, null);
        intent.setData(uri);
        startActivity(intent);
    }

    private void initData() {
        cleanApplication = (MyApplication) getApplication();
        startList = new ArrayList<>();
        for (JunkInfo info : CleanManager.getInstance(this).getAppRamList()) {
            Log.e("deep", "sss===1");
            if (info.isSelfBoot) {
                Log.e("deep", "sss===2");
                startList.add(info);
            }
        }
        viewList = new ArrayList<>();
        View layout_deep_ad = LayoutInflater.from(this).inflate(R.layout.layout_power_pager, null);
        power_size = (TextView) layout_deep_ad.findViewById(R.id.power_size);
        viewList.add(layout_deep_ad);
        View nativeView = AdUtil.getNativeAdView(this, "", R.layout.native_ad_3);
        if (nativeView != null) {
            viewList.add(nativeView);
        }
        view_pager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewList.get(position), 0);
                return viewList.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                View view = (View) object;
                container.removeView(view);
                view = null;
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }


            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }


        });
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                view_pager.setCurrentItem(1);
            }
        }, 2000);
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
        boolean isWidget;

        public HomeAdapter(boolean isWidget) {
            this.isWidget = isWidget;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    PowerActivity.this).inflate(R.layout.layout_power_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            if (isWidget) {
                holder.recyc_check.setVisibility(View.GONE);
            } else {
                holder.recyc_check.setImageResource(startList.get(position).isChecked ? R.mipmap.power_4 : R.mipmap.power_5);
            }

            holder.recyc_icon.setImageDrawable(LoadManager.getInstance(PowerActivity.this).getAppIcon(startList.get(position).pkg));
            holder.recyc_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startList.get(position).isChecked = !startList.get(position).isChecked;
                    notifyDataSetChanged();
                }
            });
        }

        public void reChangesData(int position) {

            notifyItemRangeChanged(position, startList.size() - position); //mList是数据

        }

        @Override
        public int getItemCount() {
            return startList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            FrameLayout recyc_item;
            ImageView recyc_icon;
            ImageView recyc_check;

            public MyViewHolder(View view) {
                super(view);
                recyc_item = (FrameLayout) view.findViewById(R.id.recyc_item);
                recyc_icon = (ImageView) view.findViewById(R.id.recyc_icon);
                recyc_check = (ImageView) view.findViewById(R.id.recyc_check);
            }
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        startService(new Intent(this, CustomerAccessibilityService.class).putExtra("isDis", true));
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        setResult(Constant.POWER_RESUIL);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            onBackPressed();
        }
        if (requestCode == 100) {
            if (mHandler != null) {
                mHandler.removeCallbacks(runnable_acc);
            }
            if (Util.isAccessibilitySettingsOn(PowerActivity.this)) {
                junk_button_clean.callOnClick();
            } else {
                showDialogPermiss();
            }
        }
    }

    private void showDialogPermiss() {
        View view = View.inflate(this, R.layout.dialog_permiss, null);
        LinearLayout ll_ad_exit = (LinearLayout) view.findViewById(R.id.ll_ad_exit);
        TextView exit_queren = (TextView) view.findViewById(R.id.exit_queren);
        TextView exit_quxiao = (TextView) view.findViewById(R.id.exit_quxiao);
        if (PreData.getDB(this, Constant.NATIVE_EXIT, 0) == 1) {
            View nativeExit = AdUtil.getNativeAdViewV(this, "", R.layout.native_ad_2);
            if (nativeExit != null) {
//                ll_ad_exit.addView(nativeExit);
//                ll_ad_exit.setVisibility(View.INVISIBLE);
            }
        }
        exit_queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissIntent();
                dialog.dismiss();

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
        if (ll_ad_exit.getVisibility() == View.INVISIBLE) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(ll_ad_exit, View.TRANSLATION_Y, -getResources().getDimensionPixelOffset(R.dimen.d280), 0);
            animator.setDuration(600);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.start();
            ll_ad_exit.setVisibility(View.VISIBLE);
        }
    }
}
