package com.startogamu.zikobot.module.spotify_api.manager;

import android.content.Context;

import com.startogamu.zikobot.core.utils.AppPrefs;
import com.startogamu.zikobot.module.spotify_api.model.SpotifyFeaturedPlaylist;
import com.startogamu.zikobot.module.spotify_api.model.SpotifyPlaylist;
import com.startogamu.zikobot.module.spotify_api.model.SpotifyPlaylistWithTrack;
import com.startogamu.zikobot.module.spotify_api.model.SpotifySearchResult;
import com.startogamu.zikobot.module.spotify_api.model.SpotifyUser;
import com.startogamu.zikobot.module.spotify_api.resource.SpotifyAPIService;

import javax.inject.Singleton;

import retrofit2.Retrofit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/***
 * {@link SpotifyApiManager} est injecté par le {@link com.startogamu.zikobot.module.spotify_api.SpotifyApiModule} et permet d'utiliser les méthodes
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
        return spotifyAPIService.getPlaylistTracks(AppPrefs.spotifyUser().getId(), playlistId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }


    public Observable<SpotifySearchResult> search(final int limit, final int offset, final String search) {
        return spotifyAPIService.search(limit,offset,search, "album,artist,track").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }


    public Observable<SpotifyFeaturedPlaylist> getFeaturedPlaylists() {
        return spotifyAPIService.getFeaturedPlaylists().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }


}
