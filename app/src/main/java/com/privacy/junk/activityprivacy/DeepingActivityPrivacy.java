package com.privacy.junk.activityprivacy;

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
import com.privacy.clean.core.CleanManager;
import com.privacy.clean.deepclean.MyServiceCustomerAccessibility;
import com.privacy.clean.utilsprivacy.LoadManager;
import com.privacy.clean.utilsprivacy.MyUtils;
import com.privacy.clean.utilsprivacy.PreData;
import com.privacy.clean.entity.JunkInfo;
import com.privacy.junk.R;
import com.privacy.junk.privacycustomview.DeepWidgetContainerPrivacy;
import com.privacy.junk.privacyservices.PrivacyNotificationService;
import com.privacy.junk.toolsprivacy.MyConstantPrivacy;
import com.privacy.junk.toolsprivacy.SetAdUtilPrivacy;

import java.util.ArrayList;

/**
 */

public class DeepingActivityPrivacy extends BaseActivity {
    private GridLayoutManager mLayoutManager;
    Handler mHandler;
    private HomeAdapter homeAdapter, containerAdapter;
    private ArrayList<JunkInfo> startList;
    private View containerView;
    private ImageView power_imageview;
    private boolean flag = false;
    private MyApplication cleanApplication;
    private int count = 0;
    TextView power_size;
    RecyclerView power_recycler;
    TextView junk_button_clean;
    FrameLayout title_left;
    TextView title_name;
    private DeepWidgetContainerPrivacy container;
    private RecyclerView containerView_recyclerView;
    private TextView containerView_power_size;
    private TextView containerView_junk_button_clean;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_power);
        AndroidSdk.loadFullAd(AndroidSdk.FULL_TAG_PAUSE);
        mHandler = new Handler();
        startService(new Intent(this, MyServiceCustomerAccessibility.class).putExtra("isDis", false));
        initData();
        title_name.setText(R.string.side_power);
        power_size.setText(getString(R.string.power_1, startList.size() + "") + " ");
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
                if (!MyUtils.isAccessibilitySettingsOn(DeepingActivityPrivacy.this)) {
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
                            Intent transintent = new Intent(DeepingActivityPrivacy.this, PermissingActivityPrivacy.class);
                            startActivity(transintent);
                        }
                    }, 1500);
                    return;
                }
                junk_button_clean.setOnClickListener(null);
//                showPackageDetail(startList.get(0).packageName);
                containerView = View.inflate(DeepingActivityPrivacy.this, R.layout.layout_power, null);
                container = new DeepWidgetContainerPrivacy.Builder()
                        .setHeight(DeepWidgetContainerPrivacy.MATCH_PARENT)
                        .setWidth(DeepWidgetContainerPrivacy.MATCH_PARENT)
                        .setOrientation(DeepWidgetContainerPrivacy.PORTRAIT)
                        .build(DeepingActivityPrivacy.this.getApplicationContext());
                container.addView(containerView, container.makeLayoutParams(DeepWidgetContainerPrivacy.MATCH_PARENT,
                        DeepWidgetContainerPrivacy.MATCH_PARENT, Gravity.CENTER));
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
                    MyUtils.doStartApplicationWithPackageName(DeepingActivityPrivacy.this, getIntent().getStringExtra("packageName"));
                    if (PreData.getDB(DeepingActivityPrivacy.this, MyConstantPrivacy.TONGZHILAN_SWITCH, true)) {
                        Intent intent = new Intent(DeepingActivityPrivacy.this, PrivacyNotificationService.class);
                        intent.setAction("gboost");
                        startService(intent);
                    }
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("count", count);
                    bundle.putString("from", "GBoost");
                    jumpToActivity(PrivacySucceedActivity.class, bundle, 1);
                }
                onBackPressed();
                return;
            }
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
            power_size.setText(getString(R.string.power_1, 0 + "") + " ");
            homeAdapter.notifyDataSetChanged();
            if (TextUtils.equals("GBoost", getIntent().getStringExtra("from"))) {
                if (!TextUtils.isEmpty(getIntent().getStringExtra("packageName"))) {
                    MyUtils.doStartApplicationWithPackageName(DeepingActivityPrivacy.this, getIntent().getStringExtra("packageName"));
                    if (PreData.getDB(DeepingActivityPrivacy.this, MyConstantPrivacy.TONGZHILAN_SWITCH, true)) {
                        Intent intent = new Intent(DeepingActivityPrivacy.this, PrivacyNotificationService.class);
                        intent.setAction("gboost");
                        startService(intent);
                    }

                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("count", count);
                    bundle.putString("from", "GBoost");
                    jumpToActivity(PrivacySucceedActivity.class, bundle, 1);
                }
                onBackPressed();
            } else {
                Bundle bundle = new Bundle();
                bundle.putInt("count", count);
                bundle.putString("from", "power");
                jumpToActivity(PrivacySucceedActivity.class, bundle, 1);
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
        power_size = (TextView) findViewById(R.id.power_size);
        junk_button_clean = (TextView) findViewById(R.id.junk_button_clean);
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
                        CleanManager.getInstance(DeepingActivityPrivacy.this).removeRamSelfBoot(startList.get(i));
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

    private void setContainer() {
        containerView_recyclerView = (RecyclerView) containerView.findViewById(R.id.power_recycler);
        containerView_power_size = (TextView) containerView.findViewById(R.id.power_size);
        containerView_junk_button_clean = (TextView) containerView.findViewById(R.id.junk_button_clean);
        power_imageview = (ImageView) containerView.findViewById(R.id.power_imageview);

        containerView_power_size.setText(getString(R.string.power_1, startList.size() + "") + " ");
        containerView_junk_button_clean.setVisibility(View.GONE);
        containerView_recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        containerView_recyclerView.setAdapter(containerAdapter = new HomeAdapter(true));
        // 设置item动画
        containerView_recyclerView.setItemAnimator(new DefaultItemAnimator());
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
                    DeepingActivityPrivacy.this).inflate(R.layout.layout_power_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            if (isWidget) {
                holder.recyc_name.setVisibility(View.GONE);
                holder.recyc_check.setVisibility(View.GONE);
            } else {
                holder.recyc_check.setImageResource(startList.get(position).isChecked ? R.mipmap.ram_passed : R.mipmap.ram_normal);
            }
            //不加下面if名字为空
            if (startList.get(position).label == null) {
                startList.get(position).label = LoadManager.getInstance(DeepingActivityPrivacy.this).getAppLabel(startList.get(position).pkg);
            }
            holder.recyc_name.setText(startList.get(position).label);
            holder.recyc_icon.setImageDrawable(LoadManager.getInstance(DeepingActivityPrivacy.this).getAppIcon(startList.get(position).pkg));
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
        setResult(MyConstantPrivacy.POWER_RESUIL);
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
            if (MyUtils.isAccessibilitySettingsOn(DeepingActivityPrivacy.this)) {
                junk_button_clean.callOnClick();
            }
        }
    }

    @Override
    protected void onDestroy() {
        startService(new Intent(this, MyServiceCustomerAccessibility.class).putExtra("isDis", true));
        super.onDestroy();
    }
}
