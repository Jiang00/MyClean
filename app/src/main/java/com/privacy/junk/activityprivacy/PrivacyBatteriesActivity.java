package com.privacy.junk.activityprivacy;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.privacy.clean.utilsprivacy.PreData;
import com.privacy.junk.R;
import com.privacy.junk.privacycustomview.PrivacyDrawHookView;
import com.privacy.junk.privacycustomview.FlakeViewPrivacy;
import com.privacy.junk.privacycustomview.KuoShan;
import com.privacy.junk.privacycustomview.Rotate3d;
import com.privacy.junk.toolsprivacy.MyConstantPrivacy;
import com.privacy.junk.toolsprivacy.SetAdUtilPrivacy;

import java.util.Random;


/**
 * Created by on 2017/3/2.
 */

public class PrivacyBatteriesActivity extends BaseActivity {
    ImageView cooling_xuehua, cooling_dianyuan;
    ViewGroup mContainer;
    LinearLayout cooling_text;
    KuoShan cooling_2;
    private Handler mHandler = new Handler();
    TextView title_name;
    PrivacyDrawHookView cooling_drawhookview;
    private static final int FLAKE_NUM = 5;
    TextView cooling_wendu;
    FrameLayout title_left;
    private Random random;
    private int time;
    ObjectAnimator animator1;
    private FlakeViewPrivacy flakeView;
    LinearLayout cooling_piao;
    private Animation suo;
    private Animation rotate_zheng;

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
        AndroidSdk.loadFullAd(SetAdUtilPrivacy.DEFAULT_FULL,null);

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

        startCoolingAni();

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

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        cooling_piao = (LinearLayout) findViewById(R.id.cooling_piao);
        cooling_dianyuan = (ImageView) findViewById(R.id.cooling_dianyuan);
        cooling_text = (LinearLayout) findViewById(R.id.cooling_text);
        cooling_2 = (KuoShan) findViewById(R.id.cooling_2);
        cooling_wendu = (TextView) findViewById(R.id.cooling_wendu);
        cooling_xuehua = (ImageView) findViewById(R.id.cooling_xuehua);
        mContainer = (ViewGroup) findViewById(R.id.fl);///找到FrameLayout
        cooling_drawhookview = (PrivacyDrawHookView) findViewById(R.id.cooling_drawhookview);
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideSnow();
    }

    @Override
    protected void onResume() {
        super.onResume();
        flakeView = new FlakeViewPrivacy(this);
        cooling_piao.addView(flakeView);
        mHandler.post(runnable);
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
        cooling_2.start(getResources().getDimensionPixelSize(R.dimen.d121),
                getResources().getDimensionPixelSize(R.dimen.d65),
                getResources().getDimensionPixelSize(R.dimen.d2), 15, 0.2f);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                cooling_dianyuan.setVisibility(View.GONE);
                cooling_2.setAlpha(1f);
                cooling_2.setScaleX(1f);
                cooling_2.setScaleY(1f);
                cooling_2.setVisibility(View.VISIBLE);
                applyRotation(0, 0, 180);//左旋180度
            }
        }, 1500);

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
    }

    @Override
    public void onBackPressed() {
        if ("notifi".equals(getIntent().getStringExtra("from"))) {
            jumpTo(MainActivity.class);
        } else {
            setResult(MyConstantPrivacy.COOLING_RESUIL, new Intent().putExtra("wendu", time));
        }
        finish();
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
            cooling_drawhookview.setListener(new PrivacyDrawHookView.DrawHookListener() {

                @Override
                public void duogouSc() {
                    cooling_drawhookview.setListener(null);
                    if (PreData.getDB(PrivacyBatteriesActivity.this, MyConstantPrivacy.FULL_COOL, 0) == 1) {
                        AndroidSdk.showFullAd(SetAdUtilPrivacy.DEFAULT_FULL);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putInt("wendu", time);
                    bundle.putString("from", "cooling");
                    jumpToActivity(PrivacySucceedActivity.class, bundle, 1);
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
