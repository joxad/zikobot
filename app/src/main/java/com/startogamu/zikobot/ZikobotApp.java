package com.startogamu.zikobot;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.joxad.android_easy_spotify.SpotifyManager;
import com.orhanobut.logger.Logger;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.startogamu.zikobot.core.notification.PlayerNotification;
import com.startogamu.zikobot.core.utils.AppPrefs;
import com.startogamu.zikobot.module.component.Injector;

import io.fabric.sdk.android.Fabric;

/**
 * {@link ZikobotApp} is used to initiate our Dagger classes + {@link FlowManager} + {@link AppPrefs} + {@link SpotifyManager}
 */
public class ZikobotApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        FlowManager.init(new FlowConfig.Builder(this).build());
        AppPrefs.init(this);
        try {
            new SpotifyManager.Builder().setContext(this).setApiKey(R.string.api_spotify_id).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        PlayerNotification.init(this);
        Injector.INSTANCE.init(this);
        Logger.init(ZikobotApp.class.getSimpleName());

    }


    public static ZikobotApp get(Context context) {
        return (ZikobotApp) context.getApplicationContext();
    }


}
