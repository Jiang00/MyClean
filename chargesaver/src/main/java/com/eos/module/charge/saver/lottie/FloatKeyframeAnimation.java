package com.eos.module.charge.saver.lottie;

import java.util.List;

import static com.eos.module.charge.saver.lottie.MiscUtils.lerp;


class FloatKeyframeAnimation extends KeyframeAnimation<Float> {

  FloatKeyframeAnimation(List<Keyframe<Float>> keyframes) {
    super(keyframes);
  }

  @Override Float getValue(Keyframe<Float> keyframe, float keyframeProgress) {
    return lerp(keyframe.startValue, keyframe.endValue, keyframeProgress);
  }
}
