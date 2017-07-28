package com.startogamu.zikobot;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.joxad.zikobot.data.module.localmusic.manager.LocalMusicManager;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by Jocelyn on 27/07/2017.
 */

public class ZikobotApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(new FlowConfig.Builder(this).build());
        LocalMusicManager.INSTANCE.init(this);
        Stetho.initializeWithDefaults(this);

    }
}
