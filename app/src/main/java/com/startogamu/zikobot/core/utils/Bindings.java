package com.startogamu.zikobot.core.utils;

import android.animation.Animator;
import android.databinding.BindingAdapter;
import android.support.annotation.DrawableRes;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.startogamu.zikobot.R;

/**
 * Created by josh on 26/03/16.
 */
public class Bindings {
    private static final int ROTATION = 2;
    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_picture_loading)
                .into(view);
    }

    @BindingAdapter({"imageRes"})
    public static void loadImage(ImageView view, @DrawableRes int imageRes) {
        view.setImageResource(imageRes);
    }


    @BindingAdapter({"animationRotate"})
    public static void setAnimation(View view, boolean isPlaying) {
        if (isPlaying) {
           rotate(view);
        } else {
            view.animate().cancel();
        }
    }

    private static void rotate(View view) {

        view.animate().rotationBy(ROTATION).setDuration(50).setListener(new AnimationEndListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                rotate(view);
            }
        });
    }
}
