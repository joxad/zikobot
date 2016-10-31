package com.startogamu.zikobot;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.joxad.android_easy_spotify.SpotifyManager;
import com.orhanobut.logger.Logger;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.startogamu.zikobot.core.analytics.AnalyticsManager;
import com.startogamu.zikobot.core.fragmentmanager.IntentManager;
import com.startogamu.zikobot.core.notification.PlayerNotification;
import com.startogamu.zikobot.core.utils.AppPrefs;
import com.startogamu.zikobot.module.component.Injector;
import com.startogamu.zikobot.module.deezer.manager.DeezerManager;
import com.startogamu.zikobot.module.music.PlayerMusicManager;
import com.startogamu.zikobot.alarm.AlarmManager;

import io.fabric.sdk.android.Fabric;

/**
 * {@link ZikobotApp} is used to initiate our Dagger classes + {@link FlowManager} + {@link AppPrefs} + {@link SpotifyManager}
 */
public class ZikobotApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(fabric);
        FlowManager.init(new FlowConfig.Builder(this).build());
        AppPrefs.init(this);
        AnalyticsManager.init(this);
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
        PlayerMusicManager.getInstance().init(this);
        PlayerNotification.init(this);
        Injector.INSTANCE.init(this);
        AlarmManager.init(this);

        Logger.init(ZikobotApp.class.getSimpleName());

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
