package com.startogamu.musicalarm.di.module;

import android.app.Application;

import com.startogamu.musicalarm.di.module.spotify_api.SpotifyAPINetworkModule;
import com.startogamu.musicalarm.di.module.spotify_api.SpotifyAPIServiceModule;
import com.startogamu.musicalarm.di.module.spotify_auth.SpotifyAuthNetworkModule;
import com.startogamu.musicalarm.di.module.spotify_auth.SpotifyAuthServiceModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/***
 * AppModule handle :
 * <ul>
 * <li> {@link SpotifyAuthNetworkModule} : Generate a singleton to manage the call to spotify auth api using retrofit </li>
 * <li> {@link SpotifyAuthServiceModule}: Export the WS methods of spotify auth </li>
 * <li> {@link SpotifyAPINetworkModule} : Generate a singleton to manage the call to spotify normal api using retrofit </li>
 * <li> {@link SpotifyAPIServiceModule}: Export the WS methods of spotify API </li>
 * </ul>
 */
@Module(
        includes = {
                SpotifyAuthNetworkModule.class,
                SpotifyAuthServiceModule.class,
                SpotifyAPINetworkModule.class,
                SpotifyAPIServiceModule.class,
                DBModule.class
        })
public class AppModule {

    Application mApplication;

    /***
     * @param application
     */
    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }
}
