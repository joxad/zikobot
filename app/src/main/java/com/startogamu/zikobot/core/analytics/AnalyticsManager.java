package com.startogamu.zikobot.core.analytics;

import android.content.Context;


import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Date;

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
        private static final String TAG = "ALARM_MANAGEMENT";
        private static final String TAG_LAUNCH = "ALARM_LAUNCH";
        private static final String START = "START";
        private static final String TITLE = "TITLE";
        private static final String HOUR = "HOUR";
        private static final String CREATE_FROM_ALARM = "CREATE_FROM_ALARM";
        private static final String CREATE_FROM_PLAYER = "CREATE_FROM_PLAYER";
        public static final String TRACKS_COUNT = "TRACKS_COUNT";
        public static final String RANDOM = "RANDOM";
        public static final String REPEAT = "REPEAT";
    }

    private static FirebaseAnalytics firebaseAnalytics;

    public static void init(Context context) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }


    public static void logConnectSpotify(final String account) {
        //firebaseAnalytics.logEvent(Spotify.TAG, Bundler.create().put(Spotify.LOGIN, account).get());
    }

    public static void logConnectSoundCloud(final String account) {
     //   firebaseAnalytics.logEvent(SoundClound.TAG, Bundler.create().put(SoundClound.LOGIN, account).get());
    }


    public static void logStartAlarm(com.startogamu.zikobot.module.zikobot.model.Alarm alarm) {
        Date date = new Date();
       /* Bundler bundler = Bundler.create()
                .put(Alarm.START, date.toString())
                .put(Alarm.TITLE, alarm.getName())
                .put(Alarm.TRACKS_COUNT, alarm.getTracks().size())
                .put(Alarm.RANDOM, alarm.getRandomTrack())
                .put(Alarm.REPEAT, alarm.getRepeated())
                .put(Alarm.HOUR, +alarm.getHour() + " h " + alarm.getMinute());
        firebaseAnalytics.logEvent(Alarm.TAG_LAUNCH, bundler.get());*/
    }

    public static void logCreateAlarm(com.startogamu.zikobot.module.zikobot.model.Alarm alarm,  boolean fromAlarm) {

      /*  Bundler bundler = Bundler.create()
                .put(Alarm.TITLE, alarm.getName())
                .put(Alarm.TRACKS_COUNT, alarm.getTracks().size())
                .put(Alarm.RANDOM, alarm.getRandomTrack())
                .put(Alarm.REPEAT, alarm.getRepeated())
                .put(Alarm.HOUR, +alarm.getHour() + " h " + alarm.getMinute());
        bundler.put(fromAlarm ? Alarm.CREATE_FROM_ALARM : Alarm.CREATE_FROM_PLAYER, true);
        firebaseAnalytics.logEvent(Alarm.TAG, bundler.get());*/

    }

}
