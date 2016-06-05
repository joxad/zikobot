package com.startogamu.zikobot;

import android.app.Application;
import android.content.Context;

import com.joxad.android_easy_spotify.SpotifyManager;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.startogamu.zikobot.core.notification.MusicNotification;
import com.startogamu.zikobot.core.utils.AppPrefs;
import com.startogamu.zikobot.module.component.Injector;

/**
 * {@link ZikobotApp} is used to initiate our Dagger classes + {@link FlowManager} + {@link AppPrefs} + {@link SpotifyManager}
 */
public class ZikobotApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(new FlowConfig.Builder(this).build());
        AppPrefs.init(this);
        try {
            new SpotifyManager.Builder().setContext(this).setApiKey(R.string.api_spotify_id).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        MusicNotification.init(this);
        Injector.INSTANCE.initSpotifyApi(this);
        Injector.INSTANCE.initSpotifyAuth(this);
        Injector.INSTANCE.initContentResolver(this);
        Injector.INSTANCE.initPlayerMusic(this);


    }


    public static ZikobotApp get(Context context) {
        return (ZikobotApp) context.getApplicationContext();
    }


}
