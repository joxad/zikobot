package com.startogamu.musicalarm;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;

import com.pixplicity.easyprefs.library.Prefs;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.startogamu.musicalarm.di.component.ContentComponent;
import com.startogamu.musicalarm.di.component.DaggerContentComponent;
import com.startogamu.musicalarm.di.component.DaggerNetComponent;
import com.startogamu.musicalarm.di.component.NetComponent;
import com.startogamu.musicalarm.di.module.AppModule;

/**
 * Created by josh on 08/03/16.
 */
public class MusicAlarmApplication extends Application {
    public NetComponent netComponent;
    public ContentComponent contentComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);

        AppModule appModule = new AppModule(this);
        netComponent = DaggerNetComponent.builder()
                .appModule(appModule)
                .build();
        contentComponent = DaggerContentComponent.builder()
                .appModule(appModule)
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
