package com.startogamu.musicalarm.di.module;

import android.app.Application;

import com.startogamu.musicalarm.di.module.spotify_api.SpotifyAPIRetrofitProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by josh on 28/03/16.
 */
@Module
public class DBModule {
    @Provides
    @Singleton
    public Realm provideRealmDB(final Application context) {
        RealmConfiguration config = new RealmConfiguration.Builder(context).build();
        Realm.setDefaultConfiguration(config);
        return Realm.getDefaultInstance();
    }
}
