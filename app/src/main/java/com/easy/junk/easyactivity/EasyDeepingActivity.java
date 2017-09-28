package com.easy.junk.easyactivity;

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
import com.easy.clean.core.CleanManager;
import com.easy.clean.deepclean.MyServiceCustomerAccessibility;
import com.easy.clean.entity.JunkInfo;
import com.easy.clean.easyutils.LoadManager;
import com.easy.clean.easyutils.MyUtils;
import com.easy.clean.easyutils.PreData;
import com.easy.junk.R;
import com.easy.junk.easycustomview.DeepWidgetContainer;
import com.easy.junk.easytools.EasyConstant;
import com.easy.junk.easytools.SetAdUtil;
import com.easy.junk.easyservices.EasyNotificationService;

import java.util.ArrayList;

/**
 */

public class EasyDeepingActivity extends BaseActivity {
    FrameLayout title_left;
    TextView title_name;
    private int count = 0;
    RecyclerView power_recycler;
    TextView junk_button_clean;
    private DeepWidgetContainer container;
    private MyApplication cleanApplication;
    private ObjectAnimator animator1, animator2, animator3, animator4, animator5, animator6;
    AnimatorSet animatorSet;
    private boolean flag = false;
    private ArrayList<JunkInfo> startList;
    private View containerView;
    private ImageView power_imageview;
    ImageView gboost_power_check;
    Handler mHandler;
    private HomeAdapter homeAdapter, containerAdapter;
    private ImageView power_1, power_2, power_3, power_4, power_5, power_6;
    private RecyclerView containerView_recyclerView;
    private TextView containerView_junk_button_clean;
    private GridLayoutManager mLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_power);
        AndroidSdk.loadFullAd(SetAdUtil.DEFAULT_FULL,null);
        mHandler = new Handler();
        if (MyUtils.isAccessibilitySettingsOn(this)) {
            gboost_power_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            gboost_power_check.setImageResource(R.mipmap.side_check_normal);
        }
        gboost_power_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetAdUtil.track("深度清理页面", "开启辅助功能", "", 1);
                try {
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivityForResult(intent, 100);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent transintent = new Intent(EasyDeepingActivity.this, EasyPermissingActivity.class);
                        startActivity(transintent);
                    }
                }, 1500);
            }
        });
        startService(new Intent(this, MyServiceCustomerAccessibility.class).putExtra("isDis", false));
        initData();
        title_name.setText(R.string.side_power);
//        power_size.setText(getString(R.string.power_1, startList.size() + "") + " ");
        mLayoutManager = new GridLayoutManager(this, 4);//设置为一个4列的纵向网格布局
        power_recycler.setLayoutManager(mLayoutManager);
        power_recycler.setAdapter(homeAdapter = new HomeAdapter(false));

        // 设置item动画
        power_recycler.setItemAnimator(new DefaultItemAnimator());
        junk_button_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetAdUtil.track("深度清理页面", "点击清理", "", 1);
                if (!MyUtils.isAccessibilitySettingsOn(EasyDeepingActivity.this)) {
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
                            Intent transintent = new Intent(EasyDeepingActivity.this, EasyPermissingActivity.class);
                            startActivity(transintent);
                        }
                    }, 1500);
                    return;
                }
                junk_button_clean.setOnClickListener(null);
                containerView = View.inflate(EasyDeepingActivity.this, R.layout.layout_power, null);
                container = new DeepWidgetContainer.Builder()
                        .setHeight(DeepWidgetContainer.MATCH_PARENT)
                        .setWidth(DeepWidgetContainer.MATCH_PARENT)
                        .setOrientation(DeepWidgetContainer.PORTRAIT)
                        .build(EasyDeepingActivity.this.getApplicationContext());
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
                    MyUtils.doStartApplicationWithPackageName(EasyDeepingActivity.this, getIntent().getStringExtra("packageName"));
                    if (PreData.getDB(EasyDeepingActivity.this, EasyConstant.TONGZHILAN_SWITCH, true)) {
                        Intent intent = new Intent(EasyDeepingActivity.this, EasyNotificationService.class);
                        intent.setAction("gboost");
                        startService(intent);
                    }
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("count", count);
                    bundle.putString("from", "GBoost");
                    jumpToActivity(EasySucceedActivity.class, bundle, 1);
                }
                onBackPressed();
                return;
            }
            flag = false;
            junk_button_clean.callOnClick();
        }
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
            homeAdapter.notifyDataSetChanged();
            if (TextUtils.equals("GBoost", getIntent().getStringExtra("from"))) {
                if (!TextUtils.isEmpty(getIntent().getStringExtra("packageName"))) {
                    MyUtils.doStartApplicationWithPackageName(EasyDeepingActivity.this, getIntent().getStringExtra("packageName"));
                    if (PreData.getDB(EasyDeepingActivity.this, EasyConstant.TONGZHILAN_SWITCH, true)) {
                        Intent intent = new Intent(EasyDeepingActivity.this, EasyNotificationService.class);
                        intent.setAction("gboost");
                        startService(intent);
                    }

                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("count", count);
                    bundle.putString("from", "GBoost");
                    jumpToActivity(EasySucceedActivity.class, bundle, 1);
                }
                onBackPressed();
            } else {
                Bundle bundle = new Bundle();
                bundle.putInt("count", count);
                bundle.putString("from", "power");
                jumpToActivity(EasySucceedActivity.class, bundle, 1);
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
                        CleanManager.getInstance(EasyDeepingActivity.this).removeRamSelfBoot(startList.get(i));
                        startList.remove(i);
//                        containerView_power_size.setText(getString(R.string.power_1, startList.size() + "") + " ");
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

    private void setContainer() {
        containerView_recyclerView = (RecyclerView) containerView.findViewById(R.id.power_recycler);
//        containerView_power_size = (TextView) containerView.findViewById(R.id.power_size);
        containerView_junk_button_clean = (TextView) containerView.findViewById(R.id.junk_button_clean);
        power_imageview = (ImageView) containerView.findViewById(R.id.power_imageview);
        power_1 = (ImageView) containerView.findViewById(R.id.power_1);
        power_2 = (ImageView) containerView.findViewById(R.id.power_2);
        power_3 = (ImageView) containerView.findViewById(R.id.power_3);
        power_4 = (ImageView) containerView.findViewById(R.id.power_4);
        power_5 = (ImageView) containerView.findViewById(R.id.power_5);
        power_6 = (ImageView) containerView.findViewById(R.id.power_6);


//        animatorSet = new AnimatorSet();
//        animatorSet.play(animator1).with(animator2).with(animator3).with(animator4).with(animator5).with(animator6);
//        containerView_power_size.setText(getString(R.string.power_1, startList.size() + "") + " ");
        containerView_junk_button_clean.setVisibility(View.GONE);
        containerView_recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        containerView_recyclerView.setAdapter(containerAdapter = new HomeAdapter(true));
        // 设置item动画
        containerView_recyclerView.setItemAnimator(new DefaultItemAnimator());
        startSetAnimator();
//        while (flag) {
//            try {
//                Thread.sleep(1500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        mHandler.postDelayed(runnable, 100);
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
                    EasyDeepingActivity.this).inflate(R.layout.layout_power_item, parent,
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
            holder.recyc_icon.setImageDrawable(LoadManager.getInstance(EasyDeepingActivity.this).getAppIcon(startList.get(position).pkg));
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

    @Override
    public void onBackPressed() {
        setResult(EasyConstant.POWER_RESUIL);
        finish();
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
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            onBackPressed();
        }
        if (requestCode == 100) {
            if (MyUtils.isAccessibilitySettingsOn(EasyDeepingActivity.this)) {
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

    @Override
    protected void onDestroy() {
        startService(new Intent(this, MyServiceCustomerAccessibility.class).putExtra("isDis", true));
        super.onDestroy();
        flag = true;
    }

    private void startSetAnimator() {
        animator1 = ObjectAnimator.ofFloat(power_1, "alpha", 1f, 0f, 1f);
        animator2 = ObjectAnimator.ofFloat(power_2, "alpha", 1f, 0f, 1f);
        animator3 = ObjectAnimator.ofFloat(power_3, "alpha", 1f, 0f, 1f);
        animator4 = ObjectAnimator.ofFloat(power_4, "alpha", 1f, 0f, 1f);
        animator5 = ObjectAnimator.ofFloat(power_5, "alpha", 1f, 0f, 1f);
        animator6 = ObjectAnimator.ofFloat(power_6, "alpha", 1f, 0f, 1f);
        animator1.setDuration(1700);
        animator2.setDuration(1700);
        animator3.setDuration(1700);
        animator4.setDuration(1700);
        animator5.setDuration(1700);
        animator6.setDuration(1700);
        animator1.start();
        animator2.start();
        animator3.start();
        animator4.start();
        animator5.start();
        animator6.start();
        animator1.addListener(new Animator.AnimatorListener() {
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

}
