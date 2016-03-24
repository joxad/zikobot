package com.startogamu.musicalarm;

import android.app.Application;
import android.content.Context;

import com.startogamu.musicalarm.di.component.DaggerNetComponent;
import com.startogamu.musicalarm.di.component.NetComponent;
import com.startogamu.musicalarm.di.module.AppModule;

import io.realm.Realm;
import io.realm.RealmConfiguration;

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
        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(config);
    }


    public static MusicAlarmApplication get(Context context) {
        return (MusicAlarmApplication) context.getApplicationContext();
    }




}
