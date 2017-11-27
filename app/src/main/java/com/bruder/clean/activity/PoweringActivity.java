package com.bruder.clean.activity;

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
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.bruder.clean.customeview.PowerWidgetContainerFrameLayout;
import com.bruder.clean.junk.R;
import com.bruder.clean.myservice.NotificationingService;
import com.bruder.clean.myview.WidgetContainer;
import com.bruder.clean.util.Constant;
import com.bruder.clean.util.UtilAd;
import com.cleaner.entity.JunkInfo;
import com.cleaner.heart.CleanManager;
import com.cleaner.powerclean.CustomerAccessibilitingService;
import com.cleaner.util.DataPre;
import com.cleaner.util.LoadManager;
import com.cleaner.util.Util;
import com.sample.lottie.LottieAnimationView;

import java.util.ArrayList;

/**
 */

public class PoweringActivity extends BaseActivity {

    TextView power_size;
    Button junk_button_clean;
    Handler mHandler;
    FrameLayout title_left;
    TextView title_name;
    RecyclerView power_recycler;
    private GridLayoutManager mLayoutManager;
    private HomeAdapter homeAdapter, containerAdapter;
    private MyApplication cleanApplication;
    private Button containerView_junk_button_clean;
    private PowerWidgetContainerFrameLayout container;
    private int count = 0;
    LottieAnimationView power_clean;
    private ArrayList<JunkInfo> startList;
    private View containerView;
    private RecyclerView containerView_recyclerView;
    private TextView containerView_power_size;
    private WidgetContainer widgetContainer;
    private AlertDialog dialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_power);
        AndroidSdk.loadFullAd(UtilAd.DEFAULT_FULL, null);
        registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        power_clean = (LottieAnimationView) findViewById(R.id.power_clean);
        power_clean.setAnimation("clean.json");
        power_clean.setScaleX(2f);
        power_clean.setScaleY(2f);
        power_clean.loop(true);
        power_clean.playAnimation();
        mHandler = new Handler();
        startService(new Intent(this, CustomerAccessibilitingService.class).putExtra("isDis", false));
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
                UtilAd.track("深度清理页面", "点击清理", "", 1);
                if (!Util.isAccessibilitySettingsOn(PoweringActivity.this)) {
                    permissIntent();
                    return;
                }
                junk_button_clean.setOnClickListener(null);
//                showPackageDetail(startList.get(0).packageName);
                containerView = View.inflate(PoweringActivity.this, R.layout.layout_power, null);
                container = new PowerWidgetContainerFrameLayout.Builder()
                        .setHeight(PowerWidgetContainerFrameLayout.MATCH_PARENT)
                        .setWidth(PowerWidgetContainerFrameLayout.MATCH_PARENT)
                        .setOrientation(PowerWidgetContainerFrameLayout.PORTRAIT)
                        .build(PoweringActivity.this.getApplicationContext());
                container.addView(containerView, container.makeLayoutParams(PowerWidgetContainerFrameLayout.MATCH_PARENT,
                        PowerWidgetContainerFrameLayout.MATCH_PARENT, Gravity.CENTER));
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
            title_name.setText(R.string.gboost_11);
            for (JunkInfo info : startList) {
                if (TextUtils.equals(info.pkg, getIntent().getStringExtra("packageName"))) {
                    startList.remove(info);
                    break;
                }
            }
            if (startList.size() == 0) {
                if (!TextUtils.isEmpty(getIntent().getStringExtra("packageName"))) {
                    Util.doStartApplicationWithPackageName(PoweringActivity.this, getIntent().getStringExtra("packageName"));
                    if (DataPre.getDB(PoweringActivity.this, Constant.TONGZHILAN_SWITCH, true)) {
                        Intent intent = new Intent(PoweringActivity.this, NotificationingService.class);
                        intent.putExtra("from", "gboost");
                        startService(intent);
                    }
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("count", count);
                    bundle.putString("from", "GBoost");
                    jumpToActivity(SuccessingActivity.class, bundle, 1);
                }
                onBackPressed();
                return;
            }
            junk_button_clean.callOnClick();
        }
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
        power_recycler = (RecyclerView) findViewById(R.id.power_recycler);
        power_size = (TextView) findViewById(R.id.power_size);
        junk_button_clean = (Button) findViewById(R.id.junk_button_clean);

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
            junk_button_clean.setTextColor(ContextCompat.getColor(PoweringActivity.this, R.color.main_circle_backg));
            power_size.setText(getString(R.string.power_1, 0 + "") + " ");
            homeAdapter.notifyDataSetChanged();
            if (TextUtils.equals("GBoost", getIntent().getStringExtra("from"))) {
                if (!TextUtils.isEmpty(getIntent().getStringExtra("packageName"))) {
                    Util.doStartApplicationWithPackageName(PoweringActivity.this, getIntent().getStringExtra("packageName"));
                    if (DataPre.getDB(PoweringActivity.this, Constant.TONGZHILAN_SWITCH, true)) {
                        Intent intent = new Intent(PoweringActivity.this, NotificationingService.class);
                        intent.putExtra("from", "gboost");
                        startService(intent);
                    }

                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("count", count);
                    bundle.putString("from", "GBoost");
                    jumpToActivity(SuccessingActivity.class, bundle, 1);
                }
                onBackPressed();
            } else {
                Bundle bundle = new Bundle();
                bundle.putInt("count", count);
                bundle.putString("from", "power");
                jumpToActivity(SuccessingActivity.class, bundle, 1);
            }
            container.removeFromWindow();
        }
    };

    private void setContainer() {
        containerView_recyclerView = (RecyclerView) containerView.findViewById(R.id.power_recycler);
        containerView_power_size = (TextView) containerView.findViewById(R.id.power_size);
        containerView_junk_button_clean = (Button) containerView.findViewById(R.id.junk_button_clean);
        containerView_power_size.setText(getString(R.string.power_1, startList.size() + "") + " ");
        containerView_junk_button_clean.setVisibility(View.GONE);
        containerView_recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        containerView_recyclerView.setAdapter(containerAdapter = new HomeAdapter(true));
        // 设置item动画
        containerView_recyclerView.setItemAnimator(new DefaultItemAnimator());
        containerView_junk_button_clean = (Button) containerView.findViewById(R.id.junk_button_clean);
        containerView_junk_button_clean.setVisibility(View.GONE);
        containerView_recyclerView.setAdapter(containerAdapter = new HomeAdapter(true));
        // 设置item动画
        containerView_recyclerView.setItemAnimator(new DefaultItemAnimator());
        mHandler.postDelayed(runnable, 100);

    }

    private void showPackageDetail(String packageName) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", packageName, null);
        intent.setData(uri);
        startActivity(intent);
    }

    private void animatorView(final View view, final int i) {
        final AnimatorSet set = new AnimatorSet();
        View icon = view.findViewById(R.id.recyc_iconf);

        ObjectAnimator translationX = ObjectAnimator.ofFloat(icon, "translationX",
                0, -getResources().getDimensionPixelSize(R.dimen.d3), 0, getResources().getDimensionPixelSize(R.dimen.d2), 0,
                0, -getResources().getDimensionPixelSize(R.dimen.d3), 0, getResources().getDimensionPixelSize(R.dimen.d2), 0,
                0, -getResources().getDimensionPixelSize(R.dimen.d3), 0, getResources().getDimensionPixelSize(R.dimen.d2), 0,
                0, -getResources().getDimensionPixelSize(R.dimen.d3), 0, getResources().getDimensionPixelSize(R.dimen.d2), 0,
                0, -getResources().getDimensionPixelSize(R.dimen.d3), 0, getResources().getDimensionPixelSize(R.dimen.d2), 0
        );
        ObjectAnimator alpha = ObjectAnimator.ofFloat(icon, "alpha", 1f, 0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(icon, "scaleY", 1f, 0f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(icon, "scaleX", 1f, 0f);
        set.setDuration(1000)
                .addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        CleanManager.getInstance(PoweringActivity.this).removeRamSelfBoot(startList.get(i));
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
        set.play(alpha).with(scaleX).with(scaleY);
        translationX.setDuration(1000);
        translationX.setInterpolator(new LinearInterpolator());
        translationX.start();
        translationX.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                set.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void initData() {
        cleanApplication = (MyApplication) getApplication();
        startList = new ArrayList<>();
        for (JunkInfo info : CleanManager.getInstance(this).getAppRamList()) {
            if (info.isSelfBoot) {
                startList.add(info);
            }
        }
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
        boolean isWidget;

        public HomeAdapter(boolean isWidget) {
            this.isWidget = isWidget;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    PoweringActivity.this).inflate(R.layout.layout_power_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            if (isWidget) {
                holder.recyc_check.setVisibility(View.GONE);
            } else {
                holder.recyc_check.setImageResource(startList.get(position).isChecked ? R.mipmap.ram_passed : R.mipmap.ram_normal);
            }

            holder.recyc_icon.setImageDrawable(LoadManager.getInstance(PoweringActivity.this).getAppIcon(startList.get(position).pkg));
            holder.recyc_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startList.get(position).isChecked = !startList.get(position).isChecked;
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return startList.size();
        }

        public void reChangesData(int position) {

            notifyItemRangeChanged(position, startList.size() - position); //mList是数据

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
    public void onBackPressed() {
        setResult(Constant.POWER_RESUIL);
        finish();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        startService(new Intent(this, CustomerAccessibilitingService.class).putExtra("isDis", true));
        super.onDestroy();

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
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                View view = LayoutInflater.from(PoweringActivity.this).inflate(R.layout.layout_power_promiss, null);
                widgetContainer = new WidgetContainer(PoweringActivity.this.getApplicationContext(), Gravity.NO_GRAVITY, WindowManager.LayoutParams.MATCH_PARENT,
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
        }, 1500);
    }

    private void initClean() {
        try {
            power_clean.setAnimation(this, "theme://clean.json");
        } catch (Exception e) {
            if (!power_clean.isAnimating()) {
                power_clean.setAnimation(null, "clean.json");
            }
        }
        power_clean.loop(true);
//        water.setSpeed(5.0f);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            onBackPressed();
        }
        if (requestCode == 100) {
            if (Util.isAccessibilitySettingsOn(PoweringActivity.this)) {
                junk_button_clean.callOnClick();
            } else {
                showDialogPermiss();
            }
        }
    }

    private void showDialogPermiss() {
        View view = View.inflate(this, R.layout.dialog_power, null);
        TextView exit_queren = (TextView) view.findViewById(R.id.bt_queren);
        TextView exit_quxiao = (TextView) view.findViewById(R.id.bt_quxiao);
        ImageView iv_cha = (ImageView) view.findViewById(R.id.iv_cha);
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
    }
}
