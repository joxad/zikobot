package com.joxad.zikobot.app.core.utils;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.joxad.zikobot.app.databinding.ViewToolbarImageBinding;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
 /*   public static Asset createAssetFromBitmap(Bitmap bitmap) {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteStream);
        return Asset.createFromBytes(byteStream.toByteArray());
    }*/

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
                return false;
            }
        }).into(customToolbar.ivToolbar);


    }


    public static void animateFade(View view) {
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        fadeIn.setDuration(1000);
        fadeIn.start();
    }

    public static void animateScale(View view) {
        view.setVisibility(View.VISIBLE);
        view.setScaleX(0);
        view.setScaleY(0);
        view.animate().scaleX(1).scaleY(1).setDuration(400).start();
    }

    public static void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
    public static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.SHOW_FORCED);
    }

    public static String handleUtf8(String title) {
        try {
            title = URLDecoder.decode(title, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return title;
    }
}
