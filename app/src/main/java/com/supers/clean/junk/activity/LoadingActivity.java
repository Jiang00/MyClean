package com.supers.clean.junk.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.android.client.ClientNativeAd;
import com.supers.clean.junk.R;
import com.supers.clean.junk.modle.CommonUtil;
import com.supers.clean.junk.modle.PreData;
import com.supers.clean.junk.modle.ShortCutUtils;
import com.supers.clean.junk.modle.entity.Contents;

/**
 * Created by on 2017/3/8.
 */

public class LoadingActivity extends BaseActivity {
    Handler myHandler;

    TextView tv_tiaoguo;
    LinearLayout ll_ad;
    private View nativeView;
    private static final String TAG_LOADING = "junk_loading";
    int mimmi;

    @Override
    protected void findId() {
        super.findId();
        tv_tiaoguo = (TextView) findViewById(R.id.tv_tiaoguo);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        AndroidSdk.Builder builder = new AndroidSdk.Builder();
        AndroidSdk.onCreate(this, builder);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading);
        ShortCutUtils.addShortcut(this);
        myHandler = new Handler();
        mimmi = 5;
        int random = (int) (Math.random() * 100) + 1;
//        if (random <= PreData.getDB(this, Contents.KEY_LOADING_GAI, 100)) {
//            myHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    addNative();
//                }
//            }, 1000);
//        } else {
        tv_tiaoguo.setVisibility(View.INVISIBLE);
        myHandler.removeCallbacks(runnable1);
        myHandler.postDelayed(runnable1, 2000);
//        }


        tv_tiaoguo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myHandler.removeCallbacks(runnable);
                jumpTo(MainActivity.class);
                finish();
            }
        });

    }

    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            String resolution = "";
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int a = CommonUtil.dp2px(360);
            jumpTo(MainActivity.class);
            finish();
        }
    };
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mimmi -= 1;
            if (tv_tiaoguo != null) {
                tv_tiaoguo.setVisibility(View.VISIBLE);
                tv_tiaoguo.setText(mimmi + "s " + getString(R.string.loading_tiaoguo));
                if (mimmi == 0) {
                    tv_tiaoguo.performClick();
                } else {
                    myHandler.postDelayed(runnable, 1000);
                }
            }
        }
    };

    private void addNative() {
        if (AndroidSdk.hasNativeAd(TAG_LOADING, AndroidSdk.NATIVE_AD_TYPE_ALL)) {
            AndroidSdk.loadNativeAd(TAG_LOADING, R.layout.native_ad_loading, new ClientNativeAd.NativeAdLoadListener() {
                @Override
                public void onNativeAdLoadSuccess(View view) {
                    if (ll_ad != null) {
                        ll_ad.addView(view);
                        Animation animation = AnimationUtils.loadAnimation(LoadingActivity.this, R.anim.tran_left_in);
                        ll_ad.startAnimation(animation);
                        animation.setFillAfter(true);
                        ll_ad.setVisibility(View.VISIBLE);
                        myHandler.removeCallbacks(runnable);
                        myHandler.postDelayed(runnable, 1000);
                    }
                }

                @Override
                public void onNativeAdLoadFails() {
                    tv_tiaoguo.setVisibility(View.GONE);
                    myHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            jumpTo(MainActivity.class);
                            finish();
                        }
                    }, 2000);
                }
            });
        } else {
            tv_tiaoguo.setVisibility(View.GONE);
            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    jumpTo(MainActivity.class);
                    finish();
                }
            }, 3000);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        AndroidSdk.onResumeWithoutTransition(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        myHandler.removeCallbacks(runnable);
        myHandler.removeCallbacks(runnable1);
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
