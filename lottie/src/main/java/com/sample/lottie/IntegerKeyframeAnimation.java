package com.sample.lottie;



import java.util.List;

import static com.sample.lottie.MiscUtils.lerp;


class IntegerKeyframeAnimation extends KeyframeAnimation<Integer> {

  IntegerKeyframeAnimation(List<Keyframe<Integer>> keyframes) {
    super(keyframes);
  }

  @Override Integer getValue(Keyframe<Integer> keyframe, float keyframeProgress) {
    return lerp(keyframe.startValue, keyframe.endValue, keyframeProgress);
  }


}
