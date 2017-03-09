package com.supers.clean.junk.activity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.supers.clean.junk.R;
import com.supers.clean.junk.View.RamView;
import com.supers.clean.junk.View.adapter.RamAdapter;
import com.supers.clean.junk.modle.CommonUtil;
import com.supers.clean.junk.modle.PreData;
import com.supers.clean.junk.modle.entity.Contents;
import com.supers.clean.junk.modle.entity.JunkInfo;
import com.supers.clean.junk.presenter.RamPresenter;

import java.util.List;

/**
 * Created by Ivy on 2017/3/2.
 */

public class RamAvtivity extends BaseActivity implements RamView {

    FrameLayout title_left;
    TextView title_name;
    LinearLayout junk_title_backg;
    TextView junk_size_all;
    TextView junk_unit;
    TextView junk_fangxin;
    Button junk_button_clean;
    ListView junk_list_all;

    private RamPresenter ramPresenter;
    private RamAdapter adapterRam;
    private boolean color1 = true;
    private boolean color2 = true;


    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        junk_title_backg = (LinearLayout) findViewById(R.id.junk_title_backg);
        junk_size_all = (TextView) findViewById(R.id.junk_size_all);
        junk_unit = (TextView) findViewById(R.id.junk_unit);
        junk_fangxin = (TextView) findViewById(R.id.junk_fangxin);
        junk_button_clean = (Button) findViewById(R.id.junk_button_clean);
        junk_list_all = (ListView) findViewById(R.id.junk_list_all);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ram);
        ramPresenter = new RamPresenter(this, this);
        ramPresenter.init();

    }

    @Override
    public void loadFullAd() {
        if (PreData.getDB(this, Contents.FULL_RAM, 0) == 1) {
            AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
        }
    }

    @Override
    public void onClick() {
        title_left.setOnClickListener(onClickListener);
        junk_button_clean.setOnClickListener(onClickListener);
    }

    @Override
    public void initData(long allSize) {
        title_name.setText(R.string.main_ram_name);
        if (allSize <= 0) {
            ramPresenter.jumpToActivity(SuccessActivity.class, 1);
            return;
        }
        adapterRam = new RamAdapter(this, ramPresenter);
        junk_list_all.setAdapter(adapterRam);

        ramPresenter.addAdapterData();
        junk_button_clean.setText(getResources().getText(R.string.ram_button) + "(" + CommonUtil.getFileSize4(allSize) + ")");
    }

    @Override
    public void setColor(int memory, long allSize) {
        junk_size_all.setText(String.valueOf(memory));
        if (allSize > 1024 * 1024 * 100 && allSize <= 1024 * 1024 * 200) {
            if (color1) {
                color1 = false;
                ValueAnimator colorAnim = ObjectAnimator.ofInt(junk_title_backg, "backgroundColor", getResources().getColor(R.color.app_color_first), getResources().getColor(R.color.app_color_second));
                colorAnim.setDuration(2000);
                colorAnim.setRepeatCount(0);
                colorAnim.setEvaluator(new ArgbEvaluator());
                colorAnim.start();
            }
        } else if (allSize > 1024 * 1024 * 200) {
            if (color2) {
                color2 = false;
                ValueAnimator colorAnim = ObjectAnimator.ofInt(junk_title_backg, "backgroundColor", getResources().getColor(R.color.app_color_second), getResources().getColor(R.color.app_color_third));
                colorAnim.setDuration(2000);
                colorAnim.setRepeatCount(0);
                colorAnim.setEvaluator(new ArgbEvaluator());
                colorAnim.start();
            }
        }
    }

    @Override
    public void addRamdata(long size, List<JunkInfo> list) {
        adapterRam.addDataList(list);
    }

    @Override
    public void setCleanDAta(long size) {
        junk_button_clean.setText(getResources().getText(R.string.ram_button) + "(" + CommonUtil.getFileSize4(size) + ")");
    }

    @Override
    public void cleanAnimation(List<JunkInfo> cleanList, final long cleanSize) {
        Log.e("aaa", "===ram清理");
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
                        bundle.putLong("size", cleanSize);
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

                case R.id.junk_button_clean:
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
            setResult(1);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if ("notifi".equals(getIntent().getStringExtra("from"))) {
            jumpTo(MainActivity.class);
        }
        finish();
    }
}
