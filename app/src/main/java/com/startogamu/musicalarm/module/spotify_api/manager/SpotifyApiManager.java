package com.startogamu.musicalarm.module.spotify_api.manager;

import android.content.Context;

import com.startogamu.musicalarm.module.spotify_api.object.SpotifyUser;
import com.startogamu.musicalarm.module.spotify_api.resource.SpotifyAPIService;

import javax.inject.Singleton;

import retrofit2.Retrofit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/***
 * {@link SpotifyApiManager} est injecté par le {@link com.startogamu.musicalarm.module.spotify_api.SpotifyApiModule} et permet d'utiliser les méthodes
 * {@link SpotifyAPIService}
 */
@Singleton
public class SpotifyApiManager {

    private SpotifyAPIService spotifyAPIService;
    private Context context;
    private Retrofit retrofit;

    public SpotifyApiManager(Context context, Retrofit retrofit) {

        this.context = context;
        this.retrofit = retrofit;
        spotifyAPIService = retrofit.create(SpotifyAPIService.class);
    }

    /***
     * @param token should be : "Bearer $accessToken" provided by spotify api
     */
    public Observable<SpotifyUser> getMe(final String token) {
        return spotifyAPIService.getMe().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

}
