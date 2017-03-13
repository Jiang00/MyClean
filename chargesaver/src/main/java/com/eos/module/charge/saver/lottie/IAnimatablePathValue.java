package com.eos.module.charge.saver.lottie;

import android.graphics.PointF;

public interface IAnimatablePathValue extends AnimatableValue<PointF, PointF> {
  PointF getInitialPoint();
}
