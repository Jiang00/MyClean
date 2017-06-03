package com.sample.lottie;

import android.graphics.PointF;

public interface IAnimatablePathValue extends AnimatableValue<PointF, PointF> {
  PointF getInitialPoint();
}
