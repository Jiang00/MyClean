package com.twee.module.tweenengine;

import com.twee.module.tweenengine.equations.Back;
import com.twee.module.tweenengine.equations.Bounce;
import com.twee.module.tweenengine.equations.Circ;
import com.twee.module.tweenengine.equations.Cubic;
import com.twee.module.tweenengine.equations.Elastic;
import com.twee.module.tweenengine.equations.Expo;
import com.twee.module.tweenengine.equations.Linear;
import com.twee.module.tweenengine.equations.Quad;
import com.twee.module.tweenengine.equations.Quart;
import com.twee.module.tweenengine.equations.Quint;
import com.twee.module.tweenengine.equations.Sine;

/**
 * Collection of miscellaneous utilities.
 *
 * @author Aurelien Ribon | http://www.aurelienribon.com/
 */
public class TweenUtils {
	private static TweenEquation[] easings;

	/**
	 * Takes an easing name and gives you the corresponding TweenEquation.
	 * You probably won't need this, but tools will love that.
	 *
	 * @param easingName The name of an easing, like "Quad.INOUT".
	 * @return The parsed equation, or null if there is no match.
	 */
	public static TweenEquation parseEasing(String easingName) {
		if (easings == null) {
			easings = new TweenEquation[] {Linear.INOUT,
				Quad.IN, Quad.OUT, Quad.INOUT,
				Cubic.IN, Cubic.OUT, Cubic.INOUT,
				Quart.IN, Quart.OUT, Quart.INOUT,
				Quint.IN, Quint.OUT, Quint.INOUT,
				Circ.IN, Circ.OUT, Circ.INOUT,
				Sine.IN, Sine.OUT, Sine.INOUT,
				Expo.IN, Expo.OUT, Expo.INOUT,
				Back.IN, Back.OUT, Back.INOUT,
				Bounce.IN, Bounce.OUT, Bounce.INOUT,
				Elastic.IN, Elastic.OUT, Elastic.INOUT
			};
		}

		for (int i=0; i<easings.length; i++) {
			if (easingName.equals(easings[i].toString()))
				return easings[i];
		}

		return null;
	}
}
