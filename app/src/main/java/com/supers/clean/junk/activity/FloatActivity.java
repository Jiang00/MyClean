
package com.supers.clean.junk.activity;
import com.supers.clean.junk.activity.BaseActivity;
//
//import android.animation.ArgbEvaluator;
//import android.animation.ObjectAnimator;
//import android.animation.ValueAnimator;
//import android.app.Activity;
//import android.app.ActivityManager;
//import android.content.ComponentName;
//import android.content.ContentResolver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageInfo;
//import android.graphics.PorterDuff;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.ColorDrawable;
//import android.graphics.drawable.Drawable;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.provider.Settings;
//import android.util.Log;
//import android.view.HapticFeedbackConstants;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.android.client.AndroidSdk;
//import com.android.client.ClientNativeAd;
//import com.ivy.mobi.clear.ImageAccessor;
//import com.ivy.mobi.clear.LinerAccessor;
//import com.ivy.mobi.clear.R;
//import com.ivy.mobi.clear.adapter.HorizontalListViewAdapter;
//import com.ivy.mobi.clear.biz.MemoryManager;
//import com.ivy.mobi.clear.biz.RamTask;
//import com.ivy.mobi.clear.biz.SimpleTask;
//import com.ivy.mobi.clear.entity.FileInfo;
//import com.ivy.mobi.clear.ui.CleanApplication;
//import com.ivy.mobi.clear.utils.CheckState;
//import com.ivy.mobi.clear.utils.CommonUtil;
//import com.ivy.mobi.clear.utils.SwitchControl;
//import com.ivy.mobi.clear.view.FloatWaterView;
//import com.ivy.mobi.clear.view.HorizontalListView;
//import com.ivy.module.tweenengine.Tween;
//import com.ivy.module.tweenengine.TweenEquations;
//import com.ivy.module.tweenengine.TweenManager;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.BindViews;
//
//
///**
// * Created by Ivy on 2016/12/1.
// */
//
public class FloatActivity extends BaseActivity {

}
//    @BindView(R.id.horizontal_listview)
//    HorizontalListView listview;
//    private HorizontalListViewAdapter adapter;
//    RamTask runing;
//    Handler myHandler;
//    List<FileInfo> listFloat;
//    @BindView(R.id.water_view)
//    FloatWaterView waterView;
//    @BindView(R.id.ll)
//    LinearLayout ll;
//    @BindView(R.id.fl_dian)
//    FrameLayout fl_dian;
//    @BindView(R.id.iv_wifi)
//    ImageView iv_wifi;
//    @BindView(R.id.ll_wifi)
//    LinearLayout ll_wifi;
//    @BindView(R.id.tv_wifi)
//    TextView tv_wifi;
//
//    @BindView(R.id.iv_liuliang)
//    ImageView iv_liuliang;
//    @BindView(R.id.ll_liuliang)
//    LinearLayout ll_liuliang;
//    @BindView(R.id.tv_liuliang)
//    TextView tv_liuliang;
//
//    @BindView(R.id.iv_xianshi)
//    ImageView iv_xianshi;
//    @BindView(R.id.ll_xianshi)
//    LinearLayout ll_xianshi;
//    @BindView(R.id.tv_xianshi)
//    TextView tv_xianshi;
//
//    @BindView(R.id.iv_shengyin)
//    ImageView iv_shengyin;
//    @BindView(R.id.ll_shengyin)
//    LinearLayout ll_shengyin;
//    @BindView(R.id.tv_shengyin)
//    TextView tv_shengyin;
//
//    @BindView(R.id.iv_gps)
//    ImageView iv_gps;
//    @BindView(R.id.ll_gps)
//    LinearLayout ll_gps;
//    @BindView(R.id.tv_gps)
//    TextView tv_gps;
//    @BindView(R.id.ll_ad)
//    LinearLayout ll_ad;
//    private float[][] dian;
//    private TweenManager tweenManager;
//    private boolean isrunTween = true;
//    private int memory = 0;
//    private CleanApplication cleanApplication;
//
//    public void initId() {
//        tweenManager = new TweenManager();
//        setAnimationThread();
//        isrunTween = true;
//        Tween.registerAccessor(ImageView.class, new ImageAccessor());
//        Tween.registerAccessor(LinearLayout.class, new LinerAccessor());
//        int count = 0;
//        dian = new float[20][3];
//        int fl = (int) getResources().getDimension(R.dimen.float_dian_size);
//        int qiu = (int) getResources().getDimension(R.dimen.float_qiu_size);
//        int r = fl / 2;
//        while (count < 20) {
//            int x = (int) (Math.random() * (fl));
//            int y = (int) (Math.random() * (fl));
//            double sqrt = Math.sqrt((x - r) * (x - r) + (y - r) * (y - r));
//            if (sqrt > qiu/2 && sqrt < r) {
//                float alph = (float) (sqrt / r);
//                if (alph > 0.8) {
//                    alph = (float) 0.8;
//                }
//                dian[count] = new float[]{x, y, alph};
//                count++;
//            }
//        }
//        memory = CommonUtil.getMemory(this);
//        if (memory > 40 && memory <= 80) {
//            ll.setBackgroundColor(getResources().getColor(R.color.clear2));
//        } else if (memory > 80) {
//            ll.setBackgroundColor(getResources().getColor(R.color.clear3));
//        } else {
//            ll.setBackgroundColor(getResources().getColor(R.color.clear1));
//        }
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout_float_activity);
//        cleanApplication = (CleanApplication) getApplication();
//        initId();
//        listFloat = new ArrayList<>();
//        adapter = new HorizontalListViewAdapter(this);
//        myHandler = new Handler();
//        listview.setAdapter(adapter);
//        postAd();
//        anysinit();
//        addListener();
//    }
//
//    private String TAG_DIALOG = "float_clean";
//    private View nativeView;
//
//    private void postAd() {
//        if (AndroidSdk.hasNativeAd(TAG_DIALOG, AndroidSdk.NATIVE_AD_TYPE_ALL)) {
//            AndroidSdk.loadNativeAd(TAG_DIALOG, R.layout.native_ad, new ClientNativeAd.NativeAdLoadListener() {
//                @Override
//                public void onNativeAdLoadSuccess(View view) {
//                    if (ll_ad != null) {
//                        ll_ad.addView(view);
//                    }
//                }
//
//                @Override
//                public void onNativeAdLoadFails() {
//
//                }
//            });
////            if (nativeView != null)
////                AndroidSdk.destroyNativeAdView(TAG_DIALOG, nativeView);
////            nativeView = AndroidSdk.peekNativeAdViewWithLayout(TAG_DIALOG, AndroidSdk.NATIVE_AD_TYPE_ALL, R.layout.native_ad, new ClientNativeAd.NativeAdClickListener() {
////                @Override
////                public void onNativeAdClicked(ClientNativeAd clientNativeAd) {
////                }
////            });
////            if (nativeView != null && ll_ad != null) {
////                ll_ad.addView(nativeView);
////            } else {
////
////            }
//        } else {
//
//        }
//    }
//
////    public void blurBitmap() {
////        // 1.构建Bitmap
////        WindowManager windowManager = getWindowManager();
////        Display display = windowManager.getDefaultDisplay();
////        int  w = display.getWidth();
////        int  h = display.getHeight();
////        //找到图片
////        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.float_beijing);
////        // 压缩并保存位图
////        Matrix matrix = new Matrix();
////        //压缩倍数
////        matrix.postScale(1.0f / 6, 1.0f / 6);
////        // 新的压缩位图
////        Bitmap mCompressBitmap = Bitmap.createBitmap(mBitmap, 0, 0,
////                mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
////        //mohu
////        Bitmap overlay = StackBlur.blurNatively(mCompressBitmap, (int) 3, false);
////        rl_backg.setImageBitmap(overlay);
////    }
//
//    private void wifi() {
//        Drawable d = iv_wifi.getDrawable();
//        BitmapDrawable bitmap = (BitmapDrawable) d;
//        if (CheckState.wifiState(this)) {
//            if (memory > 40 && memory <= 80) {
//                bitmap.setColorFilter(getResources().getColor(R.color.clear2), PorterDuff.Mode.SRC_ATOP);
//                tv_wifi.setTextColor(getResources().getColor(R.color.clear2));
//            } else if (memory > 80) {
//                bitmap.setColorFilter(getResources().getColor(R.color.clear3), PorterDuff.Mode.SRC_ATOP);
//                tv_wifi.setTextColor(getResources().getColor(R.color.clear3));
//            } else {
//                bitmap.setColorFilter(getResources().getColor(R.color.clear1), PorterDuff.Mode.SRC_ATOP);
//                tv_wifi.setTextColor(getResources().getColor(R.color.clear1));
//            }
//        } else {
//            bitmap.setColorFilter(0, PorterDuff.Mode.SRC_ATOP);
//            tv_wifi.setTextColor(getResources().getColor(R.color.textcolor));
//        }
//    }
//
//    private void wifiD() {
//        Drawable d = iv_wifi.getDrawable();
//        BitmapDrawable bitmap = (BitmapDrawable) d;
//        if (!CheckState.wifiState(this)) {
//            if (memory > 40 && memory <= 80) {
//                bitmap.setColorFilter(getResources().getColor(R.color.clear2), PorterDuff.Mode.SRC_ATOP);
//                tv_wifi.setTextColor(getResources().getColor(R.color.clear2));
//            } else if (memory > 80) {
//                bitmap.setColorFilter(getResources().getColor(R.color.clear3), PorterDuff.Mode.SRC_ATOP);
//                tv_wifi.setTextColor(getResources().getColor(R.color.clear3));
//            } else {
//                bitmap.setColorFilter(getResources().getColor(R.color.clear1), PorterDuff.Mode.SRC_ATOP);
//                tv_wifi.setTextColor(getResources().getColor(R.color.clear1));
//            }
//        } else {
//            bitmap.setColorFilter(0, PorterDuff.Mode.SRC_ATOP);
//            tv_wifi.setTextColor(getResources().getColor(R.color.textcolor));
//        }
//    }
//
//    private void liuLiang() {
//        Drawable d = iv_liuliang.getDrawable();
//        BitmapDrawable bitmap = (BitmapDrawable) d;
//        if (CheckState.networkState(this, null)) {
//            if (memory > 40 && memory <= 80) {
//                bitmap.setColorFilter(getResources().getColor(R.color.clear2), PorterDuff.Mode.SRC_ATOP);
//                tv_liuliang.setTextColor(getResources().getColor(R.color.clear2));
//            } else if (memory > 80) {
//                bitmap.setColorFilter(getResources().getColor(R.color.clear3), PorterDuff.Mode.SRC_ATOP);
//                tv_liuliang.setTextColor(getResources().getColor(R.color.clear3));
//            } else {
//                bitmap.setColorFilter(getResources().getColor(R.color.clear1), PorterDuff.Mode.SRC_ATOP);
//                tv_liuliang.setTextColor(getResources().getColor(R.color.clear1));
//            }
//        } else {
//            bitmap.setColorFilter(0, PorterDuff.Mode.SRC_ATOP);
//            tv_liuliang.setTextColor(getResources().getColor(R.color.textcolor));
//        }
//    }
//
//    private void liuLiangd() {
//        Drawable d = iv_liuliang.getDrawable();
//        BitmapDrawable bitmap = (BitmapDrawable) d;
//        if (!CheckState.networkState(this, null)) {
//            if (memory > 40 && memory <= 80) {
//                bitmap.setColorFilter(getResources().getColor(R.color.clear2), PorterDuff.Mode.SRC_ATOP);
//                tv_liuliang.setTextColor(getResources().getColor(R.color.clear2));
//            } else if (memory > 80) {
//                bitmap.setColorFilter(getResources().getColor(R.color.clear3), PorterDuff.Mode.SRC_ATOP);
//                tv_liuliang.setTextColor(getResources().getColor(R.color.clear3));
//            } else {
//                bitmap.setColorFilter(getResources().getColor(R.color.clear1), PorterDuff.Mode.SRC_ATOP);
//                tv_liuliang.setTextColor(getResources().getColor(R.color.clear1));
//            }
//        } else {
//            bitmap.setColorFilter(0, PorterDuff.Mode.SRC_ATOP);
//            tv_liuliang.setTextColor(getResources().getColor(R.color.textcolor));
//        }
//    }
//
//    private void xianShi() {
//        Drawable d = iv_xianshi.getDrawable();
//        BitmapDrawable bitmap = (BitmapDrawable) d;
//        if (memory > 40 && memory <= 80) {
//            bitmap.setColorFilter(getResources().getColor(R.color.clear2), PorterDuff.Mode.SRC_ATOP);
//            tv_xianshi.setTextColor(getResources().getColor(R.color.clear2));
//        } else if (memory > 80) {
//            bitmap.setColorFilter(getResources().getColor(R.color.clear3), PorterDuff.Mode.SRC_ATOP);
//            tv_xianshi.setTextColor(getResources().getColor(R.color.clear3));
//        } else {
//            bitmap.setColorFilter(getResources().getColor(R.color.clear1), PorterDuff.Mode.SRC_ATOP);
//            tv_xianshi.setTextColor(getResources().getColor(R.color.clear1));
//        }
//    }
//
//    private void xianshiD() {
//        int light = getLight();
//        if (light > 0 && light <= LIGHT_NORMAL) {
//            //低亮度
//            iv_xianshi.setImageResource(R.drawable.xianshi_di);
//        } else if (light > LIGHT_NORMAL && light < LIGHT_100_PERCENT) {
//            //中
//            iv_xianshi.setImageResource(R.drawable.xianshi_zhong);
//        } else if (light == LIGHT_100_PERCENT) {
//            //高
//            iv_xianshi.setImageResource(R.drawable.xianshi_gao);
//        } else if (light == 0) {
//            //自动
//            iv_xianshi.setImageResource(R.drawable.xianshi_zidong);
//        }
//    }
//
//    private void shengYin() {
//        Drawable d = iv_shengyin.getDrawable();
//        BitmapDrawable bitmap = (BitmapDrawable) d;
//        if (CheckState.soundState(this)) {
//            if (memory > 40 && memory <= 80) {
//                bitmap.setColorFilter(getResources().getColor(R.color.clear2), PorterDuff.Mode.SRC_ATOP);
//                tv_shengyin.setTextColor(getResources().getColor(R.color.clear2));
//            } else if (memory > 80) {
//                bitmap.setColorFilter(getResources().getColor(R.color.clear3), PorterDuff.Mode.SRC_ATOP);
//                tv_shengyin.setTextColor(getResources().getColor(R.color.clear3));
//            } else {
//                bitmap.setColorFilter(getResources().getColor(R.color.clear1), PorterDuff.Mode.SRC_ATOP);
//                tv_shengyin.setTextColor(getResources().getColor(R.color.clear1));
//            }
//        } else {
//            bitmap.setColorFilter(0, PorterDuff.Mode.SRC_ATOP);
//            tv_shengyin.setTextColor(getResources().getColor(R.color.textcolor));
//        }
//    }
//
//    private void gps() {
//        Drawable d = iv_gps.getDrawable();
//        BitmapDrawable bitmap = (BitmapDrawable) d;
//        if (CheckState.gpsState(this)) {
//            if (memory > 40 && memory <= 80) {
//                bitmap.setColorFilter(getResources().getColor(R.color.clear2), PorterDuff.Mode.SRC_ATOP);
//                tv_gps.setTextColor(getResources().getColor(R.color.clear2));
//            } else if (memory > 80) {
//                bitmap.setColorFilter(getResources().getColor(R.color.clear3), PorterDuff.Mode.SRC_ATOP);
//                tv_gps.setTextColor(getResources().getColor(R.color.clear3));
//            } else {
//                bitmap.setColorFilter(getResources().getColor(R.color.clear1), PorterDuff.Mode.SRC_ATOP);
//                tv_gps.setTextColor(getResources().getColor(R.color.clear1));
//            }
//        } else {
//            bitmap.setColorFilter(0, PorterDuff.Mode.SRC_ATOP);
//            tv_gps.setTextColor(getResources().getColor(R.color.textcolor));
//        }
//    }
//
//    long allSize;
//
//    private void anysinit() {
//        listFloat = cleanApplication.getAppRam();
//        adapter.clear();
//        if (listFloat.size() == 0) {
//            runing = new RamTask(this, new SimpleTask.SimpleTaskListener() {
//                @Override
//                public void startLoad() {
//
//                }
//
//                @Override
//                public void loading(FileInfo fileInfo, long size) {
//
//                }
//
//                @Override
//                public void cancelLoading() {
//
//                }
//
//                @Override
//                public void finishLoading(final long dataSize, final ArrayList<FileInfo> dataList) {
//                    myHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            allSize = dataSize;
//                            adapter.addDataList(dataList);
//                            adapter.notifyDataSetChanged();
//                            waterView.setOnClickListener(WaterViewOnclick);
//                        }
//                    });
//                }
//            });
//            runing.start();
//        } else {
//            allSize = cleanApplication.getRamSize();
//            adapter.addDataList(listFloat);
//            adapter.notifyDataSetChanged();
//            waterView.setOnClickListener(WaterViewOnclick);
//        }
//        wifi();
//        shengYin();
//        xianshiD();
//        xianShi();
//    }
//
//    private void addListener() {
//        waterView.setFloatWaterListener(floatWaterListener);
//        ll_wifi.setOnClickListener(kuaijieListener);
//        ll_liuliang.setOnClickListener(kuaijieListener);
//        ll_xianshi.setOnClickListener(kuaijieListener);
//        ll_shengyin.setOnClickListener(kuaijieListener);
//        ll_gps.setOnClickListener(kuaijieListener);
////        listview.setOnItemLongClickListener(horizontalListener);
//    }
//
//    AdapterView.OnItemLongClickListener horizontalListener = new AdapterView.OnItemLongClickListener() {
//        @Override
//        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//            view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY,
//                    HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING
//                            | HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
//            adapter.removeData(position);
//            adapter.notifyDataSetChanged();
//            return false;
//        }
//    };
//    FloatWaterView.FloatWaterListener floatWaterListener = new FloatWaterView.FloatWaterListener() {
//        @Override
//        public void success() {
//            startDian();
//        }
//    };
//
//    View.OnClickListener WaterViewOnclick = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            waterView.setOnClickListener(null);
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    killAll(FloatActivity.this);
//                }
//            }).start();
//            setListAnimation();
//            memory = CommonUtil.getMemory(FloatActivity.this);
//            waterView.setPratent(memory);
//            ValueAnimator colorAnim;
//            int color = 0xff1a9af3;
//            Drawable background = ll.getBackground();
//            if (background instanceof ColorDrawable) {
//                ColorDrawable colordDrawable = (ColorDrawable) background;
//                color = colordDrawable.getColor();
//            }
//            if (memory > 40 && memory <= 80) {
//                colorAnim = ObjectAnimator.ofInt(ll, "backgroundColor", color, getResources().getColor(R.color.clear2));
//                colorAnim.setDuration(2000);
//                colorAnim.setRepeatCount(0);
//                colorAnim.setEvaluator(new ArgbEvaluator());
//                colorAnim.start();
//            } else if (memory > 80) {
//                colorAnim = ObjectAnimator.ofInt(ll, "backgroundColor", color, getResources().getColor(R.color.clear3));
//                colorAnim.setDuration(2000);
//                colorAnim.setRepeatCount(0);
//                colorAnim.setEvaluator(new ArgbEvaluator());
//                colorAnim.start();
//            } else {
//                colorAnim = ObjectAnimator.ofInt(ll, "backgroundColor", color, getResources().getColor(R.color.clear1));
//                colorAnim.setDuration(2000);
//                colorAnim.setRepeatCount(0);
//                colorAnim.setEvaluator(new ArgbEvaluator());
//                colorAnim.start();
//            }
//        }
//    };
//    View.OnClickListener kuaijieListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.ll_wifi:
//                    wifiD();
//                    SwitchControl.switchWifi(FloatActivity.this);
//                    break;
//                case R.id.ll_liuliang:
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        try {
//                            Intent intentNet = new Intent().addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intentNet.setComponent(new ComponentName("com.android.settings",
//                                    "com.android.settings.Settings$DataUsageSummaryActivity"));
//                            startActivity(intentNet);
//                        } catch (Exception ee) {
//                        }
//                    } else {
//                        liuLiangd();
//                        SwitchControl.setMobileData(FloatActivity.this, !CheckState.networkState(FloatActivity.this, null));
//                    }
//
//
//                    break;
//                case R.id.ll_xianshi:
//                    brightnessSwitchUtils(FloatActivity.this);
//                    xianShi();
//                    break;
//                case R.id.ll_shengyin:
//                    SwitchControl.switchSound(FloatActivity.this);
//                    shengYin();
//                    break;
//                case R.id.ll_gps:
////                    SwitchControl.toggleGPS(FloatActivity.this);
//                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                    startActivityForResult(intent, 0);
//                    break;
//            }
//        }
//    };
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        AndroidSdk.onResumeWithoutTransition(this);
//        waterView.upDate(memory);
//        waterView.start();
//        liuLiang();
//        gps();
//    }
//
//    private void setListAnimation() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                int a = adapter.getCount();
//                int time = 100;
//                for (int i = 0; i < a; i++) {
//                    try {
//                        if (i > 10) {
//                            time = 30;
//                        }
//                        Thread.sleep(time--);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    runOnUiThread(new Runnable() {
//                        public void run() {
//                            if (adapter.getData().size() != 0) {
//                                adapter.removeData(0);
//                                adapter.notifyDataSetChanged();
//                            }
//                        }
//                    });
//                }
//            }
//        }).start();
//    }
//
//    public void startDian() {
//        // mHandler.removeCallbacks(mUpdateTimeTask);
//        final float grop = (int) getResources().getDimension(R.dimen.float_qiu_size) / 2;
//        Log.e("aaa", "aa+" + grop);
//        final float vxy = dp2px(15) / 2;
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < 20; i++) {
//
////                    try {
////                        Thread.sleep(10);
////                    } catch (InterruptedException e) {
////                        e.printStackTrace();
////                    }
//                    final int finalI = i;
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            iv[finalI].setX(grop - vxy);
//                            iv[finalI].setY(grop - vxy);
//                            iv[finalI].setAlpha(0f);
//                            iv[finalI].setScaleX(0.3f);
//                            iv[finalI].setScaleY(0.3f);
//                            iv[finalI].setVisibility(View.VISIBLE);
//                            Tween.to(iv[finalI], ImageAccessor.BOUNCE_EFFECT, 0.5f).target(dian[finalI][0], dian[finalI][1], 0.3f, 1)
//                                    .ease(TweenEquations.easeInQuad).delay(0)
//                                    .start(tweenManager);
//                        }
//                    });
//
//                }
//                myHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        fl_dian.setVisibility(View.GONE);
//                        wifi();
//                        xianShi();
//                        liuLiang();
//                        gps();
//                        shengYin();
//                    }
//                }, 500);
//            }
//        }).start();
//    }
//
//    private void setAnimationThread() {
//        new Thread(new Runnable() {
//            private long lastMillis = -1;
//
//            public void run() {
//                while (isrunTween) {
//                    if (lastMillis > 0) {
//                        long currentMillis = System.currentTimeMillis();
//                        final float delta = (currentMillis - lastMillis) / 1000f;
//
//                        runOnUiThread(new Runnable() {
//
//                            public void run() {
//                                tweenManager.update(delta);
//
//                            }
//                        });
//
//                        lastMillis = currentMillis;
//                    } else {
//                        lastMillis = System.currentTimeMillis();
//                    }
//                    try {
//                        Thread.sleep(1000 / 60);
//                    } catch (InterruptedException ex) {
//                    }
//                }
//            }
//        }).start();
//    }
//
//    public int killAll(Context context) {
//        long ram_all = MemoryManager.getPhoneTotalRamMemory();
//        final ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        final List<PackageInfo> installedPackages = context.getPackageManager().getInstalledPackages(0);
//        for (PackageInfo packageInfo : installedPackages) {
//            if (packageInfo.packageName.equals(context.getPackageName())) {
//                continue;
//            }
//            try {
//                am.killBackgroundProcesses(packageInfo.packageName);
//            } catch (Exception e) {
//
//            }
//        }
//        final long afterMem = getAvailMemory(am);
//        int pratent = (int) ((ram_all - afterMem) * 100 / ram_all);
//        return pratent;
//    }
//
//    private long getAvailMemory(ActivityManager am) {
//        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
//        am.getMemoryInfo(mi);
//        //mi.availMem; 当前系统的可用内存
//        return (mi.availMem);
//    }
//
//    private int getLight() {
//        int light = 0;
//        ContentResolver cr = this.getContentResolver();
//        try {
//            boolean auto = Settings.System.getInt(cr,
//                    Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
//            light = Settings.System.getInt(cr,
//                    Settings.System.SCREEN_BRIGHTNESS, -1);
//        } catch (Settings.SettingNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        return light;
//    }
//
//    private static final int LIGHT_NORMAL = 64;
//    private static final int LIGHT_50_PERCENT = 127;
//    //    private static final int LIGHT_75_PERCENT = 191;
//    private static final int LIGHT_100_PERCENT = 255;
//    private static final int LIGHT_AUTO = 0;
//    private static final int LIGHT_ERR = -1;
//
//    public void brightnessSwitchUtils(Context context) {
//        int light = 0;
//        ContentResolver cr = context.getContentResolver();
//        try {
//            boolean auto = Settings.System.getInt(cr,
//                    Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
//
//            if (!auto) {
//                light = Settings.System.getInt(cr,
//                        Settings.System.SCREEN_BRIGHTNESS, -1);
//                if (light > 0 && light <= LIGHT_NORMAL) {
//                    light = LIGHT_NORMAL;
//                } else if (light > LIGHT_NORMAL && light <= LIGHT_50_PERCENT) {
//                    light = LIGHT_50_PERCENT;
//                } else {
//                    light = LIGHT_100_PERCENT;
//                }
//            } else {
//                light = LIGHT_AUTO;
//            }
//            switch (light) {
//                case LIGHT_NORMAL:
//                    light = LIGHT_50_PERCENT - 1;
//                    iv_xianshi.setImageResource(R.drawable.xianshi_zhong);
//                    break;
//                case LIGHT_50_PERCENT:
//                    light = LIGHT_100_PERCENT - 1;
//                    iv_xianshi.setImageResource(R.drawable.xianshi_gao);
//                    break;
//                case LIGHT_100_PERCENT:
//                    light = LIGHT_NORMAL - 1;
//                    Settings.System.putInt(cr,
//                            Settings.System.SCREEN_BRIGHTNESS_MODE,
//                            Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
//                    iv_xianshi.setImageResource(R.drawable.xianshi_zidong);
//                    break;
//                case LIGHT_AUTO:
//                    light = LIGHT_NORMAL - 1;
//                    Settings.System.putInt(cr,
//                            Settings.System.SCREEN_BRIGHTNESS_MODE,
//                            Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
//                    iv_xianshi.setImageResource(R.drawable.xianshi_di);
//
//                    break;
//                case LIGHT_ERR:
//                    light = LIGHT_NORMAL - 1;
//                    break;
//
//            }
//
//            changeAppBrightness(context, light);
//            Settings.System.putInt(cr,
//                    Settings.System.SCREEN_BRIGHTNESS, light);
//
//        } catch (Settings.SettingNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//    }
//
//    public void changeAppBrightness(Context context, int brightness) {
//        Window window = ((Activity) context).getWindow();
//        WindowManager.LayoutParams lp = window.getAttributes();
//        if (brightness == -1) {
//            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
//        } else {
//            lp.screenBrightness = (brightness <= 0 ? 1 : brightness) / 255f;
//        }
//        window.setAttributes(lp);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        waterView.stop();
//        if (runing != null)
//            runing.cancelTask();
//        isrunTween = false;
//    }
//
//}
