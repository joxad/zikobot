package com.joxad.zikobot.data.module.spotify_api.manager;

import android.content.Context;

import com.joxad.zikobot.data.AppPrefs;
import com.joxad.zikobot.data.R;
import com.joxad.zikobot.data.module.spotify_api.model.Albums;
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyArtist;
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyArtists;
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyFeaturedPlaylist;
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyPlaylist;
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyPlaylistWithTrack;
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyResultAlbum;
import com.joxad.zikobot.data.module.spotify_api.model.SpotifySearchResult;
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyTopTracks;
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyUser;
import com.joxad.zikobot.data.module.spotify_api.resource.SpotifyAPIEndpoint;
import com.joxad.zikobot.data.module.spotify_api.resource.SpotifyApiInterceptor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/***
 * {@link SpotifyApiManager} est initialisé au lancement de l'application et permet d'utiliser les méthodes
 * {@link SpotifyAPIEndpoint}
 */

public enum SpotifyApiManager {
    INSTANCE;
    private SpotifyAPIEndpoint spotifyAPIEndpoint;
    private String language;
    public void init(Context context) {
        SpotifyRetrofit spotifyRetrofit = new SpotifyRetrofit(context, context.getString(R.string.spotify_base_api_url),
                new SpotifyApiInterceptor());
language = Locale.getDefault().getLanguage();
        spotifyAPIEndpoint = spotifyRetrofit.retrofit().create(SpotifyAPIEndpoint.class);
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
    public Observable<List<SpotifyArtist>> getSimilarArtists(final String artistId) {
        return spotifyAPIEndpoint.getSimilarArtist(artistId)
                .flatMap(spotifyArtists -> Observable.just(spotifyArtists.artists))
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
     * Get tracks local an album using its id
     *
     * @param id
     * @return
     */
    public Observable<SpotifyResultAlbum> getAlbumTracks(String id, int limit, int offset) {
        return spotifyAPIEndpoint.getTracksAlbum(id,language, limit, offset).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * @param idArtist
     * @return
     */
    public Observable<Albums> getAlbums(String idArtist, int limit, int offset) {
        return spotifyAPIEndpoint.getAlbums(idArtist, language, limit, offset).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * @param idArtist
     * @return
     */
    public Observable<SpotifyTopTracks> getTopTracks(String idArtist) {
        return spotifyAPIEndpoint.getTopTracks(idArtist, language).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /***
     * @param playlistId
     */
    public Observable<SpotifyPlaylistWithTrack> getPlaylistTracks(final String playlistId, final int limit, final int offset) {

        String userEncoded = "";
        try {
            userEncoded = URLEncoder.encode(AppPrefs.spotifyUser().getId(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return spotifyAPIEndpoint.getPlaylistTracks(userEncoded, playlistId, limit, offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    public Observable<SpotifySearchResult> search(final int limit, final int offset, final String search) {
        return spotifyAPIEndpoint.search(limit, offset, search, "album,artist,track", language).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    public Observable<SpotifyFeaturedPlaylist> getFeaturedPlaylists() {
        return spotifyAPIEndpoint.getFeaturedPlaylists().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
}
