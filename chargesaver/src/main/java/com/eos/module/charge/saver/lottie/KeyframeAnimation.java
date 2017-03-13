package com.eos.module.charge.saver.lottie;

import java.util.List;

public abstract class KeyframeAnimation<T> extends BaseKeyframeAnimation<T, T> {
  KeyframeAnimation(List<? extends Keyframe<T>> keyframes) {
    super(keyframes);
  }
}
