package com.myboost.junk.boostactivity;

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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.myboost.clean.core.CleanManager;
import com.myboost.clean.deepclean.MyServiceCustomerAccessibility;
import com.myboost.clean.entity.JunkInfo;
import com.myboost.clean.utilsprivacy.LoadManager;
import com.myboost.clean.utilsprivacy.MyUtils;
import com.myboost.clean.utilsprivacy.PreData;
import com.myboost.junk.R;
import com.myboost.junk.boosttools.BoostMyConstant;
import com.myboost.junk.boosttools.SetAdUtilPrivacy;
import com.myboost.junk.customviewboost.BoostDeepWidgetContainer;
import com.myboost.junk.servicesboost.NotificationServiceBoost;

import java.util.ArrayList;

/**
 */

public class BoostDeepingActivity extends BaseActivity {
    private GridLayoutManager mLayoutManager;
    Handler mHandler;
    private HomeAdapter homeAdapter, containerAdapter;
    private ArrayList<JunkInfo> startList;
    private View containerView;
    private boolean flag = false;
    private ImageView power_1, power_2, power_3, power_4;
    private ObjectAnimator animator1, animator2, animator3, animator4;
    AnimatorSet animatorSet;
    TextView title_name;
    private BoostDeepWidgetContainer container;
    private RecyclerView containerView_recyclerView;
    private TextView containerView_junk_button_clean;
    ImageView gboost_power_check;
    private MyApplication cleanApplication;
    private int count = 0;
    RecyclerView power_recycler;
    TextView junk_button_clean;
    FrameLayout title_left;


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
            homeAdapter.notifyDataSetChanged();
            if (TextUtils.equals("GBoost", getIntent().getStringExtra("from"))) {
                if (!TextUtils.isEmpty(getIntent().getStringExtra("packageName"))) {
                    MyUtils.doStartApplicationWithPackageName(BoostDeepingActivity.this, getIntent().getStringExtra("packageName"));
                    if (PreData.getDB(BoostDeepingActivity.this, BoostMyConstant.TONGZHILAN_SWITCH, true)) {
                        Intent intent = new Intent(BoostDeepingActivity.this, NotificationServiceBoost.class);
                        intent.setAction("gboost");
                        startService(intent);
                    }

                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("count", count);
                    bundle.putString("from", "GBoost");
                    jumpToActivity(SucceedActivityBoost.class, bundle, 1);
                }
                onBackPressed();
            } else {
                Bundle bundle = new Bundle();
                bundle.putInt("count", count);
                bundle.putString("from", "power");
                jumpToActivity(SucceedActivityBoost.class, bundle, 1);
            }
            container.removeFromWindow();
        }
    };

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        power_recycler = (RecyclerView) findViewById(R.id.power_recycler);
        junk_button_clean = (TextView) findViewById(R.id.junk_button_clean);
        gboost_power_check = (ImageView) findViewById(R.id.gboost_power_check);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_power);
        AndroidSdk.loadFullAd(AndroidSdk.FULL_TAG_PAUSE);
        mHandler = new Handler();
        if (MyUtils.isAccessibilitySettingsOn(this)) {
            gboost_power_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            gboost_power_check.setImageResource(R.mipmap.side_check_normal);
        }
        gboost_power_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetAdUtilPrivacy.track("深度清理页面", "开启辅助功能", "", 1);
                try {
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivityForResult(intent, 100);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent transintent = new Intent(BoostDeepingActivity.this, BoostPermissingActivity.class);
                        startActivity(transintent);
                    }
                }, 1500);
            }
        });
        startService(new Intent(this, MyServiceCustomerAccessibility.class).putExtra("isDis", false));
        initData();
        title_name.setText(R.string.side_power);
        mLayoutManager = new GridLayoutManager(this, 1);//设置为一个4列的纵向网格布局
        power_recycler.setLayoutManager(mLayoutManager);
        power_recycler.setAdapter(homeAdapter = new HomeAdapter(false));

        // 设置item动画
        power_recycler.setItemAnimator(new DefaultItemAnimator());
        junk_button_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = true;
                SetAdUtilPrivacy.track("深度清理页面", "点击清理", "", 1);
                if (!MyUtils.isAccessibilitySettingsOn(BoostDeepingActivity.this)) {
                    try {
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        startActivityForResult(intent, 100);
                    } catch (Exception e) {
                        e.printStackTrace();
                        SetAdUtilPrivacy.track("深度清理页面", "进入辅助功能失败:" + Build.MODEL, "", 1);
                    }

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent transintent = new Intent(BoostDeepingActivity.this, BoostPermissingActivity.class);
                            startActivity(transintent);
                        }
                    }, 1500);
                    return;
                }
                junk_button_clean.setOnClickListener(null);
//                showPackageDetail(startList.get(0).packageName);
                containerView = View.inflate(BoostDeepingActivity.this, R.layout.layout_power, null);
                container = new BoostDeepWidgetContainer.Builder()
                        .setHeight(BoostDeepWidgetContainer.MATCH_PARENT)
                        .setWidth(BoostDeepWidgetContainer.MATCH_PARENT)
                        .setOrientation(BoostDeepWidgetContainer.PORTRAIT)
                        .build(BoostDeepingActivity.this.getApplicationContext());
                container.addView(containerView, container.makeLayoutParams(BoostDeepWidgetContainer.MATCH_PARENT,
                        BoostDeepWidgetContainer.MATCH_PARENT, Gravity.CENTER));
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
                    MyUtils.doStartApplicationWithPackageName(BoostDeepingActivity.this, getIntent().getStringExtra("packageName"));
                    if (PreData.getDB(BoostDeepingActivity.this, BoostMyConstant.TONGZHILAN_SWITCH, true)) {
                        Intent intent = new Intent(BoostDeepingActivity.this, NotificationServiceBoost.class);
                        intent.setAction("gboost");
                        startService(intent);
                    }
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("count", count);
                    bundle.putString("from", "GBoost");
                    jumpToActivity(SucceedActivityBoost.class, bundle, 1);
                }
                onBackPressed();
                return;
            }
            junk_button_clean.callOnClick();
        }
    }

    private void animatorView(final View view, final int i) {
        AnimatorSet set = new AnimatorSet();
        View icon = view.findViewById(R.id.recyc_icon);
        ObjectAnimator rotate = ObjectAnimator.ofFloat(icon, "rotation", 0f, 360f);
        ObjectAnimator translationX = ObjectAnimator.ofFloat(icon, "translationX", 0f, -360f);

        set.setDuration(2000)
                .addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        CleanManager.getInstance(BoostDeepingActivity.this).removeRamSelfBoot(startList.get(i));
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
        set.play(translationX).with(rotate);
        set.start();
    }

    private void showPackageDetail(String packageName) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", packageName, null);
        intent.setData(uri);
        startActivity(intent);
    }

    private void setContainer() {
        containerView_recyclerView = (RecyclerView) containerView.findViewById(R.id.power_recycler);
        containerView_junk_button_clean = (TextView) containerView.findViewById(R.id.junk_button_clean);

        power_1 = (ImageView) containerView.findViewById(R.id.power_1);
        power_2 = (ImageView) containerView.findViewById(R.id.power_2);
        power_3 = (ImageView) containerView.findViewById(R.id.power_3);
        power_4 = (ImageView) containerView.findViewById(R.id.power_4);


        containerView_junk_button_clean.setVisibility(View.GONE);
        containerView_recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        containerView_recyclerView.setAdapter(containerAdapter = new HomeAdapter(true));
        // 设置item动画
        containerView_recyclerView.setItemAnimator(new DefaultItemAnimator());
        flag = false;
        startSetAnimator();
        mHandler.postDelayed(runnable, 100);
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
        boolean isWidget;

        public HomeAdapter(boolean isWidget) {
            this.isWidget = isWidget;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    BoostDeepingActivity.this).inflate(R.layout.layout_power_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            if (isWidget) {
                holder.recyc_name.setVisibility(View.GONE);
                holder.recyc_check.setVisibility(View.GONE);
            } else {
                holder.recyc_check.setImageResource(startList.get(position).isChecked ? R.mipmap.side_check_passed : R.mipmap.side_check_normal);
            }
            //不加下面if名字为空
            if (startList.get(position).label == null) {
                startList.get(position).label = LoadManager.getInstance(BoostDeepingActivity.this).getAppLabel(startList.get(position).pkg);
            }
            holder.recyc_name.setText(startList.get(position).label);
            holder.recyc_icon.setImageDrawable(LoadManager.getInstance(BoostDeepingActivity.this).getAppIcon(startList.get(position).pkg));
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
            TextView recyc_name;

            public MyViewHolder(View view) {
                super(view);
                recyc_item = (FrameLayout) view.findViewById(R.id.recyc_item);
                recyc_icon = (ImageView) view.findViewById(R.id.recyc_icon);
                recyc_check = (ImageView) view.findViewById(R.id.recyc_check);
                recyc_name = (TextView) view.findViewById(R.id.recyc_name);
            }
        }
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

    @Override
    public void onBackPressed() {
        setResult(BoostMyConstant.POWER_RESUIL);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        endSetAnimator();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            onBackPressed();
        }
        if (requestCode == 100) {
            if (MyUtils.isAccessibilitySettingsOn(BoostDeepingActivity.this)) {
                junk_button_clean.callOnClick();
            }
        }
        if (requestCode == 100 || requestCode == 1) {
            if (MyUtils.isAccessibilitySettingsOn(this)) {
                gboost_power_check.setImageResource(R.mipmap.side_check_passed);
            } else {
                gboost_power_check.setImageResource(R.mipmap.side_check_normal);
            }
        }
    }

    private void startSetAnimator() {
        animatorSet = new AnimatorSet();
        animator1 = ObjectAnimator.ofFloat(power_1, "alpha", 1f, 0f, 1f);
        animator2 = ObjectAnimator.ofFloat(power_2, "alpha", 1f, 0f, 1f);
        animator3 = ObjectAnimator.ofFloat(power_3, "alpha", 1f, 0f, 1f);
        animator4 = ObjectAnimator.ofFloat(power_4, "alpha", 1f, 0f, 1f);
        animatorSet.setDuration(1800);
        animatorSet.play(animator1).with(animator2).with(animator3).with(animator4);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!flag) {
                    startSetAnimator();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        startService(new Intent(this, MyServiceCustomerAccessibility.class).putExtra("isDis", true));
        super.onDestroy();
    }
}
