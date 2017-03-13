package com.eos.module.charge.saver.lottie;

interface AnimatableValue<V, O> {
  BaseKeyframeAnimation<?, O> createAnimation();
  boolean hasAnimation();

  interface Factory<V> {
    V valueFromObject(Object object, float scale);
  }
}
