package com.startogamu.musicalarm.network.spotify_api;


import com.startogamu.musicalarm.model.spotify.SpotifyPlaylist;
import com.startogamu.musicalarm.model.spotify.SpotifyPlaylistWithTrack;
import com.startogamu.musicalarm.model.spotify.SpotifyUser;

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
    Observable<SpotifyPlaylist> getFeaturedPlaylists();

    @GET("users/{userId}/playlists/{playlistId}/tracks")
    Observable<SpotifyPlaylistWithTrack> getPlaylistTracks(@Path("userId") String userId, @Path("playlistId") final String playlistId);

}
