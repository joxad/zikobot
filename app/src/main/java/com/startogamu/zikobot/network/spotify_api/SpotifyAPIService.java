package com.startogamu.zikobot.network.spotify_api;


import com.startogamu.zikobot.module.spotify_api.model.SpotifyFeaturedPlaylist;
import com.startogamu.zikobot.module.spotify_api.model.SpotifyPlaylist;
import com.startogamu.zikobot.module.spotify_api.model.SpotifyPlaylistWithTrack;
import com.startogamu.zikobot.module.spotify_api.model.SpotifyUser;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by josh on 11/03/16.
 * Interface using retrofit to call Spotify API
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

}
