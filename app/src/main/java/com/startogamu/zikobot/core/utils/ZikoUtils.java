package com.startogamu.zikobot.core.utils;

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
}
