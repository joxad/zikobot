package com.startogamu.zikobot

import android.app.Application

import com.facebook.stetho.Stetho
import com.joxad.androidtemplate.core.log.AppLog
import com.joxad.zikobot.data.db.ZikoDB
import com.joxad.zikobot.data.module.localmusic.manager.LocalMusicManager
import com.raizlabs.android.dbflow.config.DatabaseConfig
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.runtime.DirectModelNotifier

/**
 * Created by Jocelyn on 27/07/2017.
 */

class ZikobotApp : Application() {

    override fun onCreate() {
        super.onCreate()
        val databaseConfig = DatabaseConfig.Builder(ZikoDB::class.java).modelNotifier(DirectModelNotifier.get()).build()
        FlowManager.init(FlowConfig.Builder(this).addDatabaseConfig(databaseConfig).build())
        LocalMusicManager.INSTANCE.init(this)
        AppLog.INSTANCE.init("Zikobot")
        Stetho.initializeWithDefaults(this)

    }
}
