package com.mutter.clean.junk.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mingle.circletreveal.CircularRevealCompat;
import com.mingle.widget.animation.CRAnimation;
import com.mingle.widget.animation.SimpleAnimListener;
import com.mutter.clean.core.CleanManager;
import com.mutter.clean.junk.R;
import com.mutter.clean.junk.myActivity.CleanActivity;
import com.mutter.clean.junk.myActivity.RamAvtivity;
import com.mutter.clean.junk.myActivity.SuccessActivity;
import com.mutter.clean.junk.util.UtilGp;
import com.mutter.clean.junk.view.ShowPercentView;
import com.mutter.clean.util.LoadManager;
import com.mutter.clean.util.MemoryManager;
import com.mutter.clean.util.Util;

/**
 * Created by ${} on 2017/11/16.
 */

public class JunkFragment extends Fragment {
    ShowPercentView ram_baifen;
    ImageView junk_zhuangtai, yuan_kedu;
    TextView ram_parent;
    TextView ram_zhanyong, ram_all, ram_msg_2;
    LinearLayout ram_msg;
    LinearLayout ram_ani_1;
    Button ram_button;

    Handler handler;
    boolean firststart;
    private View view;
    private AnimatorSet animatorSet;
    private ObjectAnimator animator_t;
    boolean adShow;
    private long ramSize;

    @SuppressLint("ValidFragment")
    public JunkFragment(boolean adShow) {
        this.adShow = adShow;
    }

    public JunkFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.layout_junk_fragment, container, false);
        }
        ram_baifen = (ShowPercentView) view.findViewById(R.id.ram_baifen);
        junk_zhuangtai = (ImageView) view.findViewById(R.id.junk_zhuangtai);
        yuan_kedu = (ImageView) view.findViewById(R.id.yuan_kedu);
        ram_parent = (TextView) view.findViewById(R.id.ram_parent);
        ram_zhanyong = (TextView) view.findViewById(R.id.ram_zhanyong);
        ram_all = (TextView) view.findViewById(R.id.ram_all);
        ram_msg = (LinearLayout) view.findViewById(R.id.ram_msg);
        ram_ani_1 = (LinearLayout) view.findViewById(R.id.ram_ani_1);
        ram_msg_2 = (TextView) view.findViewById(R.id.ram_msg_2);
        ram_button = (Button) view.findViewById(R.id.ram_button);

        ram_baifen.setListener(percentViewListener);
        ram_baifen.setOnClickListener(onClickListener);
        ram_button.setOnClickListener(onClickListener);


        return view;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            long allSize = CleanManager.getInstance(getActivity()).getApkSize() + CleanManager.getInstance(getActivity()).getCacheSize() + CleanManager.getInstance(getActivity()).getUnloadSize() + CleanManager.getInstance(getActivity()).getLogSize()
                    + CleanManager.getInstance(getActivity()).getDataSize();
            if (allSize == 0) {
                Intent intent = new Intent(getActivity(), SuccessActivity.class);
                intent.putExtra("from", "junk_fragment");
                intent.putExtra("size", 0l);
                startActivityForResult(intent, 1);
            } else {
                startActivityForResult(new Intent(getActivity(), CleanActivity.class), 1);
            }
        }
    };
    ShowPercentView.PercentViewListener percentViewListener = new ShowPercentView.PercentViewListener() {
        @Override
        public void setPerc(int percent) {
            ram_parent.setText("" + percent);
            yuan_kedu.setRotation(percent * 360f / 100);
        }

        @Override
        public void setSucc() {
            handler.removeCallbacks(runnable_kedu);
            if (firststart) {
                firststart = false;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        animatorSet = new AnimatorSet();
                        ram_ani_1.setScaleX(0);
                        ram_msg.setScaleX(0);
                        ram_button.setScaleX(0);
                        ObjectAnimator objectAnimator_3 = ObjectAnimator.ofFloat(ram_button, View.SCALE_X, 0, 1.1f, 1);
                        ObjectAnimator objectAnimator_1 = ObjectAnimator.ofFloat(ram_ani_1, View.SCALE_X, 0, 1.1f, 1);
                        ObjectAnimator objectAnimator_2 = null;
                        if (ramSize > 0) {
                            objectAnimator_2 = ObjectAnimator.ofFloat(ram_msg, View.SCALE_X, 0, 1.1f, 1);
                            objectAnimator_2.setDuration(300);
                        }
                        objectAnimator_3.setDuration(300);
                        objectAnimator_1.setDuration(300);
                        if (objectAnimator_2 == null) {
                            animatorSet.play(objectAnimator_1);
                            animatorSet.play(objectAnimator_3).after(200);
                        } else {
                            animatorSet.play(objectAnimator_1);
                            animatorSet.play(objectAnimator_2).after(200);
                            animatorSet.play(objectAnimator_3).after(400);
                            ram_msg.setVisibility(View.VISIBLE);
                        }
                        animatorSet.setInterpolator(new LinearInterpolator());
                        animatorSet.start();
                        ram_ani_1.setVisibility(View.VISIBLE);
                        ram_button.setVisibility(View.VISIBLE);

                    }
                });
            }
        }
    };

    public void startJunk() {
        if (firststart) {
            ram_ani_1.setVisibility(View.INVISIBLE);
            ram_msg.setVisibility(View.INVISIBLE);
            ram_button.setVisibility(View.INVISIBLE);

        }
        if (ram_baifen != null) {
            ram_baifen.setPercent(100);
        }
        ramSize = CleanManager.getInstance(getActivity()).getApkSize() + CleanManager.getInstance(getActivity()).getCacheSize() + CleanManager.getInstance(getActivity()).getUnloadSize() + CleanManager.getInstance(getActivity()).getLogSize()
                + CleanManager.getInstance(getActivity()).getDataSize();
        if (ramSize > 0) {
            ram_msg_2.setText(Util.convertStorage(ramSize, true));
            junk_zhuangtai.setImageResource(R.mipmap.yuan_bad);
        } else {
            junk_zhuangtai.setImageResource(R.mipmap.yuan_good);
            ram_msg.setVisibility(View.GONE);
        }
        handler.postDelayed(runnable_kedu, 150);
    }

    Runnable runnable_kedu = new Runnable() {
        @Override
        public void run() {
            //ram使用
            long sd_all = MemoryManager.getPhoneAllSize();
            long sd_kongxian = MemoryManager.getPhoneAllFreeSize();
            long sd_shiyong = sd_all - sd_kongxian;
            int sd_me = (int) (sd_shiyong * 100 / sd_all);
            ram_zhanyong.setText(Util.convertStorage(sd_shiyong, true));
            ram_all.setText("/" + Util.convertStorage(sd_all, true));
            ram_baifen.start(sd_me);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        handler = new Handler();
        firststart = true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        if (null != view) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        if (animator_t != null) {
            animator_t.removeAllListeners();
            animator_t.cancel();
        }
        if (animatorSet != null && animatorSet.isRunning()) {
            animatorSet.cancel();
        }
    }
}


