package com.joxad.zikobot.app.core.module.spotify_api.resource;


import com.joxad.zikobot.app.core.module.spotify_api.model.SpotifyFeaturedPlaylist;
import com.joxad.zikobot.app.core.module.spotify_api.model.SpotifyPlaylist;
import com.joxad.zikobot.app.core.module.spotify_api.model.SpotifyPlaylistWithTrack;
import com.joxad.zikobot.app.core.module.spotify_api.model.SpotifySearchResult;
import com.joxad.zikobot.app.core.module.spotify_api.model.SpotifyUser;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;


/**
 * Created by josh on 11/03/16.
 * <p>
 * Interface using retrofit to call Spotify API
 * </p>
 */
public interface SpotifyAPIService {

    @GET("me")
    Observable<SpotifyUser> getMe();


    @GET("me/playlists")
    Observable<SpotifyPlaylist> getUserPlaylists();

    @GET("browse/featured-playlists")
    Observable<SpotifyFeaturedPlaylist> getFeaturedPlaylists();

    @GET("users/{spotifyUser}/playlists/{playlistId}/tracks")
    Observable<SpotifyPlaylistWithTrack> getPlaylistTracks(@Path("spotifyUser") String userId, @Path("playlistId") final String playlistId);

    @GET("search")
    Observable<SpotifySearchResult> search(@Query("limit") int limit, @Query("offset") int offset, @Query("q") String search, @Query("type")String type);
}
