package com.startogamu.zikobot.core.analytics;

import android.content.Context;

import com.f2prateek.dart.henson.Bundler;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by josh on 21/06/16.
 */
public class AnalyticsManager {

    class Spotify {
        private static final String TAG = "SPOTIFY";
        private static final String LOGIN = "LOGIN";
    }

    class SoundClound {
        private static final String TAG = "SOUNDCLOUD";
        private static final String LOGIN = "LOGIN";
    }

    class Alarm {
        private static final String TAG = "Alarm";
        private static final String CREATE = "CREATE";
        private static final String UPDATE = "UPDATE";
        private static final String TITLE = "TITLE";
        private static final String HOUR = "HOUR";
        private static final String CREATE_FROM_ALARM = "CREATE_FROM_ALARM";
        private static final String CREATE_FROM_PLAYER = "CREATE_FROM_PLAYER";
    }

    private static FirebaseAnalytics firebaseAnalytics;

    public static void init(Context context) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }


    public static void logConnectSpotify(final String account) {
        firebaseAnalytics.logEvent(Spotify.TAG, Bundler.create().put(Spotify.LOGIN, account).get());
    }

    public static void logConnectSoundCloud(final String account) {
        firebaseAnalytics.logEvent(SoundClound.TAG, Bundler.create().put(SoundClound.LOGIN, account).get());
    }

    public static void logCreateAlarm(com.startogamu.zikobot.module.zikobot.model.Alarm alarm, boolean isCreation, boolean fromAlarm) {

        Bundler bundler = Bundler.create()
                .put(isCreation ? Alarm.CREATE : Alarm.UPDATE, true)
                .put(Alarm.HOUR, +alarm.getHour() + " h " + alarm.getMinute())
                .put(Alarm.TITLE, alarm.getName());
        bundler.put(fromAlarm ? Alarm.CREATE_FROM_ALARM : Alarm.CREATE_FROM_PLAYER, true);
        firebaseAnalytics.logEvent(Alarm.TAG, bundler.get());

    }

}
