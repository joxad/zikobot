package com.startogamu.musicalarm.module.spotify_api.manager;

import android.content.Context;

import com.startogamu.musicalarm.core.utils.SpotifyPrefs;
import com.startogamu.musicalarm.module.spotify_api.object.SpotifyFeaturedPlaylist;
import com.startogamu.musicalarm.module.spotify_api.object.SpotifyPlaylist;
import com.startogamu.musicalarm.module.spotify_api.object.SpotifyPlaylistWithTrack;
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
     */
    public Observable<SpotifyUser> getMe() {
        return spotifyAPIService.getMe().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
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
     */
    public Observable<SpotifyPlaylistWithTrack> getPlaylistTracks(final String playlistId) {
        return spotifyAPIService.getPlaylistTracks(SpotifyPrefs.spotifyYserId(), playlistId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }


    public Observable<SpotifyFeaturedPlaylist> getFeaturedPlaylists() {
        return spotifyAPIService.getFeaturedPlaylists().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }


}
