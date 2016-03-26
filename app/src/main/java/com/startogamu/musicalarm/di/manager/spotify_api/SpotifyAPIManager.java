package com.startogamu.musicalarm.di.manager.spotify_api;

import com.startogamu.musicalarm.model.spotify.SpotifyPlaylist;
import com.startogamu.musicalarm.model.spotify.SpotifyUser;
import com.startogamu.musicalarm.network.spotify_api.SpotifyAPIService;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/***
 * Manager that will handle all the call to the {@link SpotifyAPIService}
 */
public class SpotifyAPIManager {
    public SpotifyAPIService spotifyAPIService;

    @Inject
    public SpotifyAPIManager(final SpotifyAPIService spotifyAPIService) {
        this.spotifyAPIService = spotifyAPIService;
    }

    /***
     * @param token      should be : "Bearer $accessToken" provided by spotify api
     * @param subscriber
     */
    public void getMe(final String token, Subscriber<SpotifyUser> subscriber) {
        spotifyAPIService.getMe(token).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    /***
     * @param token      should be : "Bearer $accessToken" provided by spotify api
     * @param subscriber
     */
    public void getPlaylist(final String token, Subscriber<SpotifyPlaylist> subscriber) {
        spotifyAPIService.getPlaylists(token).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }


}
