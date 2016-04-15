package com.startogamu.musicalarm;

import android.app.Application;
import android.content.Context;

import com.joxad.android_easy_spotify.SpotifyManager;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.startogamu.musicalarm.core.notification.MusicNotification;
import com.startogamu.musicalarm.core.utils.AppPrefs;
import com.startogamu.musicalarm.module.component.Injector;

/**
 * {@link MusicAlarmApplication} is used to initiate our Dagger classes + {@link FlowManager} + {@link AppPrefs} + {@link SpotifyManager}
 */
public class MusicAlarmApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
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


    public static MusicAlarmApplication get(Context context) {
        return (MusicAlarmApplication) context.getApplicationContext();
    }


}
