package com.easy.junk.easyactivity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.easy.clean.entity.JunkInfo;
import com.easy.clean.easyutils.MyUtils;
import com.easy.junk.R;
import com.easy.junk.easytools.EasyConstant;
import com.easy.junk.easytools.SetAdUtil;
import com.easy.junk.easypresenter.EasyPresenterRam;
import com.easy.junk.easycustomadapter.RamAdapter;
import com.easy.junk.easyinterfaceview.CustomRamView;
import com.easy.clean.easyutils.PreData;

import java.util.List;

/**
 * Created by on 2017/3/2.
 */

public class EasyMemoryAvtivity extends BaseActivity implements CustomRamView {
    TextView junk_unit;
    TextView junk_fangxin;
    TextView junk_button_clean;
    TextView title_name;
    ImageView title_right;
    LinearLayout junk_title_backg;
    FrameLayout title_left;
    private EasyPresenterRam ramPresenter;
    private RamAdapter adapterRam;
    public Handler myHandler;
    TextView junk_size_all;
    ListView junk_list_all;
    private boolean color1 = true;
    private boolean color2 = true;
    private boolean color3 = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ram);
        AndroidSdk.loadFullAd(SetAdUtil.DEFAULT_FULL,null);
        ramPresenter = new EasyPresenterRam(this, this);
        myHandler = new Handler();
        ramPresenter.init();
        title_right.setImageResource(R.mipmap.ram_white);
        title_right.setVisibility(View.VISIBLE);
    }

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        title_right = (ImageView) findViewById(R.id.title_right);
        junk_title_backg = (LinearLayout) findViewById(R.id.junk_title_backg);
        junk_size_all = (TextView) findViewById(R.id.junk_size_all);
        junk_unit = (TextView) findViewById(R.id.junk_unit);
        junk_fangxin = (TextView) findViewById(R.id.junk_fangxin);
        junk_button_clean = (TextView) findViewById(R.id.junk_button_clean);
        junk_list_all = (ListView) findViewById(R.id.junk_list_all);
    }

    @Override
    public void setColor(int memory, final long allSize) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int time = 100;
                for (long i = 0; i <= allSize; i += (allSize / 15)) {
                    final long finalI = i;
                    time -= 5;
                    if (time < 30) {
                        time = 30;
                    }
                    if (onDestroyed) {
                        break;
                    }
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            junk_size_all.setText(MyUtils.convertStorage(finalI, false));
                            setUnit(allSize, junk_unit);
                        }
                    });
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Animation animation = AnimationUtils.loadAnimation(EasyMemoryAvtivity.this, R.anim.translate_notifi);
                        junk_button_clean.startAnimation(animation);
                        junk_button_clean.setVisibility(View.VISIBLE);
                    }
                });
            }
        }).start();
//        junk_size_all.setText(String.valueOf(memory));
        if (allSize > 1024 * 1024 * 100 && allSize <= 1024 * 1024 * 200) {
            if (color1) {
                color1 = false;
                ValueAnimator colorAnim = ObjectAnimator.ofInt(junk_title_backg, "backgroundColor", getResources().getColor(R.color.A1), getResources().getColor(R.color.A21));
                colorAnim.setDuration(2000);
                colorAnim.setRepeatCount(0);
                colorAnim.setEvaluator(new ArgbEvaluator());
                colorAnim.start();
                junk_button_clean.setBackgroundColor(getResources().getColor(R.color.A21));
            }
        } else if (allSize > 1024 * 1024 * 200) {
            if (color2) {
                color2 = false;
                ValueAnimator colorAnim = ObjectAnimator.ofInt(junk_title_backg, "backgroundColor", getResources().getColor(R.color.A21), getResources().getColor(R.color.A2));
                colorAnim.setDuration(2000);
                colorAnim.setRepeatCount(0);
                colorAnim.setEvaluator(new ArgbEvaluator());
                colorAnim.start();
                junk_button_clean.setBackgroundColor(getResources().getColor(R.color.A2));
            }
        } else {
            if (color3) {
                color3 = false;
                ValueAnimator colorAnim = ObjectAnimator.ofInt(junk_title_backg, "backgroundColor", getResources().getColor(R.color.A1), getResources().getColor(R.color.A1));
                colorAnim.setDuration(2000);
                colorAnim.setRepeatCount(0);
                colorAnim.setEvaluator(new ArgbEvaluator());
                colorAnim.start();
                junk_button_clean.setBackgroundColor(getResources().getColor(R.color.A1));
            }
        }
    }

    @Override
    public void loadFullAd() {
    }

    @Override
    public void onClick() {
        title_left.setOnClickListener(onClickListener);
        junk_button_clean.setOnClickListener(onClickListener);
        title_right.setOnClickListener(onClickListener);
    }

    @Override
    public void addRamdata(long size, List<JunkInfo> list) {
        adapterRam.upList(list);
        adapterRam.notifyDataSetChanged();
    }

    @Override
    public void cleanAnimation(List<JunkInfo> cleanList, final long cleanSize) {
        adapterRam.upList(cleanList);
        adapterRam.notifyDataSetChanged();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int a = adapterRam.getCount();
                int time = 100;
                for (int i = 0; i < a; i++) {
                    if (i > 10) {
                        time = 30;
                    }
                    try {
                        Thread.sleep(time--);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (adapterRam.getData().size() != 0) {
                                adapterRam.removeData(adapterRam.getData(0));
                                adapterRam.notifyDataSetChanged();
                                Log.e("aaa", "===ram清理2");
                            }
                        }
                    });
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Bundle bundle = new Bundle();
                        bundle.putLong("sizeR", cleanSize);
                        bundle.putString("name", (String) getText(R.string.jiasu_success));
                        bundle.putString("from", "ramSpeed");
                        ramPresenter.jumpToActivity(EasySucceedActivity.class, bundle, 1);
                    }
                });
            }
        }).start();
    }

    public void setUnit(long size, TextView textView) {
        if (size < 1024) {
            textView.setText("B");
        } else if (size < 1048576) {
            textView.setText("KB");
        } else if (size < 1073741824) {
            textView.setText("MB");
        } else {
            textView.setText("GB");
        }
    }

    @Override
    public void setCleanDAta(boolean isFirst, final long size) {
        if (isFirst) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int time = 100;
                    for (long i = 0; i <= size; i += (size / 15)) {
                        final long finalI = i;
                        time -= 5;
                        if (time < 30) {
                            time = 30;
                        }
                        try {
                            Thread.sleep(time);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (finalI != 0) {
                                    junk_button_clean.setText(getResources().getText(R.string.ram_button) + "(" + MyUtils.convertStorage(finalI, true) + ")");
                                }
                            }
                        });
                    }
                }
            }).start();
        } else {
            if (size != 0) {
                junk_button_clean.setText(getResources().getText(R.string.ram_button) + "(" + MyUtils.convertStorage(size, true) + ")");
            }
        }
    }

    @Override
    public void initData(long allSize) {
        title_name.setText(R.string.main_ram_name);
        if (allSize <= 0) {
            Bundle bundle = new Bundle();
            bundle.putString("name", (String) getText(R.string.jiasu_success));
            bundle.putString("from", "ramSpeed");
            ramPresenter.jumpToActivity(EasySucceedActivity.class, bundle, 1);
            return;
        }
        adapterRam = new RamAdapter(this, ramPresenter);
        junk_list_all.setAdapter(adapterRam);

        ramPresenter.addAdapterData();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_left:
                    onBackPressed();
                    break;
                case R.id.title_right:

                    jumpToActivity(EasyWhiteAvtivity.class, 1);

                    break;

                case R.id.junk_button_clean:
                    PreData.putDB(EasyMemoryAvtivity.this, EasyConstant.KEY_CLEAN_TIME, System.currentTimeMillis());
                    SetAdUtil.track("ram页面", "点击清理", "", 1);
                    junk_button_clean.setOnClickListener(null);
                    showToast((String) getText(R.string.toast_ing));
                    ramPresenter.bleachFile(adapterRam.getData());
                    break;
            }

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            setResult(EasyConstant.RAM_RESUIL);
            onBackPressed();
        } else if (resultCode == EasyConstant.WHITE_RESUIL) {
            ramPresenter.addAdapterData();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AndroidSdk.onResumeWithoutTransition(this);
    }

    @Override
    public void onBackPressed() {
        if ("notifi".equals(getIntent().getStringExtra("from"))) {
            SetAdUtil.track("通知栏", "跳转垃圾清页面", "", 1);
            jumpTo(MainActivity.class);
        } else {
            setResult(EasyConstant.RAM_RESUIL);
        }
        finish();
    }
}
