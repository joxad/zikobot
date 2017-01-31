package com.joxad.zikobot.data.module.spotify_api.manager;

import android.content.Context;

import com.joxad.zikobot.data.AppPrefs;
import com.joxad.zikobot.data.R;
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyFeaturedPlaylist;
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyPlaylist;
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyPlaylistWithTrack;
import com.joxad.zikobot.data.module.spotify_api.model.SpotifySearchResult;
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyUser;
import com.joxad.zikobot.data.module.spotify_api.resource.SpotifyAPIService;
import com.joxad.zikobot.data.module.spotify_api.resource.SpotifyApiInterceptor;

import retrofit2.Retrofit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/***
 * {@link SpotifyApiManager} est initialisé au lancement de l'application et permet d'utiliser les méthodes
 * {@link SpotifyAPIService}
 */

public class SpotifyApiManager {

    private SpotifyAPIService spotifyAPIService;
    private Context context;
    private Retrofit retrofit;

    /**
     * Holder
     */
    private static class SpotifyApiManagerHolder {
        /**
         * Instance unique non préinitialisée
         */
        private final static SpotifyApiManager instance = new SpotifyApiManager();
    }

    /**
     * Point d'accès pour l'instance unique du singleton
     */
    public static SpotifyApiManager getInstance() {
        return SpotifyApiManager.SpotifyApiManagerHolder.instance;
    }

    public void init(Context context) {

        this.context = context;
        SpotifyRetrofit spotifyRetrofit = new SpotifyRetrofit(context.getString(R.string.spotify_base_api_url), new SpotifyApiInterceptor());
        this.retrofit = spotifyRetrofit.retrofit();
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
        return spotifyAPIService.search(limit, offset, search, "album,artist,track").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }


    public Observable<SpotifyFeaturedPlaylist> getFeaturedPlaylists() {
        return spotifyAPIService.getFeaturedPlaylists().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }


}
