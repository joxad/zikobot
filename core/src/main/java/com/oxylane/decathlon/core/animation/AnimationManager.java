package com.oxylane.decathlon.core.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;

/**
 * Created by josh on 24/08/16.
 */
public class AnimationManager {

    public static void slideFromOutLeft(View view, int duration) {
        view.setVisibility(View.VISIBLE);
        view.clearAnimation();
        Animation slide = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                0.0f, Animation.RELATIVE_TO_PARENT, 0f);
        slide.setDuration(duration);
        slide.setInterpolator(new DecelerateInterpolator());
        view.startAnimation(slide);
    }

    public static void slideFromOutRight(View view, int duration) {
        view.setVisibility(View.VISIBLE);
        view.clearAnimation();
        Animation slide = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 1f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                0.0f, Animation.RELATIVE_TO_PARENT, 0f);
        slide.setDuration(duration);
        slide.setInterpolator(new DecelerateInterpolator());
        view.startAnimation(slide);

    }

}
