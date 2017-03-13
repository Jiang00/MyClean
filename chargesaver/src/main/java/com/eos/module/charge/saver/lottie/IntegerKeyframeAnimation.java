package com.eos.module.charge.saver.lottie;



import java.util.List;

import static com.eos.module.charge.saver.lottie.MiscUtils.lerp;


class IntegerKeyframeAnimation extends KeyframeAnimation<Integer> {

  IntegerKeyframeAnimation(List<Keyframe<Integer>> keyframes) {
    super(keyframes);
  }

  @Override Integer getValue(Keyframe<Integer> keyframe, float keyframeProgress) {
    return lerp(keyframe.startValue, keyframe.endValue, keyframeProgress);
  }


}
