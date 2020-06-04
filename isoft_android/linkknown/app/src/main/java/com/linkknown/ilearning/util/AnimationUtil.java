package com.linkknown.ilearning.util;

import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.RotateAnimation;

public class AnimationUtil {

    /**
     * 晃动动画
     * @param counts 1秒钟晃动多少下
     * @return Animation
     */
    public static Animation getShakeAnimation(int counts) {
        Animation rotateAnimation = new RotateAnimation(0, 20, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setInterpolator(new CycleInterpolator(counts));
        rotateAnimation.setRepeatCount(-1);
        rotateAnimation.setDuration(3000);
        return rotateAnimation;
    }

}
