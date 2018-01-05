package com.upupup.clean.junk.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.upupup.clean.junk.R;
import com.upupup.clean.junk.myActivity.JiangwenActivity;
import com.upupup.clean.junk.util.CpuTempReader;
import com.upupup.clean.junk.view.ShowPercentView;

/**
 * Created by ${} on 2017/11/16.
 */

public class CoolingFragment extends Fragment {
    ShowPercentView ram_baifen;
    TextView ram_parent;
    ImageView ram_zhuangtai, yuan_kedu;
    TextView ram_zhanyong;
    LinearLayout ram_msg;
    LinearLayout ram_ani_1;
    Button ram_button;

    Handler handler;
    boolean firststart;
    private View view;
    private AnimatorSet animatorSet;
    private ObjectAnimator animator_t;
    boolean adShow;
    private int cpuTemp;

    @SuppressLint("ValidFragment")
    public CoolingFragment(boolean adShow) {
        this.adShow = adShow;
    }

    public CoolingFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.layout_cooling_fragment, container, false);
        }
        ram_baifen = (ShowPercentView) view.findViewById(R.id.ram_baifen);
        ram_zhuangtai = (ImageView) view.findViewById(R.id.ram_zhuangtai);
        yuan_kedu = (ImageView) view.findViewById(R.id.yuan_kedu);
        ram_parent = (TextView) view.findViewById(R.id.ram_parent);
        ram_zhanyong = (TextView) view.findViewById(R.id.ram_zhanyong);
        ram_msg = (LinearLayout) view.findViewById(R.id.ram_msg);
        ram_ani_1 = (LinearLayout) view.findViewById(R.id.ram_ani_1);
        ram_button = (Button) view.findViewById(R.id.ram_button);

        ram_baifen.setListener(percentViewListener);
        ram_baifen.setOnClickListener(onClickListener);
        ram_button.setOnClickListener(onClickListener);
        return view;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), JiangwenActivity.class);
            intent.putExtra("wendu", cpuTemp);
            startActivityForResult(intent, 1);
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
                        if (cpuTemp > 50) {
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


    public void startFen() {
        if (firststart) {
            ram_ani_1.setVisibility(View.INVISIBLE);
            ram_msg.setVisibility(View.INVISIBLE);
            ram_button.setVisibility(View.INVISIBLE);

        }
        if (ram_baifen != null) {
            ram_baifen.setPercent(100);
        }
        CpuTempReader.getCPUTemp(new CpuTempReader.TemperatureResultCallback() {
            @Override
            public void callbackResult(CpuTempReader.ResultCpuTemperature result) {
                if (result != null) {
                    cpuTemp = (int) result.getTemperature();

                } else {
                    cpuTemp = 40;
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (cpuTemp > 50) {
                            ram_zhuangtai.setImageResource(R.mipmap.yuan_bad);
                            ram_msg.setVisibility(View.VISIBLE);
                        } else {
                            ram_zhuangtai.setImageResource(R.mipmap.yuan_good);
                            ram_msg.setVisibility(View.GONE);
                        }
                        ram_baifen.start(cpuTemp);
                    }
                });
            }
        });
    }


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



