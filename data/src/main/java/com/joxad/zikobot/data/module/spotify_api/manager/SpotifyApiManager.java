package com.joxad.zikobot.data.module.spotify_api.manager;

import android.content.Context;

import com.joxad.zikobot.data.AppPrefs;
import com.joxad.zikobot.data.R;
import com.joxad.zikobot.data.module.spotify_api.model.Albums;
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyArtist;
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyFeaturedPlaylist;
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyPlaylist;
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyPlaylistWithTrack;
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyResultAlbum;
import com.joxad.zikobot.data.module.spotify_api.model.SpotifySearchResult;
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyTopTracks;
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyUser;
import com.joxad.zikobot.data.module.spotify_api.resource.SpotifyAPIEndpoint;
import com.joxad.zikobot.data.module.spotify_api.resource.SpotifyApiInterceptor;

import java.util.Locale;

import retrofit2.Retrofit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/***
 * {@link SpotifyApiManager} est initialisé au lancement de l'application et permet d'utiliser les méthodes
 * {@link SpotifyAPIEndpoint}
 */

public class SpotifyApiManager {

    private SpotifyAPIEndpoint spotifyAPIEndpoint;
    private Context context;
    private Retrofit retrofit;

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
        spotifyAPIEndpoint = retrofit.create(SpotifyAPIEndpoint.class);
    }

    /***
     */
    public Observable<SpotifyUser> getMe() {
        return spotifyAPIEndpoint.getMe().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    /***
     * 1     *
     */
    public Observable<SpotifyArtist> getArtist(final String artistId) {
        return spotifyAPIEndpoint.getArtist(artistId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    /***
     * 1     *
     */
    public Observable<SpotifyPlaylist> getUserPlaylists() {
        return spotifyAPIEndpoint.getUserPlaylists()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    /**
     * Get tracks from an album using its id
     *
     * @param id
     * @return
     */
    public Observable<SpotifyResultAlbum> getAlbumTracks(String id) {
        return spotifyAPIEndpoint.getTracksAlbum(id).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * @param idArtist
     * @return
     */
    public Observable<Albums> getAlbums(String idArtist, int limit, int offset) {
        return spotifyAPIEndpoint.getAlbums(idArtist, Locale.getDefault().getCountry(), limit, offset).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * @param idArtist
     * @return
     */
    public Observable<SpotifyTopTracks> getTopTracks(String idArtist) {
        return spotifyAPIEndpoint.getTopTracks(idArtist, Locale.getDefault().getCountry()).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /***
     * @param playlistId
     */
    public Observable<SpotifyPlaylistWithTrack> getPlaylistTracks(final String playlistId) {
        return spotifyAPIEndpoint.getPlaylistTracks(AppPrefs.spotifyUser().getId(), playlistId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    public Observable<SpotifySearchResult> search(final int limit, final int offset, final String search) {
        return spotifyAPIEndpoint.search(limit, offset, search, "album,artist,track", Locale.getDefault().getCountry()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    public Observable<SpotifyFeaturedPlaylist> getFeaturedPlaylists() {
        return spotifyAPIEndpoint.getFeaturedPlaylists().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    /**
     * Holder
     */
    private static class SpotifyApiManagerHolder {
        /**
         * Instance unique non préinitialisée
         */
        private final static SpotifyApiManager instance = new SpotifyApiManager();
    }


}
