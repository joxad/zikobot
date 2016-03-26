package com.startogamu.musicalarm.network.spotify_api;


import com.startogamu.musicalarm.model.SpotifyUser;

import retrofit2.http.GET;
import retrofit2.http.Header;
import rx.Observable;

/**
 * Created by josh on 11/03/16.
 * Interface using retrofit to call Spotify API
 */
public interface SpotifyAPIService {

    @GET("me")
    Observable<SpotifyUser> getMe(@Header("Authorization") final String token);
}
