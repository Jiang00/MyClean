package com.sample.lottie;


import java.util.List;

import static com.sample.lottie.MiscUtils.lerp;

class NumberKeyframeAnimation<T extends Number> extends KeyframeAnimation<T> {
  private final Class<T> klass;

  NumberKeyframeAnimation(List<Keyframe<T>> keyframes, Class<T> klass) {
    super(keyframes);
    this.klass = klass;
  }

  @Override T getValue(Keyframe<T> keyframe, float keyframeProgress) {
    Number startValue = keyframe.startValue;
    Number endValue = keyframe.endValue;

    if (klass.isAssignableFrom(Integer.class)) {
      return klass.cast(MiscUtils.lerp(startValue.intValue(), endValue.intValue(), keyframeProgress));
    } else {
      return klass.cast(MiscUtils.lerp(startValue.floatValue(), endValue.floatValue(), keyframeProgress));
    }
  }
}
