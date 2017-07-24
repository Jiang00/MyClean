package com.easy.junk.easyactivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.easy.clean.easyutils.PreData;
import com.easy.junk.R;
import com.easy.junk.easycustomview.DrawHookView;
import com.easy.junk.easycustomview.FlakeView;
import com.easy.junk.easycustomview.Rotate3d;
import com.easy.junk.easytools.MyConstant;

import java.util.Random;


/**
 * Created by on 2017/3/2.
 */

public class BatteriesActivity extends BaseActivity {
    private static final int FLAKE_NUM = 5;
    TextView cooling_wendu;
    FrameLayout title_left;
    LinearLayout cooling_piao;
    //    FrameLayout cooling_fl;
    private Animation suo;
    private Animation rotate_zheng;
    private Random random;
    private int time;
    private FlakeView flakeView;
    LinearLayout cooling_text;
    //    ImageView cooling_xuehua;
    ImageView cooling_2;
    TextView title_name;
    ImageView cooling_xuehua, cooling_dianyuan;
    ViewGroup mContainer;
    DrawHookView cooling_drawhookview;

    private Handler mHandler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (flakeView != null) {
                flakeView.addFlakes(FLAKE_NUM);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cooling);
        AndroidSdk.loadFullAd(AndroidSdk.FULL_TAG_PAUSE);

        title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        title_name.setText(R.string.main_cooling_name);

        rotate_zheng = AnimationUtils.loadAnimation(this, R.anim.rotate_cooling);
        suo = AnimationUtils.loadAnimation(this, R.anim.suo);
        mHandler = new Handler();

        cooling_xuehua.setClickable(true);
        cooling_xuehua.setFocusable(true);

//        cooling_xuehua.startAnimation(rotate_zheng);
        startCoolingAni();



     /*   suo.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                cooling_fl.setVisibility(View.INVISIBLE);
                cooling_xuehua.clearAnimation();
                if (PreData.getDB(BatteriesActivity.this, MyConstant.FULL_COOL, 0) == 1) {
                    AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
                }
                Bundle bundle = new Bundle();
                bundle.putInt("wendu", time);
                bundle.putString("from", "cooling");
                jumpToActivity(SucceedActivity.class, bundle, 1);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
            @Override
            public void onAnimationStart(Animation animation) {

            }
        });*/
        if (TextUtils.equals("main", getIntent().getStringExtra("from"))) {
            cooling_text.setVisibility(View.VISIBLE);
            final int wendu = getIntent().getIntExtra("wendu", 40);
            cooling_wendu.setText(wendu + "℃");
            for (int i = 0; i <= time; i++) {
                cooling_wendu.setText((wendu - i) + "℃");
            }
            new Thread(new Runnable() {
                @Override
                public void run() {

                    for (int i = 0; i <= time; i++) {
                        if (onPause) {
                            return;
                        }
                        final int finalI = i;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cooling_wendu.setText((wendu - finalI) + "℃");
                            }
                        });
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        cooling_piao = (LinearLayout) findViewById(R.id.cooling_piao);
        cooling_dianyuan = (ImageView) findViewById(R.id.cooling_dianyuan);
//        cooling_fl = (FrameLayout) findViewById(R.id.cooling_fl);
        cooling_text = (LinearLayout) findViewById(R.id.cooling_text);
        cooling_2 = (ImageView) findViewById(R.id.cooling_2);
        cooling_wendu = (TextView) findViewById(R.id.cooling_wendu);
        cooling_xuehua = (ImageView) findViewById(R.id.cooling_xuehua);
        mContainer = (ViewGroup) findViewById(R.id.fl);///找到FrameLayout
        cooling_drawhookview = (DrawHookView) findViewById(R.id.cooling_drawhookview);
    }

    @Override
    protected void onResume() {
        super.onResume();
        flakeView = new FlakeView(this);
        cooling_piao.addView(flakeView);
        mHandler.post(runnable);
    }

    @Override
    public void onBackPressed() {
        if ("notifi".equals(getIntent().getStringExtra("from"))) {
            jumpTo(MainActivity.class);
        } else {
            setResult(MyConstant.COOLING_RESUIL, new Intent().putExtra("wendu", time));
        }
        finish();
    }

    ObjectAnimator animator1, animator2, animator3, animator4;
    AnimatorSet animSet;

    @Override
    protected void onPause() {
        super.onPause();
        hideSnow();
    }

    private void startCoolingAni() {
        random = new Random();
        time = random.nextInt(5) + 1;
//        cooling_wendu.setText(time + "℃");
        LinearInterpolator linearInterpolator = new LinearInterpolator();
        animator1 = ObjectAnimator.ofFloat(cooling_dianyuan, "rotation", 0f, 360f);
        animator1.setInterpolator(linearInterpolator);
        animator1.setDuration(300);
        animator1.setRepeatCount(5);
        animator1.start();

        final ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 20);

        animSet = new AnimatorSet();
        animator2 = ObjectAnimator.ofFloat(cooling_2, "scaleY", 1f, 1.5f);
        animator3 = ObjectAnimator.ofFloat(cooling_2, "scaleX", 1f, 1.5f);
        animator4 = ObjectAnimator.ofFloat(cooling_2, "alpha", 1f, 0f);
        animSet.setDuration(1500);
        animSet.play(animator2).with(animator3).with(animator4);
        animSet.start();
        animSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (animSet != null) {
                    animSet.pause();
                }
                if (animator1 != null) {
                    animator1.pause();
                }
                cooling_dianyuan.setVisibility(View.GONE);
                cooling_2.setAlpha(1f);
                cooling_2.setScaleX(1f);
                cooling_2.setScaleY(1f);
                cooling_2.setVisibility(View.VISIBLE);
                applyRotation(0, 0, 180);//左旋90度
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()

        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                if (value == 20) {
//                    cooling_fl.startAnimation(suo);
//                    hideSnow();
                }
            }
        });
//        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
//        valueAnimator.setDuration(2000);
//        valueAnimator.start();
      /*  mHandler.postDelayed(new

                                     Runnable() {
                                         @Override
                                         public void run() {
//                cooling_fl.setVisibility(View.INVISIBLE);
//                cooling_xuehua.clearAnimation();
                                             if (PreData.getDB(BatteriesActivity.this, MyConstant.FULL_COOL, 0) == 1) {
                                                 AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
                                             }
                                             Bundle bundle = new Bundle();
                                             bundle.putInt("wendu", time);
                                             bundle.putString("from", "cooling");
//                jumpToActivity(SucceedActivity.class, bundle, 1);
                                         }
                                     }, 4000);*/
    }

    //应用变换的方法，里面将会使用之前写好的Rotate3d类
    private void applyRotation(int position, float start, float end) {
        // Find the center of the container
        //获取FrameLayout的x、y值。这样图片在翻转的时候会以这个x、y值为中心翻转。
        //这就是为什么我要用FrameLayout的原因。如果直接使用的是父容器RelativeLayout将会以RelativeLayout的中心为轴心
        //翻转。由于我的图片不是处于RelativeLayout的中心，翻转时就会有差错.效果可以看看下面的图片。
        //当然，有时候你就想要那样的效果。你也可以在自行调整centerX和centerY的值来达到你想要的效果
        final float centerX = mContainer.getWidth() / 2.0f;
        final float centerY = mContainer.getHeight() / 2.0f;
        final Rotate3d rotation =
                new Rotate3d(start, end, centerX, centerY, 360, true);
        rotation.setDuration(1000); //可设置翻转的时间，以ms为单位
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new DisplayNextView());
        mContainer.startAnimation(rotation);  //开始翻转前90度
    }

    /*
     * 这个类用于监听前90度翻转完成
     */
    private final class DisplayNextView implements Animation.AnimationListener {
        private DisplayNextView() {
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            hideSnow();
            mContainer.setVisibility(View.GONE);
            cooling_xuehua.setVisibility(View.GONE);
            cooling_drawhookview.startProgress(500);
            cooling_drawhookview.setListener(new DrawHookView.DrawHookListener() {

                @Override
                public void duogouSc() {
                    cooling_drawhookview.setListener(null);
                    if (PreData.getDB(BatteriesActivity.this, MyConstant.FULL_COOL, 0) == 1) {
                        AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putInt("wendu", time);
                    bundle.putString("from", "cooling");
                    jumpToActivity(SucceedActivity.class, bundle, 1);
                }
            });
        }

        public void onAnimationRepeat(Animation animation) {
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            onBackPressed();
        }
    }

    private void hideSnow() {
        if (flakeView != null) {
            flakeView.subtractFlakes(FLAKE_NUM);
            flakeView.pause();
            flakeView = null;
            mHandler.removeCallbacks(runnable);
        }
        if (cooling_piao != null) {
            cooling_piao.removeAllViews();
            cooling_piao.setVisibility(View.GONE);
        }
        if (animSet != null) {
            animSet.cancel();
        }
        if (animator1 != null) {
            animator1.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        if (mHandler != null && runnable != null) {
            mHandler.removeCallbacks(runnable);
        }
        super.onDestroy();
    }
}
