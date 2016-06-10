package com.startogamu.zikobot.core.utils;

import android.databinding.BindingAdapter;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.startogamu.zikobot.R;

/**
 * Created by josh on 26/03/16.
 */
public class Bindings {

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_picture_loading)
                .into(view);
    }

    @BindingAdapter({"imageRes"})
    public static void loadImage(ImageView view,@DrawableRes int imageRes) {
        view.setImageResource(imageRes);
    }

}
