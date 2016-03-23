package com.startogamu.musicalarm;

import android.app.Application;
import android.content.Context;

import dagger.ObjectGraph;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by josh on 08/03/16.
 */
public class MusicAlarmApplication extends Application {
    private ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        objectGraph = ObjectGraph.create(new AppModule(this));

        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(config);
    }


    public static MusicAlarmApplication get(Context context) {
        return (MusicAlarmApplication) context.getApplicationContext();
    }

    public void inject(Object obj) {
        objectGraph.inject(obj);
    }

    
}
