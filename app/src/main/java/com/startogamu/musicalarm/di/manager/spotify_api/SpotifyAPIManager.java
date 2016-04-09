package com.startogamu.musicalarm.di.manager.spotify_api;

import com.startogamu.musicalarm.model.spotify.SpotifyFeaturedPlaylist;
import com.startogamu.musicalarm.model.spotify.SpotifyPlaylist;
import com.startogamu.musicalarm.model.spotify.SpotifyPlaylistWithTrack;
import com.startogamu.musicalarm.model.spotify.SpotifyUser;
import com.startogamu.musicalarm.network.spotify_api.SpotifyAPIService;
import com.startogamu.musicalarm.core.utils.SpotifyPrefs;

import javax.inject.Inject;

import rx.Observable;
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
     * @param subscriber
     */
    public void getMe(Subscriber<SpotifyUser> subscriber) {
        spotifyAPIService.getMe().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<SpotifyUser>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        subscriber.onError(e);
                    }

                    @Override
                    public void onNext(SpotifyUser spotifyUser) {
                        SpotifyPrefs.saveUser(spotifyUser.getId());
                        subscriber.onNext(spotifyUser);
                    }
                });
    }

    /***
     * 1     *
     */
    public Observable<SpotifyPlaylist> getUserPlaylists() {
        return spotifyAPIService.getUserPlaylists()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    /***
     * @param playlistId
     * @param subscriber
     */
    public void getPlaylistTracks(final String playlistId, final Subscriber<SpotifyPlaylistWithTrack> subscriber) {
        spotifyAPIService.getPlaylistTracks(SpotifyPrefs.getSpotifyUserId(), playlistId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io()).subscribe(subscriber);
    }


    public Observable<SpotifyFeaturedPlaylist> getFeaturedPlaylists() {
        return spotifyAPIService.getFeaturedPlaylists().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

}
