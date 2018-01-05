package com.upupup.clean.junk.myActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.upupup.clean.junk.util.BadgerCount;
import com.upupup.clean.util.PreData;
import com.upupup.clean.util.Util;
import com.android.client.AndroidSdk;
import com.upupup.clean.junk.R;
import com.upupup.clean.junk.myAdapter.RamAdapter;
import com.upupup.clean.entity.JunkInfo;
import com.upupup.clean.junk.presenter.RamPresenter;
import com.upupup.clean.junk.util.AdUtil;
import com.upupup.clean.junk.util.Constant;
import com.upupup.clean.junk.view.RamView;

import java.util.List;

/**
 * Created by on 2017/3/2.
 */

public class RamAvtivity extends BaseActivity implements RamView {

    TextView junk_size_all;
    TextView junk_unit;
    TextView junk_fangxin;
    FrameLayout title_left;
    TextView title_name;
    ListView junk_list_all;
    ImageView title_right;
    LinearLayout junk_title_backg;
    Button junk_button_clean;

    private RamPresenter ramPresenter;
    private RamAdapter adapterRam;
    private boolean color1 = true;
    private boolean color2 = true;
    public Handler myHandler;

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
        junk_button_clean = (Button) findViewById(R.id.junk_button_clean);
        junk_list_all = (ListView) findViewById(R.id.junk_list_all);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ram);
        PreData.putDB(this, Constant.HONG_RAM, false);
        BadgerCount.setCount(this);
        AndroidSdk.loadFullAd(AdUtil.FULL_DEFAULT, null);
        ramPresenter = new RamPresenter(this, this);
        myHandler = new Handler();
        ramPresenter.init();
        title_right.setImageResource(R.mipmap.ram_white);
        title_right.setVisibility(View.VISIBLE);

    }

    @Override
    public void initData(long allSize) {
        title_name.setText(R.string.main_ram_name);
        if (allSize <= 0) {
            Bundle bundle = new Bundle();
            bundle.putString("name", (String) getText(R.string.jiasu_success));
            bundle.putString("from", "ramSpeed");
            ramPresenter.jumpToActivity(SuccessActivity.class, bundle, 1);
            return;
        }
        adapterRam = new RamAdapter(this, ramPresenter);
        junk_list_all.setLayoutAnimation(getAnimationController());
        junk_list_all.setAdapter(adapterRam);

        ramPresenter.addAdapterData();
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
                            junk_size_all.setText(Util.convertStorage(finalI, false));
                            setUnit(allSize, junk_fangxin);
                        }
                    });
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Animation animation = AnimationUtils.loadAnimation(RamAvtivity.this, R.anim.translate_notifi);
                        junk_button_clean.startAnimation(animation);
                        junk_button_clean.setVisibility(View.VISIBLE);
                    }
                });
            }
        }).start();
//        junk_size_all.setText(String.valueOf(memory));
//        if (allSize > 1024 * 1024 * 100 && allSize <= 1024 * 1024 * 200) {
//            if (color1) {
//                color1 = false;
//                ValueAnimator colorAnim = ObjectAnimator.ofInt(junk_title_backg, "backgroundColor", getResources().getColor(R.color.A1), getResources().getColor(R.color.A4));
//                colorAnim.setDuration(2000);
//                colorAnim.setRepeatCount(0);
//                colorAnim.setEvaluator(new ArgbEvaluator());
//                colorAnim.start();
//            }
//        } else if (allSize > 1024 * 1024 * 200) {
//            if (color2) {
//                color2 = false;
//                ValueAnimator colorAnim = ObjectAnimator.ofInt(junk_title_backg, "backgroundColor", getResources().getColor(R.color.A4), getResources().getColor(R.color.A2));
//                colorAnim.setDuration(2000);
//                colorAnim.setRepeatCount(0);
//                colorAnim.setEvaluator(new ArgbEvaluator());
//                colorAnim.start();
//            }
//        }
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
    public void addRamdata(long size, final List<JunkInfo> list) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapterRam.addDataList(list);
                adapterRam.notifyDataSetChanged();
            }
        });

    }

    protected LayoutAnimationController getAnimationController() {
        int duration = 300;
        AnimationSet set = new AnimationSet(true);

        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(duration);
        set.addAnimation(animation);

        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(duration);
        set.addAnimation(animation);

        LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        return controller;
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
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (finalI != 0) {
//                                    junk_button_clean.setText(getResources().getText(R.string.ram_button) + "(" + Util.convertStorage(finalI, true) + ")");
//                                }
//                            }
//                        });
                    }
                }
            }).start();
        } else {
//            if (size != 0) {
            junk_size_all.setText(Util.convertStorage(size, false));
//            }
        }

    }

    @Override
    public void cleanAnimation(final List<JunkInfo> cleanList, final long cleanSize) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapterRam.upList(cleanList);
                adapterRam.notifyDataSetChanged();
            }
        });
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
                        ramPresenter.jumpToActivity(SuccessActivity.class, bundle, 1);
                    }
                });
            }
        }).start();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_left:
                    onBackPressed();
                    break;
                case R.id.title_right:

                    jumpToActivity(IgnoreListAvtivity.class, 1);

                    break;

                case R.id.junk_button_clean:
                    PreData.putDB(RamAvtivity.this, Constant.KEY_CLEAN_TIME, System.currentTimeMillis());
                    AdUtil.track("ram页面", "点击清理", "", 1);
                    junk_button_clean.setOnClickListener(null);
                    Log.e("aaa", "===ram点击");
                    showToast((String) getText(R.string.toast_ing));
                    ramPresenter.bleachFile(adapterRam.getData());
                    break;
            }

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        AndroidSdk.onResumeWithoutTransition(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            setResult(Constant.RAM_RESUIL);
            onBackPressed();
        } else if (resultCode == Constant.WHITE_RESUIL) {
            ramPresenter.addAdapterData();
        }
    }

    @Override
    public void onBackPressed() {
        if ("notifi".equals(getIntent().getStringExtra("from"))) {
            AdUtil.track("通知栏", "跳转垃圾清页面", "", 1);
            jumpTo(MainActivity.class);
        } else {
            setResult(Constant.RAM_RESUIL);
        }
        finish();
    }
}
