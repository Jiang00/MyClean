package com.bruder.clean.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.bruder.clean.customeview.PowerWidgetContainerFrameLayout;
import com.bruder.clean.junk.R;
import com.bruder.clean.myservice.NotificationingService;
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
    FrameLayout title_left;
    Button junk_button_clean;
    Handler mHandler;
    private TextView containerView_power_size;
    private Button containerView_junk_button_clean;
    private PowerWidgetContainerFrameLayout container;
    private int count = 0;
    LottieAnimationView power_clean;
    private GridLayoutManager mLayoutManager;
    private HomeAdapter homeAdapter, containerAdapter;
    private MyApplication cleanApplication;
    TextView title_name;
    RecyclerView power_recycler;
    TextView power_size;
    private ArrayList<JunkInfo> startList;
    private View containerView;
    private RecyclerView containerView_recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_power);
        AndroidSdk.loadFullAd(AndroidSdk.FULL_TAG_PAUSE);
        power_clean = (LottieAnimationView) findViewById(R.id.power_clean);
        power_clean.setAnimation("clean.json");
        power_clean.setScaleX(2f);
        power_clean.setScaleY(2.4f);
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
                    try {
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        startActivityForResult(intent, 100);
                    } catch (Exception e) {
                        e.printStackTrace();
                        UtilAd.track("深度清理页面", "进入辅助功能失败:" + Build.MODEL, "", 1);
                    }

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent transintent = new Intent(PoweringActivity.this, JurisdictionActivity.class);
                            startActivity(transintent);
                        }
                    }, 1500);
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
                        intent.setAction("gboost");
                        //startService
                        startService(intent);
                    }
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("count", count);
                    bundle.putString("from", "GBoost");
                    power_clean.cancelAnimation();
                    jumpToActivity(SuccessingActivity.class, bundle, 1);
                }
                onBackPressed();
                return;
            }
            junk_button_clean.callOnClick();
        }
    }

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
        mHandler.postDelayed(runnable, 100);
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
                        intent.setAction("gboost");
//                        startService(intent);
                    }

                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("count", count);
                    bundle.putString("from", "GBoost");
                    power_clean.cancelAnimation();
                    jumpToActivity(SuccessingActivity.class, bundle, 1);
                }
                onBackPressed();
            } else {
                Bundle bundle = new Bundle();
                bundle.putInt("count", count);
                bundle.putString("from", "power");
                power_clean.cancelAnimation();
                jumpToActivity(SuccessingActivity.class, bundle, 1);
            }
            container.removeFromWindow();
        }
    };


    private void showPackageDetail(String packageName) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", packageName, null);
        intent.setData(uri);
        startActivity(intent);
    }

    private void animatorView(final View view, final int i) {
        AnimatorSet set = new AnimatorSet();
        View icon = view.findViewById(R.id.recyc_icon);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(icon, "scaleX", 1f, 0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(icon, "scaleY", 1f, 0f);
        ObjectAnimator rotate = ObjectAnimator.ofFloat(icon, "rotation", 0f, 360f);
        set.setDuration(2000).addListener(new Animator.AnimatorListener() {
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
//                        mHandler.post(runnable);
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
    public void onBackPressed() {
        power_clean.cancelAnimation();
        setResult(Constant.POWER_RESUIL);
        finish();
    }

    @Override
    protected void onDestroy() {
        startService(new Intent(this, CustomerAccessibilitingService.class).putExtra("isDis", true));
        super.onDestroy();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            onBackPressed();
        }
        if (requestCode == 100) {
            if (Util.isAccessibilitySettingsOn(PoweringActivity.this)) {
                junk_button_clean.callOnClick();
            }
        }
    }
}
