package com.icleaner.junk.icleaneractivity;

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
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.icleaner.clean.core.CleanManager;
import com.icleaner.clean.deepclean.MyServiceCustomerAccessibility;
import com.icleaner.clean.entity.JunkInfo;
import com.icleaner.clean.utils.MyUtils;
import com.icleaner.junk.R;
import com.icleaner.junk.mytools.MyConstant;
import com.icleaner.junk.services.MyNotificationService;
import com.icleaner.junk.mytools.SetAdUtil;
import com.icleaner.junk.mycustomview.DeepWidgetContainer;
import com.icleaner.clean.utils.LoadManager;
import com.icleaner.clean.utils.PreData;

import java.util.ArrayList;

/**
 */

public class DeepingActivity extends BaseActivity {
    FrameLayout title_left;
    TextView title_name;
    private DeepWidgetContainer container;
    private MyApplication cleanApplication;
    private int count = 0;
    TextView power_size;
    RecyclerView power_recycler;
    ImageView junk_button_clean;
    private ArrayList<JunkInfo> startList;
    private View containerView;
    private ImageView power_imageview;
    private ObjectAnimator animator1;
    private boolean flag = false;
    private RecyclerView containerView_recyclerView;
    private TextView containerView_power_size;
    private ImageView containerView_junk_button_clean;
    private GridLayoutManager mLayoutManager;
    Handler mHandler;
    private HomeAdapter homeAdapter, containerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_power);
        AndroidSdk.loadFullAd(SetAdUtil.DEFAULT_FULL, null);
        mHandler = new Handler();
        startService(new Intent(this, MyServiceCustomerAccessibility.class).putExtra("isDis", false));
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
                flag = true;
                SetAdUtil.track("深度清理页面", "点击清理", "", 1);
                if (!MyUtils.isAccessibilitySettingsOn(DeepingActivity.this)) {
                    try {
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        startActivityForResult(intent, 100);
                    } catch (Exception e) {
                        e.printStackTrace();
                        SetAdUtil.track("深度清理页面", "进入辅助功能失败:" + Build.MODEL, "", 1);
                    }

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent transintent = new Intent(DeepingActivity.this, PermissingActivity.class);
                            startActivity(transintent);
                        }
                    }, 1500);
                    return;
                }
                junk_button_clean.setOnClickListener(null);
//                showPackageDetail(startList.get(0).packageName);
                containerView = View.inflate(DeepingActivity.this, R.layout.layout_power, null);
                container = new DeepWidgetContainer.Builder()
                        .setHeight(DeepWidgetContainer.MATCH_PARENT)
                        .setWidth(DeepWidgetContainer.MATCH_PARENT)
                        .setOrientation(DeepWidgetContainer.PORTRAIT)
                        .build(DeepingActivity.this.getApplicationContext());
                container.addView(containerView, container.makeLayoutParams(DeepWidgetContainer.MATCH_PARENT,
                        DeepWidgetContainer.MATCH_PARENT, Gravity.CENTER));
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
                    MyUtils.doStartApplicationWithPackageName(DeepingActivity.this, getIntent().getStringExtra("packageName"));
                    if (PreData.getDB(DeepingActivity.this, MyConstant.TONGZHILAN_SWITCH, true)) {
                        Intent intent = new Intent(DeepingActivity.this, MyNotificationService.class);
                        intent.putExtra("from", "gboost");
                        startService(intent);
                    }
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("count", count);
                    bundle.putString("from", "GBoost");
                    jumpToActivity(SucceedActivity.class, bundle, 1);
                }
                onBackPressed();
                return;
            }
            junk_button_clean.callOnClick();
        }
    }

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        power_recycler = (RecyclerView) findViewById(R.id.power_recycler);
        power_size = (TextView) findViewById(R.id.power_size);
        junk_button_clean = (ImageView) findViewById(R.id.junk_button_clean);
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
            power_size.setText(getString(R.string.power_1, 0 + "") + " ");
            homeAdapter.notifyDataSetChanged();
            if (TextUtils.equals("GBoost", getIntent().getStringExtra("from"))) {
                if (!TextUtils.isEmpty(getIntent().getStringExtra("packageName"))) {
                    MyUtils.doStartApplicationWithPackageName(DeepingActivity.this, getIntent().getStringExtra("packageName"));
                    if (PreData.getDB(DeepingActivity.this, MyConstant.TONGZHILAN_SWITCH, true)) {
                        Intent intent = new Intent(DeepingActivity.this, MyNotificationService.class);
                        intent.putExtra("from", "gboost");
                        startService(intent);
                    }

                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("count", count);
                    bundle.putString("from", "GBoost");
                    jumpToActivity(SucceedActivity.class, bundle, 1);
                }
                onBackPressed();
            } else {
                Bundle bundle = new Bundle();
                bundle.putInt("count", count);
                bundle.putString("from", "power");
                jumpToActivity(SucceedActivity.class, bundle, 1);
            }
            container.removeFromWindow();
        }
    };

    private void setContainer() {
        containerView_recyclerView = (RecyclerView) containerView.findViewById(R.id.power_recycler);
        containerView_power_size = (TextView) containerView.findViewById(R.id.power_size);
        containerView_junk_button_clean = (ImageView) containerView.findViewById(R.id.junk_button_clean);
        power_imageview = (ImageView) containerView.findViewById(R.id.power_imageview);
        animator1 = ObjectAnimator.ofFloat(power_imageview, "rotation", 0f, 360f);
        LinearInterpolator lir = new LinearInterpolator();
        animator1.setDuration(500);
        animator1.setInterpolator(lir);
        animator1.setRepeatCount(-1);
        animator1.start();
        containerView_power_size.setText(getString(R.string.power_1, startList.size() + "") + " ");
        containerView_junk_button_clean.setVisibility(View.GONE);
        containerView_recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        containerView_recyclerView.setAdapter(containerAdapter = new HomeAdapter(true));
        // 设置item动画
        containerView_recyclerView.setItemAnimator(new DefaultItemAnimator());
        mHandler.postDelayed(runnable, 100);
    }

    private void animatorView(final View view, final int i) {
        AnimatorSet set = new AnimatorSet();
        View icon = view.findViewById(R.id.recyc_icon);
//        ObjectAnimator scaleX = ObjectAnimator.ofFloat(icon, "scaleX", 1f, 0f);
//        ObjectAnimator scaleY = ObjectAnimator.ofFloat(icon, "scaleY", 1f, 0f);
        ObjectAnimator rotate = ObjectAnimator.ofFloat(icon, "rotation", 0f, 360f);
        ObjectAnimator translationX = ObjectAnimator.ofFloat(icon, "translationX", 0f, -360f);

        set.setDuration(2000)
                .addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        CleanManager.getInstance(DeepingActivity.this).removeRamSelfBoot(startList.get(i));
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

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
        boolean isWidget;

        public HomeAdapter(boolean isWidget) {
            this.isWidget = isWidget;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    DeepingActivity.this).inflate(R.layout.layout_power_item, parent,
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
            holder.recyc_name.setText(startList.get(position).label);
            holder.recyc_icon.setImageDrawable(LoadManager.getInstance(DeepingActivity.this).getAppIcon(startList.get(position).pkg));
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
        setResult(MyConstant.POWER_RESUIL);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            onBackPressed();
        }
        if (requestCode == 100) {
            if (MyUtils.isAccessibilitySettingsOn(DeepingActivity.this)) {
                junk_button_clean.callOnClick();
            }
        }
    }

    private void endAnimator() {
        animator1.pause();
    }

    @Override
    protected void onDestroy() {
        startService(new Intent(this, MyServiceCustomerAccessibility.class).putExtra("isDis", true));
        super.onDestroy();
        if (animator1 != null) {
            endAnimator();
        }
    }
}
