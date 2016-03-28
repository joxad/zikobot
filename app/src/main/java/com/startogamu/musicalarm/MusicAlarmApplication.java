package com.startogamu.musicalarm;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;

import com.pixplicity.easyprefs.library.Prefs;
import com.startogamu.musicalarm.di.component.DaggerNetComponent;
import com.startogamu.musicalarm.di.component.NetComponent;
import com.startogamu.musicalarm.di.module.AppModule;

/**
 * Created by josh on 08/03/16.
 */
public class MusicAlarmApplication extends Application {
    public NetComponent netComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        netComponent = DaggerNetComponent.builder()
                .appModule(new AppModule(this))
                .build();
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
        }


    public static MusicAlarmApplication get(Context context) {
        return (MusicAlarmApplication) context.getApplicationContext();
    }




}
