package com.startogamu.zikobot;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.joxad.android_easy_spotify.SpotifyManager;
import com.orhanobut.logger.Logger;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.startogamu.zikobot.core.fragmentmanager.IntentManager;
import com.startogamu.zikobot.core.notification.PlayerNotification;
import com.startogamu.zikobot.core.utils.AppPrefs;

import com.startogamu.zikobot.core.module.localmusic.manager.LocalMusicManager;
import com.startogamu.zikobot.core.module.deezer.DeezerManager;
import com.startogamu.zikobot.core.module.lyrics.manager.LyricsManager;
import com.startogamu.zikobot.core.module.music.PlayerMusicManager;
import com.startogamu.zikobot.alarm.AlarmManager;
import com.startogamu.zikobot.core.module.soundcloud.manager.SoundCloudApiManager;
import com.startogamu.zikobot.core.module.spotify_api.manager.SpotifyApiManager;
import com.startogamu.zikobot.core.module.spotify_auth.manager.SpotifyAuthManager;

import io.fabric.sdk.android.Fabric;

/**
 * {@link ZikobotApp} is used to initiate our Dagger classes + {@link FlowManager} + {@link AppPrefs} + {@link SpotifyManager}
 */
public class ZikobotApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(new FlowConfig.Builder(this).build());
        AppPrefs.init(this);
        IntentManager.init(this);
        try {
            new SpotifyManager.Builder().setContext(this).setApiKey(R.string.api_spotify_id).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            new DeezerManager.Builder().context(this).appId(R.string.deezer_id).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        SpotifyAuthManager.getInstance().init(this);
        PlayerMusicManager.getInstance().init(this);
        SoundCloudApiManager.getInstance().init(this);
        SpotifyApiManager.getInstance().init(this);
        LocalMusicManager.getInstance().init(this);
        LyricsManager.getInstance().init(this);
        PlayerNotification.init(this);
        AlarmManager.init(this);

        Logger.init(ZikobotApp.class.getSimpleName());

        Fabric.with(this,new Crashlytics());

    }

    @Override
    protected void attachBaseContext(Context base) {
        MultiDex.install(base);
        super.attachBaseContext(base);
    }

    public static ZikobotApp get(Context context) {
        return (ZikobotApp) context.getApplicationContext();
    }


}
