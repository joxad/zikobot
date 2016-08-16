package com.startogamu.zikobot.core.utils;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.databinding.ViewToolbarImageBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by josh on 12/07/16.
 */
public class ZikoUtils {
    /***
     * Return am or pm value
     *
     * @param hour
     * @return
     */
    public static String amPm(int hour) {
        String am = "AM";
        String pm = "PM";
        String after = am;
        if (hour >= 12) {
            after = pm;
        }
        return after;
    }

    public static int amPmHour(int hour) {
        if (hour > 12)
            hour -= 12;
        else {
            if (hour == 0)
                hour = 12;
        }
        return hour;
    }


    public static String readableTime(int millis) {
        return String.format("%2d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis), TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        );
    }

    /**
     * Return date in specified format.
     *
     * @param milliSeconds Date in milliseconds
     * @param dateFormat   Date format
     * @return String representing date in specified format
     */
    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static void prepareToolbar(AppCompatActivity activity, ViewToolbarImageBinding customToolbar, String title, String image) {
        activity.setSupportActionBar(customToolbar.toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(true);
        customToolbar.toolbar.setNavigationOnClickListener(listener -> activity.onBackPressed());

        customToolbar.mainCollapsing.setTitle(title);
        customToolbar.title.setText("");
        customToolbar.rlToolbarImage.setVisibility(View.VISIBLE);


        Glide.with(activity).load(image).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                ZikoUtils.animateFade(customToolbar.rlOverlay);
                return false;
            }
        }).placeholder(R.drawable.ic_vinyl).into(customToolbar.ivToolbar);


    }



    public static void animateFade(View view) {
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        fadeIn.setDuration(1000);
        fadeIn.start();
    }

    public static void animateScale(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f);
        scaleX.setDuration(800);
        scaleY.setDuration(800);
        view.setVisibility(View.VISIBLE);
        scaleX.start();
        scaleY.start();
    }

    public static void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }
}
