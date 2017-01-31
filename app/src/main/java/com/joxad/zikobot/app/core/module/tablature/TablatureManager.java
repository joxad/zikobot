package com.joxad.zikobot.app.core.module.tablature;

import android.app.Activity;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;

/**
 * Created by josh on 23/07/16.
 */
public class TablatureManager {

    public static void showTab(Activity context, String track, String artist) {
        // Use a CustomTabsIntent.Builder to configure CustomTabsIntent.
// Once ready, call CustomTabsIntent.Builder.build() to create a CustomTabsIntent
// and launch the desired Url with CustomTabsIntent.launchUrl()
        track = track.replaceAll(" ", "+");
        artist = artist.replaceAll(" ", "+");
        String url = String.format("http://www.songsterr.com/a/wa/bestMatchForQueryString?s=%s&a=%s", track, artist);
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(context, Uri.parse(url));
    }
}
