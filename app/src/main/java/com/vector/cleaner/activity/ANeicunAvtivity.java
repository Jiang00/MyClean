package com.vector.cleaner.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.vector.cleaner.madapter.RamAdapter;
import com.vector.cleaner.myview.BubbleLayoutJunk;
import com.vector.cleaner.myview.BubbleLayoutRam;
import com.vector.cleaner.presenter.RamPresenter;
import com.vector.cleaner.utils.AdUtil;
import com.vector.cleaner.utils.Constant;
import com.vector.cleaner.view.MRamView;
import com.vector.mcleaner.entity.JunkInfo;
import com.vector.mcleaner.mutil.PreData;
import com.vector.mcleaner.mutil.Util;
import com.android.client.AndroidSdk;
import com.vector.cleaner.R;

import java.util.List;

/**
 * Created by on 2017/3/2.
 */

public class ANeicunAvtivity extends BaseActivity implements MRamView {

    ImageView title_right;
    LinearLayout junk_title_backg;
    FrameLayout title_left;
    TextView junk_unit;
    TextView junk_fangxin;
    Button junk_button_clean;
    TextView title_name;
    TextView junk_size_all;
    ListView junk_list_all;
    FrameLayout ram_clean;
    BubbleLayoutJunk ram_clean_bubble;
    BubbleLayoutRam ram_clean_bubble_2;
    LinearLayout ram_clean_feiji;
    ImageView ram_clean_huo;

    private RamAdapter adapterRam;
    private RamPresenter ramPresenter;
    private boolean color1 = true;
    private boolean color2 = true;
    public Handler myHandler;
    private AnimatorSet animatorSet;

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
        ram_clean = (FrameLayout) findViewById(R.id.ram_clean);
        ram_clean_bubble = (BubbleLayoutJunk) findViewById(R.id.ram_clean_bubble);
        ram_clean_bubble_2 = (BubbleLayoutRam) findViewById(R.id.ram_clean_bubble_2);
        ram_clean_feiji = (LinearLayout) findViewById(R.id.ram_clean_feiji);
        ram_clean_huo = (ImageView) findViewById(R.id.ram_clean_huo);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ram);
        ram_clean_bubble.pause();
        ram_clean_bubble_2.pause();
        AndroidSdk.loadFullAd(AdUtil.DEFAULT, null);
        ramPresenter = new RamPresenter(this, this);
        myHandler = new Handler();
        ramPresenter.init();
        title_right.setImageResource(R.mipmap.ram_white);
        title_right.setVisibility(View.VISIBLE);

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
                            setUnit(allSize, junk_unit);
                        }
                    });
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Animation animation = AnimationUtils.loadAnimation(ANeicunAvtivity.this, R.anim.translate_notifi);
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
                ValueAnimator colorAnim = ObjectAnimator.ofInt(junk_title_backg, "backgroundColor", getResources().getColor(R.color.A1), getResources().getColor(R.color.A4));
                colorAnim.setDuration(2000);
                colorAnim.setRepeatCount(0);
                colorAnim.setEvaluator(new ArgbEvaluator());
                colorAnim.start();
            }
        } else if (allSize > 1024 * 1024 * 200) {
            if (color2) {
                color2 = false;
                ValueAnimator colorAnim = ObjectAnimator.ofInt(junk_title_backg, "backgroundColor", getResources().getColor(R.color.A1), getResources().getColor(R.color.A2));
                colorAnim.setDuration(2000);
                colorAnim.setRepeatCount(0);
                colorAnim.setEvaluator(new ArgbEvaluator());
                colorAnim.start();
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ram_clean_bubble.resume();
        ram_clean_bubble_2.resume();
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
    public void addRamdata(long size, List<JunkInfo> list) {
        adapterRam.upList(list);
        adapterRam.notifyDataSetChanged();
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
//                                    junk_button_clean.setText(getResources().getText(R.string.ram_button) + "(" + FileUtil.convertStorage(finalI, true) + ")");
//                                }
//                            }
//                        });
                    }
                }
            }).start();
        } else {
//            if (size != 0) {
//                junk_button_clean.setText(getResources().getText(R.string.ram_button) + "(" + FileUtil.convertStorage(size, true) + ")");
//            }
        }

    }

    @Override
    public void cleanAnimation(List<JunkInfo> cleanList, final long cleanSize) {
        final Bundle bundle = new Bundle();
        bundle.putLong("sizeR", cleanSize);
        bundle.putString("name", (String) getText(R.string.jiasu_success));
        bundle.putString("from", "ramSpeed");

        ram_clean.setVisibility(View.VISIBLE);
        ram_clean_bubble.reStart();
        ram_clean_bubble_2.reStart();
        animatorSet = new AnimatorSet();
        ObjectAnimator objectAnimator_1 = ObjectAnimator.ofFloat(ram_clean_feiji, View.TRANSLATION_Y, getResources().getDimension(R.dimen.d425), 0);
        objectAnimator_1.setDuration(500);
        ram_clean_huo.setPivotY(0);
        ObjectAnimator objectAnimator_2 = ObjectAnimator.ofFloat(ram_clean_huo, View.SCALE_Y, 1, 1.3f, 1);
        objectAnimator_2.setDuration(500);
        objectAnimator_2.setRepeatCount(4);
        ObjectAnimator objectAnimator_3 = ObjectAnimator.ofFloat(ram_clean_feiji, View.TRANSLATION_Y, 0, -getResources().getDimension(R.dimen.d425));
        objectAnimator_3.setDuration(500);
        animatorSet.playSequentially(objectAnimator_1, objectAnimator_2, objectAnimator_3);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ramPresenter.jumpToActivity(SuccessActivity.class, bundle, 1);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationStart(Animator animation) {

            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (animatorSet != null && animatorSet.isRunning()) {
            animatorSet.removeAllListeners();
            animatorSet.cancel();
        }
        ram_clean_bubble.pause();
        ram_clean_bubble.destroy();
        ram_clean_bubble_2.pause();
        ram_clean_bubble_2.destroy();
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
                    PreData.putDB(ANeicunAvtivity.this, Constant.KEY_CLEAN_TIME, System.currentTimeMillis());
                    AdUtil.track("ram页面", "点击清理", "", 1);
                    junk_button_clean.setOnClickListener(null);
                    Log.e("aaa", "===ram点击");
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
