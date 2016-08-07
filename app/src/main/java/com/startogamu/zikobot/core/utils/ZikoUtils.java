package com.startogamu.zikobot.core.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;



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

}
