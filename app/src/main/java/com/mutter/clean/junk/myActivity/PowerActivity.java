package com.mutter.clean.junk.myActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mutter.clean.core.CleanManager;
import com.mutter.clean.deepclean.CustomerAccessibilityService;
import com.mutter.clean.util.LoadManager;
import com.mutter.clean.util.PreData;
import com.mutter.clean.util.Util;
import com.android.client.AndroidSdk;
import com.mutter.clean.junk.R;
import com.mutter.clean.junk.myview.PowerWidgetContainer;
import com.mutter.clean.entity.JunkInfo;
import com.mutter.clean.junk.service.NotificationService;
import com.mutter.clean.junk.util.AdUtil;
import com.mutter.clean.junk.util.Constant;

import java.util.ArrayList;

/**
 */

public class PowerActivity extends BaseActivity {
    private LinearLayoutManager mLayoutManager;
    private MyApplication cleanApplication;
    private View containerView;
    Button junk_button_clean;
    ImageView power_check;
    TextView title_name;
    FrameLayout title_left;
    private Button containerView_junk_button_clean;
    Handler mHandler;
    RecyclerView power_recycler;
    private HomeAdapter homeAdapter, containerAdapter;
    private ArrayList<JunkInfo> startList;
    private RecyclerView containerView_recyclerView;
    private PowerWidgetContainer container;
    private int count = 0;


    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        power_recycler = (RecyclerView) findViewById(R.id.power_recycler);
        junk_button_clean = (Button) findViewById(R.id.junk_button_clean);
        power_check = (ImageView) findViewById(R.id.power_check);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_power);
        AndroidSdk.loadFullAd(AndroidSdk.FULL_TAG_PAUSE);
        mHandler = new Handler();
        startService(new Intent(this, CustomerAccessibilityService.class).putExtra("isDis", false));
        initData();
        title_name.setText(R.string.side_power);
        mLayoutManager = new LinearLayoutManager(this);//设置为一个4列的纵向网格布局
        power_recycler.setLayoutManager(mLayoutManager);
        power_recycler.setAdapter(homeAdapter = new HomeAdapter(false));
        // 设置item动画
        power_recycler.setItemAnimator(new DefaultItemAnimator());
        junk_button_clean.setOnClickListener(clickListener);
        power_check.setOnClickListener(clickListener);
        title_left.setOnClickListener(clickListener);

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

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_left:
                    onBackPressed();
                    break;
                case R.id.power_check:
                    AdUtil.track("深度清理页面", "开启辅助功能", "", 1);
                    try {
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        startActivityForResult(intent, 100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent transintent = new Intent(PowerActivity.this, ShowPermissionActivity.class);
                            startActivity(transintent);
                        }
                    }, 1500);
                    break;
                case R.id.junk_button_clean:
                    AdUtil.track("深度清理页面", "点击清理", "", 1);
                    if (!Util.isAccessibilitySettingsOn(PowerActivity.this)) {
                        try {
                            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                            startActivityForResult(intent, 100);
                        } catch (Exception e) {
                            e.printStackTrace();
                            AdUtil.track("深度清理页面", "进入辅助功能失败:" + Build.MODEL, "", 1);
                        }

                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent transintent = new Intent(PowerActivity.this, ShowPermissionActivity.class);
                                startActivity(transintent);
                            }
                        }, 1500);
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

                    break;

            }
        }
    };

    private void setContainer() {
        containerView_recyclerView = (RecyclerView) containerView.findViewById(R.id.power_recycler);
        containerView_junk_button_clean = (Button) containerView.findViewById(R.id.junk_button_clean);
        containerView_junk_button_clean.setVisibility(View.GONE);
        containerView_recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
        View recyc_item = view.findViewById(R.id.recyc_item);
        ObjectAnimator translationX = ObjectAnimator.ofFloat(recyc_item, "translationX", 0f, getResources().getDimensionPixelOffset(R.dimen.d333));
        set.setDuration(1000)
                .addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        CleanManager.getInstance(PowerActivity.this).removeRamSelfBoot(startList.get(i));
                        startList.remove(i);
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
        set.play(translationX);
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
            holder.junk_item_lable.setText(LoadManager.getInstance(PowerActivity.this).getAppLabel(startList.get(position).pkg));
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
            RelativeLayout recyc_item;
            ImageView recyc_icon;
            ImageView recyc_check;
            TextView junk_item_lable;

            public MyViewHolder(View view) {
                super(view);
                recyc_item = (RelativeLayout) view.findViewById(R.id.recyc_item);
                recyc_icon = (ImageView) view.findViewById(R.id.recyc_icon);
                recyc_check = (ImageView) view.findViewById(R.id.recyc_check);
                junk_item_lable = (TextView) view.findViewById(R.id.junk_item_lable);
            }
        }
    }

    @Override
    protected void onDestroy() {
        startService(new Intent(this, CustomerAccessibilityService.class).putExtra("isDis", true));
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        setResult(Constant.POWER_RESUIL);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Util.isAccessibilitySettingsOn(this)) {
            power_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            power_check.setImageResource(R.mipmap.side_check_normal);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            onBackPressed();
        }
        if (requestCode == 100) {
            if (Util.isAccessibilitySettingsOn(PowerActivity.this)) {
                junk_button_clean.callOnClick();
            }
        }
    }
}
