package com.startogamu.musicalarm;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;

import com.joxad.android_easy_spotify.SpotifyManager;
import com.pixplicity.easyprefs.library.Prefs;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.startogamu.musicalarm.core.utils.SpotifyPrefs;
import com.startogamu.musicalarm.module.component.Injector;

/**
 * Created by josh on 08/03/16.
 */
public class MusicAlarmApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
        Injector.INSTANCE.initSpotifyApi(this);
        Injector.INSTANCE.initSpotifyAuth(this);
        Injector.INSTANCE.initContentResolver(this);
        //playerComponent = Da.builder().appModule(appModule).build();
        SpotifyPrefs.init(this);
        try {
            new SpotifyManager.Builder().setContext(this).setApiKey(R.string.api_spotify_id).build();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public static MusicAlarmApplication get(Context context) {
        return (MusicAlarmApplication) context.getApplicationContext();
    }


}
