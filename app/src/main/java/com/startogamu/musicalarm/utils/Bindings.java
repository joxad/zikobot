package com.startogamu.musicalarm.utils;

import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.startogamu.musicalarm.R;

/**
 * Created by josh on 26/03/16.
 */
public class Bindings {

    @BindingAdapter({"app:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_picture_loading)
                .into(view);
    }

}
