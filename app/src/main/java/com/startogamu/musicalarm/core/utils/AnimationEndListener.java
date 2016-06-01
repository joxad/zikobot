package com.startogamu.musicalarm.core.utils;

import android.animation.Animator;

public abstract class AnimationEndListener implements Animator.AnimatorListener {

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public abstract void onAnimationEnd(Animator animation);

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

}
